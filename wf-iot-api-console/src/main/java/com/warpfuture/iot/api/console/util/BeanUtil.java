package com.warpfuture.iot.api.console.util;

import com.warpfuture.dto.RouteInfoDto;
import com.warpfuture.entity.Productions;
import com.warpfuture.entity.Route;
import com.warpfuture.entity.RouteFront;
import com.warpfuture.iot.api.console.jwt.core.JwtPayload;
import com.warpfuture.vo.ResultVO;
import io.jsonwebtoken.Claims;
import org.springframework.cglib.beans.BeanMap;

import java.util.ArrayList;
import java.util.List;

public class BeanUtil {

    /**
     * @param claims Claims
     * @return JwtPayload
     */
    public static JwtPayload toBean(Claims claims) {
        JwtPayload jwtPayload = new JwtPayload();
        BeanMap beanMap = BeanMap.create(jwtPayload);
        beanMap.putAll(claims);
        return jwtPayload;
    }

    /**
     * 关系 0：上报 1：下发 2：双向通信
     *
     * @param relation [0] || [1] || [0,1]
     * @return 0：上报 1：下发 2：双向通信
     */
    private static Integer getIntRelation(Integer[] relation) {
        if (relation != null && relation.length == 1) {
            return relation[0];
        } else {
            return 2;
        }
    }

    /**
     * 将前端的数据转换为后端需要的数据
     *
     * @param routeFront RouteFront
     * @return RouteInfoDto
     */
    public static RouteInfoDto convertToRoute(RouteFront routeFront) {
        String accountId = routeFront.getAccountId();
        String applicationId = routeFront.getApplicationId();
        RouteInfoDto routeInfoDto = new RouteInfoDto();
        routeInfoDto.setAccountId(accountId);
        routeInfoDto.setApplicationId(applicationId);

        List<Productions> productions = routeFront.getProductions();

        if (productions != null && !productions.isEmpty()) {
            Route route;
            List<Route> routes = new ArrayList<>();
            for (Productions production : productions) {
                Integer relation = getIntRelation(production.getRelation());

                route = new Route(accountId, applicationId, production.getProductionId(), relation, production.isBroadcast());
                routes.add(route);
            }
            routeInfoDto.setProductions(routes);
        }
        return routeInfoDto;
    }


    private static Integer[] getIntsRelation(Integer relation) {
        if (relation == 2) {
            return new Integer[]{0, 1};
        } else {
            return new Integer[]{relation};
        }
    }

    /**
     * 将后端的数据转换为前端需要的数据
     * @param routes
     * @return RouteFront
     */
    public static RouteFront convertToRouteFront(List<Route> routes) {
        RouteFront routeFront = new RouteFront();
        if (routes != null) {
            Productions production;
            List<Productions> productions = new ArrayList<>();
            for (Route route : routes) {
                routeFront.setAccountId(route.getAccountId());
                routeFront.setApplicationId(route.getApplicationId());

                production = new Productions(route.getProductionId(), getIntsRelation(route.getRelation()), route.getBroadcast());
                productions.add(production);
            }
            routeFront.setProductions(productions);
        }
        return routeFront;
    }

    public static ResultVO getRouteResult(ResultVO<List<Route>> result) {
        if (result != null) {
            return new ResultVO<>(result.getCode(), result.getMessage(), convertToRouteFront(result.getData()));
        }
        return null;
    }

}
