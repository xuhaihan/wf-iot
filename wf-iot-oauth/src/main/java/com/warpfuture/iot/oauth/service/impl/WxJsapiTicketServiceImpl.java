package com.warpfuture.iot.oauth.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warpfuture.iot.oauth.constant.OauthUrl;
import com.warpfuture.iot.oauth.entity.OrdinaryAccessToken;
import com.warpfuture.iot.oauth.entity.WxJsapiTicket;
import com.warpfuture.iot.oauth.repository.WxJsapiTicketRepository;
import com.warpfuture.iot.oauth.service.WxAccessTokenService;
import com.warpfuture.iot.oauth.service.WxJsapiTicketService;
import com.warpfuture.iot.oauth.util.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class WxJsapiTicketServiceImpl extends BaseServiceImpl<WxJsapiTicket>
    implements WxJsapiTicketService {
  private WxJsapiTicketRepository wxJsapiTicketRepository;

  @Autowired private WxAccessTokenService wxAccessTokenService;
  @Autowired private ObjectMapper objectMapper;

  @Autowired
  public void setWxJsapiTicketRepository(WxJsapiTicketRepository wxJsapiTicketRepository) {
    this.wxJsapiTicketRepository = wxJsapiTicketRepository;
    setBaseRepository(wxJsapiTicketRepository);
  }

  @Override
  public WxJsapiTicket getJsapiTicket(String appId) {
    if (StringUtils.isNotBlank(appId)) {
      OrdinaryAccessToken ordinaryAccessToken = null;
      WxJsapiTicket wxJsapiTicket =
          wxJsapiTicketRepository.findOne(new Query(Criteria.where("appId").is(appId)));
      if (wxJsapiTicket != null && (wxJsapiTicket.getExpireTime() >= System.currentTimeMillis())) {
        return wxJsapiTicket;
      } else {
        StringBuilder url = new StringBuilder(OauthUrl.GET_WX_JS_TICKET);
        try {
          ordinaryAccessToken = wxAccessTokenService.getOrdinaryAccessToken(appId);
          if (ordinaryAccessToken != null) {
            url.append("access_token=" + ordinaryAccessToken.getAccess_token());
            url.append("&type=jsapi");
            String data = HttpClient.get(MediaType.APPLICATION_JSON_UTF8, url.toString());
            try {
              Map<String, Object> map = objectMapper.readValue(data, Map.class);
              if (map != null) {
                String ticket = (String) map.get("ticket");
                if (StringUtils.isNotBlank(ticket)) {
                  wxJsapiTicket = new WxJsapiTicket();
                  wxJsapiTicket.setAppId(appId);
                  wxJsapiTicket.setTicket(ticket);
                  int expiresIn = (int) map.get("expires_in");
                  wxJsapiTicket.setExpireTime(System.currentTimeMillis() + expiresIn * 1000);
                  wxJsapiTicketRepository.save(wxJsapiTicket);
                  return wxJsapiTicket;
                }
              }
            } catch (IOException e) {
              log.error("获取微信ticket出错:{}", e.getMessage());
            }
          }
        } catch (IOException e) {
          log.error("获取access_token发生异常:{}", e.getMessage());
        }
      }
    }
    return null;
  }
}
