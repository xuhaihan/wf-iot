package com.warpfuture.dto;

import lombok.Data;
import lombok.ToString;

/** Created by fido on 2018/5/17. */
@ToString
@Data
public class ConnectAutoDto {
  private String accountId;
  private String productionId;
  private String deviceId;
  private String userId;
  private String applicationId;
  private Long keepTime;
  private Long startTime;
}
