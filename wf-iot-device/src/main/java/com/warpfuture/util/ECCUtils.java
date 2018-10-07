package com.warpfuture.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Security;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/** @Auther: fido @Date: 2018/5/31 19:48 @Description: 解密 */
@Slf4j
public class ECCUtils {
  static {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
  }

  // 将Base64编码后的私钥转换成PrivateKey对象
  public static ECPrivateKey string2PrivateKey(String priStr) throws Exception {
    byte[] keyBytes = Base64.getDecoder().decode(priStr);
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
    ECPrivateKey privateKey = (ECPrivateKey) keyFactory.generatePrivate(keySpec);
    return privateKey;
  }

  // 私钥解密
  public static String decrypt(String encryptText, String privateKey) {
    byte[] bytes = null;
    try {
      Cipher cipher = Cipher.getInstance("ECIES", "BC");
      PrivateKey privateECKey = ECCUtils.string2PrivateKey(privateKey);
      cipher.init(Cipher.DECRYPT_MODE, privateECKey);
      bytes = cipher.doFinal(Base64.getDecoder().decode(encryptText));
    } catch (Exception e) {
      e.printStackTrace();
      log.info("ECC解密失败");
    }
    return new String(bytes);
  }
}
