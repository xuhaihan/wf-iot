package com.warpfuture.service;

import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.Production;

import java.util.Map;

/** Created by fido on 2018/4/15. */
public interface ProductionService {

  public Production createProduction(Production production);

  public PageModel<Production> findByAccountId(
      String accountId, Integer pageSize, Integer pageIndex);

  public Production findById(String productionId, String accountId);

  public Production updateProductionInfo(Production production);

  public String regenKey(String productionId, String accountId);

  public String regenKeyPair(String productionId, String accountId);

  public Production publishProduction(String productionId, String accountId);

  public Production revokeProduction(String productionId, String accountId);

  public void deleteProduction(String productionId, String accountId);

  public PageModel<Production> findByName(
      String accountId, String productionName, Integer pageSize, Integer pageIndex);

  public Production updateExtensions(Production production);
}
