package com.warpfuture.repository;

import com.warpfuture.entity.Device;
import com.warpfuture.entity.PageModel;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/** Created by fido on 2018/4/23. 获得产品下在线，离线的设备信息 */
public interface DeviceInfoRepository {

  /**
   * 给设备打上标签分组信息
   *
   * @param accountId
   * @param deviceId
   * @param tagId
   */
  public void addTag(String accountId, String deviceId, String tagId);

  /**
   * 根据条件查询对应的设备列表
   *
   * @param query
   * @return
   */
  public List<Device> queryDeviceList(Query query);

  /**
   * 将设备从某个标签移除
   *
   * @param accountId
   * @param deviceId
   * @param tagId
   */
  public void removeTag(String accountId, String deviceId, String tagId);

  /**
   * 查找某个设备
   *
   * @param deviceId
   * @return
   */
  public Device findByDeviceId(String accountId, String productionId, String deviceId);

  /**
   * 更新设备信息
   *
   * @param device
   */
  public void update(Device device);

  /**
   * 查找产品下的在线设备列表
   *
   * @param productionId
   * @param pageIndex
   * @param pageSize
   * @return
   */
  public PageModel<Device> getOnlineDevicesByProd(
      String productionId, Integer pageIndex, Integer pageSize);

  /**
   * 查找产品下的离线设备列表
   *
   * @param productionId
   * @param pageIndex
   * @param pageSize
   * @return
   */
  public PageModel<Device> getOffLineDevicesByProd(
      String productionId, Integer pageIndex, Integer pageSize);

  /**
   * 查找产品下的所有设备列表
   *
   * @param productionId
   * @param pageIndex
   * @param pageSize
   * @return
   */
  public PageModel<Device> getAllDevicesByProd(
      String productionId, Integer pageIndex, Integer pageSize);

  /**
   * 查找账户下的所有在线设备
   *
   * @param accountId
   * @param pageIndex
   * @param pageSize
   * @return
   */
  public PageModel<Device> getOnlineDevicesByAccount(
      String accountId, Integer pageIndex, Integer pageSize);

  /**
   * 查找账户下的所有离线设备
   *
   * @param accountId
   * @param pageIndex
   * @param pageSize
   * @return
   */
  public PageModel<Device> getOffLineDeviceByAccount(
      String accountId, Integer pageIndex, Integer pageSize);

  /**
   * 根据设备主键查询设备
   *
   * @param deviceCloudId
   * @return
   */
  public Device getDeviceById(String deviceCloudId);

  /**
   * 查找账户下的所有设备
   *
   * @param accountId
   * @param pageIndex
   * @param pageSize
   * @return
   */
  public PageModel<Device> getAllDevicesByAccount(
      String accountId, Integer pageIndex, Integer pageSize);

  /**
   * * 更新设备的ota信息
   *
   * @param deviceId
   * @param newOTAVersion
   */
  public void updateOTAInfo(
      String accountId, String productionId, String deviceId, String newOTAVersion);
}
