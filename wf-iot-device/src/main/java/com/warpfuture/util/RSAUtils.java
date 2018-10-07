package com.warpfuture.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.SyncFailedException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.TimeZone;

/** Created by fido on 2018/4/15. RSA解密工具类 */
public class RSAUtils {
  private static final String encryptRole = "RSA";
  private static Cipher cipher;

  static {
    try {
      cipher = Cipher.getInstance(encryptRole);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    }
  }

  /**
   * 获得私钥
   *
   * @param key
   * @return
   * @throws Exception
   */
  public static PrivateKey getPrivateKey(String key) throws Exception {
    byte[] keyBytes = Base64.getDecoder().decode(key);
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(encryptRole);
    PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
    return privateKey;
  }

  /**
   * 使用私钥对密文进行解密
   *
   * @param privateKey 私钥
   * @param enStr 密文
   * @return
   */
  public static String decrypt(String privateKey, String enStr) {
    try {
      cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));
      byte[] deBytes = cipher.doFinal(Base64.getDecoder().decode(enStr));
      return new String(deBytes);
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
