package org.crawl.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class PageContentExtractServiceTest {
    private Document getDocument(String url) {
        // 重试次数
        int count = 10;
        boolean flag = true;
        Document document = null;
        while (flag) {
            try {
                document = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
                        .get();
                flag = false;
            } catch (IOException e) {
                if (count-- != 0) {
                    System.out.println(("网页获取失败，原因：" + e.getMessage()));
                    System.out.println("开始第" + (10 - count) + "次重试");
                } else {
                    System.out.println("获取文档未知异常:" + e.getMessage());
                }
            }
        }
        return document;
    }

    @Test
    void getContent() {
        String url="https://blog.csdn.net/weixin_46684099/article/details/121399635";
        String content="";
        Document document = getDocument(url);
        String[] exclude={"script","style"};
        String[] include={"div", "h1", "h2", "h3", "p", "br", "table", "tr", "dl"};
        for (Element i:document.body().children()) {
            if(Arrays.stream(exclude).anyMatch(j->j.equals(i.tagName()))){
                continue;
            }
            if(Arrays.stream(include).anyMatch(j->j.equals(i.tagName()))){
                if(i.hasText()){
                    content = content+"\n"+i.wholeText().trim().strip();
                }
            }

        }
//        String res="\\n\\s{3,}\\n";
//        Matcher m= Pattern.compile(res).matcher(content);
//        while (m.find()) {
//            String temp = m.group();
//            content=content.replace(temp,"\n");
//            m= Pattern.compile(res).matcher(content);
//        }
        String result="";
        for(String line : content.split("\n")){
            line=line.trim().strip();
            result=result+line;
        }
        System.out.println(result);
    }
    @Test
    public void test(){
        String url="https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=83018325_10_hao_pg&wd=%E6%AD%A3%E5%88%99%20%E5%8C%B9%E9%85%8D%E5%A4%9A%E4%B8%AA%E6%88%91%E7%A9%BA%E6%A0%BC&fenlei=256&oq=%25E6%25AD%25A3%25E5%2588%2599%25E5%259C%25A8%25E7%25BA%25BF%25E6%25B5%258B%25E8%25AF%2595&rsv_pq=9025b00c00016552&rsv_t=14f0T5UwUX8MpmruHHc7%2BgkJobNQVseMtOd42U52SbpYYKBrimlU15%2BzpVUH%2F9m9q5eqAHF1DsSL&rqlang=cn&rsv_dl=tb&rsv_enter=1&rsv_btype=t&inputT=14221&rsv_sug3=37&rsv_sug1=33&rsv_sug7=100&rsv_sug2=0&rsv_sug4=15020";
        String url2="https://blog.csdn.net/wangyuxiang946/article/details/127932313?spm=1001.2100.3001.7377&utm_medium=distribute.pc_feed_blog_category.none-task-blog-classify_tag-1-127932313-null-null.nonecase&depth_1-utm_source=distribute.pc_feed_blog_category.none-task-blog-classify_tag-1-127932313-null-null.nonecase";
        System.out.println(url2.length());
        String url3="";
        System.out.println(url2.length());
        String url4="https://blog.csdn.net/wangyuxiang946/article/details/127932313?spm=1001.2100.3001.7377&utm_medium=distribute.pc_feed_blog_category.none-task-blog-classify_tag-1-127932313-null-null.nonecase&depth_1-utm_source=distribute.pc_feed_blog_category.none-task-blog-classify_tag-1-127932313-null-null.nonecase";
        System.out.println(url2.length());
    }
}