package com.warpfuture.iot.api.console.controller;

import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.entity.RouteFront;
import com.warpfuture.iot.api.console.service.RouteConsoleService;
import com.warpfuture.iot.api.console.util.BeanUtil;
import com.warpfuture.util.CompareUtil;
import com.warpfuture.util.ModelBeanUtil;
import com.warpfuture.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RouteConsoleController {

    @Autowired
    private RouteConsoleService routeConsoleService;

    @PostMapping(value = "/route/create")
    public ResultVO createRoute(Model model) {
        RouteFront routeFront = ModelBeanUtil.getBeanFromModel(model, "routeFront", RouteFront.class);

        if (CompareUtil.routeFrontNotNull(routeFront) && routeFront.getProductions() != null) {
            return routeConsoleService.createRoute(BeanUtil.convertToRoute(routeFront));
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/route/query")
    public ResultVO queryRoute(Model model) {
        RouteFront routeFront = ModelBeanUtil.getBeanFromModel(model, "routeFront", RouteFront.class);

        if (CompareUtil.routeFrontNotNull(routeFront)) {
            return routeConsoleService.queryRoute(BeanUtil.convertToRoute(routeFront));
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @ModelAttribute(value = "routeFront")
    public RouteFront setAccountId(@RequestBody(required = false) RouteFront routeFront, HttpServletRequest request) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (routeFront != null&&!StringUtils.isEmpty(accountId)) {
            routeFront.setAccountId(accountId);
        }
        return routeFront;
    }

}
