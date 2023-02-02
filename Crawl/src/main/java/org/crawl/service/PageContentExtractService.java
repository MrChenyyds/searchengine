package org.crawl.service;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PageContentExtractService {
    public Map<String,String> ectract(String url, Html htmlCode) {
        Map<String,String> m = new HashMap<String,String>();
        String title = "";
        String content = "";


//        String REGEXES = {
//                "okMaybeItsACandidateRe": re.compile(
//                "and|article|artical|body|column|main|shadow", re.I),
//                "positiveRe": re.compile(
//                ("article|arti|body|content|entry|hentry|main|page|"
//        "artical|zoom|arti|context|message|editor|"
//        "pagination|post|txt|text|blog|story"), re.I),
//        "negativeRe": re.compile(
//                ("copyright|combx|comment|com-|contact|foot|footer|footnote|decl|copy|"
//        "notice|"
//        "masthead|media|meta|outbrain|promo|related|scroll|link|pagebottom|bottom|"
//        "other|shoutbox|sidebar|sponsor|shopping|tags|tool|widget"), re.I);
//}

        title =getTitle(htmlCode);

        content=getContent(htmlCode);

        m.put("title",title);
        m.put("content",content);

        return m;
    }

    public String getContent(Html htmlCode) {

        String content="";

        Document document = htmlCode.getDocument();

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
        return result;

    }

    public String getTitle(Html htmlCode) {

        //通过《title》标签提取/html/head/title
        String title_label =htmlCode.xpath("//title").toString();
        if(title_label!=null && title_label.length()>=7){
            return title_label.strip();
        }

        //通过《meta》标签提取
        title_label =htmlCode.xpath("//meta[@name=\"title\"]]/@content").toString();
        if(title_label!=null && title_label.length()>=7){
            return title_label.strip();
        }

        //通过带title的标签类型
        List<String> title =htmlCode.xpath("//*[contains(@id, \"title\") or contains(@class, \"title\")]").all();
        if(title==null && title.size()==0){
            title =htmlCode.xpath("//*[contains(@id, \"font01\") or contains(@class, \"font01\")]").all();
        }
        for (String t : title) {
            title_label=t.strip();
            if(title_label!=null && title_label.length()>=7){
                return title_label.strip();
            }
        }
        return "no title found!!!!!!!";
    }
}
