package com.warpfuture.iot.api.enterprise.service.impl;

import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.dto.HistoryDataInfo;
import com.warpfuture.entity.Device;
import com.warpfuture.entity.Production;
import com.warpfuture.iot.api.enterprise.feign.service.DeviceEnterpriseFeignService;
import com.warpfuture.iot.api.enterprise.feign.service.DeviceHistoryDataFeignService;
import com.warpfuture.iot.api.enterprise.service.DeviceEnterpriseService;
import com.warpfuture.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeviceEnterpriseServiceImpl implements DeviceEnterpriseService {

    @Autowired
    private DeviceEnterpriseFeignService deviceEnterpriseFeignService;

    @Autowired
    private DeviceHistoryDataFeignService deviceHistoryDataFeignService;

//    @Autowired
//    private Sender sender;
//
//    @Autowired
//    private ObjectMapper objectMapper;

    @Override
    public ResultVO getOnlineDeviceByProductionId(Production production, Integer pageSize, Integer pageIndex) {
        ResultVO result;
        try {
            result = deviceEnterpriseFeignService.getOnlineDeviceByProductionId(production, pageSize, pageIndex);
        } catch (Exception e) {
            log.warn("Service Error : {}", e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO getOfflineDeviceByProductionId(Production production, Integer pageSize, Integer pageIndex) {
        ResultVO result;
        try {
            result = deviceEnterpriseFeignService.getOfflineDeviceByProductionId(production, pageSize, pageIndex);
        } catch (Exception e) {
            log.warn("Service Error : {}", e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO getAllDeviceByProductionId(Production production, Integer pageSize, Integer pageIndex) {
        ResultVO result;
        try {
            result = deviceEnterpriseFeignService.getAllDeviceByProductionId(production, pageSize, pageIndex);
        } catch (Exception e) {
            log.warn("Service Error : {}", e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO getOnlineDeviceByAccountId(String accountId, Integer pageSize, Integer pageIndex) {
        ResultVO result;
        try {
            result = deviceEnterpriseFeignService.getOnlineDeviceByAccountId(accountId, pageSize, pageIndex);
        } catch (Exception e) {
            log.warn("Service Error : {}", e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO getOfflineDeviceByAccountId(String accountId, Integer pageSize, Integer pageIndex) {
        ResultVO result;
        try {
            result = deviceEnterpriseFeignService.getOfflineDeviceByAccountId(accountId, pageSize, pageIndex);
        } catch (Exception e) {
            log.warn("Service Error : {}", e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO getAllDeviceByAccountId(String accountId, Integer pageSize, Integer pageIndex) {
        ResultVO result;
        try {
            result = deviceEnterpriseFeignService.getAllDeviceByAccountId(accountId, pageSize, pageIndex);
        } catch (Exception e) {
            log.warn("Service Error : {}", e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO findHistoryDataByDeviceId(HistoryDataInfo dataInfo, Integer pageSize, Integer pageIndex) {
        ResultVO result;
        try {
            result = deviceHistoryDataFeignService.findHistoryDataByDeviceId(dataInfo, pageSize, pageIndex);
        } catch (Exception e) {
            log.warn("Service Error : {}", e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO findHistoryDataByDeviceIdAndDeviceType(HistoryDataInfo dataInfo, Integer pageSize, Integer pageIndex) {
        ResultVO result;
        try {
            result = deviceHistoryDataFeignService.findHistoryDataByDeviceIdAndDeviceType(dataInfo, pageSize, pageIndex);
        } catch (Exception e) {
            log.warn("Service Error : {}", e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO findByHistoryDataIdAndDataTime(HistoryDataInfo dataInfo,Integer pageSize, Integer pageIndex) {
        ResultVO result;
        try {
            result = deviceHistoryDataFeignService.findByHistoryDataIdAndDataTime(dataInfo, pageSize, pageIndex);
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
            result = deviceEnterpriseFeignService.getDeviceByDeviceId(device);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO getAuthRecord(Device device) {
        ResultVO result;
        try {
            result = deviceEnterpriseFeignService.getAuthRecord(device);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }
}
