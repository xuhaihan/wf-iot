package com.warpfuture.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** Created by fido on 2018/5/11. 鉴权数据表 */
@Document(collection = "iot_auth_route_info")
@ToString
@Data
public class RouteInfo {
  @Id private String routeInfoId;
  private String accountId;
  private String productionId;
  private String applicationId;
  private String userId;
  private String deviceId;
  private Long startTime;
  private Long keepTime;
  private Boolean report; // 是否允许设备上报
  private Boolean broadCast; // 是否允许广播
  private Long endTime; // 用于找出过期的记录
}
