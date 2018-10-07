package com.warpfuture.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/** Created by fido on 2018/5/16. */
public class DateUtils {
  public static String getTimeStamp() {
    int time = (int) (System.currentTimeMillis() / 1000);
    return String.valueOf(time);
  }

  public static Long getTime(String time) {
    Date date = null;
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
      date = sdf.parse(time);
      return date.getTime();
    } catch (Exception e) {
      e.printStackTrace();
      return 0L;
    }
  }
}
