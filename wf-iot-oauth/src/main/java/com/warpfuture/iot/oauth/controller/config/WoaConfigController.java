package com.warpfuture.iot.oauth.controller.config;

import com.warpfuture.iot.oauth.entity.WxJsapiTicket;
import com.warpfuture.iot.oauth.service.WxJsapiTicketService;
import com.warpfuture.iot.oauth.util.SHA1;
import com.warpfuture.iot.oauth.util.UUIDUtil;
import com.warpfuture.vo.ResultVO;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.LinkedHashMap;
import java.util.Map;

/** 微信接口配置控制类 */
@RestController
@RequestMapping("/config")
@Slf4j
public class WoaConfigController {

  @Autowired private WxJsapiTicketService wxJsapiTicketService;

  /**
   * @param appId 微信公众号的appid
   * @param url 调用js接口页面的完整url，不带#后面部分
   * @return
   */
  @RequestMapping(value = "/woa", method = RequestMethod.POST)
  public ResultVO<Map<String, String>> getConfig(
      @NonNull @RequestParam("appId") String appId, @NonNull @RequestParam("url") String url) {
    ResultVO<Map<String, String>> resultVO = new ResultVO<>();
    WxJsapiTicket wxJsapiTicket = wxJsapiTicketService.getJsapiTicket(appId);
    if (wxJsapiTicket != null) {
      String ticket = wxJsapiTicket.getTicket(); // js接口调用票据
      String nonceStr = UUIDUtil.get12UUID(); // 获得的随机字符串
      String timestamp = String.valueOf(System.currentTimeMillis() / 1000); // 时间戳
      String str =
          "jsapi_ticket="
              + ticket
              + "&noncestr="
              + nonceStr
              + "&timestamp="
              + timestamp
              + "&url="
              + url;
      String signature = SHA1.getSHA1String(str); // 签名
      Map<String, String> data = new LinkedHashMap<>();
      data.put("appId", appId);
      data.put("timestamp", timestamp);
      data.put("nonceStr", nonceStr);
      data.put("signature", signature);
      resultVO.setMessage("获取配置成功");
      return resultVO.success(data);
    } else {
      resultVO.fail("获取微信ticket失败");
    }
    return resultVO;
  }
}
