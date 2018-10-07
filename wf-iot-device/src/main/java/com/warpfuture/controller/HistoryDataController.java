package com.warpfuture.controller;

import com.warpfuture.dto.HistoryDataInfo;
import com.warpfuture.entity.HistoricalData;
import com.warpfuture.entity.HistoryDataPageModel;
import com.warpfuture.enums.ResponseCode;
import com.warpfuture.exception.PermissionFailException;
import com.warpfuture.service.HistoryDataService;
import com.warpfuture.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/** Created by fido on 2018/5/14. */
@RestController
@RequestMapping("/historyData")
public class HistoryDataController {
  @Autowired private HistoryDataService historyDataService;

  @RequestMapping(value = "/findByHistoryDataId", method = RequestMethod.POST)
  public ResultVO<HistoryDataPageModel<HistoricalData>> findByHistoryDataId(
      @RequestBody HistoryDataInfo dataInfo,
      @RequestParam Integer pageSize,
      @RequestParam Integer pageIndex) {
    ResultVO<HistoryDataPageModel<HistoricalData>> resultVO = null;
    try {
      HistoryDataPageModel<HistoricalData> result =
          historyDataService.findByHistoryDataId(dataInfo, pageIndex, pageSize);
      resultVO =
          new ResultVO<HistoryDataPageModel<HistoricalData>>(
              ResponseCode.QUERY_SUCCESS.getCode(), "查询成功", result);
    } catch (PermissionFailException e) {
      resultVO =
          new ResultVO<HistoryDataPageModel<HistoricalData>>(
              ResponseCode.QUERY_FAIL.getCode(), "查询权限有误");

    } catch (Exception e) {
      resultVO =
          new ResultVO<HistoryDataPageModel<HistoricalData>>(
              ResponseCode.QUERY_FAIL.getCode(), "查询出错");
    }
    return resultVO;
  }

  @RequestMapping(value = "/findByHistoryDataIdAndDataType", method = RequestMethod.POST)
  public ResultVO<HistoryDataPageModel<HistoricalData>> findByHistoryDataIdAndDataType(
      @RequestBody HistoryDataInfo dataInfo,
      @RequestParam Integer pageSize,
      @RequestParam Integer pageIndex) {
    ResultVO<HistoryDataPageModel<HistoricalData>> resultVO = null;
    try {
      HistoryDataPageModel<HistoricalData> result =
          historyDataService.findByHistoryDataIdAndDataType(dataInfo, pageIndex, pageSize);
      resultVO =
          new ResultVO<HistoryDataPageModel<HistoricalData>>(
              ResponseCode.QUERY_SUCCESS.getCode(), "查询成功", result);
    } catch (PermissionFailException e) {
      resultVO =
          new ResultVO<HistoryDataPageModel<HistoricalData>>(
              ResponseCode.QUERY_FAIL.getCode(), "查询权限有误");

    } catch (Exception e) {
      resultVO =
          new ResultVO<HistoryDataPageModel<HistoricalData>>(
              ResponseCode.QUERY_FAIL.getCode(), "查询出错");
    }
    return resultVO;
  }

  @RequestMapping(value = "/findByHistoryDataIdAndDataTime", method = RequestMethod.POST)
  public ResultVO<HistoryDataPageModel<HistoricalData>> findByHistoryDataIdAndDataTime(
      @RequestBody HistoryDataInfo dataInfo,
      @RequestParam Integer pageSize,
      @RequestParam Integer pageIndex) {
    ResultVO<HistoryDataPageModel<HistoricalData>> resultVO = null;
    try {
      HistoryDataPageModel<HistoricalData> result =
          historyDataService.findByHistoryDataIdAndDataTime(dataInfo, pageIndex, pageSize);
      resultVO =
          new ResultVO<HistoryDataPageModel<HistoricalData>>(
              ResponseCode.QUERY_SUCCESS.getCode(), "查询成功", result);
    } catch (PermissionFailException e) {
      resultVO =
          new ResultVO<HistoryDataPageModel<HistoricalData>>(
              ResponseCode.QUERY_FAIL.getCode(), "查询权限有误");

    } catch (Exception e) {
      resultVO =
          new ResultVO<HistoryDataPageModel<HistoricalData>>(
              ResponseCode.QUERY_FAIL.getCode(), "查询出错");
    }
    return resultVO;
  }
}
