package com.warpfuture.iot.api.console.service;

import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.UserEntity;
import com.warpfuture.vo.ResultVO;

public interface UserConsoleService {

    ResultVO createUer(UserEntity userEntity);

    ResultVO updateUser(UserEntity userEntity);

    ResultVO deleteUser(UserEntity userEntity);

    ResultVO queryUser(UserEntity userEntity);

    ResultVO listUser(String accountId, String applicationId, PageModel pageModel);

    ResultVO updateBasic(UserEntity userEntity);

    ResultVO updateExtension(UserEntity userEntity);

}
