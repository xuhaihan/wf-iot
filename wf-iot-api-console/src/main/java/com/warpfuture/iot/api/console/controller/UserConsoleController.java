package com.warpfuture.iot.api.console.controller;

import com.warpfuture.constant.PageConstant;
import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.UserEntity;
import com.warpfuture.iot.api.console.service.UserConsoleService;
import com.warpfuture.util.CompareUtil;
import com.warpfuture.util.ModelBeanUtil;
import com.warpfuture.util.PageUtils;
import com.warpfuture.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserConsoleController {

    @Autowired
    private UserConsoleService userConsoleService;

    @PostMapping(value = "/user/create")
    public ResultVO createUer(Model model) {
        UserEntity userEntity = ModelBeanUtil.getBeanFromModel(model, "userEntity", UserEntity.class);

        if (CompareUtil.createUserNotNull(userEntity)) {
            return userConsoleService.createUer(userEntity);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    /**
     * 暂时无用
     *
     * @param model
     * @return
     */
    @PostMapping(value = "/user/update")
    public ResultVO updateUser(Model model) {
        UserEntity userEntity = ModelBeanUtil.getBeanFromModel(model, "userEntity", UserEntity.class);

        if (CompareUtil.userIdNotNull(userEntity) && CompareUtil.userBasicNotNull(userEntity) && CompareUtil.userExtensionNotNull(userEntity)) {
            return userConsoleService.updateUser(userEntity);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/user/delete")
    public ResultVO deleteUser(Model model) {
        UserEntity userEntity = ModelBeanUtil.getBeanFromModel(model, "userEntity", UserEntity.class);

        if (CompareUtil.userIdNotNull(userEntity)) {
            return userConsoleService.deleteUser(userEntity);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/user/query")
    public ResultVO queryUser(Model model) {
        UserEntity userEntity = ModelBeanUtil.getBeanFromModel(model, "userEntity", UserEntity.class);

        if (CompareUtil.userIdNotNull(userEntity)) {
            return userConsoleService.queryUser(userEntity);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/user/list")
    public ResultVO listUser(Model model, @RequestParam(required = false) Integer pageSize,
                             @RequestParam(required = false) Integer pageIndex) {
        UserEntity userEntity = ModelBeanUtil.getBeanFromModel(model, "userEntity", UserEntity.class);
        if (userEntity != null && CompareUtil.strNotNull(userEntity.getAccountId()) && CompareUtil.strNotNull(userEntity.getApplicationId())) {
            PageModel pageModel = PageUtils.dealPage(pageSize, pageIndex, PageConstant.USER_DEFAULT_PAGE_SIZE);
            return userConsoleService.listUser(userEntity.getAccountId(), userEntity.getApplicationId(), pageModel);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/user/update/basic")
    public ResultVO updateBasic(Model model) {
        UserEntity userEntity = ModelBeanUtil.getBeanFromModel(model, "userEntity", UserEntity.class);

        if (CompareUtil.userIdNotNull(userEntity) && CompareUtil.userBasicNotNull(userEntity)) {
            return userConsoleService.updateBasic(userEntity);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/user/update/extension")
    public ResultVO updateExtension(Model model) {
        UserEntity userEntity = ModelBeanUtil.getBeanFromModel(model, "userEntity", UserEntity.class);

        if (CompareUtil.userIdNotNull(userEntity) && CompareUtil.userExtensionNotNull(userEntity)) {
            return userConsoleService.updateExtension(userEntity);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @ModelAttribute(value = "userEntity")
    public UserEntity setAccountId(@RequestBody(required = false) UserEntity userEntity, HttpServletRequest request) {
        String accountId = String.valueOf(request.getAttribute("accountId"));

        if (userEntity != null && !StringUtils.isEmpty(accountId)) {
            userEntity.setAccountId(accountId);
        }
        return userEntity;
    }
}
