package com.warpfuture.entity.locationInfo;

import lombok.Data;
import lombok.ToString;

/** @Auther: fido @Date: 2018/6/1 09:44 @Description: 调用腾讯定位位置服务接口返回的参数 */
@ToString
@Data
public class IPResponse {
  private Integer status; // 状态码
  private String message; // 状态说明
  private Result result; // 结果
}
