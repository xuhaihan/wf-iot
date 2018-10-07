package com.warpfuture.iot.api.enterprise.controller;

import com.warpfuture.constant.PageConstant;
import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.dto.HistoryDataInfo;
import com.warpfuture.entity.Device;
import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.Production;
import com.warpfuture.iot.api.enterprise.service.DeviceEnterpriseService;
import com.warpfuture.util.CompareUtil;
import com.warpfuture.util.PageUtils;
import com.warpfuture.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class DeviceEnterpriseController {

    @Autowired
    private DeviceEnterpriseService deviceEnterpriseService;

    @GetMapping(value = "/dev/online/{pid}")
    public ResultVO getOnlineDeviceByProductionId(@PathVariable String pid,
                                                  @RequestParam(required = false) Integer pageSize,
                                                  @RequestParam(required = false) Integer pageIndex,
                                                  HttpServletRequest request) {
//        Production production = ModelBeanUtil.getBeanFromModel(model, "production", Production.class);
        String accountId = String.valueOf(request.getAttribute("accountId"));
        Production production = new Production(pid, accountId);
        if (CompareUtil.productionIdNotNull(production)) {
            PageModel pageModel = PageUtils.dealPage(pageSize, pageIndex, PageConstant.DEVICE_DEFAULT_PAGE_SIZE);
            return deviceEnterpriseService.getOnlineDeviceByProductionId(production, pageModel.getPageSize(), pageModel.getPageIndex());
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @GetMapping(value = "/dev/offline/{pid}")
    public ResultVO getOfflineDeviceByProductionId(@PathVariable String pid,
                                                   @RequestParam(required = false) Integer pageSize,
                                                   @RequestParam(required = false) Integer pageIndex,
                                                   HttpServletRequest request) {
//        Production production = ModelBeanUtil.getBeanFromModel(model, "production", Production.class);
        String accountId = String.valueOf(request.getAttribute("accountId"));
        Production production = new Production(pid, accountId);
        if (CompareUtil.productionIdNotNull(production)) {
            PageModel pageModel = PageUtils.dealPage(pageSize, pageIndex, PageConstant.DEVICE_DEFAULT_PAGE_SIZE);
            return deviceEnterpriseService.getOfflineDeviceByProductionId(production, pageModel.getPageSize(), pageModel.getPageIndex());
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @GetMapping(value = "/dev/all/{pid}")
    public ResultVO getAllDeviceByProductionId(@PathVariable String pid,
                                               @RequestParam(required = false) Integer pageSize,
                                               @RequestParam(required = false) Integer pageIndex,
                                               HttpServletRequest request) {
//        Production production = ModelBeanUtil.getBeanFromModel(model, "production", Production.class);
        String accountId = String.valueOf(request.getAttribute("accountId"));
        Production production = new Production(pid, accountId);
        if (CompareUtil.productionIdNotNull(production)) {
            PageModel pageModel = PageUtils.dealPage(pageSize, pageIndex, PageConstant.DEVICE_DEFAULT_PAGE_SIZE);
            return deviceEnterpriseService.getAllDeviceByProductionId(production, pageModel.getPageSize(), pageModel.getPageIndex());
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @GetMapping(value = "/dev/online")
    public ResultVO getOnlineDeviceByAccountId(HttpServletRequest request,
                                               @RequestParam(required = false) Integer pageSize,
                                               @RequestParam(required = false) Integer pageIndex) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (!StringUtils.isEmpty(accountId)) {
            PageModel pageModel = PageUtils.dealPage(pageSize, pageIndex, PageConstant.DEVICE_DEFAULT_PAGE_SIZE);
            return deviceEnterpriseService.getOnlineDeviceByAccountId(accountId, pageModel.getPageSize(), pageModel.getPageIndex());
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @GetMapping(value = "/dev/offline")
    public ResultVO getOfflineDeviceByAccountId(HttpServletRequest request,
                                                @RequestParam(required = false) Integer pageSize,
                                                @RequestParam(required = false) Integer pageIndex) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (!StringUtils.isEmpty(accountId)) {
            PageModel pageModel = PageUtils.dealPage(pageSize, pageIndex, PageConstant.DEVICE_DEFAULT_PAGE_SIZE);
            return deviceEnterpriseService.getOfflineDeviceByAccountId(accountId, pageModel.getPageSize(), pageModel.getPageIndex());
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @GetMapping(value = "/dev/all")
    public ResultVO getAllDeviceByAccountId(HttpServletRequest request,
                                            @RequestParam(required = false) Integer pageSize,
                                            @RequestParam(required = false) Integer pageIndex) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (!StringUtils.isEmpty(accountId)) {
            PageModel pageModel = PageUtils.dealPage(pageSize, pageIndex, PageConstant.DEVICE_DEFAULT_PAGE_SIZE);
            return deviceEnterpriseService.getAllDeviceByAccountId(accountId, pageModel.getPageSize(), pageModel.getPageIndex());
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/history-data/id")
    public ResultVO findHistoryDataByDeviceId(@RequestBody HistoryDataInfo dataInfo,
                                              HttpServletRequest request,
                                              @RequestParam(required = false) Integer pageSize,
                                              @RequestParam(required = false) Integer pageIndex) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (CompareUtil.historyDataIdNotNull(dataInfo) && CompareUtil.strNotNull(accountId)) {
            dataInfo.setAccountId(accountId);
            PageModel pageModel = PageUtils.dealPage(pageSize, pageIndex, PageConstant.DEVICE_DEFAULT_PAGE_SIZE);
            return deviceEnterpriseService.findHistoryDataByDeviceId(dataInfo, pageModel.getPageSize(), pageModel.getPageIndex());
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/history-data/type")
    public ResultVO findHistoryDataByDeviceIdAndDeviceType(@RequestBody HistoryDataInfo dataInfo,
                                                           HttpServletRequest request,
                                                           @RequestParam(required = false) Integer pageSize,
                                                           @RequestParam(required = false) Integer pageIndex) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (CompareUtil.historyDataTypeNotNull(dataInfo) && CompareUtil.strNotNull(accountId)) {
            dataInfo.setAccountId(accountId);
            PageModel pageModel = PageUtils.dealPage(pageSize, pageIndex, PageConstant.DEVICE_DEFAULT_PAGE_SIZE);
            return deviceEnterpriseService.findHistoryDataByDeviceIdAndDeviceType(dataInfo, pageModel.getPageSize(), pageModel.getPageIndex());
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/history-data/time")
    public ResultVO findByHistoryDataIdAndDataTime(@RequestBody HistoryDataInfo dataInfo,
                                                   HttpServletRequest request,
                                                   @RequestParam(required = false) Integer pageSize,
                                                   @RequestParam(required = false) Integer pageIndex) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (CompareUtil.historyDataTimeNotNull(dataInfo) && CompareUtil.strNotNull(accountId)) {
            dataInfo.setAccountId(accountId);
            PageModel pageModel = PageUtils.dealPage(pageSize, pageIndex, PageConstant.DEVICE_DEFAULT_PAGE_SIZE);
            return deviceEnterpriseService.findByHistoryDataIdAndDataTime(dataInfo, pageModel.getPageSize(), pageModel.getPageIndex());
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @GetMapping(value = "/dev/{deviceId}/{productionId}")
    public ResultVO getDeviceByDeviceId(HttpServletRequest request,
                                        @PathVariable String deviceId,
                                        @PathVariable String productionId) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        Device device = new Device(deviceId, productionId);
        if (CompareUtil.deviceNotNull(device) && CompareUtil.strNotNull(accountId)) {
            device.setAccountId(accountId);
            return deviceEnterpriseService.getDeviceByDeviceId(device);
        }
        return new ResultVO<>().fail(ResponseMsg.PARAMS_NULL);
    }

    @GetMapping(value = "/dev/{deviceId}/record/{productionId}")
    public ResultVO getAuthRecord(HttpServletRequest request,
                                  @PathVariable String deviceId,
                                  @PathVariable String productionId) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (CompareUtil.strNotNull(accountId) && CompareUtil.strNotNull(deviceId) && CompareUtil.strNotNull(productionId)) {
            Device device = new Device(deviceId, productionId);
            device.setAccountId(accountId);
            return deviceEnterpriseService.getAuthRecord(device);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

}
