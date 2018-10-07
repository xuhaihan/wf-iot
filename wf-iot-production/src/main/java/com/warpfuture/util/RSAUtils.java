package com.warpfuture.util;

import com.warpfuture.constant.EncryptConstant;

import javax.crypto.*;
import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/** Created by fido on 2018/4/13. 用于生成RSA加密所需要的公钥密钥 */
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
   * 生成公钥私钥对
   *
   * @return
   */
  public static Map<String, String> generateKeyPair() {
    try {
      KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(encryptRole);
      // 密钥位数
      keyPairGen.initialize(1024);
      // 密钥对
      KeyPair keyPair = keyPairGen.generateKeyPair();
      // 公钥
      PublicKey publicKey = keyPair.getPublic();
      // 私钥
      PrivateKey privateKey = keyPair.getPrivate();
      // 得到公钥字符串
      String publicKeyString = getKeyString(publicKey);
      // 得到私钥字符串
      String privateKeyString = getKeyString(privateKey);
      // 将生成的密钥对返回
      Map<String, String> map = new HashMap<String, String>();
      map.put(EncryptConstant.mapPublicKey, publicKeyString);
      map.put(EncryptConstant.mapPrivateKey, privateKeyString);
      return map;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static PublicKey getPublicKey(String key) throws Exception {
    byte[] keyBytes = Base64.getDecoder().decode(key);
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(encryptRole);
    PublicKey publicKey = keyFactory.generatePublic(keySpec);
    return publicKey;
  }

  public static String getKeyString(Key key) throws Exception {
    byte[] keyBytes = key.getEncoded();
    String s = new String(Base64.getEncoder().encode(keyBytes));
    return s;
  }

  /**
   * 使用公钥对明文进行加密
   *
   * @param publicKey
   * @param plainText
   * @return
   */
  public static String encrypt(String publicKey, String plainText) {
    try {
      cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
      byte[] enBytes = cipher.doFinal(plainText.getBytes());
      return Base64.getEncoder().encodeToString(enBytes);
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
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

  public static void main(String[] args) throws Exception {

  }
}
