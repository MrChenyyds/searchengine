package org.brower.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.springframework.data.elasticsearch.annotations.Document;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
//@Document(indexName="blog",type="article",indexStoreType="fs",shards=5,replicas=1,refreshInterval="-1")
public class Article {
    private int id;
    private String url;
    private String publish_time;
    private String keywords;
    private String description;
    private String title;
    private String content;
    private String encode;
    private String lang;
}
