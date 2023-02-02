package org.crawl.utils;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application.properties")
class BloomFilterTest {

//    @Autowired
//    BloomFilter bloomFilter;

//    @Test
//    void test() {
//        bloomFilter.add("asd","111");
//        System.out.println(bloomFilter.exist("asd", "111"));
//    }
}