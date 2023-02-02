package org.brower.service;

import org.apache.commons.lang3.StringUtils;
import org.brower.pojo.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleServiceImplTest {
    @Autowired
    ArticleService articleService;
    public static String setHighLight(String keywords, String info) {
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
    @Test
    public void test(){
//        articleService.fetchMysqlToElasticSearch();
        List<Article> articles = articleService.queryByKey("开发者",10,1);
        for (Article article :articles){
//参数一：需要高亮展示的关键字；参数二：需要高亮展示的字段
            System.out.println(setHighLight("开发者", article.getKeywords()));
        }
        System.out.println(articles);
    }
}