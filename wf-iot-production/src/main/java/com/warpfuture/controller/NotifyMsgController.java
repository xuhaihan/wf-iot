package com.warpfuture.controller;

import com.warpfuture.constant.Constant;
import com.warpfuture.entity.NotifyMsg;
import com.warpfuture.service.NotifyMsgService;
import com.warpfuture.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/** @Auther: fido @Date: 2018/6/12 00:11 @Description:企业的回调地址处理 */
@RestController
@RequestMapping("/notifymsg")
@Slf4j
public class NotifyMsgController {
  @Autowired private NotifyMsgService notifyMsgService;

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  public ResultVO<NotifyMsg> create(@RequestBody NotifyMsg notifyMsg) {
    NotifyMsg result = notifyMsgService.create(notifyMsg);
    ResultVO<NotifyMsg> resultVO = null;
    if (result != null) {
      resultVO = new ResultVO<>(Constant.SUCCESS, "创建成功", result);
    } else {
      resultVO = new ResultVO<>(Constant.FAIL, "创建失败");
    }
    return resultVO;
  }

  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public ResultVO<NotifyMsg> update(@RequestBody NotifyMsg notifyMsg) {
    NotifyMsg result = notifyMsgService.update(notifyMsg);
    ResultVO<NotifyMsg> resultVO = null;
    if (result != null) {
      resultVO = new ResultVO<>(Constant.SUCCESS, "更新成功", result);
    } else {
      resultVO = new ResultVO<>(Constant.FAIL, "更新失败");
    }
    return resultVO;
  }

  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public ResultVO delete(@RequestParam String accountId) {
    ResultVO resultVO = null;
    try {
      notifyMsgService.delete(accountId);
      resultVO = new ResultVO<>(Constant.SUCCESS, "删除成功");
    } catch (Exception e) {
      log.info("删除回调异常");
      resultVO = new ResultVO<>(Constant.FAIL, "删除失败");
    }
    return resultVO;
  }

  @RequestMapping(value = "/find", method = RequestMethod.POST)
  public ResultVO<NotifyMsg> find(@RequestParam String accountId) {
    NotifyMsg result = notifyMsgService.find(accountId);
    ResultVO<NotifyMsg> resultVO = null;
    if (result != null) {
      resultVO = new ResultVO<>(Constant.SUCCESS, "查找成功", result);
    } else {
      resultVO = new ResultVO<>(Constant.FAIL, "查找失败");
    }
    return resultVO;
  }

  @RequestMapping(value = "/notAccept", method = RequestMethod.POST)
  public ResultVO<NotifyMsg> notAccept(@RequestParam String accountId) {
    NotifyMsg result = notifyMsgService.notAccept(accountId);
    ResultVO<NotifyMsg> resultVO = null;
    if (result != null) {
      resultVO = new ResultVO<>(Constant.SUCCESS, "更改成功", result);
    } else {
      resultVO = new ResultVO<>(Constant.FAIL, "更改失败");
    }
    return resultVO;
  }

  @RequestMapping(value = "/accept", method = RequestMethod.POST)
  public ResultVO<NotifyMsg> accept(@RequestParam String accountId) {
    NotifyMsg result = notifyMsgService.accept(accountId);
    ResultVO<NotifyMsg> resultVO = null;
    if (result != null) {
      resultVO = new ResultVO<>(Constant.SUCCESS, "更改成功", result);
    } else {
      resultVO = new ResultVO<>(Constant.FAIL, "更改失败");
    }
    return resultVO;
  }
}
