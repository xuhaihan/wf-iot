package com.warpfuture.iot.oauth.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1 {
  public static String getSHA1String(String decript) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-1");
      digest.update(decript.getBytes());
      byte messageDigest[] = digest.digest();
      // Create Hex String
      StringBuffer hexString = new StringBuffer();
      // 字节数组转换为 十六进制 数
      for (int i = 0; i < messageDigest.length; i++) {
        String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
        if (shaHex.length() < 2) {
          hexString.append(0);
        }
        hexString.append(shaHex);
      }
      return hexString.toString();

    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return "";
  }
}
