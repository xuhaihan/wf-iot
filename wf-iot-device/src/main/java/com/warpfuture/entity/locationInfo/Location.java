package com.warpfuture.entity.locationInfo;

import lombok.Data;
import lombok.ToString;

/** @Auther: fido @Date: 2018/6/1 09:41 @Description: 地理位置坐标等信息 */
@ToString
@Data
public class Location {
  private String lat; // 纬度
  private String lng; // 经度
}
