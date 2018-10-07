package com.warpfuture.dto;

import lombok.Data;
import lombok.ToString;

/** @Auther: fido @Date: 2018/6/15 11:18 @Description: 用于与protocol服务进行交互的数据传输对象 */
@ToString
@Data
public class ProtocolRouteDto {
  private String accountId;
  private String productionId;
  private String deviceId;
  private String userId;
  private String applicationId;
}
