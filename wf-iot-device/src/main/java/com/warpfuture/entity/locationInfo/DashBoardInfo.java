package com.warpfuture.entity.locationInfo;

import lombok.Data;
import lombok.ToString;

/** @Auther: fido @Date: 2018/6/29 16:51 @Description:用于地图展示 */
@ToString
@Data
public class DashBoardInfo {
  private String city; // 城市
  private String lat; // 纬度
  private String lng; // 经度
  private Long nums; // 数量
}
