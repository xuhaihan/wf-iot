package com.warpfuture.iot.api.console.jwt;


import com.warpfuture.iot.api.console.jwt.config.JwtProperties;
import com.warpfuture.iot.api.console.jwt.core.JwtResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author scolia
 */
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
@ComponentScan
public class JwtAutoConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private JwtResolver jwtResolver;

    /**
     * jwt 参数解析
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(jwtResolver);
    }
}
