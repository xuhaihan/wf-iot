package com.warpfuture.repository;

import com.warpfuture.entity.Device;
import com.warpfuture.entity.PageModel;

import java.util.List;

/** Created by fido on 2018/4/13. */
public interface DeviceRepository {

  /**
   * 添加设备记录
   *
   * @param device
   */
  public void save(Device device);

  /**
   * 根据设备Id查找设备详情
   *
   * @param deviceId
   * @return
   */
  public Device findById(String accountId, String productionId, String deviceId);

  /**
   * 更新设备状态信息
   *
   * @param device
   */
  public void updateDevice(Device device);

  /**
   * 查找产品下的在线设备
   *
   * @param productionId
   * @param pageSize
   * @param pageIndex
   * @return
   */
  public PageModel<Device> getOnlineByProductionId(
      String productionId, Integer pageSize, Integer pageIndex);

  /**
   * 产品下的离线设备
   *
   * @param productionId
   * @param pageSize
   * @param pageIndex
   * @return
   */
  public PageModel<Device> getOffLineByProductionId(
      String productionId, Integer pageSize, Integer pageIndex);

  /**
   * 获得账户下的在线设备，用于地图展示
   *
   * @param accountId
   * @return
   */
  public List<Device> getByAccountId(String accountId);

  /**
   * 获取该日期的设备在线量
   *
   * @param accountId
   * @return
   */
  public Long getTodayOnlineNums(String accountId, Long startTime);

  /**
   * 获得该日期的设备激活量
   *
   * @param accountId
   * @param startTime
   * @return
   */
  public Long getTodayActive(String accountId, Long startTime);

  /**
   * 获得账户下的设备总量
   *
   * @param accountId
   * @return
   */
  public Long getDeviceCount(String accountId);

  /**
   * 管理设备的属性
   *
   * @param device
   * @return
   */
  public Device updateExtensions(Device device);

  /**
   * 账户近七日设备活跃量
   *
   * @param accountId
   * @param startTime
   * @return
   */
  public Long getSevenDaysActiveNums(String accountId, Long startTime);
}
