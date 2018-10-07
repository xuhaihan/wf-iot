package com.warpfuture.iot.api.console.jwt.core;


import com.warpfuture.iot.api.console.jwt.config.JwtProperties;
import com.warpfuture.iot.api.console.jwt.exception.JwtExpiryException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Date;

/**
 * jwt token解析
 *
 * @author scolia
 */
@Component
public class JwtResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(Jwt.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String token = webRequest.getHeader(jwtProperties.getToken().getHeader());
        Claims claims = jwtUtils.decode(token);
        Date now = new Date(System.currentTimeMillis());
        // 校验是否过期
        if (!claims.getExpiration().after(now)) {
            throw new JwtExpiryException();
        }
        // 组装数据
        JwtPayload payload = new JwtPayload();
        BeanMap beanMap = BeanMap.create(payload);
        beanMap.putAll(claims);
        return payload;
    }
}
