package com.warpfuture.vo;

import lombok.Data;
import lombok.ToString;

/** Created by fido on 2018/4/26. 若需要升级时返回给设备的信息 */
@ToString
@Data
public class OTAUpdateInfo {
  private String productionId; // 产品Id
  private String ootaFileURL; // 固件文件地址
  private Long otaFileSize; // 固件文件大小
  private String otaDesc; // 固件文件描述
  private String otaVersion; // 固件版本号
  private String otaHash; // 固件文件Hash值
}
