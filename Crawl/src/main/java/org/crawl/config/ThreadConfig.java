package org.crawl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

@Configuration
public class ThreadConfig {

    @Bean
    public ThreadPoolExecutor executor(){

        int i = Runtime.getRuntime().availableProcessors();

        final int CORE_POOL_SIZE = i;
        final int MAX_POOL_SIZE = i*2;
        final int QUEUE_CAPACITY = 100;
        final Long KEEP_ALIVE_TIME = 1L;

        ThreadFactory threadFactory = Executors.defaultThreadFactory();


        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue(QUEUE_CAPACITY), threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());



        return executor;
    }
}
