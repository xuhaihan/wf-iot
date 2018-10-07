package com.warpfuture.vo;

import com.warpfuture.entity.Device;
import com.warpfuture.entity.Tag;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/** Created by fido on 2018/4/23. 标签分组信息 */
@ToString
@Data
public class TagDevInfo {
  private Tag tag;
  private List<Device> deviceList;
}
