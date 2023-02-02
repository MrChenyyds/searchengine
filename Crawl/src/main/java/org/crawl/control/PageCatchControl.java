package org.crawl.control;

import org.crawl.config.DistributedRedisScheduler;
import org.crawl.service.PageCatchService;
import org.crawl.service.PipLineServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.RedisScheduler;


@Controller
public class PageCatchControl implements CommandLineRunner {
    private static final String[] urls = {"https://www.51cto.com/",
            "https://www.iteye.com/", "https://www.cnblogs.com/",
            "http://www.blogjava.net/", "https://blogread.cn//it/",
            "http://blog.chinaunix.net/", "https://www.oschina.net/",
            "http://blog.itpub.net/", "https://cuiqingcai.com/",
            "http://blog.jobbole.com/", "https://segmentfault.com/",
            "https://www.infoq.cn/", "https://www.v2ex.com/",
            "https://www.jianshu.com/", "https://blogs.360.cn/",
            "https://tech.meituan.com/", "http://www.ruanyifeng.com/blog/",
            "http://it.deepinmind.com/", "https://coolshell.cn/",
            "https://imzl.com/", "https://www.itzhai.com/",
            "http://macshuo.com/", "http://ifeve.com/",
            "http://blog.zhaojie.me/", "https://juejin.cn/",
            "https://www.runoob.com/"};

    private static final String start_url = "https://www.csdn.net/?spm=1001.2101.3001.4476";

    @Autowired
    PageCatchService pageCatchService;

    @Autowired
    JedisPool jedis;

    @Autowired
    PipLineServer pipLineServer;

//    @Override
//    public void run(String... args) throws Exception {
//    单机版，多线程爬取
//        Spider.create(pageCatchService).addUrl(start_url) //设置使用布隆过滤器去重
//                //设置10个线程同时抓取
//                .thread(10)
//                //使用自己的Pipeline将结果保存到数据库中
//                .addPipeline(pipLineServer)
//                .setScheduler(new RedisScheduler(jedis).setDuplicateRemover(new BloomFilterDuplicateRemover(100000000)))
//                .run();
//    }

    //多机协作获取,去除重复url
    //多机启动的时候，要让一个先启动一段时间，防止出现其他机从同一个队列中取的时候出现没有url的情况
    @Override
    public void run(String... args) throws Exception {

        pipLineServer.createTable();

        Jedis resource = jedis.getResource();
        //断点续爬或者多机协作的后起动机中都有uuid
        if(resource.exists("uuid")){
            String uuid =resource.get("uuid");
            Spider s=Spider.create(pageCatchService) //设置使用布隆过滤器去重
                    //设置10个线程同时抓取
                    .thread(10)
                    .setUUID(uuid)
                    //使用自己的Pipeline将结果保存到数据库中
                    .addPipeline(pipLineServer);
            RedisScheduler redisScheduler = new DistributedRedisScheduler(jedis);
            Request request = redisScheduler.poll(s);
            if(request==null){
                throw new IllegalStateException();
            }else{
                s.addRequest(request);
            }

            s.runAsync();
        }
        else{
            //初始启动入口
            Spider s=Spider.create(pageCatchService) //设置使用布隆过滤器去重
                    //设置10个线程同时抓取
                    .thread(10)
                    .addUrl(start_url)
                    //使用自己的Pipeline将结果保存到数据库中
                    .addPipeline(pipLineServer);
            RedisScheduler redisScheduler = new DistributedRedisScheduler(jedis);
            redisScheduler.setDuplicateRemover(new BloomFilterDuplicateRemover(100000000));
            Request request = redisScheduler.poll(s);
            if(request!=null){
                s.addRequest(request);
            }
            String uuid = s.getUUID();
            resource.set("uuid",uuid);
            s.runAsync();
        }
    }
}
