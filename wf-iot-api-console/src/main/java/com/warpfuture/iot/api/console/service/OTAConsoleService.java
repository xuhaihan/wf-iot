package com.warpfuture.iot.api.console.service;

import com.warpfuture.entity.OTAInfo;
import com.warpfuture.entity.PageModel;
import com.warpfuture.vo.ResultVO;

public interface OTAConsoleService {

    ResultVO createOTA(OTAInfo otaInfo);

    ResultVO queryOTA(OTAInfo otaInfo, PageModel pageModel);

    ResultVO disableOTA(OTAInfo otaInfo);

    ResultVO uploadingOTA(OTAInfo otaInfo);
}
