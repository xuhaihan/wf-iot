package com.warpfuture.iot.api.enterprise.service.impl;

import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.UserEntity;
import com.warpfuture.iot.api.enterprise.feign.service.UserEnterpriseFeignService;
import com.warpfuture.iot.api.enterprise.service.UserEnterpriseService;
import com.warpfuture.util.CompareUtil;
import com.warpfuture.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserEnterpriseServiceImpl implements UserEnterpriseService {

    @Autowired
    private UserEnterpriseFeignService userEnterpriseFeignService;

    @Override
    public ResultVO<UserEntity> userLogin(String userAccount, String password) {
        try {
            ResultVO<UserEntity> result = userEnterpriseFeignService.userLogin(userAccount, password);
            if (result != null && result.getData() != null) {
                UserEntity user = result.getData();
                String jwt = userEnterpriseFeignService.getEnterpriseJwt(user.getAccountId(), user.getUserId(), user.getApplicationId());
                if (CompareUtil.strNotNull(jwt)) {
                    Map<String, Object> expand = user.getExpand();
                    if (expand == null) {
                        expand = new HashMap<>();
                    }
                    expand.put("token", jwt);
                    user.setExpand(expand);
                    return result;
                }
            }
        } catch (Exception e) {
            log.warn("Service Error : {}", e.getMessage());
            return new ResultVO<UserEntity>().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return new ResultVO<UserEntity>().fail(ResponseMsg.REQUEST_ERROR);
    }

    @Override
    public ResultVO createUer(UserEntity userEntity) {
        ResultVO result;
        try {
            result = userEnterpriseFeignService.createUer(userEntity);
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
            result = userEnterpriseFeignService.updateUser(userEntity);
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
            result = userEnterpriseFeignService.deleteUser(userEntity.getUserId());
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO<UserEntity> queryUser(UserEntity userEntity) {
        ResultVO result;
        try {
            result = userEnterpriseFeignService.queryUser(userEntity);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO<UserEntity>().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO<UserEntity>().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO updateBasic(UserEntity userEntity) {
        ResultVO result;
        try {
            result = userEnterpriseFeignService.updateBasic(userEntity);
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
            result = userEnterpriseFeignService.updateExtension(userEntity);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }
}
