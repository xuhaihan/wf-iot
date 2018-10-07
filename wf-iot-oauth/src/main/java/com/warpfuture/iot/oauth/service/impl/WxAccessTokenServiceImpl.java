package com.warpfuture.iot.oauth.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warpfuture.entity.ApplicationEntity;
import com.warpfuture.iot.oauth.constant.OauthUrl;
import com.warpfuture.iot.oauth.entity.OrdinaryAccessToken;
import com.warpfuture.iot.oauth.repository.ApplicationRepository;
import com.warpfuture.iot.oauth.repository.WxAccessTokenRepository;
import com.warpfuture.iot.oauth.service.WxAccessTokenService;
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
public class WxAccessTokenServiceImpl extends BaseServiceImpl<OrdinaryAccessToken>
    implements WxAccessTokenService {
  private WxAccessTokenRepository wxAccessTokenRepository;
  @Autowired private ApplicationRepository applicationRepository;
  @Autowired private ObjectMapper objectMapper;

  @Autowired
  public void setApplicationRepository(WxAccessTokenRepository wxAccessTokenRepository) {
    this.wxAccessTokenRepository = wxAccessTokenRepository;
    this.setBaseRepository(wxAccessTokenRepository);
  }

  @Override
  public OrdinaryAccessToken getOrdinaryAccessToken(String appId) {
    if (StringUtils.isNotBlank(appId)) {
      OrdinaryAccessToken ordinaryAccessToken =
          wxAccessTokenRepository.findOne(new Query(Criteria.where("appId").is(appId)));
      if (ordinaryAccessToken != null
          && (ordinaryAccessToken.getExpireTime() >= System.currentTimeMillis())) {
        return ordinaryAccessToken;
      } else {
        ApplicationEntity applicationEntity =
            applicationRepository.findOne(
                new Query(Criteria.where("oauthData.wx.h5.appid").is(appId)));
        if (applicationEntity != null && !applicationEntity.getDelete()) {
          StringBuilder url = new StringBuilder(OauthUrl.GET_WX_JS_ACCESS_TOKEN);
          url.append("&appid=" + appId);
          Map<String, Object> oauthData = applicationEntity.getOauthData();
          Map<String, Object> wx = (Map<String, Object>) oauthData.get("wx");
          Map<String, Object> h5 = (Map<String, Object>) wx.get("h5");
          url.append("&secret=" + h5.get("appSecret"));
          String data = HttpClient.get(MediaType.APPLICATION_JSON_UTF8, url.toString());
          try {
            Map<String, Object> map = objectMapper.readValue(data, Map.class);
            if (map != null) {
              ordinaryAccessToken = new OrdinaryAccessToken();
              ordinaryAccessToken.setAppId(appId);
              ordinaryAccessToken.setAccess_token((String) map.get("access_token"));
              int expiresIn = (int) map.get("expires_in");
              ordinaryAccessToken.setExpireTime(System.currentTimeMillis() + expiresIn * 1000);
              wxAccessTokenRepository.save(ordinaryAccessToken);
              return ordinaryAccessToken;
            }
          } catch (IOException e) {
            log.error("获取微信access_token发生异常:{}", e.getMessage());
          }
        }
      }
    }
    return null;
  }
}
