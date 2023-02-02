package org.crawl.service;

import org.crawl.control.PageCatchControl;
import org.crawl.pojo.HtmlContent;
import org.crawl.utils.BloomFilter;
import org.crawl.utils.HtmlJudge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.jsoup.nodes.Document.OutputSettings.Syntax.html;


@Service
@Scope(value="prototype")
public class
PageCatchService implements PageProcessor {

    private static final String rule_encode = "//meta/@charset";
    private static final String rule_keywords = "//meta[@name='keywords']/@content";
    private static final String rule_description = "//meta[@name='description']/@content";
    private static final String rule_lang = "////@lang";
    private static final String rule_time = "//span[@class='time']/@content";

    private Site site = Site.me();

    private static final Logger log=LoggerFactory.getLogger(PageCatchService.class);

    @Autowired
    PageContentExtractService   extractService;


    @Override
    public void process(Page page) {


        String url = page.getRequest().getUrl();
        log.info("-".repeat(30));
        log.info("Start catch:"+url);

        if(page.getStatusCode()==200){

            Html htmlCode = page.getHtml();

            HtmlContent htmlEntity=new HtmlContent();

            String encode = htmlCode.xpath(rule_encode).toString()==null?"":htmlCode.xpath(rule_encode).toString();
            String keywords = htmlCode.xpath(rule_keywords).toString()==null?"":htmlCode.xpath(rule_keywords).toString();
            String description = htmlCode.xpath(rule_description).toString()==null?"":htmlCode.xpath(rule_description).toString();
            String lang = htmlCode.xpath(rule_lang).toString()==null?"":htmlCode.xpath(rule_lang).toString();
            String publish_time = htmlCode.xpath(rule_time).toString()==null?"":htmlCode.xpath(rule_time).toString();
            List<String> urls = htmlCode.links().all();
            //  这里的links去除了javascrapet,同时对相对地址进行了拼接
            //  System.out.println(encode+"\n"+ keywords+"\n"+ description+"\n"+ lang+"\n"+urls);
            List<String> urls_clean = urls.stream().filter(i->i!=null && (!"".equals(i)) &&(!i.startsWith("javascript:")) &&(!HtmlJudge.isStaticPage(i) )).collect(Collectors.toList());
            //  System.out.println(urls_clean);


            if(HtmlJudge.isDetailPage(url)){
                log.info("该网页符合详情页规则.....\n" +
                        String.format("开始提取[%s]的标题及内容", url));

                page.putField("encode",encode);
                page.putField("keywords",keywords);
                page.putField("description",description);
                page.putField("lang",lang);
                page.putField("url",url);
                page.putField("publish_time",publish_time);

                page.putField("isDetail",true);
                Map<String, String> extract_content = extractService.ectract(url, htmlCode);

                page.putField("title",extract_content.get("title"));
                page.putField("content",extract_content.get("content"));

            }
            else if(HtmlJudge.isListPage(url)){
                log.info("该网页符合列表页规则.....\n" +
                        String.format("仅仅提取[%s]的部分", url));
                page.putField("encode",encode);
                page.putField("keywords",keywords);
                page.putField("description",description);
                page.putField("lang",lang);
                page.putField("url",url);
                page.putField("publish_time",publish_time);

                page.putField("isDetail",false);
            }
            else{
                log.info("该网页不符合规则.....\n" +
                        String.format("跳过爬取提取[%s]的内容!!!!!!", url));
            }
            for(String path:urls_clean){
                Object deep = page.getRequest().getExtra("deep");
                //控制爬虫深度 最多4层，相当于广度优先的树的深度
                int deep_now = deep!=null? Integer.valueOf(deep.toString())+1:0;
                if(deep_now>=5){
                    continue;
                }
                Map<String,Object> m=new HashMap<>();
                m.put("deep",deep_now);
                Request request_new = new Request(path).setExtras(m);
                page.addTargetRequest(request_new);
            }

        }else{
            System.out.println(String.format("[%s]网页未爬取成功。。。。。。。", url));
        }
        return;
    }

    @Override
    public Site getSite() {
        Site site = Site.me()
                //设置字符集,此处使用的字符集应该和网站使用的字符集相同，不然就会出现乱码的情况
                .setCharset("utf8")
                //设置超时时间为10s
                .setTimeOut(15*1000)
                //重试间隔时间为3S
                .setRetrySleepTime(3000)
                //重试次数为 3次
                .setRetryTimes(3)
                ;
        return site;
    }


}
