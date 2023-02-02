package org.crawl.utils;

import org.springframework.stereotype.Component;

//package org.crawl.utils;
//
//import jakarta.annotation.Resource;
//import org.redisson.Redisson;
//import org.redisson.api.RBloomFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
@Component
public class BloomFilter{
//
//    protected Long expectedInsertions = 100000000L;
//    /**
//     * 误差
//     */
//    protected Double falseProbability = 0.001;
//
//    @Resource
//    private Redisson redisson;
//
//    public void add(String key , String data) {
//        RBloomFilter bloomFilter = redisson.getBloomFilter(key);
//        bloomFilter.tryInit(expectedInsertions, falseProbability);
//        bloomFilter.add(data);
//    }
//
//    public Boolean exist(String key , String data) {
//        RBloomFilter bloomFilter = redisson.getBloomFilter(key);
//        bloomFilter.tryInit(expectedInsertions , falseProbability);
//        boolean isExist = bloomFilter.contains(data);
//        return isExist;
//    }
//
}
