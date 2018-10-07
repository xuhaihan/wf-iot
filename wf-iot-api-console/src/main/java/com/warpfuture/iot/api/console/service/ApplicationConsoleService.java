package com.warpfuture.iot.api.console.service;

import com.warpfuture.entity.ApplicationEntity;
import com.warpfuture.entity.PageModel;
import com.warpfuture.vo.ResultVO;

public interface ApplicationConsoleService {

    ResultVO createApplication(ApplicationEntity applicationEntity);

    ResultVO updateApplication(ApplicationEntity applicationEntity);

    ResultVO queryApplication(ApplicationEntity applicationEntity);

    ResultVO deleteApplication(String applicationId);

    ResultVO listApplication(String accountId, PageModel pageModel);

    ResultVO updateAppBasic(ApplicationEntity applicationEntity);

    ResultVO updateAppExpansion(ApplicationEntity applicationEntity);

    ResultVO updateAppOAuthWays(ApplicationEntity applicationEntity);
}
