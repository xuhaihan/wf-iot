package com.warpfuture.repository;

import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.Production;

/** Created by fido on 2018/4/13. 产品类 mongodb层操作接口 */
public interface ProductionRepository {

  PageModel<Production> findByAccountId(String accountId, Integer pageSize, Integer pageIndex);

  /**
   * 根据产品Id查找产品详情
   *
   * @param productionId
   * @return
   */
  Production findByProductionId(String productionId);

  /**
   * 新增产品
   *
   * @param production
   */
  void saveProduction(Production production);

  /**
   * 重新生成产品密码
   *
   * @param productionId
   * @param productionKey
   */
  void updateProductionKey(String productionId, String productionKey);

  /**
   * 重新生成密钥对
   *
   * @param productionId
   * @param productionPublicSecure
   * @param productionPrivateSecurt
   */
  void regenKeyPair(
      String productionId, String productionPublicSecure, String productionPrivateSecurt);

  /**
   * 更新产品信息
   *
   * @param production
   */
  void updateProductionInfo(Production production);

  /**
   * 发布产品
   *
   * @param productionId
   */
  void publishProduction(String productionId);

  /**
   * 下架产品
   *
   * @param productionId
   */
  void revokeProduction(String productionId);

  /**
   * 删除产品
   *
   * @param productionId
   */
  void deleteProduction(String productionId);

  /**
   * 根据名称查找
   *
   * @param productionName
   * @return
   */
  PageModel<Production> findByName(
      String accountId, String productionName, Integer pageSize, Integer pageIndex);

  /**
   * 根据产品密码查找
   *
   * @param productionKey
   * @return
   */
  public Production findByProductionKey(String productionKey);

  public Production getByProductionName(String accountId,String productName);
}
