package com.warpfuture.controller;

import com.warpfuture.entity.locationInfo.DashBoardInfo;
import com.warpfuture.enums.ResponseCode;
import com.warpfuture.service.DashBoardService;
import com.warpfuture.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Created by fido on 2018/4/20. 获取账户的在线设备列表数据，用于地图展示 */
@RestController
@RequestMapping("/dashboard")
public class DashBoardController {
  @Autowired private DashBoardService dashBoardService;

  @RequestMapping(value = "/getDashBoardInfo", method = RequestMethod.POST)
  public ResultVO<List<DashBoardInfo>> getDashBoardInfo(@RequestParam String accountId) {
    List<DashBoardInfo> list = dashBoardService.dashBoard(accountId);
    ResultVO<List<DashBoardInfo>> resultVO =
        new ResultVO<>(ResponseCode.QUERY_SUCCESS.getCode(), "查询成功", list);
    return resultVO;
  }
}
