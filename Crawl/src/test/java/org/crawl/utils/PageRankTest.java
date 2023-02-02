package org.crawl.utils;

import org.crawl.pojo.HtmlEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PageRankTest {
    @Autowired
    PageRank pageRank;

    @Test
    void getPageRank() {
        HtmlEntity a = new HtmlEntity("a",
                Arrays.asList(new String[]{"d","b", "c"}),
                0.0);
        HtmlEntity b = new HtmlEntity("b",
                Arrays.asList(new String[]{"d", "c"}),
                0.0);
        HtmlEntity c = new HtmlEntity("c",
                Arrays.asList(new String[]{"d"}),
                0.0);
        HtmlEntity d = new HtmlEntity("d",
                Arrays.asList(new String[]{"b"}),
                0.0);
        List<HtmlEntity> list=Arrays.asList(new HtmlEntity[]{a,b,c,d});
        pageRank.getPageRank(list,1e-7);
        System.out.println(list.stream().map(HtmlEntity::getPr).collect(Collectors.toList()));

    }
}