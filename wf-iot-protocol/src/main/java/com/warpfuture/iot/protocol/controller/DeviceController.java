package com.warpfuture.iot.protocol.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warpfuture.entity.Instruction;
import com.warpfuture.iot.protocol.constant.Command;
import com.warpfuture.iot.protocol.service.ProducerService;
import com.warpfuture.iot.protocol.service.impl.WTTServiceImpl;
import com.warpfuture.vo.ResultVO;
import io.netty.channel.Channel;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.*;

/** 为了特殊情况下可以提供给维护和测试人员查看当前服务器连接的设备 */
@RestController
@RequestMapping("/wtt")
@Slf4j
public class DeviceController {

  @Autowired ObjectMapper objectMapper;
  @Autowired ProducerService producerService;

  /**
   * 单台部署服务下查看所有连接设备 当from 和 to 参数全部为0时，返回当前服务器单个服务下所有连接的设备id列表
   *
   * @param accountId 企业id
   * @param from 开始位置 最低以0开始
   * @param to 结束位置
   * @return
   */
  @RequestMapping(value = "/account/list",method = RequestMethod.POST)
  public ResultVO<List<String>> getDeviceListByAccountId(String accountId, int from, int to) {
    ResultVO<List<String>> resultVO = new ResultVO<>();
    Map<String, Map<String, Channel>> map = WTTServiceImpl.getChannelMap();
    if (map.containsKey(accountId)) {
      Map<String, Channel> deviceMap = map.get(accountId);
      Set<String> deviceSet = deviceMap.keySet();
      if (deviceSet != null) {
        int amount = deviceSet.size();
        List<String> deviceList = new ArrayList<>(deviceSet);
        if (from >= 0 && to >= 0) {
          if (amount < (to - from)) {
            resultVO.setCode(0);
            resultVO.setMessage("超过查询范围");
          } else if (from == 0 && to == 0) {
            resultVO.setCode(0);
            resultVO.setMessage("查询设备成功");
            resultVO.setData(deviceList);
            return resultVO;
          } else {
            List<String> newDeviceList = new ArrayList<>();
            for (int i = from; i <= to; i++) {
              if (deviceList.get(i) != null) {
                newDeviceList.add(deviceList.get(i));
              }
            }
            resultVO.setCode(1);
            resultVO.setMessage("查询设备成功");
            resultVO.setData(newDeviceList);
            return resultVO;
          }
          resultVO.setCode(1);
          resultVO.setMessage("查询所有设备");
        }
      }
    }else {
      resultVO.setCode(0);
      resultVO.setMessage("企业id不存在");
    }
    return resultVO;
  }

  /**
   * 查询单个服务器单个服务下是否存在该设备
   *
   * @param deviceId 设备id
   * @param accountId 企业id
   * @return
   */
  @RequestMapping(value = "/exist",method = RequestMethod.POST)
  public ResultVO isExistDevice(
      @NonNull @RequestParam("accountId") String accountId,
      @NonNull @RequestParam("deviceId") String deviceId) {
    ResultVO resultVO = new ResultVO();
    Map<String, Map<String, Channel>> map = WTTServiceImpl.getChannelMap();
    Map<String, Channel> deviceMap = map.get(accountId);
    if (deviceMap != null) {
      if (deviceMap.containsKey(deviceId)) {
        resultVO.setCode(1);
        resultVO.setMessage("设备存在");
        return resultVO;
      } else {
        resultVO.setCode(0);
        resultVO.setMessage("设备不存在");
      }
    } else {
      resultVO.setCode(0);
      resultVO.setMessage("服务器当前没有设备连接");
    }
    return resultVO;
  }

  @RequestMapping(value = "/kill", method = RequestMethod.POST)
  public ResultVO killDevice(@RequestBody Instruction instruction) {
    ResultVO resultVO = new ResultVO();
    if (instruction != null) {
      if (instruction.getStatement() != null) {
        if (Command.ENTERPRISE_KILL_DIVICES.equals(instruction.getStatement())) {
          try {
            String data = objectMapper.writeValueAsString(instruction);
            producerService.sendDataMsg(data);
            resultVO.setCode(1);
            resultVO.setMessage("删除设备连接成功");
            return resultVO;
          } catch (IOException e) {
            log.error("企业kill掉连接捕获到异常:{}", e.getMessage());
            resultVO.setCode(0);
            resultVO.setMessage("服务内部发生异常");
          }
        } else {
          resultVO.setCode(0);
          resultVO.setMessage("指令类型错误");
        }
      } else {
        resultVO.setCode(0);
        resultVO.setMessage("设置指令statement不能为空");
      }
    }
    return resultVO;
  }
}
