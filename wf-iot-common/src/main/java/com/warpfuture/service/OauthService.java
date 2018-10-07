package com.warpfuture.service;

import com.warpfuture.entity.RepayEntity;
        import org.springframework.cloud.openfeign.FeignClient;
        import org.springframework.web.bind.annotation.RequestBody;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RequestMethod;
        import org.springframework.web.bind.annotation.RequestParam;

        import java.util.Map;

/** Created by 徐海瀚 on 2018/4/26. */
@FeignClient(value = "wf-iot-oauth-service")
public interface OauthService {

  @RequestMapping(value = "/oauth/woa", method = RequestMethod.GET)
  RepayEntity<String> getJwt(
          @RequestParam("code") String code, @RequestParam("state") String state);

  @RequestMapping(value = "/oauth/verifyJwt", method = RequestMethod.POST)
  RepayEntity<Map<String, Object>> verifyJwt(@RequestParam("token") String token);
}
