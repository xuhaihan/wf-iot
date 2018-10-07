package com.warpfuture.util;

import java.util.UUID;

/** Created by fido on 2018/4/16. 用于生成唯一Id */
public class IdUtils {
  public static String getId() {
    long currentTime = System.currentTimeMillis();
    String time = String.valueOf(currentTime);
    String uuid = UUID.randomUUID().toString();
    StringBuffer buffer = new StringBuffer();
    String strTime = String.valueOf(currentTime).substring(time.length() - 4, time.length());
    String id =
        buffer
            .append(uuid.substring(0, 4))
            .append(strTime)
            .append(uuid.substring(uuid.length() - 4, uuid.length()))
            .toString();
    return id;
  }
}
