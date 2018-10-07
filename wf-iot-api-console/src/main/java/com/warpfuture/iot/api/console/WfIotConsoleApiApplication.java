package com.warpfuture.iot.api.console;

import com.warpfuture.config.CorsFilterUtils;
import com.warpfuture.iot.api.console.jwt.JwtAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;


@EnableFeignClients
@AutoConfigureAfter(JwtAutoConfiguration.class)
@SpringBootApplication
public class WfIotConsoleApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(WfIotConsoleApiApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean corsFilterBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(CorsFilterUtils.getAllowAllCorsFilter());
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }
}