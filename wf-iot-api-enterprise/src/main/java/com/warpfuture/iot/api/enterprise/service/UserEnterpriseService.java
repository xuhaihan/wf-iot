package com.warpfuture.iot.api.enterprise.service;

import com.warpfuture.entity.UserEntity;
import com.warpfuture.vo.ResultVO;

public interface UserEnterpriseService {

    ResultVO<UserEntity> queryUser(UserEntity userEntity);

    ResultVO<UserEntity> userLogin(String userAccount, String password);

    ResultVO createUer(UserEntity userEntity);

    ResultVO updateUser(UserEntity userEntity);

    ResultVO deleteUser(UserEntity userEntity);

    ResultVO updateBasic(UserEntity userEntity);

    ResultVO updateExtension(UserEntity userEntity);
}
