package com.warpfuture.util;

import com.warpfuture.entity.RefundBook;
import com.warpfuture.repository.RefundBookRepostiory;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayInputStream;
import java.security.KeyStore;

/** @Auther: fido @Date: 2018/5/29 22:03 @Description: 微信退款的双向证书 */
@Slf4j
@Component
public class ClientCustomSSL {
  @Autowired private RefundBookRepostiory refundBookRepostiory;
  @Autowired private AliOSSUtils aliOSSUtils;

  public String doRefund(String url, String data, String refundLocation, String mchId)
      throws Exception {
    /** 注意PKCS12证书 是从微信商户平台-》账户设置-》 API安全 中下载的 */
    KeyStore keyStore = KeyStore.getInstance("PKCS12");
    if (refundBookRepostiory.findById(refundLocation) == null) {
      aliOSSUtils.downLoadFile(refundLocation);
    }
    RefundBook nowRefundBook = refundBookRepostiory.findById(refundLocation);
    ByteArrayInputStream instream = new ByteArrayInputStream(nowRefundBook.getContent());
    try {
      /** 此处要改 */
      keyStore.load(instream, mchId.toCharArray()); // 这里写密码..默认是你的MCHID
    } finally {
      instream.close();
    }
    // Trust own CA and all self-signed certs
    /** 此处要改 */
    SSLContext sslcontext =
        SSLContexts.custom().loadKeyMaterial(keyStore, mchId.toCharArray()).build();
    // Allow TLSv1 protocol only
    SSLConnectionSocketFactory sslsf =
        new SSLConnectionSocketFactory(
            sslcontext,
            new String[] {"TLSv1"},
            null,
            SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
    CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
    try {
      HttpPost httpost = new HttpPost(url); // 设置响应头信息
      httpost.addHeader("Connection", "keep-alive");
      httpost.addHeader("Accept", "*/*");
      httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
      httpost.addHeader("Host", "api.mch.weixin.qq.com");
      httpost.addHeader("X-Requested-With", "XMLHttpRequest");
      httpost.addHeader("Cache-Control", "max-age=0");
      httpost.setEntity(new StringEntity(data, "UTF-8"));
      CloseableHttpResponse response = httpclient.execute(httpost);
      try {
        HttpEntity entity = response.getEntity();
        String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
        EntityUtils.consume(entity);
        return jsonStr;
      } finally {
        response.close();
      }
    } finally {
      httpclient.close();
    }
  }
}
