package com.warpfuture.iot.api.enterprise.feign.service;

import com.warpfuture.entity.UserEntity;
import com.warpfuture.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "wf-iot-oauth-service")
public interface UserEnterpriseFeignService {

    @PostMapping(value = "/user/login")
    ResultVO<UserEntity> userLogin(@RequestParam("userAccount") String userAccount,
                                   @RequestParam("password") String password);

    @PostMapping(value = "/user/query")
    ResultVO<UserEntity> queryUser(@RequestBody UserEntity userEntity);

    @PostMapping(value = "/oauth/jwt")
    String getEnterpriseJwt(@RequestParam(value = "accountId") String accountId,
                            @RequestParam(value = "userId") String userId,
                            @RequestParam(value = "applicationId") String applicationId);

    @PostMapping(value = "/user/add")
    ResultVO<UserEntity> createUer(@RequestBody UserEntity userEntity);

    @PostMapping(value = "/user/update")
    ResultVO<UserEntity> updateUser(@RequestBody UserEntity userEntity);

    @PostMapping(value = "/user/delete")
    ResultVO<UserEntity> deleteUser(@RequestParam(value = "userId") String userId);

    @PostMapping(value = "/user/update/basic")
    ResultVO updateBasic(@RequestBody UserEntity userEntity);

    @PostMapping(value = "/user/update/extension")
    ResultVO updateExtension(@RequestBody UserEntity userEntity);
}
