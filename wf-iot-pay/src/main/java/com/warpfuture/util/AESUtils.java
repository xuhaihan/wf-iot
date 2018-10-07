package com.warpfuture.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

/** @Auther: fido @Date: 2018/5/28 17:41 @Description: AES加密解密工具类，用于退款 */
public class AESUtils {
  /** 密钥算法 */
  private static final String ALGORITHM = "AES";
  /** 加解密算法/工作模式/填充方式 */
  private static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS5Padding";

  private static SecretKeySpec getKey(String key) {
    return new SecretKeySpec(MD5Utils.MD5Encode(key, "UTF-8").toLowerCase().getBytes(), ALGORITHM);
  }
  /**
   * AES加密
   *
   * @param data
   * @return
   * @throws Exception
   */
  public static String encryptData(String data, String key) throws Exception {
    // 创建密码器
    Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
    // 初始化
    SecretKeySpec md5Key = getKey(key);
    cipher.init(Cipher.ENCRYPT_MODE, md5Key);
    return Base64Utils.encode(cipher.doFinal(data.getBytes()));
  }

  /**
   * AES解密
   *
   * @param base64Data
   * @return
   * @throws Exception
   */
  public static String decryptData(String base64Data, String key) throws Exception {
    Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
    SecretKeySpec md5Key = getKey(key);
    cipher.init(Cipher.DECRYPT_MODE, md5Key);
    return new String(cipher.doFinal(Base64Utils.decode(base64Data)));
  }
}
