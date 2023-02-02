package org.crawl.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.common.utils.AddressUtils;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import org.brower.pojo.Article;
import org.brower.service.ArticleService;
import org.brower.service.ArticleServiceImpl;
import org.hibernate.mapping.Column;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimpleCanalClient {

    private final CanalConnector connector;
    private Thread thread = null;
    private final Thread.UncaughtExceptionHandler handler = (t, e) -> e.printStackTrace();
    private volatile boolean running = false;
    private final static int BATCH_SIZE = 5 * 1024;

    public SimpleCanalClient(CanalConnector connector) {
        this.connector = connector;
    }

    public  void main(String[] args) throws IOException {


        if(!articleService.getIndex("crawl")){
            articleService.createIndex("crawl");
        }

        // 根据ip，直接创建链接，无HA的功能
        String destination = "crawl";
        String ip = AddressUtils.getHostIp();
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(ip, 11111),
                destination,
                "canal",
                "canal@123456");
        final SimpleCanalClient simpleCanalClient = new SimpleCanalClient(connector);
        simpleCanalClient.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("## stop the canal client");
                simpleCanalClient.stop();
            } catch (Throwable e) {
                System.out.println("##something goes wrong when stopping canal:");
                e.printStackTrace();
            } finally {
                System.out.println("## canal client is down.");
            }
        }));
    }

    public void start() {
        if (this.connector == null) {
            System.out.println("connector不能为空,启动失败");
            return;
        }
        thread = new Thread(this::process);
        thread.setUncaughtExceptionHandler(handler);
        running = true;
        thread.start();
        System.out.println("canal client started...");
    }

    public void stop() {
        if (!running) {
            return;
        }
        running = false;
        if (thread != null) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                // ignore
            }
        }
        System.out.println("canal client stopped...");
    }

    private void process() {
        while (running) {
            try {
                //打开连接
                connector.connect();
                //订阅数据库表,全部表
                connector.subscribe(".*\\..*");
                //回滚到未进行ack的地方，下次fetch的时候，可以从最后一个没有ack的地方开始拿
                connector.rollback();
                while (running) {
                    // 获取指定数量的数据
                    Message message = connector.getWithoutAck(BATCH_SIZE);
                    //获取批量ID
                    long batchId = message.getId();
                    //获取批量的数量
                    int size = message.getEntries().size();
                    //如果没有数据
                    if (batchId == -1 || size == 0) {
                        try {
                            //线程休眠2秒
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //如果有数据,处理数据
                        printEntry(message.getEntries());
                    }
                    if (batchId != -1) {
                        // 提交确认
                        connector.ack(batchId);
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e1) {
                    // ignore
                }
                connector.rollback(); // 处理失败, 回滚数据
            } finally {
                connector.disconnect();
            }
        }
    }

    /**
     * 打印canal server解析binlog获得的实体类信息
     */

    ArticleService articleService=new ArticleServiceImpl();

    private  void printEntry(List<CanalEntry.Entry> entrys) throws IOException {
        for (CanalEntry.Entry entry : entrys) {
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                //开启/关闭事务的实体类型，跳过
                continue;
            }
            //RowChange对象，包含了一行数据变化的所有特征
            //比如isDdl 是否是ddl变更操作 sql 具体的ddl sql beforeColumns afterColumns 变更前后的数据字段等等
            CanalEntry.RowChange rowChage;
            try {
                rowChage = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(), e);
            }
            //获取操作类型：insert/update/delete类型
            CanalEntry.EventType eventType = rowChage.getEventType();
            //打印Header信息
//            System.out.println(String.format("================》; binlog[%s:%s] , dbName:%s, tableName:%s , eventType : %s",
//                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
//                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
//                    eventType));
            //判断是否是DDL语句
//            if (rowChage.getIsDdl()) {
//                System.out.println("================》;isDDL: true,sql:" + rowChage.getSql());
//            }
            //获取RowChange对象里的每一行数据，打印出来
            String dbname ="crawl";
            for (CanalEntry.RowData rowData : rowChage.getRowDatasList()) {
                //如果是删除语句
                if (eventType == CanalEntry.EventType.DELETE) {
//                    printColumn(rowData.getBeforeColumnsList());
                    String id = rowData.getBeforeColumnsList().stream().filter(i->"id".equals(i.getName())).map(CanalEntry.Column::getValue).collect(Collectors.toList()).get(0);
                    articleService.deleteDocument(dbname,Integer.valueOf(id));

                } else  {
                    //如果是新增语句
                    //如果是更新的语句
//                    printColumn(rowData.getAfterColumnsList());
                    Article article= getArticle(rowData.getAfterColumnsList());
                    if(eventType ==CanalEntry.EventType.UPDATE){
                        articleService.updateDocument(dbname,article);
                    }
                    if(eventType ==CanalEntry.EventType.INSERT){
                        articleService.createDocument(dbname,article);
                    }
                }
            }
        }
    }

    private static Article getArticle(List<CanalEntry.Column> afterColumnsList) {
//        Map<String,String> m=new HashMap<>();
//        for(CanalEntry.Column column : afterColumnsList){
//            m.put(column.getName(), column.getValue());
//        }
        Map<String, String> m = afterColumnsList.stream().collect(Collectors.toMap(CanalEntry.Column::getName, CanalEntry.Column::getValue));
        return BeanUtil.toBean(m,Article.class);
    }
}
