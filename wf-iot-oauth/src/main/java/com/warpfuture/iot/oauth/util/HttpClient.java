package com.warpfuture.iot.oauth.util;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class HttpClient {

  private static RestTemplate restTemplate = new RestTemplate();

    /**
     * post请求
     * @param contentType 请求数据类型
     * @param url 请求路径
     * @param data  请求体数据
     * @return
     */
  public static String post(MediaType contentType, String url, String data) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(contentType);
    HttpEntity<String> requestEntity = new HttpEntity<>(data, headers);
    ResponseEntity<String> response =
        restTemplate.exchange(
            url, HttpMethod.POST, requestEntity, String.class); // 用code换取token和openid
    return response.getBody();
  }

  /**
   * get 请求
   *
   * @param contentType 请求内容类型
   * @param url 请求路径(可带参数与不带参数)
   * @return
   */
  public static String get(MediaType contentType, String url) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(contentType);
    HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
    ResponseEntity<String> response =
        restTemplate.exchange(
            url, HttpMethod.GET, requestEntity, String.class); // 用code换取token和openid
    return response.getBody();
  }
}
