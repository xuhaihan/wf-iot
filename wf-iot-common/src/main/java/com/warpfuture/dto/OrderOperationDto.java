package com.warpfuture.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

/** Created by fido on 2018/5/16. 用于接收调用支付相关接口时传递的参数 */
@ToString
@Data
public class OrderOperationDto {
  private String accountId;
  private String merchantId;
  private Map<String, String> params;
}
