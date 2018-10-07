package com.warpfuture.iot.api.console.service.impl;

import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.entity.ApplicationEntity;
import com.warpfuture.entity.PageModel;
import com.warpfuture.iot.api.console.feign.service.ApplicationConsoleFeignService;
import com.warpfuture.iot.api.console.service.ApplicationConsoleService;
import com.warpfuture.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApplicationConsoleServiceImpl implements ApplicationConsoleService {

    @Autowired
    private ApplicationConsoleFeignService applicationConsoleFeignService;

    @Override
    public ResultVO createApplication(ApplicationEntity applicationEntity) {
        ResultVO result;
        try {
            result = applicationConsoleFeignService.createApp(applicationEntity);
        } catch (Exception e) {
            log.warn("createApplication Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO updateApplication(ApplicationEntity applicationEntity) {
        ResultVO result;
        try {
            result = applicationConsoleFeignService.updateApp(applicationEntity);
        } catch (Exception e) {
            log.warn("updateApplication Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO queryApplication(ApplicationEntity applicationEntity) {
        ResultVO result;
        try {
            result = applicationConsoleFeignService.queryApp(applicationEntity);
        } catch (Exception e) {
            log.warn("updateApplication Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO deleteApplication(String applicationId) {
        ResultVO result;
        try {
            result = applicationConsoleFeignService.deleteApp(applicationId);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO listApplication(String accountId, PageModel pageModel) {
        ResultVO result;
        try {
            result = applicationConsoleFeignService.listApplication(accountId, pageModel.getPageSize(), pageModel.getPageIndex());
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO updateAppBasic(ApplicationEntity applicationEntity) {
        ResultVO result;
        try {
            result = applicationConsoleFeignService.updateAppBasic(applicationEntity);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO updateAppExpansion(ApplicationEntity applicationEntity) {
        ResultVO result;
        try {
            result = applicationConsoleFeignService.updateAppExpansion(applicationEntity);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO updateAppOAuthWays(ApplicationEntity applicationEntity) {
        ResultVO result;
        try {
            result = applicationConsoleFeignService.updateAppOAuthWays(applicationEntity);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }
}
