package org.search.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class HtmlContent implements Serializable {

    private String page_url;
    private String encode;
    private String keywords;
    private String description;
    private String lang;
    private String title;
    private String content;
    private List<String> urls;
    private String publish_time;

}
