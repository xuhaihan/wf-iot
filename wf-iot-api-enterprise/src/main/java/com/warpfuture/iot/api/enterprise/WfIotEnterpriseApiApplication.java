package com.warpfuture.iot.api.enterprise;

import com.warpfuture.config.CorsFilterUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

@SpringBootApplication
@EnableFeignClients
public class WfIotEnterpriseApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WfIotEnterpriseApiApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean corsFilterBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(CorsFilterUtils.getAllowAllCorsFilter());
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }
}