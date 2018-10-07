package com.warpfuture.iot.api.console.feign.service;

import com.warpfuture.entity.UserEntity;
import com.warpfuture.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "wf-iot-oauth-service")
public interface UserConsoleFeignService {

    @PostMapping(value = "/user/add")
    ResultVO createUer(@RequestBody UserEntity userEntity);

    @PostMapping(value = "/user/update")
    ResultVO updateUser(@RequestBody UserEntity userEntity);

    @PostMapping(value = "/user/delete")
    ResultVO deleteUser(@RequestParam(value = "userId") String userId);

    @PostMapping(value = "/user/query")
    ResultVO queryUser(@RequestBody UserEntity userEntity);

    @PostMapping(value = "/user/list")
    ResultVO listUser(@RequestParam(value = "accountId") String accountId,
                      @RequestParam(value = "applicationId") String applicationId,
                      @RequestParam(value = "pageSize") Integer pageSize,
                      @RequestParam(value = "pageIndex") Integer pageIndex);

    @PostMapping(value = "/user/update/basic")
    ResultVO updateBasic(@RequestBody UserEntity userEntity);

    @PostMapping(value = "/user/update/extension")
    ResultVO updateExtension(@RequestBody UserEntity userEntity);

}
