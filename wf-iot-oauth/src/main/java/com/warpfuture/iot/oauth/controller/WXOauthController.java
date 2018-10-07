package com.warpfuture.iot.oauth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warpfuture.entity.ApplicationEntity;
import com.warpfuture.iot.oauth.constant.OauthUrl;
import com.warpfuture.iot.oauth.constant.RegexStr;
import com.warpfuture.iot.oauth.entity.AccessToken;
import com.warpfuture.iot.oauth.entity.RepayEntity;
import com.warpfuture.iot.oauth.entity.WeiXinUserInfo;
import com.warpfuture.iot.oauth.service.ApplicationService;
import com.warpfuture.iot.oauth.service.UserService;
import com.warpfuture.iot.oauth.util.HttpClient;
import com.warpfuture.iot.oauth.util.JwtUtil;
import com.warpfuture.iot.oauth.util.RequestUtil;
import com.warpfuture.vo.ResultVO;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by 徐海瀚 on 2018/4/21.
 */
@Controller
@Log4j2
@RequestMapping("/oauth")
public class WXOauthController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${wx.h5.redirectUri}")
    private String redirectUri;

    /**
     * 配置响应微信接口配置
     *
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   加密字符串
     * @return
     */
    @RequestMapping(value = "/news", method = RequestMethod.GET)
    @ResponseBody
    public String getNews(
            @RequestParam("signature") String signature,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("nonce") String nonce,
            @RequestParam("echostr") String echostr) {
        return echostr;
    }

    /**
     * 微信公众号用户授权回调处理
     *
     * @param code
     * @param state
     * @return
     */
    @RequestMapping(value = "/woa", method = RequestMethod.GET)
    public String getJwt(
            @NonNull @RequestParam("code") String code,
            @NonNull @RequestParam("state") String state,
            HttpServletRequest request) {
        //RepayEntity<String> repayEntity = new RepayEntity<>();
        String jwt = "";
        String[] split = state.split(OauthUrl.STATE_SPLIT_CHAR);
        String url = "";
        String appid;
        if (split.length == 2) {
            appid = split[0];
            String encodeUrl = split[1];
            try {
                String decode = URLDecoder.decode(encodeUrl, "utf-8");

                byte[] bytes = Base64.getDecoder().decode(decode.getBytes());
                url = new String(bytes);
            } catch (UnsupportedEncodingException e) {
                log.error("URL解码失败: {}, e -> {}", encodeUrl, e.getMessage());
            }


            ApplicationEntity applicationEntity =
                    applicationService.findOne(new Query(Criteria.where("oauthData.wx.h5.appid").is(appid)));
            log.info("----Application: {}, applicationId: {}", applicationEntity, appid);
            if (applicationEntity != null) {
                Map<String, Object> map = applicationEntity.getOauthData();
                Map<String, Object> map1 = (Map<String, Object>) map.get("wx");
                Map<String, Object> map2 = (Map<String, Object>) map1.get("h5");
                String appId = (String) map2.get("appid");
                String appSecret = (String) map2.get("appSecret");
                StringBuilder tokenUrl = new StringBuilder(OauthUrl.GET_WX_ACCESS_TOKEN);
                tokenUrl.append("appid=" + appId);
                tokenUrl.append("&secret=" + appSecret);
                tokenUrl.append("&code=" + code);
                tokenUrl.append("&grant_type=authorization_code");
                String responseData =
                        HttpClient.get(
                                MediaType.APPLICATION_JSON_UTF8, tokenUrl.toString()); // 用code换取token和openid
                try {
                    AccessToken accessToken = objectMapper.readValue(responseData, AccessToken.class);
                    log.info("----换取token:{}", accessToken.toString());
                    if (!StringUtils.isEmpty(accessToken.getAccess_token())) {
                        String token = accessToken.getAccess_token();
                        String openId = accessToken.getOpenId();
                        StringBuilder userInfoUrl = new StringBuilder(OauthUrl.GET_WX_USER_INFO);
                        userInfoUrl.append("access_token=" + token);
                        userInfoUrl.append("&openid=" + openId);
                        userInfoUrl.append("&lang=zh_CN"); // 请求微信服务器换取用户信息
                        String responseData1 =
                                HttpClient.get(MediaType.APPLICATION_JSON_UTF8, userInfoUrl.toString());
                        responseData1 = new String(responseData1.getBytes("ISO-8859-1"), "UTF-8"); // 解决中文乱码
                        WeiXinUserInfo weiXinUserInfo =
                                objectMapper.readValue(responseData1, WeiXinUserInfo.class);
                        log.info("----微信用户基本信息: {}", weiXinUserInfo);

                        Map<String, Object> payload = userService.verifyWOAUser(applicationEntity.getAccountId(), applicationEntity.getApplicationId(), openId, weiXinUserInfo);
                        log.info("用户Jwt payload : {}",payload);
                        jwt = JwtUtil.createJavaWebToken(payload);
                        
                        if (url.matches(RegexStr.URL_REGEX)) {
                            if (url.contains("?")) {
                                url = url + "&token=" + jwt + "&openid=" + openId;
                            } else {
                                url = url + "?token=" + jwt + "&openid=" + openId;
                            }
                            log.info("=======> 重定向URL: {} ", url);
                            return "redirect:" + url;
                        }
                        String contextPath = request.getContextPath();
                        return "redirect:" + contextPath + "/error";
                    } else {
                        log.info("请求微信获取access_token出错");
                    }
                } catch (IOException e) {
                    log.error("微信公众号回调捕获到异常:{}", e.getMessage());
                } catch (RuntimeException e) {
                    log.error("微信公众号回调捕获到运行时异常:{}", e.getMessage());
                }
            } else {
                log.info("未开通公众号Oauth服务");
            }
        }
        String contextPath = request.getContextPath();
        return "redirect:" + contextPath + "/error";
    }

    /**
     * 检验 token
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "/verifyJwt", method = RequestMethod.POST)
    @ResponseBody
    public RepayEntity<Map<String, Object>> verifyJwt(@RequestParam("token") String token) {
        RepayEntity<Map<String, Object>> repayEntity = new RepayEntity<>();
        Map<String, Object> map = JwtUtil.parserJavaWebToken(token);
        if (map != null) {
            repayEntity.setCode(2000);
            repayEntity.setMessage("用户token验证成功");
            repayEntity.setData(map);
        } else {
            repayEntity.setCode(2001);
            repayEntity.setMessage("用户token验证失败");
            repayEntity.setData(null);
        }
        return repayEntity;
    }

    @RequestMapping(value = "/jwt", method = RequestMethod.POST)
    @ResponseBody
    public String getOauthJwt(
            @RequestParam("accountId") String accountId,
            @RequestParam("userId") String userId,
            @RequestParam("applicationId") String applicationId) {
        Map<String, Object> map = new HashMap<>();
        String token = null;
        log.info(accountId + " " + userId + " " + applicationId);
        if (accountId != null && userId != null && applicationId != null) {
            map.put("accountId", accountId);
            map.put("userId", userId);
            map.put("applicationId", applicationId);
            token = JwtUtil.createJavaWebToken(map);
            log.info("jwt", token);
        }
        return token;
    }

    /**
     * 获取微信公众号授权链接，包含两个链接:有授权页面的链接，无页面授权的链接
     *
     * @param applicationId
     * @param accountId
     * @return
     */
    @RequestMapping(value = "/woaUrl", method = RequestMethod.POST)
    @ResponseBody
    public ResultVO<Map<String, String>> getWOAUrl(
            @NonNull @RequestParam("applicationId") String applicationId,
            @NonNull @RequestParam("accountId") String accountId) {
        ResultVO<Map<String, String>> resultVO = new ResultVO<>();
        try {
            ApplicationEntity applicationEntity =
                    applicationService.findOne(new Query(Criteria.where("applicationId").is(applicationId)));
            if (applicationEntity != null) {
                if (accountId.equals(applicationEntity.getAccountId())) {
                    Map<String, Object> oauthData = applicationEntity.getOauthData();
                    if (oauthData != null) {
                        Map<String, Object> wx = (Map<String, Object>) oauthData.get("wx");
                        if (wx != null) {
                            Map<String, Object> h5 = (Map<String, Object>) wx.get("h5");
                            if (h5 != null) {
                                String appid = (String) h5.get("appid");
                                if (appid != null) {
                                    Map<String, Object> map = new LinkedHashMap<>();
                                    map.put("appid", appid);
                                    map.put("redirect_uri", redirectUri);
                                    map.put("response_type", "code");
                                    map.put("scope", "snsapi_userinfo");
                                    map.put("state", appid);
                                    String authUtl0 =
                                            RequestUtil.appendParam(OauthUrl.GET_WX_OAUTH_URL, map)
                                                    + "#wechat_redirect"; // 有授权页面url
                                    map.put("scope", "snsapi_base");
                                    String authUtl1 =
                                            RequestUtil.appendParam(OauthUrl.GET_WX_OAUTH_URL, map)
                                                    + "#wechat_redirect"; // 无授权页面url
                                    Map<String, String> urlMap = new HashMap<>();
                                    urlMap.put("url0", authUtl0);
                                    urlMap.put("url1", authUtl1);
                                    resultVO.setCode(1);
                                    resultVO.setMessage("获取微信公众号授权连接成功");
                                    resultVO.setData(urlMap);
                                } else {
                                    resultVO.setCode(0);
                                    resultVO.setMessage("应用未开通OAuth2.0授权");
                                }
                            }
                        }
                    }
                } else {
                    resultVO.setCode(0);
                    resultVO.setMessage("企业未创建该应用");
                }
            } else {
                resultVO.setCode(0);
                resultVO.setMessage("应用不存在");
            }
        } catch (RuntimeException e) {
            log.error("获取微信授权链接捕获到异常:{}", e.getMessage());
        }
        return resultVO;
    }
}
