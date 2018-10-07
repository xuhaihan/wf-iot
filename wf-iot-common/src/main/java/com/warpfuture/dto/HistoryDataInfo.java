package com.warpfuture.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/** Created by fido on 2018/5/14. 设备历史通信数据的数据传输对象 */
@Data
@ToString
public class HistoryDataInfo {
  private String accountId;
  private String productionId;
  private String deviceId;
  private Integer dataType;
  private Long startTime;
  private Long endTime;
}
