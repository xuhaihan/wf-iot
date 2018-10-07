package com.warpfuture.util;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

/** @Auther: fido @Date: 2018/5/28 17:39 @Description: Base64编码解码工具类 */
public class Base64Utils {
  public static byte[] decode(String encodedText) {
    final Base64.Decoder decoder = Base64.getDecoder();
    return decoder.decode(encodedText);
  }

  public static String encode(byte[] data) {
    final Base64.Encoder encoder = Base64.getEncoder();
    return encoder.encodeToString(data);
  }
}
