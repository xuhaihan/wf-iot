package com.warpfuture.service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/** Created by fido on 2018/4/14. */
public interface RedisService {

  /**
   * 设置key value，并设置过期时间
   *
   * @param key
   * @param value
   * @param expiredTime
   */
  public void setKeyAndValueWithTime(String key, String value, Long expiredTime);

    public void setKeyAndValueWithTime(String key, String value, Long expiredTime, TimeUnit time);

  /**
   * 设置key 和 value
   *
   * @param key
   * @param value
   */
  public void setKeyAndValue(String key, String value);

  /**
   * 通过key找value
   *
   * @param key
   * @return
   */
  public String getValue(String key);

  /**
   * 重新设置过期时间
   *
   * @param key
   * @param refreshTime
   */
  public void resetTime(String key, Long refreshTime);

  /**
   * 判断Key是否存在
   *
   * @param key
   * @return
   */
  public boolean isAlive(String key);

  /**
   * 获得该记录的TTL
   *
   * @param key
   * @return
   */
  public Long getExpired(String key);

  /**
   * 根据key模糊匹配
   *
   * @param pattern
   * @return
   */
  public Set<String> queryByPattern(String pattern);

  /**
   * 删除key
   *
   * @param key
   */
  public boolean delete(String key);

  /**
   * 模糊匹配删除
   *
   * @param pattern
   * @return
   */
  public boolean deleteByPattern(String pattern);
}
