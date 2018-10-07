package com.warpfuture.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by 徐海瀚 on 2018/4/21.
 */
@Slf4j
public class JwtCommonUtil {

    // 该方法使用HS256算法和Secret:WFIOT生成signKey
    private static Key getKeyInstance() {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("WFIOT");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        return signingKey;
    }

    // 使用HS256签名算法和生成的signingKey最终的Token,claims中是有效载荷
    public static String createJsonWebToken(Map<String, Object> claims) {
        long nowMillis = System.currentTimeMillis(); // 当前毫秒数
        long ttlMillis = 24 * 60 * 60 * 1000; // 有效时间毫秒数
        return Jwts.builder()
                .setClaims(claims)
                .setId(get32UUID())
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(new Date(nowMillis + ttlMillis))
                .signWith(SignatureAlgorithm.HS256, getKeyInstance())
                .compact();
    }

    // 解析Token，同时也能验证Token，当验证失败返回null
    public static Claims parserJsonWebToken(String jwt) {
        try {
            return Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(jwt).getBody();
        } catch (Exception e) {
            log.error("json web token verify failed:{}", e.getMessage());
            return null;
        }
    }

    /**
     * 获得32个长度的十六进制的UUID
     *
     * @return UUID
     */
    private static String get32UUID() {
        UUID id = UUID.randomUUID();
        String[] idd = id.toString().split("-");
        return idd[0] + idd[1] + idd[2] + idd[3] + idd[4];
    }

    public static void main(String[] args) {
        String accountId = "1231";
        Map<String,Object> map = new HashMap<>();
        map.put("accountId",accountId);
        String jsonWebToken = createJsonWebToken(map);
        System.out.println(jsonWebToken);
    }

}
