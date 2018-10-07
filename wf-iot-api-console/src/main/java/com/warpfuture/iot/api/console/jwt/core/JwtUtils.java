package com.warpfuture.iot.api.console.jwt.core;


import com.warpfuture.iot.api.console.jwt.config.JwtProperties;
import com.warpfuture.iot.api.console.jwt.exception.JwtInvalidatedException;
import com.warpfuture.iot.api.console.jwt.exception.JwtMissException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @author scolia
 */
@Getter
@Setter
@Component
public class JwtUtils {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private JwtKeyPairFactory jwtKeyPairFactory;

    /**
     * 创建jwt
     *
     * @return jwt的字符串
     */
    @SuppressWarnings("unchecked")
    public String create(JwtPayload payload) {
        Map<String, Object> map = BeanMap.create(payload);
        JwtBuilder jwtBuilder = Jwts.builder()
                // 签发者
                .setIssuer(jwtProperties.getToken().getIssuer())
                // 签发时间
                .setIssuedAt(new Date())
                // 过期时间
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getToken().getExpiry()))
                // 私钥签名
                .signWith(SignatureAlgorithm.RS256, jwtKeyPairFactory.getKeyPair().getPrivate());

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            jwtBuilder.claim(entry.getKey(), entry.getValue());
        }
        return jwtBuilder.compact();
    }

    /**
     * 解码jwt, 获得claims, 不校验是否过期
     *
     * @param tokenStr token字符串
     * @return 自定义claims
     */
    public Claims decode(String tokenStr) {
        if (StringUtils.isEmpty(tokenStr)) {
            throw new JwtMissException();
        }
        try {
            Claims claims = Jwts.parser()
                    // 公钥验证
                    .setSigningKey(jwtKeyPairFactory.getKeyPair().getPublic())
                    .parseClaimsJws(tokenStr)
                    .getBody();
            if (!StringUtils.equals(claims.getIssuer(), jwtProperties.getToken().getIssuer())) {
                throw new JwtInvalidatedException();
            }
            return claims;
        } catch (Exception e) {
            throw new JwtInvalidatedException();
        }
    }

}
