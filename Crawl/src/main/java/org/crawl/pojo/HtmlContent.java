package org.crawl.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class HtmlContent {


    private String url;
    private String publish_time;
    private String keywords;
    private String description;
    private String title;
    private String content;
    private String encode;
    private String lang;


}
