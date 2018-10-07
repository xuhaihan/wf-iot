package com.warpfuture.controller;

import com.warpfuture.constant.Constant;
import com.warpfuture.entity.Device;
import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.Production;
import com.warpfuture.entity.RouteInfo;
import com.warpfuture.exception.PermissionFailException;
import com.warpfuture.service.AuthRouteService;
import com.warpfuture.service.DeviceInfoService;
import com.warpfuture.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Created by fido on 2018/4/29. */
@RestController
@RequestMapping("/deviceDetails")
public class DevInfoController {
  @Autowired private DeviceInfoService deviceInfoService;
  @Autowired private AuthRouteService authRouteService;
  private Logger logger = LoggerFactory.getLogger(DevInfoController.class);

  @RequestMapping(value = "/getOnlineDevsByProd", method = RequestMethod.POST)
  public ResultVO<PageModel<Device>> getOnlineDevsByProd(
      @RequestBody Production production,
      @RequestParam Integer pageIndex,
      @RequestParam Integer pageSize) {
    ResultVO resultVO = null;
    try {
      String accountId = production.getAccountId();
      String productionId = production.getProductionId();
      PageModel<Device> pageModel =
          deviceInfoService.getOnlineDevsByProd(accountId, productionId, pageSize, pageIndex);
      resultVO = new ResultVO(Constant.SUCCESS, "查询成功", pageModel);
    } catch (PermissionFailException e) {
      resultVO = new ResultVO(Constant.FAIL, e.getMessage(), null);
    } catch (Exception e) {
      logger.error("获取在线设备错误" + e.getMessage());
      resultVO = new ResultVO(Constant.FAIL, "获取在线设备错误", null);
    }
    return resultVO;
  }

  @RequestMapping(value = "/getOffLineDevsByProd", method = RequestMethod.POST)
  public ResultVO<PageModel<Device>> getOffLineDevsByProd(
      @RequestBody Production production,
      @RequestParam Integer pageIndex,
      @RequestParam Integer pageSize) {
    ResultVO resultVO = null;
    try {
      String accountId = production.getAccountId();
      String productionId = production.getProductionId();
      PageModel<Device> pageModel =
          deviceInfoService.getOffLineDevsByProd(accountId, productionId, pageSize, pageIndex);
      resultVO = new ResultVO(Constant.SUCCESS, "查询成功", pageModel);
    } catch (PermissionFailException e) {
      resultVO = new ResultVO(Constant.FAIL, e.getMessage(), null);
    } catch (Exception e) {
      logger.error("获取离线设备错误" + e.getMessage());
      resultVO = new ResultVO(Constant.FAIL, "获取离线设备错误", null);
    }
    return resultVO;
  }

  @RequestMapping(value = "/getAllDevsByProd", method = RequestMethod.POST)
  public ResultVO<PageModel<Device>> getAllDevsByProd(
      @RequestBody Production production,
      @RequestParam Integer pageIndex,
      @RequestParam Integer pageSize) {
    ResultVO resultVO = null;
    try {
      String accountId = production.getAccountId();
      String productionId = production.getProductionId();
      PageModel<Device> pageModel =
          deviceInfoService.getAllDevsByProd(accountId, productionId, pageSize, pageIndex);
      resultVO = new ResultVO(Constant.SUCCESS, "查询成功", pageModel);
    } catch (PermissionFailException e) {
      resultVO = new ResultVO(Constant.FAIL, e.getMessage(), null);
    } catch (Exception e) {
      logger.error("获取所有设备错误" + e.getMessage());
      resultVO = new ResultVO(Constant.FAIL, "获取所有设备错误", null);
    }
    return resultVO;
  }

  @RequestMapping(value = "/getOnlineDevsByAccount", method = RequestMethod.POST)
  public ResultVO<PageModel<Device>> getOnlineDevsByAccount(
      @RequestParam String accountId,
      @RequestParam Integer pageIndex,
      @RequestParam Integer pageSize) {
    ResultVO resultVO = null;
    try {
      PageModel<Device> pageModel =
          deviceInfoService.getOnlineDevsByAccount(accountId, pageSize, pageIndex);
      resultVO = new ResultVO(Constant.SUCCESS, "查询成功", pageModel);
    } catch (PermissionFailException e) {
      resultVO = new ResultVO(Constant.FAIL, e.getMessage(), null);
    } catch (Exception e) {
      logger.error("获取在线设备错误" + e.getMessage());
      resultVO = new ResultVO(Constant.FAIL, "获取在线设备错误", null);
    }
    return resultVO;
  }

  @RequestMapping(value = "/getOffLineDevsByAccount", method = RequestMethod.POST)
  public ResultVO<PageModel<Device>> getOffLineDevsByAccount(
      @RequestParam String accountId,
      @RequestParam Integer pageIndex,
      @RequestParam Integer pageSize) {
    ResultVO resultVO = null;
    try {
      PageModel<Device> pageModel =
          deviceInfoService.getOffLineDevsByAccount(accountId, pageSize, pageIndex);
      resultVO = new ResultVO(Constant.SUCCESS, "查询成功", pageModel);
    } catch (PermissionFailException e) {
      resultVO = new ResultVO(Constant.FAIL, e.getMessage(), null);
    } catch (Exception e) {
      logger.error("获取离线设备错误" + e.getMessage());
      resultVO = new ResultVO(Constant.FAIL, "获取离线设备错误", null);
    }
    return resultVO;
  }

  @RequestMapping(value = "/getAllDevsByAccount", method = RequestMethod.POST)
  public ResultVO<PageModel<Device>> getAllDevsByAccount(
      @RequestParam String accountId,
      @RequestParam Integer pageIndex,
      @RequestParam Integer pageSize) {
    ResultVO resultVO = null;
    try {
      PageModel<Device> pageModel =
          deviceInfoService.getAllDevsByAccount(accountId, pageSize, pageIndex);
      resultVO = new ResultVO(Constant.SUCCESS, "查询成功", pageModel);
    } catch (PermissionFailException e) {
      resultVO = new ResultVO(Constant.FAIL, e.getMessage(), null);
    } catch (Exception e) {
      logger.error("获取所有设备错误" + e.getMessage());
      resultVO = new ResultVO(Constant.FAIL, "获取所有设备错误", null);
    }
    return resultVO;
  }

  @RequestMapping(value = "/getDeviceByDeviceId", method = RequestMethod.POST)
  public ResultVO<Device> getDeviceByDeviceId(@RequestBody Device device) {
    ResultVO<Device> result = null;
    try {
      Device findDevice =
          deviceInfoService.getDeviceByDevId(
              device.getAccountId(), device.getProductionId(), device.getDeviceId());
      return new ResultVO<>(Constant.SUCCESS, "查询单个设备成功", findDevice);
    } catch (PermissionFailException e) {
      logger.info("查询单个设备失败");
      return new ResultVO<>(Constant.FAIL, e.getMessage());
    }
  }

  @RequestMapping(value = "/getAuthRecord", method = RequestMethod.POST)
  public ResultVO<List<RouteInfo>> getAuthRecord(@RequestBody Device device) {
    ResultVO<List<RouteInfo>> result = null;
    try {
      String accountId = device.getAccountId();
      String productionId = device.getProductionId();
      String deviceId = device.getDeviceId();
      List<RouteInfo> list =
          authRouteService.findDeviceAuthRecord(accountId, productionId, deviceId);
      return new ResultVO<>(Constant.SUCCESS, "查询关于该设备的使用情况成功", list);
    } catch (Exception e) {
      logger.info("查询失败");
      return new ResultVO<>(Constant.FAIL, e.getMessage());
    }
  }
}
