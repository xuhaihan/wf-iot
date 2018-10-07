package com.warpfuture.util;

import com.warpfuture.exception.ParamsErrorException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/** Created by fido on 2018/4/17. */
@Log4j2
public class PayWxUtils {
  private Logger logger = LoggerFactory.getLogger(PayWxUtils.class);
  /**
   * 利用md5加密得到微信支付签名
   *
   * @param params
   * @param key
   * @return
   */
  public static String getMD5Sign(Map<String, String> params, String key) {
    String str = getParamsStr(params, key);
    return MD5Utils.MD5Encode(str, "UTF-8").toUpperCase();
  }

  public static String getSpbillIp() {
    try {
      InetAddress netAddress = InetAddress.getLocalHost();
      String spbillCreateIp = netAddress.getHostAddress();
      return spbillCreateIp;
    } catch (UnknownHostException e) {
      return null;
    }
  }

  public static String getParamsStr(Map<String, String> params, String apiKey) {
    StringBuilder paramsText = new StringBuilder();
    List<String> keys = new ArrayList<>();
    Iterator<String> keyIt = params.keySet().iterator();
    while (keyIt.hasNext()) {
      keys.add(keyIt.next());
    }
    Collections.sort(keys); // Sort the key by ASCII sequence
    int size = keys.size();
    for (int i = 0; i < size; i++) {
      String key = keys.get(i);
      String val = params.get(key);

      if (!"sign".equals(key) && StringUtils.isNotBlank(val)) {
        paramsText.append(key + "=" + val).append("&");
      }
    }
    String wholeText = paramsText.toString() + "key=" + apiKey;
    return wholeText;
  }

  /**
   * 根据HmacSHA256方式得到签名
   *
   * @param params
   * @param key
   * @return
   */
  public static String getSHA256Sign(Map<String, String> params, String key) {
    try {
      SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
      Mac mac = Mac.getInstance("HmacSHA256");
      mac.init(signingKey);
      String data = getParamsStr(params, key);
      return byte2hex(mac.doFinal(data.getBytes()));
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static String byte2hex(byte[] b) {
    StringBuilder hs = new StringBuilder();
    String stmp;
    for (int n = 0; b != null && n < b.length; n++) {
      stmp = Integer.toHexString(b[n] & 0XFF);
      if (stmp.length() == 1) hs.append('0');
      hs.append(stmp);
    }
    return hs.toString().toUpperCase();
  }

  /**
   * 获得随机字符串
   *
   * @return
   */
  public static String getNonceStr() {
    Random random = new Random();
    long val = random.nextLong();
    String res = DigestUtils.md5Hex(val + "wfiot").toUpperCase();
    if (32 < res.length()) return res.substring(0, 32);
    else return res;
  }

  /**
   * 校验签名
   *
   * @param params
   * @param key
   * @return
   */
  public static boolean verfitySign(Map<String, String> params, String key) {
    String originSign = params.get("sign");
    log.info("==原来的签名==" + originSign);
    if (originSign == null) {
      throw new ParamsErrorException("缺少签名");
    }
    params.remove("sign");
    String getSign = PayWxUtils.getMD5Sign(params, key);
    log.info("==计算得到的签名==" + getSign);
    if (!originSign.equals(getSign)) {
      return false;
    } else return true;
  }

  /**
   * 获得本机Ip
   *
   * @return
   */
  public static String getLocalIP() {
    String localIp = null; // 获得本机IP
    try {
      localIp = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      log.info("获得本机ip失败");
      e.printStackTrace();
    }
    return localIp;
  }

  /**
   * 成功时返回给微信支付服务器的响应信息 防止微信支付服务器一直发送回调通知
   *
   * @return
   */
  public static String returnSuccess() {
    String s =
        "<xml>\n"
            + "\n"
            + "   <return_code><![CDATA[SUCCESS]]></return_code>\n"
            + "   <return_msg><![CDATA[OK]]></return_msg>\n"
            + "</xml>";
    return s;
  }

  public static String returnFail() {
    String s =
        "<xml>\n"
            + "\n"
            + "   <return_code><![CDATA[FAIL]]></return_code>\n"
            + "   <return_msg><![CDATA[OK]]></return_msg>\n"
            + "</xml>";
    return s;
  }
}
