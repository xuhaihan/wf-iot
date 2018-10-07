package com.warpfuture.service;

import com.warpfuture.entity.OTAInfo;
import com.warpfuture.entity.PageModel;
import com.warpfuture.vo.OTAUpdateInfo;

import java.util.Map;

/** Created by fido on 2018/4/26. */
public interface OTAService {
  /**
   * @param otaInfo
   * @return
   */
  public OTAInfo createOTA(OTAInfo otaInfo);

  public PageModel<OTAInfo> queryByProductionId(
      String accountId, String productionId, Integer pageSize, Integer pageIndex);

  public OTAInfo disable(String accountId, String productionId, String otaId);

  public OTAUpdateInfo uploading(
      String productionKey,
      String originOtaVersion,
      String deviceId,
      Map<String, Object> extensions);
}
