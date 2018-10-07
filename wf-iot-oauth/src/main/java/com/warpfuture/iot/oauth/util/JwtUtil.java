package com.warpfuture.iot.oauth.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/** Created by 徐海瀚 on 2018/4/21. */
@Log4j2
public class JwtUtil {

  // 该方法使用HS256算法和Secret:WFIOT生成signKey
  private static Key getKeyInstance() {
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("WFIOT");
    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
    return signingKey;
  }

  // 使用HS256签名算法和生成的signingKey最终的Token,claims中是有效载荷
  public static String createJavaWebToken(Map<String, Object> claims) {
    long nowMillis = System.currentTimeMillis(); // 当前毫秒数
    long ttlMillis = 24 * 60 * 60 * 1000; // 有效时间毫秒数
    return Jwts.builder()
        .setClaims(claims)
        .setId(UUIDUtil.get32UUID())
        .setIssuedAt(new Date(nowMillis))
        .setExpiration(new Date(nowMillis + ttlMillis))
        .signWith(SignatureAlgorithm.HS256, getKeyInstance())
        .compact();
  }

  // 解析Token，同时也能验证Token，当验证失败返回null
  public static Map<String, Object> parserJavaWebToken(String jwt) {
    try {
      Map<String, Object> jwtClaims =
          Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(jwt).getBody();
      return jwtClaims;
    } catch (Exception e) {
      log.error("json web token verify failed:{}", e.getMessage());
      return null;
    }
  }

  public static void main(String[] args) {
    Map<String, Object> map = new HashMap<>();
    map.put("accountId", "111111");
    map.put("userId", "abc");
    map.put("applicationId", "abc123");
    String token = JwtUtil.createJavaWebToken(map);
    Map<String, Object> map1 = JwtUtil.parserJavaWebToken(token);
    log.info(map1);
  }
}
