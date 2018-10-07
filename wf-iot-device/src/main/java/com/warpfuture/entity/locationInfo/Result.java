package com.warpfuture.entity.locationInfo;

import lombok.Data;
import lombok.ToString;

/** @Auther: fido @Date: 2018/6/1 09:42 @Description: */
@ToString
@Data
public class Result {
  private Location location; // 定位坐标等信息
  private AddressInfo ad_info; // 地理位置等信息
}
