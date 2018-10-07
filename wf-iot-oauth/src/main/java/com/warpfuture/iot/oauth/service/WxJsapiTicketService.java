package com.warpfuture.iot.oauth.service;

import com.warpfuture.iot.oauth.entity.WxJsapiTicket;
import com.warpfuture.iot.oauth.service.base.BaseService;

import java.io.IOException;

public interface WxJsapiTicketService extends BaseService<WxJsapiTicket> {
    WxJsapiTicket getJsapiTicket(String appId);
}
