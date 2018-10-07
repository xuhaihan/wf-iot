package com.warpfuture.dto;

import lombok.Data;
import lombok.ToString;

/** Created by fido on 2018/4/23. */
@Data
@ToString
public class DeviceData {
  private String accountId; // 账户Id
  private Long activateNums; // 设备总量
  private Long currentActivateNums; // 今日设备激活量
  private Long currentOnlineNums; // 今日设备在线量
  private Long sevenDaysOnlineNums; // 近七日设备在线量
}
