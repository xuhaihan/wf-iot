package com.warpfuture.iot.api.console.service;

import com.warpfuture.entity.ApplicationEntity;
import com.warpfuture.entity.UserEntity;
import com.warpfuture.vo.ResultVO;

public interface OAuthConsoleService {

    ResultVO getWOAUrl(ApplicationEntity applicationEntity);

}
