package com.warpfuture.iot.api.enterprise.controller;

import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.entity.UserEntity;
import com.warpfuture.iot.api.enterprise.dto.UserLogin;
import com.warpfuture.iot.api.enterprise.service.UserEnterpriseService;
import com.warpfuture.util.CompareUtil;
import com.warpfuture.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserEnterpriseController {

    @Autowired
    private UserEnterpriseService userEnterpriseService;

    /**
     * 用户登录
     *
     * @param userLogin
     * @return
     */
    @PostMapping(value = "/login")
    public ResultVO userLogin(@RequestBody UserLogin userLogin) {
        if (userLogin != null && CompareUtil.strNotNull(userLogin.getUserAccount()) && CompareUtil.strNotNull(userLogin.getPassword())) {
            return userEnterpriseService.userLogin(userLogin.getUserAccount(), userLogin.getPassword());
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    /**
     * 新建用户
     *
     * @param request
     * @param userEntity
     * @return
     */
    @PostMapping(value = "/user")
    public ResultVO createUer(HttpServletRequest request, @RequestBody UserEntity userEntity) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        String applicationId = String.valueOf(request.getAttribute("applicationId"));
        if (userEntity != null) {
            userEntity.setAccountId(accountId);
            userEntity.setApplicationId(applicationId);
            if (CompareUtil.createUserNotNull(userEntity)) {
                return userEnterpriseService.createUer(userEntity);
            }
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    /**
     * 更新所有字段
     *
     * @param request
     * @param userEntity
     * @return
     */
    @PutMapping(value = "/user")
    public ResultVO updateUser(HttpServletRequest request, @RequestBody UserEntity userEntity) {
        String applicationId = String.valueOf(request.getAttribute("applicationId"));
        if (CompareUtil.userIdNotNull(userEntity) && CompareUtil.userBasicNotNull(userEntity) && CompareUtil.userExtensionNotNull(userEntity)) {
            userEntity.setApplicationId(applicationId);
            return userEnterpriseService.updateUser(userEntity);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    /**
     * 删除
     *
     * @param userEntity
     * @return
     */
    @DeleteMapping(value = "/user")
    public ResultVO deleteUser(@RequestBody UserEntity userEntity) {
        if (CompareUtil.userIdNotNull(userEntity)) {
            return userEnterpriseService.deleteUser(userEntity);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    /**
     * 通过userId查询
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/user/{userId}")
    public ResultVO queryUser(HttpServletRequest request, @PathVariable String userId) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        UserEntity userEntity = new UserEntity(accountId, userId);
        if (CompareUtil.userIdNotNull(userEntity)) {
            return userEnterpriseService.queryUser(userEntity);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    /**
     * 更新基本信息
     *
     * @param request
     * @param userEntity
     * @return
     */
    @PutMapping(value = "/user/basic")
    public ResultVO updateBasic(HttpServletRequest request, @RequestBody UserEntity userEntity) {
        String applicationId = String.valueOf(request.getAttribute("applicationId"));
        if (CompareUtil.userIdNotNull(userEntity) && CompareUtil.userBasicNotNull(userEntity)) {
            userEntity.setApplicationId(applicationId);
            return userEnterpriseService.updateBasic(userEntity);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    /**
     * 更新扩展字段
     *
     * @param request
     * @param userEntity
     * @return
     */
    @PutMapping(value = "/user/expand")
    public ResultVO updateExtension(HttpServletRequest request, @RequestBody UserEntity userEntity) {
        String applicationId = String.valueOf(request.getAttribute("applicationId"));
        if (CompareUtil.userIdNotNull(userEntity)) {
            userEntity.setApplicationId(applicationId);
            return userEnterpriseService.updateExtension(userEntity);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }
}
