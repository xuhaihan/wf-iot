package com.warpfuture.iot.protocol.controller;

import com.warpfuture.iot.protocol.service.impl.WSServiceImpl;
import com.warpfuture.vo.ResultVO;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Created by 徐海瀚 on 2018/4/25. */
@RestController
@RequestMapping("/ws")
@Slf4j
public class UserController {
  /**
   * 特殊情况下给开发人员维护并测试查看单台服务器单个服务下所连接的用户 当from 和 to 参数输入都是0时，返回所有用户id列表
   *
   * @param accountId 企业id
   * @param from 查询开始标志 从0开始
   * @param to 查询结束标记
   * @return
   */
  @RequestMapping(value = "/user/list", method = RequestMethod.POST)
  public ResultVO getUsers(
      @NonNull @RequestParam("accountId") String accountId,
      @NonNull @RequestParam("from") int from,
      @NonNull @RequestParam("to") int to) {
    Map<String, Map<String, Object>> map = WSServiceImpl.getChannelMap();
    ResultVO<List<String>> resultVO = new ResultVO<>();
    if (map.get(accountId) != null) {
      Map<String, Object> userMap = map.get(accountId);
      Set<String> userSet = userMap.keySet();
      int amount = userMap.size();
      List<String> userList = new ArrayList<>(userSet);
      if (from >= 0 && to >= 0) {
        if (amount >= from - to) {
          List<String> newUserList = new ArrayList<>();
          for (int i = from; i <= to; i++) {
            if (userList.get(i) != null) {
              newUserList.add(userList.get(i));
            }
          }
          resultVO.setCode(1);
          resultVO.setMessage("获取当前服务器连接用户列表成功");
          resultVO.setData(newUserList);
          return resultVO;

        } else if (from == 0 && to == 0) {
          resultVO.setCode(1);
          resultVO.setMessage("获取当前服务器连接用户列表成功");
          resultVO.setData(userList);
          return resultVO;
        } else {
          resultVO.setCode(0);
          resultVO.setMessage("查询超过范围");
        }
      } else {
        resultVO.setCode(0);
        resultVO.setMessage("输入的始末标记不合法");
      }
    } else {
      resultVO.setCode(0);
      resultVO.setMessage("企业id不存在");
    }
    return resultVO;
  }
}
