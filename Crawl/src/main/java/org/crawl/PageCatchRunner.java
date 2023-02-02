package org.crawl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.crawl.*"})
public class PageCatchRunner {
    public static void main(String[] args) {
        SpringApplication.run(PageCatchRunner.class,args);
    }
}
