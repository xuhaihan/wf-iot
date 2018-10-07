package com.warpfuture.repository;

import com.warpfuture.entity.OTAInfo;
import com.warpfuture.entity.PageModel;

/** Created by fido on 2018/4/26. */
public interface OTARepository {
  public void createOTA(OTAInfo otaInfo);

  public PageModel<OTAInfo> queryByProductionId(String productionId,Integer pageSize,Integer pageIndex);

  public void diable(String otaId);

  public OTAInfo queryByOTAId(String otaId);

  public OTAInfo getTheLatestOta(String productionId);
}
