package com.warpfuture.util;

import org.apache.commons.lang.StringUtils;

import java.security.MessageDigest;

/** Created by fido on 2018/5/16. */
public class MD5Utils {
  public static String MD5Encode(String origin, String charset) {
    String md5 = null;
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      if (StringUtils.isBlank(charset)) {
        md5 = byteArrayToHexString(md.digest(origin.getBytes("UTF-8")));
      } else {
        md5 = byteArrayToHexString(md.digest(origin.getBytes(charset)));
      }

    } catch (Exception ex) {
    }

    if (md5 != null) md5 = md5.toUpperCase();

    return md5;
  }

  private static String byteArrayToHexString(byte b[]) {
    StringBuffer resultSb = new StringBuffer();
    for (int i = 0; i < b.length; i++) {
      resultSb.append(byteToHexString(b[i]));
    }

    return resultSb.toString();
  }

  private static String byteToHexString(byte b) {
    int n = b;
    if (n < 0) n += 256;
    int d1 = n / 16;
    int d2 = n % 16;

    return hexDigits[d1] + hexDigits[d2];
  }

  private static final String hexDigits[] = {
    "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"
  };
}
