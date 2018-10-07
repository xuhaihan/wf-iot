//package com.warpfuture.iot.api.enterprise.service.impl;
//
//import com.warpfuture.constant.ResponseMsg;
//import com.warpfuture.iot.api.enterprise.feign.service.AuthBindingEnterpriseFeignService;
//import com.warpfuture.iot.api.enterprise.service.AuthBindingEnterpriseService;
//import com.warpfuture.vo.ResultVO;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//@Slf4j
//public class AuthBindingEnterpriseServiceImpl implements AuthBindingEnterpriseService {
//
//    @Autowired
//    private AuthBindingEnterpriseFeignService authBindingEnterpriseFeignService;
//
//    @Override
//    public ResultVO bindingByPhone(String authType, String unionId, String phone) {
//        ResultVO result;
//        try {
//            result = authBindingEnterpriseFeignService.bindingByPhone(authType, unionId, phone);
//        } catch (Exception e) {
//            log.warn("Service Error : {}",e.getMessage());
//            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
//        }
//        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
//    }
//
//    @Override
//    public ResultVO bindingByOAuth(String authType1, String unionId1, String authType2, String unionId2) {
//        ResultVO result;
//        try {
//            result = authBindingEnterpriseFeignService.bindingByOAuth(authType1, unionId1, authType2, unionId2);
//        } catch (Exception e) {
//            log.warn("Service Error : {}",e.getMessage());
//            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
//        }
//        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
//    }
//
//    @Override
//    public ResultVO unBindingByPhone(String authType, String unionId, String phone) {
//        ResultVO result;
//        try {
//            result = authBindingEnterpriseFeignService.unBindingByPhone(authType, unionId, phone);
//        } catch (Exception e) {
//            log.warn("Service Error : {}",e.getMessage());
//            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
//        }
//        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
//    }
//}
