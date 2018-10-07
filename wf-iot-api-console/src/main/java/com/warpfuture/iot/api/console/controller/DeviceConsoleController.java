package com.warpfuture.iot.api.console.controller;

import com.warpfuture.constant.PageConstant;
import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.dto.HistoryDataInfo;
import com.warpfuture.entity.Device;
import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.Production;
import com.warpfuture.iot.api.console.service.DeviceConsoleService;
import com.warpfuture.util.CompareUtil;
import com.warpfuture.util.PageUtils;
import com.warpfuture.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class DeviceConsoleController {

    @Autowired
    private DeviceConsoleService deviceConsoleService;

    @PostMapping(value = "/deviceData/getDeviceData")
    public ResultVO getDeviceData(HttpServletRequest request) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (CompareUtil.strNotNull(accountId)) {
            return deviceConsoleService.getDeviceData(accountId);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/dashboard/getDashBoardInfo")
    public ResultVO getDashBoardInfo(HttpServletRequest request) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (CompareUtil.strNotNull(accountId)) {
            return deviceConsoleService.getDashBoardInfo(accountId);
        }
        return new ResultVO<>().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/deviceDetails/getAllDevsByProd")
    public ResultVO getAllDeviceByProductionId(HttpServletRequest request, @RequestBody Production production, @RequestParam(value = "pageSize", required = false) Integer pageSize, @RequestParam(value = "pageIndex", required = false) Integer pageIndex) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (production != null && CompareUtil.strNotNull(production.getProductionId())) {
            production.setAccountId(accountId);
            PageModel pageModel = PageUtils.dealPage(pageSize, pageIndex, PageConstant.DEVICE_DEFAULT_PAGE_SIZE);
            return deviceConsoleService.getAllDeviceByProductionId(production, pageModel);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/deviceDetails/getDeviceByDeviceId")
    public ResultVO getDeviceByDeviceId(HttpServletRequest request, @RequestBody Device device) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (CompareUtil.deviceNotNull(device) && CompareUtil.strNotNull(accountId)) {
            device.setAccountId(accountId);
            return deviceConsoleService.getDeviceByDeviceId(device);
        }
        return new ResultVO<>().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/historyData/findByHistoryDataId")
    public ResultVO findHistoryDataByDeviceId(@RequestBody HistoryDataInfo dataInfo,
                                       @RequestParam(required = false) Integer pageSize,
                                       @RequestParam(required = false) Integer pageIndex,
                                       HttpServletRequest request) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (CompareUtil.strNotNull(accountId) && CompareUtil.historyDataIdNotNull(dataInfo)) {
            dataInfo.setAccountId(accountId);
            PageModel pageModel = PageUtils.dealPage(pageSize, pageIndex, PageConstant.APPLICATION_DEFAULT_PAGE_SIZE);
            return deviceConsoleService.findHistoryDataByDeviceId(dataInfo, pageModel);
        }
        return new ResultVO<>().fail(ResponseMsg.PARAMS_NULL);
    }

}
