package com.warpfuture.iot.api.console.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.warpfuture.constant.AliProperty;
import com.warpfuture.iot.api.console.dto.AliResult;
import com.warpfuture.iot.api.console.exception.SendAliOssException;
import com.warpfuture.util.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Calendar;

@Slf4j
public class AliOssUtil {

    private static OSSClient ossClient;

    /**
     * 向阿里Oss发送文件数据
     * 文件路径 ：  /wf-iot/年-月-{mark}-name-accountId-productionId-randomId.xxx
     *
     * @param file
     * @return
     */
    public static AliResult putObject(MultipartFile file, String fileName, String mark) throws SendAliOssException {
        try {
            ossClient = new OSSClient(AliProperty.endpoint, AliProperty.accessKeyId, AliProperty.accessKeySecret);

            ObjectMetadata metadata = getMetaData(file);

            String key = getKey(fileName, file.getOriginalFilename(), mark);

            PutObjectResult result = ossClient.putObject(AliProperty.bucketName, key, file.getInputStream(), metadata);
            log.info("Send To AliOss: ResultHash: {}, File: {}", result.getETag(), fileName);
            String url = AliProperty.endpoint + "/" + AliProperty.bucketName + "/" + key;
            return new AliResult(url, key, result.getETag());
        } catch (OSSException e) {
            throw new SendAliOssException("发送云端文件异常");
        } catch (IOException x) {
            throw new SendAliOssException("发送云端文件异常!");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 使用时间月份作为一级文件夹
     *
     * @return
     */
    private static String getTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1);
    }

    /**
     * 获得ota的key
     *
     * @param fileName
     * @param originalFilename
     * @param mark
     * @return
     */
    private static String getKey(String fileName, String originalFilename, String mark) {
        return getTime() + "-" + mark + "-" + fileName + "-" + IdUtils.getId() + "." + StringUtils.substringAfterLast(originalFilename, ".");
    }

    /**
     * 文件元数据信息
     *
     * @param file
     * @return
     */
    private static ObjectMetadata getMetaData(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        if (file != null) {
            metadata.setContentType(file.getContentType());
            metadata.setContentEncoding(CharEncoding.UTF_8);
            metadata.setCacheControl("no-cache");
            metadata.setContentDisposition(file.getOriginalFilename());
            metadata.setContentLength(file.getSize());
        } else {
            throw new SendAliOssException("文件为空");
        }
        return metadata;
    }

}
