package org.brower.service;

import cn.hutool.json.JSONObject;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.brower.pojo.Article;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;

import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.mapper.ObjectMapper;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService{

    @Autowired(required = false)
    JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("restHighLevelClient")
    RestHighLevelClient client;

    Logger log = LoggerFactory.getLogger(ArticleServiceImpl.class);




    @Override
    public void createIndex(String name) {
        //1????????? ?????????????????????
        CreateIndexRequest request = new CreateIndexRequest(name);//?????????
        //2????????????????????????,????????????
        try{
            CreateIndexResponse response = client.indices().create(request,RequestOptions.DEFAULT);
            //3?????????
            System.out.println("???????????????????????????????????????" + response.index());
        }catch(Exception e){
            log.error("????????????");
        }


    }

    /**
     * ??????????????????
     */
    @Override
    public boolean getIndex(String name) throws IOException {
        //1????????? ?????????????????????
        GetIndexRequest request = new GetIndexRequest(name);
        //2???????????????????????????????????????
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        //3?????????
        System.out.println("????????????????????????"+exists);
        return exists;
    }

    @Override
    public void createDocument(String dbname,Article article) throws IOException {

        //1???????????????
        IndexRequest request = new IndexRequest(dbname);

        //2???????????????  PUT /user_index/user/_doc/1
        // request.id("1");//??????id
        //request.timeout(TimeValue.timeValueSeconds(1));//??????????????????

        //3??????????????????????????????,???JSON???????????????
        request.source(new GsonBuilder().create().toJson(article), XContentType.JSON);

        //4????????????????????????,??????????????????
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);

        //5?????????
        System.out.println("???????????????"+response.toString());
    }

    @Override
    public boolean deleteDocument(String dbname,int id) throws IOException {
            DeleteByQueryRequest request = new DeleteByQueryRequest();
            request.indices(dbname);

            request.setQuery(new TermQueryBuilder("id", id));
            // ?????????????????????
            request.setSize(10);
            // ????????????
            request.setBatchSize(1000);
            // ??????
            request.setSlices(2);
            // ???????????????????????????????????????????????????????????????
            request.setScroll(TimeValue.timeValueMinutes(10));
            // ??????
            request.setTimeout(TimeValue.timeValueMinutes(2));
            // ????????????
            request.setRefresh(true);

            BulkByScrollResponse response = null;
            try {
                response = client.deleteByQuery(request, RequestOptions.DEFAULT);
                /**??????0?????????????????????-1????????????**/
                return response.getStatus().getUpdated()==0;
            } catch (IOException e) {
                log.error(e.getMessage(),e);
                return false;
            }
    }

    @Override
    public int updateDocument(String dbname,Article article) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("id", article.getId());
        map.put("url", article.getUrl());
        map.put("publish_time", article.getPublish_time());
        map.put("keywords", article.getKeywords());
        map.put("description", article.getDescription());
        map.put("title", article.getTitle());
        map.put("content", article.getContent());

        UpdateRequest request = new UpdateRequest(dbname,map.get("id").toString()).doc(map);

        try {
            UpdateResponse update = client.update(request, RequestOptions.DEFAULT);
            if(update.status().getStatus() == 200){
                return 1;
            }else {
                return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * ????????????
     */
    @Override
    public Article getDocument(String dbname,String id) throws IOException {
        //??????id???1??????????????????
        GetRequest request = new GetRequest(dbname,id);

        boolean exists = client.exists(request, RequestOptions.DEFAULT);
        System.out.println("?????????????????????"+exists);
        //?????????????????????????????????
        if (exists){
            GetResponse response = client.get(request, RequestOptions.DEFAULT);
            System.out.println("??????????????????"+response.getSourceAsString());
            Article item = new Article();
            Map<String,Object> map=response.getSourceAsMap();
            item.setDescription((String) map.get("author"));
            item.setContent((String) map.get("content"));
            item.setTitle((String) map.get("title"));
            item.setKeywords((String) map.get("url"));
            return item;
        }
        return new Article();
    }

    @Override
    public  String setHighLight(String keywords, String info) {
        if (StringUtils.isBlank(keywords)|| StringUtils.isBlank(info)) {
            return info;
        }
        String[] keywordArray = keywords.split(" ");
        String newInfo = info;
        for (String keyword : keywordArray) {
            newInfo = newInfo.replace(keyword, "<em style=\"color:red\">" + keyword + "</em>");
        }
        return newInfo;
    }

    @Override
    public List<Article> queryByKey(String keyword,int pageSize,int pageNum){
        SearchRequest request = new SearchRequest();
        //????????????????????????
        request.indices("crawl");
        /*
         * ??????  ??????????????????????????????:SearchSourceBuilder
         * ?????????matchQuery???multiMatchQuery??????????????????fi eld?????????????????????multiMatchQuery??????fieldNames????????????????????????????????????matchQuery?????????
         * ??????fieldNames????????????????????????field1???field2?????????????????????????????????field1?????????text?????????field2?????????text???
         */
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders
                .multiMatchQuery(keyword, "content","title","description","keywords"));
        searchSourceBuilder.size(pageSize);
        searchSourceBuilder.from((pageNum-1)*pageSize);
        request.source(searchSourceBuilder);
        List<Article> result = new ArrayList<>();
        try {
            SearchResponse search = client.search(request, RequestOptions.DEFAULT);
            for (SearchHit hit:search.getHits()){
                Map<String, Object> map = hit.getSourceAsMap();
                Article item = new Article();
//                item.setDescription((String) map.get("author"));
                item.setId((int) map.get("id"));
                item.setContent(setHighLight(keyword,(String) map.get("content")));
                item.setTitle(setHighLight(keyword,(String) map.get("title")));
                item.setUrl((String) map.get("url"));
                item.setKeywords(setHighLight(keyword,(String) map.get("keywords")));
                item.setDescription(setHighLight(keyword,(String) map.get("description")));
                item.setPublish_time((String) map.get("publish_time"));
                result.add(item);
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
