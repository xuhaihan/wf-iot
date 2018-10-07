package com.warpfuture.constant;

/** Created by fido on 2018/4/14. Redis里的一些常量参数 */
public class RedisConstant {
  public static final String ALIVE_KEY = "iot:alive:"; // 设备在线
  public static final String USING_KEY = "iot:using:"; // 用户-设备权限
  public static final String RELATION_KEY = "iot:relation:"; // 用户-应用权限
  public static final Long ALIVE_EXPIRE = 40L; // 维持设备在线状态的过期时间,毫秒为单位，默认为40秒
  public static final String READY_ROUTE = "iot:ready:"; // 设置用户-设备权限时，开始使用时间还未到，则暂存起来
  public static final String REPORT_KEY = "iot:report:"; // 设备上报
  public static final String EXPIRE_CHANNEL = "__keyevent@0__:expired";
}
