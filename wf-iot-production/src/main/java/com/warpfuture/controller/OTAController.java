package com.warpfuture.controller;

import com.warpfuture.constant.Constant;
import com.warpfuture.dto.UploadDeviceInfo;
import com.warpfuture.entity.OTAInfo;
import com.warpfuture.entity.PageModel;
import com.warpfuture.exception.PermissionFailException;
import com.warpfuture.service.OTAService;
import com.warpfuture.vo.OTAUpdateInfo;
import com.warpfuture.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/** Created by fido on 2018/4/26. */
@RestController
@RequestMapping("/ota")
public class OTAController {
  @Autowired private OTAService otaService;

  private Logger logger = LoggerFactory.getLogger(OTAController.class);

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  public ResultVO create(@RequestBody OTAInfo otaInfo) {
    logger.info("--创建OTA升级信息--：" + otaInfo);
    ResultVO resultVO = null;
    try {
      OTAInfo result = otaService.createOTA(otaInfo);
      resultVO = new ResultVO(Constant.SUCCESS, "创建ota成功", otaInfo);
    } catch (PermissionFailException e) {
      logger.debug(e.getMessage());
      return new ResultVO(Constant.FAIL, e.getMessage());
    } catch (Exception e) {
      logger.debug("创建ota发生异常");
      return new ResultVO(Constant.FAIL, "创建ota错误");
    }
    return resultVO;
  }

  @RequestMapping(value = "/query", method = RequestMethod.POST)
  public ResultVO queryByProductionId(
      @RequestBody OTAInfo otaInfo,
      @RequestParam Integer pageSize,
      @RequestParam Integer pageIndex) {
    ResultVO resultVO = null;
    try {
      String accountId = otaInfo.getAccountId();
      String productionId = otaInfo.getProductionId();
      PageModel<OTAInfo> otaInfos =
          otaService.queryByProductionId(accountId, productionId, pageSize, pageIndex);
      resultVO = new ResultVO(Constant.SUCCESS, "查询ota列表成功", otaInfos);
    } catch (PermissionFailException e) {
      logger.debug(e.getMessage());
      return new ResultVO(Constant.FAIL, e.getMessage());
    } catch (Exception e) {
      logger.debug("查询ota列表权限错误");
      return new ResultVO(Constant.FAIL, "查询ota列表错误");
    }
    return resultVO;
  }

  @RequestMapping(value = "/disable", method = RequestMethod.POST)
  public ResultVO disable(@RequestBody OTAInfo queryOtaInfo) {
    ResultVO resultVO = null;
    try {
      String accountId = queryOtaInfo.getAccountId();
      String productionId = queryOtaInfo.getProductionId();
      String otaId = queryOtaInfo.getOtaId();
      OTAInfo otaInfo = otaService.disable(accountId, productionId, otaId);
      return new ResultVO(Constant.SUCCESS, "禁用OTA成功", otaInfo);
    } catch (PermissionFailException e) {
      logger.debug(e.getMessage());
      return new ResultVO(Constant.FAIL, e.getMessage());
    } catch (Exception e) {
      logger.debug("禁用ota错误");
      return new ResultVO(Constant.FAIL, "禁用OTA错误");
    }
  }

  @RequestMapping(value = "/uploading", method = RequestMethod.POST)
  public ResultVO uploading(@RequestBody UploadDeviceInfo uploadDeviceInfo) {
    ResultVO resultVO = null;
    OTAUpdateInfo otaUpdateInfo =
        otaService.uploading(
            uploadDeviceInfo.getProductionKey(),
            uploadDeviceInfo.getOriginOtaVersion(),
            uploadDeviceInfo.getDeviceId(),
            uploadDeviceInfo.getExtensions());
    if (otaUpdateInfo == null) {
      return new ResultVO(Constant.OTA_NONEDDTOUPDATE, "暂无升级信息");
    } else {
      return new ResultVO(Constant.OTA_NEEDTOUPDATE, "需要升级", otaUpdateInfo);
    }
  }
}
