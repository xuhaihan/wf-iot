package com.warpfuture.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/** Created by fido on 2018/4/23. OTA固件升级实体 */
@ToString
@Data
@Document(collection = "iot_ota_info")
public class OTAInfo {
  private String accountId; // 账户Id
  @Id private String otaId; // ota固件升级规则Id
  private String productionId; // 产品Id
  private String otaFileURL; // 固件存放地址
  private Long otaFileSize; // 固件文件大小
  private String otaHash; // 固件文件的md5 hash值
  private Integer otaStatus; // 固件状态 0:激活，1：禁用
  private Integer otaRole; // 规则：0：静默升级 1:用户确认升级
  private String otaVersion; // 本次固件版本信息
  private List<String> deviceIdList; // 若为空，代表对整个产品升级，否则，代表对多个设备升级
  private String otaDesc; // ota描述
  private Long publishTime; // 发布的时间，选择静默升级的时候存在此字段
  private Long createTime; // 创建时间
  private Long updateTime; // 更新时间
  private String otaName;
  private MultipartFile file;
}
