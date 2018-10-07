package com.warpfuture.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/** Created by fido on 2018/4/14. redis的配置 */
@Component
public class RedisConfiguration extends CachingConfigurerSupport {
  @Value("${spring.redis.host}")
  private String host;

  @Value("${spring.redis.port}")
  private int port;

  @Value("${spring.redis.timeout}")
  private int timeout;

  @Bean
  public JedisPool redisPoolFactory() {
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout);
    return jedisPool;
  }
}
