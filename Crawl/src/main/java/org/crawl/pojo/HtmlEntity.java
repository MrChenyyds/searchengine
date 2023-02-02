package org.crawl.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class HtmlEntity {

    private String url;

    private List<String> urls;

    private double pr;

}
