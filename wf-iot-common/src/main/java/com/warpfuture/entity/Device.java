package com.warpfuture.entity;

import com.warpfuture.dto.TagInfo;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/** Created by fido on 2018/4/13. 设备类 */
@Document(collection = "iot_device_info")
@Data
@ToString
public class Device implements Serializable {

  private String accountId;
  private String productionId;
  @Id private String deviceCloudId; // 云端设备id
  private String deviceId; // 企业传过来的id
  private String deviceIp; // 设备Ip
  private Map<String, String> locations; // 设备的相关地理信息
  private String originOtaVersion; // 固件版本
  private Long firstConnectTime; // 激活时间
  private Long lastConnectTime; // 最后一次连接时间
  private Long sendMsgTime; // 最后一次发包时间
  private Long updateTime; // 更新信息时间
  private Set<TagInfo> tagList; // 设备标签列表
  private Map<String, String> extensions; // 设备拓展属性

  public Device() {
  }

  public Device(String deviceId,String productionId) {
    this.deviceId = deviceId;
    this.productionId = productionId;
  }
}
