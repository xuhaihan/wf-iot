package com.warpfuture.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

/** Created by fido on 2018/5/1. RestTemplate的配置 */
@Configuration
public class RestTemplateConfig {

  @Bean
  public RestTemplate restTemplate(ClientHttpRequestFactory simpleFactory) {
    RestTemplate restTemplate = new RestTemplate(simpleFactory);
    // 处理中文乱码
    restTemplate
        .getMessageConverters()
        .set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    return restTemplate;
  }

  @Bean
  public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
    SimpleClientHttpRequestFactory simpleFactory = new SimpleClientHttpRequestFactory();
    simpleFactory.setReadTimeout(5000); // ms
    simpleFactory.setConnectTimeout(15000); // ms
    return simpleFactory;
  }
}
