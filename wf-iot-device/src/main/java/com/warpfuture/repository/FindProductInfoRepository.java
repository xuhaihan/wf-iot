package com.warpfuture.repository;

import com.warpfuture.entity.Production;

/** Created by fido on 2018/4/13. 设备登录认证 */
public interface FindProductInfoRepository {

  /**
   * 根据产品密码查找
   *
   * @param productionKey
   * @return
   */
  public Production findByProductionKey(String productionKey);

  /**
   * 根据产品公钥查找
   *
   * @param productionPublicSecure
   * @return
   */
  public Production findByPPS(String productionPublicSecure);

  /**
   * 根据产品Id查询
   *
   * @param productionId
   * @return
   */
  public Production findById(String productionId);
}
