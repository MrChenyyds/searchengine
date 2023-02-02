package org.crawl.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


@ConfigurationProperties(
        prefix = "spring.redis"
)
@Configuration
public class RedisConfig {
    private String host;
    private String port;
    private String password;


    @Bean
    JedisPool redisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(30);
        jedisPoolConfig.setMaxWaitMillis(1200);

        return new JedisPool(jedisPoolConfig, host, Integer.parseInt(port), 1000, password);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
