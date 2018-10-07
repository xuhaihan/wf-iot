package com.warpfuture.util;

import com.warpfuture.constant.EncryptConstant;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/** @Auther: fido @Date: 2018/5/31 18:17 @Description: */
@Slf4j
public class ECCUtils {
  static {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
  }

  // 生成秘钥对
  private static KeyPair getKeyPair() throws Exception {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", "BC");
    keyPairGenerator.initialize(256, new SecureRandom());
    KeyPair keyPair = keyPairGenerator.generateKeyPair();
    return keyPair;
  }

  // 获取公钥(Base64编码)
  public static String getPublicKey(KeyPair keyPair) {
    ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
    byte[] bytes = publicKey.getEncoded();
    return new String(Base64.getEncoder().encode(bytes));
  }

  // 获取私钥(Base64编码)
  public static String getPrivateKey(KeyPair keyPair) {
    ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();
    byte[] bytes = privateKey.getEncoded();
    return new String(Base64.getEncoder().encode(bytes));
  }

  // 将Base64编码后的公钥转换成PublicKey对象
  public static ECPublicKey string2PublicKey(String pubStr) throws Exception {
    byte[] keyBytes = Base64.getDecoder().decode(pubStr);
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
    ECPublicKey publicKey = (ECPublicKey) keyFactory.generatePublic(keySpec);
    return publicKey;
  }

  // 将Base64编码后的私钥转换成PrivateKey对象
  public static ECPrivateKey string2PrivateKey(String priStr) throws Exception {
    byte[] keyBytes = Base64.getDecoder().decode(priStr);
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
    ECPrivateKey privateKey = (ECPrivateKey) keyFactory.generatePrivate(keySpec);
    return privateKey;
  }

  // 公钥加密
  public static String encrypt(String originText, String publicKey) throws Exception {
    Cipher cipher = Cipher.getInstance("ECIES", "BC");
    PublicKey publicECKey = ECCUtils.string2PublicKey(publicKey);
    cipher.init(Cipher.ENCRYPT_MODE, publicECKey);
    byte[] bytes = cipher.doFinal(originText.getBytes());
    return Base64.getEncoder().encodeToString(bytes);
  }

  // 私钥解密
  public static String decrypt(String encryptText, String privateKey) throws Exception {
    Cipher cipher = Cipher.getInstance("ECIES", "BC");
    PrivateKey privateECKey = ECCUtils.string2PrivateKey(privateKey);
    cipher.init(Cipher.DECRYPT_MODE, privateECKey);
    byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(encryptText));
    return new String(bytes);
  }

  public static Map<String, String> getECCKeyPair() {
    Map<String, String> map = new HashMap<>();
    try {
      KeyPair keyPair = ECCUtils.getKeyPair();
      String publicKeyStr = ECCUtils.getPublicKey(keyPair);
      String privateKeyStr = ECCUtils.getPrivateKey(keyPair);
      map.put(EncryptConstant.mapPublicKey, publicKeyStr);
      map.put(EncryptConstant.mapPrivateKey, privateKeyStr);
    } catch (Exception e) {
      log.info("==生成ECC加密算法密钥对时出错==");
    }
    return map;
  }

  public static void main(String[] args) throws Exception {
    Map<String, String> keyPair = ECCUtils.getECCKeyPair();
    String publicKeyStr = keyPair.get(EncryptConstant.mapPublicKey);
    String privateKeyStr = keyPair.get(EncryptConstant.mapPrivateKey);
    System.out.println("ECC公钥Base64编码:" + publicKeyStr);
    System.out.println("ECC私钥Base64编码:" + privateKeyStr);
    ECPublicKey publicKey = string2PublicKey(publicKeyStr);
    ECPrivateKey privateKey = string2PrivateKey(privateKeyStr);

    String publicEncrypt =
        encrypt(
            "3dcade5c2759455396c62b3adbeef880,ddd123,1527762116327",
            "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEFhn4rhwNqpxccqR9t6I0aEcHpWZW4112IoSZOJJ8nWCw4JPdH1BGsUNXWbLT073huRuSv94ip/EyKsbvAcZkvQ==");
    System.out.println(publicEncrypt);
    //    String privateDecrypt = decrypt(publicEncrypt, privateKeyStr);
//    String privateDecrypt =
//        decrypt(
//            "BPo2wmhQWKHM73T5+pKnZvoXtP664GW5ElygQ4yPa28fvFbv0lF+gRdt9s55/vLU+aa4ajpKbQaCs458qV8yyZu8Con1iryDxjQlNvp8uzSyF85H2++KX75kUaupfOW7DJi4F2eGqVwNHdJSN7tvxVwWwKpPKLl3DklSFvMOY2gvQk9tQeV9Uu7B",
//            "MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQg39GXLJw7V4YoHS3vd8cUt4bwiR1A7udDW5fDOANfLPegCgYIKoZIzj0DAQehRANCAASW6shv9JIuFOO0kPSCOROXdiFj1M1DQMafRjuU4+oBc+3F+9haqcT2wC+KMzQ+H0lU2/Pk556oiqmmwJioH7k0");
//    System.out.println("解密字符串==" + privateDecrypt);
  }
}
