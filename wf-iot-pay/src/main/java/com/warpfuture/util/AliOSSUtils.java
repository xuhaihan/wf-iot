package com.warpfuture.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.warpfuture.constant.AliProperty;
import com.warpfuture.entity.RefundBook;
import com.warpfuture.repository.RefundBookRepostiory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/** @Auther: fido @Date: 2018/6/1 15:41 @Description: 从阿里云OSS对象存储中下载证书到本地 */
@Slf4j
@Component
public class AliOSSUtils {
  @Autowired private RefundBookRepostiory refundBookRepostiory;

  /**
   * 下载对应的证书文件到本地
   *
   * @param objectName
   * @return
   */
  public void downLoadFile(String objectName) throws IOException {
    // Endpoint以杭州为例，其它Region请按实际情况填写。
    String endpoint = AliProperty.endpoint;
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录
    // https://ram.console.aliyun.com 创建RAM账号。
    String accessKeyId = AliProperty.accessKeyId;
    String accessKeySecret = AliProperty.accessKeySecret;
    String bucketName = AliProperty.bucketName;
    // 创建OSSClient实例。
    OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    // 下载OSS文件到本地文件。
    InputStream inputStream =
        ossClient.getObject(new GetObjectRequest(bucketName, objectName)).getObjectContent();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
    byte[] buffer = new byte[8192];
    int bytesRead;
    while ((bytesRead = inputStream.read(buffer)) != -1) {
      outputStream.write(buffer, 0, bytesRead);
    }
    RefundBook refundBook = new RefundBook();
    refundBook.setContent(outputStream.toByteArray());
    refundBook.setRefundBookLocation(objectName);
    refundBookRepostiory.insert(refundBook);
    ossClient.shutdown();
  }
}
