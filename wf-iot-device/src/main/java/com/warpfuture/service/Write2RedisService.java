package com.warpfuture.service;

import com.warpfuture.entity.RouteInfo;

/** @Author fido @Date 2018/5/19 @Description */
public interface Write2RedisService {
  public void writeRoute2Redis(RouteInfo routeInfo);
}
