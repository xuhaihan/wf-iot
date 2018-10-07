//package com.warpfuture.iot.api.enterprise.controller;
//
//import com.warpfuture.constant.ResponseMsg;
//import com.warpfuture.iot.api.enterprise.dto.BingOAuth;
//import com.warpfuture.iot.api.enterprise.dto.BingPhone;
//import com.warpfuture.iot.api.enterprise.service.AuthBindingEnterpriseService;
//import com.warpfuture.util.CompareUtil;
//import com.warpfuture.vo.ResultVO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class AuthBindingEnterpriseController {
//
//    @Autowired
//    private AuthBindingEnterpriseService authBindingEnterpriseService;
//
//    @PostMapping(value = "/bing/phone")
//    public ResultVO bindingByPhone(@RequestBody BingPhone bingPhone) {
//        if (bingPhoneNotNull(bingPhone)) {
//            return authBindingEnterpriseService.bindingByPhone(
//                    bingPhone.getAuthType(),
//                    bingPhone.getUnionId(),
//                    bingPhone.getPhone());
//        }
//        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
//    }
//
//    @PostMapping(value = "/bing/oauth")
//    public ResultVO bindingByOAuth(@RequestBody BingOAuth bingOAuth) {
//        if (bingOAuth != null && CompareUtil.strNotNull(bingOAuth.getAuthType1()) &&
//                CompareUtil.strNotNull(bingOAuth.getUnionId1()) &&
//                CompareUtil.strNotNull(bingOAuth.getAuthType2()) &&
//                CompareUtil.strNotNull(bingOAuth.getUnionId2())) {
//            return authBindingEnterpriseService.bindingByOAuth(
//                    bingOAuth.getAuthType1(), bingOAuth.getUnionId1(),
//                    bingOAuth.getAuthType2(), bingOAuth.getUnionId2());
//        }
//        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
//    }
//
//    @PostMapping(value = "/unbing/phone")
//    public ResultVO unBindingByPhone(@RequestBody BingPhone bingPhone) {
//        if (bingPhoneNotNull(bingPhone)) {
//            return authBindingEnterpriseService.unBindingByPhone(
//                    bingPhone.getAuthType(),
//                    bingPhone.getUnionId(),
//                    bingPhone.getPhone());
//        }
//        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
//    }
//
//    private boolean bingPhoneNotNull(@RequestBody BingPhone bingPhone) {
//        return bingPhone != null &&
//                CompareUtil.strNotNull(bingPhone.getAuthType()) &&
//                CompareUtil.strNotNull(bingPhone.getUnionId()) &&
//                CompareUtil.strNotNull(bingPhone.getPhone());
//    }
//
//}
