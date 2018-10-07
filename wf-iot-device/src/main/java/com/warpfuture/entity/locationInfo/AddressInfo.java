package com.warpfuture.entity.locationInfo;

import lombok.Data;
import lombok.ToString;

/** @Auther: fido @Date: 2018/6/1 09:39 @Description: 存储地址相关的信息 */
@ToString
@Data
public class AddressInfo {
  private String nation; // 国家
  private String province; // 省
  private String city; // 市
  private Integer adcode; // 行政区划代码
}
