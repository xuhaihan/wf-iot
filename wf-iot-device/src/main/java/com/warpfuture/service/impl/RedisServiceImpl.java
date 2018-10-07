package com.warpfuture.service.impl;

import com.warpfuture.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/** Created by fido on 2018/4/14. redis相关操作 */
@Service
public class RedisServiceImpl implements RedisService {
  @Autowired private StringRedisTemplate redisTemplate;

  @Override
  public void setKeyAndValueWithTime(String key, String value, Long expiredTime) {
    redisTemplate.opsForValue().set(key, value, expiredTime, TimeUnit.SECONDS);
  }

  @Override
  public void setKeyAndValueWithTime(String key, String value, Long expiredTime, TimeUnit time) {
    redisTemplate.opsForValue().set(key, value, expiredTime, time);
  }

  @Override
  public void setKeyAndValue(String key, String value) {
    redisTemplate.opsForValue().set(key, value);
  }

  @Override
  public boolean isAlive(String key) {
    return redisTemplate.hasKey(key);
  }

  @Override
  public String getValue(String key) {
    return (String) redisTemplate.opsForValue().get(key);
  }

  @Override
  public void resetTime(String key, Long refreshTime) {
    redisTemplate.expire(key, refreshTime, TimeUnit.SECONDS);
  }

  @Override
  public Long getExpired(String key) {
    return redisTemplate.getExpire(key, TimeUnit.SECONDS);
  }

  @Override
  public Set<String> queryByPattern(String pattern) {
    return redisTemplate.keys(pattern);
  }

  @Override
  public boolean delete(String key) {
    return redisTemplate.delete(key);
  }

  @Override
  public boolean deleteByPattern(String pattern) {
    Set<String> result = this.queryByPattern(pattern);
    long deleteNums = redisTemplate.delete(result);
    if (deleteNums != 0) {
      return true;
    }
    return false;
  }
}
