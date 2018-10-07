package com.warpfuture.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.warpfuture.service")
public class WfIotProtocolApplication {
    public static void main(String[] args) {
        SpringApplication.run(WfIotProtocolApplication.class, args);
    }
}
