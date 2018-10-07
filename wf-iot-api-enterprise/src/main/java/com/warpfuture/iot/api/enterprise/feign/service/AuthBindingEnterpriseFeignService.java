//package com.warpfuture.iot.api.enterprise.feign.service;
//
//import com.warpfuture.vo.ResultVO;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@FeignClient(value = "wf-iot-oauth-service")
//public interface AuthBindingEnterpriseFeignService {
//
//    @PostMapping(value = "/bing/phone")
//    ResultVO bindingByPhone(@RequestParam("authType") String authType,
//                            @RequestParam("unionId") String unionId,
//                            @RequestParam("phone") String phone);
//
//    @PostMapping(value = "/bing/oauth")
//    ResultVO bindingByOAuth(@RequestParam("authType1") String authType1,
//                            @RequestParam("unionId1") String unionId1,
//                            @RequestParam("authType2") String authType2,
//                            @RequestParam("unionId2") String unionId2);
//
//    @PostMapping(value = "/unbing/phone")
//    ResultVO unBindingByPhone(@RequestParam(value = "authType") String authType,
//                              @RequestParam(value = "unionId") String unionId,
//                              @RequestParam(value = "phone") String phone);
//}
