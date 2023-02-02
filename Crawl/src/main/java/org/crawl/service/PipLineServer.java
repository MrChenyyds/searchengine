package org.crawl.service;


import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;


@Service
public class PipLineServer implements Pipeline {

    @Resource
    JdbcTemplate jdbcTemplate;

    public void createTable(){
        Logger log = LoggerFactory.getLogger(PipLineServer.class);
        for(int i = 1; i <=256;i++){
//            String sql=("ALTER TABLE `crawl%s` \n" +
//                    "MODIFY COLUMN `keywords` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL;").formatted(i);

            String sql="CREATE TABLE IF NOT EXISTS `crawl%s`  (".formatted(i) +
                    " `id` int UNSIGNED NOT NULL AUTO_INCREMENT," +
                    "  `url` varchar(500) NULL," +
                    "  `publish_time` varchar(255) NULL," +
                    "  `keywords` varchar(255) NULL," +
                    "  `description` text CHARACTER SET utf8mb4 NULL," +
                    "  `title` varchar(255) NULL," +
                    "  `content` longtext CHARACTER SET utf8mb4 NULL," +
                    "  `encode` varchar(20) NULL," +
                    "  `lang` varchar(20) NULL," +
                    " PRIMARY KEY(id)"+
                    ")ENGINE=INNODB;";
            int update=jdbcTemplate.update(sql);
            if(update!=1)
                log.info("create error!!!! ");
            else {
                log.info("create succrss "+i);
            }
        }
    }

    @Override
    public void process(ResultItems resultItems, Task task) {

        Logger log = LoggerFactory.getLogger(PipLineServer.class);

        String table= String.format("crawl%s",String.valueOf(Math.abs(resultItems.get("url").hashCode())%256+1));

        String sql="INSERT INTO "+table+" value(?,?,?,?,?,?,?,?,?);";
        boolean isDetail = (boolean)resultItems.get("isDetail");
        if(isDetail){
            Object[] args={
                    null,
                    resultItems.get("url"),
                    resultItems.get("publish_time"),
                    resultItems.get("keywords"),
                    resultItems.get("description"),
                    resultItems.get("title"),
                    resultItems.get("content"),
                    resultItems.get("encode"),
                    resultItems.get("lang")};
                    int update=jdbcTemplate.update(sql,args);
                    if(update!=1){
                        log.error(resultItems.get("urls")+"save error!!!!");

                        return;
                    }
                    log.info("save detail into "+table+" "+resultItems.get("url"));

        }
        else{
            Object[] args={
                    null,
                    resultItems.get("url"),
                    resultItems.get("publish_time"),
                    resultItems.get("keywords"),
                    resultItems.get("description"),
                    null,
                    null,
                    resultItems.get("encode"),
                    resultItems.get("lang")};
                    int update=jdbcTemplate.update(sql,args);
                    if(update!=1){
                        log.error(resultItems.get("urls")+"save error!!!!");
                        return;
                    }
                    log.info("save list into "+table+" "+resultItems.get("url"));


        }





    }
}
