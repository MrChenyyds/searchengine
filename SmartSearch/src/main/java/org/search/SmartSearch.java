package org.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"org.search.*"})
public class SmartSearch {
    public static void main(String[] args) {
        SpringApplication.run(SmartSearch.class,args);
    }
}
