package com.warpfuture.util;

import com.warpfuture.entity.RouteInfo;

public class VertifyRouteUtils {
  public static boolean ifLegal(RouteInfo routeInfo, Long currentTime) {
    if (currentTime < routeInfo.getStartTime()) {
      return false;
    }
    if (currentTime > routeInfo.getEndTime()) {
      return false;
    }
    return true;
  }
}
