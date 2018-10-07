package com.warpfuture.service;

import com.warpfuture.entity.NotifyMsg;

/** @Auther: fido @Date: 2018/6/12 00:18 @Description: */
public interface NotifyMsgService {
  public NotifyMsg create(NotifyMsg notifyMsg);

  public NotifyMsg update(NotifyMsg notifyMsg);

  public void delete(String accountId);

  public NotifyMsg find(String accountId);

  public NotifyMsg notAccept(String accountId);

  public NotifyMsg accept(String accountId);
}
