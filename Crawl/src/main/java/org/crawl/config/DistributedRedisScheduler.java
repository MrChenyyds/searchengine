package org.crawl.config;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.RedisScheduler;

public class DistributedRedisScheduler extends RedisScheduler {

    private Redisson redisson = new RedisLockApplication().redisson();

    protected JedisPool pool;

    private static final String QUEUE_PREFIX = "queue_";

    private static final String SET_PREFIX = "set_";

    private static final String ITEM_PREFIX = "item_";

    private static final String DISTRIBUTE_KEY = "DestributeKey";

    public DistributedRedisScheduler(String host) {
        this(new JedisPool(new JedisPoolConfig(), host));
    }

    public DistributedRedisScheduler(JedisPool pool) {
        super(pool);
        this.pool=pool;
    }

    @Override
    public void resetDuplicateCheck(Task task) {
        try (Jedis jedis = pool.getResource()) {
            jedis.del(getSetKey(task));
        }
    }

    @Override
    public boolean isDuplicate(Request request, Task task) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.sadd(getSetKey(task), request.getUrl()) == 0;
        }

    }

    @Override
    protected void pushWhenNoDuplicate(Request request, Task task) {
        Jedis jedis = pool.getResource();
        RLock redissonLock = redisson.getLock(DISTRIBUTE_KEY);
        try {
            redissonLock.lock();
            jedis.rpush(getQueueKey(task), request.getUrl());
            if (checkForAdditionalInfo(request)) {
                String field = DigestUtils.sha1Hex(request.getUrl());
                String value = JSON.toJSONString(request);
                jedis.hset((ITEM_PREFIX + task.getUUID()), field, value);
            }
        } finally {
            jedis.close();
            redissonLock.unlock();
        }
    }

    private boolean checkForAdditionalInfo(Request request) {
        if (request == null) {
            return false;
        }

        if (!request.getHeaders().isEmpty() || !request.getCookies().isEmpty()) {
            return true;
        }

        if (StringUtils.isNotBlank(request.getCharset()) || StringUtils.isNotBlank(request.getMethod())) {
            return true;
        }

        if (request.isBinaryContent() || request.getRequestBody() != null) {
            return true;
        }

        if (request.getExtras() != null && !request.getExtras().isEmpty()) {
            return true;
        }
        if (request.getPriority() != 0L) {
            return true;
        }

        return false;
    }

    @Override
    public  Request poll(Task task) {
        try (Jedis jedis = pool.getResource()) {
            RLock redissonLock = redisson.getLock(DISTRIBUTE_KEY);
            try {
                redissonLock.lock();
                String url = jedis.lpop(getQueueKey(task));
                if (url == null) {
                    return null;
                }
                String key = ITEM_PREFIX + task.getUUID();
                String field = DigestUtils.sha1Hex(url);
                byte[] bytes = jedis.hget(key.getBytes(), field.getBytes());
                if (bytes != null) {
                    Request o = JSON.parseObject(new String(bytes), Request.class);
                    return o;
                }
                Request request = new Request(url);
                return request;
            }finally {
                redissonLock.unlock();
            }

        }
    }

    protected String getSetKey(Task task) {
        return SET_PREFIX + task.getUUID();
    }

    protected String getQueueKey(Task task) {
        return QUEUE_PREFIX + task.getUUID();
    }

    protected String getItemKey(Task task) {
        return ITEM_PREFIX + task.getUUID();
    }

    @Override
    public int getLeftRequestsCount(Task task) {
        try (Jedis jedis = pool.getResource()) {
            Long size = jedis.llen(getQueueKey(task));
            return size.intValue();
        }
    }

    @Override
    public int getTotalRequestsCount(Task task) {
        try (Jedis jedis = pool.getResource()) {
            Long size = jedis.scard(getSetKey(task));
            return size.intValue();
        }
    }
}
