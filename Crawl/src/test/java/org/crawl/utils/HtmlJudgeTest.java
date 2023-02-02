package org.crawl.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HtmlJudgeTest {
    @Test
    void isDetailPage() {

//        String url = "https://blog.csdn.net/csdnnews/article/details/128297529]";

        String url = "https://blog.csdn.net/SnowyForest___";

        System.out.println(HtmlJudge.isDetailPage(url));

    }
}