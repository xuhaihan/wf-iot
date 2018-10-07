package com.warpfuture.iot.oauth.repository.impl;

import com.warpfuture.iot.oauth.entity.WxJsapiTicket;
import com.warpfuture.iot.oauth.repository.WxJsapiTicketRepository;
import org.springframework.stereotype.Repository;

@Repository
public class WxJsapiTicketRepositoryImpl extends BaseRepositoryImpl<WxJsapiTicket>
    implements WxJsapiTicketRepository {

  @Override
  protected Class<WxJsapiTicket> getEntityClass() {
    return WxJsapiTicket.class;
  }
}
