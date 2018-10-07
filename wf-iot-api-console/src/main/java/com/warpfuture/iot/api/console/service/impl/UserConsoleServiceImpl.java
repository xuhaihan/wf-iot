package com.warpfuture.iot.api.console.service.impl;

import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.UserEntity;
import com.warpfuture.iot.api.console.feign.service.UserConsoleFeignService;
import com.warpfuture.iot.api.console.service.UserConsoleService;
import com.warpfuture.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserConsoleServiceImpl implements UserConsoleService {

    @Autowired
    private UserConsoleFeignService userConsoleFeignService;

    @Override
    public ResultVO createUer(UserEntity userEntity) {
        ResultVO result;
        try {
            result = userConsoleFeignService.createUer(userEntity);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO updateUser(UserEntity userEntity) {
        ResultVO result;
        try {
            result = userConsoleFeignService.updateUser(userEntity);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO deleteUser(UserEntity userEntity) {
        ResultVO result;
        try {
            result = userConsoleFeignService.deleteUser(userEntity.getUserId());
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO queryUser(UserEntity userEntity) {
        ResultVO result;
        try {
            result = userConsoleFeignService.queryUser(userEntity);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO listUser(String accountId, String applicationId, PageModel pageModel) {
        ResultVO result;
        try {
            result = userConsoleFeignService.listUser(accountId, applicationId, pageModel.getPageSize(), pageModel.getPageIndex());
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO updateBasic(UserEntity userEntity) {
        ResultVO result;
        try {
            result = userConsoleFeignService.updateBasic(userEntity);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO updateExtension(UserEntity userEntity) {
        ResultVO result;
        try {
            result = userConsoleFeignService.updateExtension(userEntity);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

}
