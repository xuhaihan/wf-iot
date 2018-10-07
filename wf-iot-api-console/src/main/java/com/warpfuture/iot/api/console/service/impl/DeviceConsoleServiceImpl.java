package com.warpfuture.iot.api.console.service.impl;

import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.dto.DevInfo;
import com.warpfuture.dto.DeviceData;
import com.warpfuture.dto.HistoryDataInfo;
import com.warpfuture.entity.Device;
import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.Production;
import com.warpfuture.iot.api.console.feign.service.DeviceConsoleFeignService;
import com.warpfuture.iot.api.console.feign.service.DeviceInfoConsoleFeignService;
import com.warpfuture.iot.api.console.service.DeviceConsoleService;
import com.warpfuture.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DeviceConsoleServiceImpl implements DeviceConsoleService {

    @Autowired
    private DeviceConsoleFeignService deviceConsoleFeignService;

    @Autowired
    private DeviceInfoConsoleFeignService deviceInfoConsoleFeignService;

    @Override
    public ResultVO getDashBoardInfo(String accountId) {
        ResultVO result;
        try {
            result = deviceConsoleFeignService.getDashBoardInfo(accountId);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO<>().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO getDeviceData(String accountId) {
        ResultVO result;
        try {
            result = deviceConsoleFeignService.getDeviceData(accountId);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO<>().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO getAllDeviceByProductionId(Production production, PageModel pageModel) {
        ResultVO result;
        try {
            result = deviceInfoConsoleFeignService.getAllDeviceByProductionId(production, pageModel.getPageIndex(), pageModel.getPageSize());
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO getDeviceByDeviceId(Device device) {
        ResultVO<Device> result;
        try {
            result = deviceInfoConsoleFeignService.getDeviceByDeviceId(device);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO findHistoryDataByDeviceId(HistoryDataInfo dataInfo, PageModel pageModel) {
        ResultVO result;
        try {
            result = deviceConsoleFeignService.findHistoryDataByDeviceId(dataInfo,pageModel.getPageSize(),pageModel.getPageIndex());
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }
}
