package org.brower;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"org.brower.*"})
public class BrowerStart {
    public static void main(String[] args) {
        SpringApplication.run(BrowerStart.class,args);
    }
}
