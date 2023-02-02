package org.crawl.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class HtmlJudge {
    private static final String[] NEG_WORDS = {"user", "list", "author", "comment","writer"};
    private static final String[]  POS_WORDS = {"article", "blog", "details", "question"};

    private static final String[] FILE_WORDS={".gif",".png",".bmp",".jpeg",".jpg", ".svg",
            ".mp3",".wma",".flv",".mp4",".wmv",".ogg",".avi",
            ".doc",".docx",".xls",".xlsx",".ppt",".pptx",".txt",".pdf",
            ".zip",".exe",".tat",".ico",".css",".js",".swf",".apk",".m3u8",".ts"};

    public static boolean isDetailPage(String url){
        double threshold=0.4;
        double score=0;
        if(Pattern.compile("[0-9]{8,}").matcher(url).find()){
            score+=30;
        }

        for(String i:POS_WORDS){
            if(url.contains(i)){
                score+=15;
            }
        }

        for(String i:NEG_WORDS){
            if(url.contains(i)){
                score-=10;
            }
        }

        if(score/url.length()/2>=threshold){
            return true;
        }
        return false;


    }
    public static boolean isDetailPage(String url,String body){
        double threshold=0.4;

        return false;
    }
    public static boolean isStaticPage(String url){

        String[] url_part = url.split("\\.");

        if(url_part.length==0){
            return true;
        }

        return Arrays.stream(FILE_WORDS).anyMatch(i->url_part[url_part.length-1].contains(i));
    }

    public static boolean isListPage(String url) {


        return !isDetailPage(url);
    }
}
