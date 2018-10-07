package com.warpfuture.service.impl;

import com.warpfuture.entity.NotifyMsg;
import com.warpfuture.exception.PermissionFailException;
import com.warpfuture.repository.NotifyMsgRepository;
import com.warpfuture.service.NotifyMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/** @Auther: fido @Date: 2018/6/12 00:21 @Description: */
@Service
public class NotifyMsgServiceImpl implements NotifyMsgService {
  @Autowired private NotifyMsgRepository notifyMsgRepository;

  @Override
  public NotifyMsg create(NotifyMsg notifyMsg) {
    notifyMsg.setIsAccept(true);
    notifyMsgRepository.insert(notifyMsg);
    Optional<NotifyMsg> optionalNotifyMsg = notifyMsgRepository.findById(notifyMsg.getAccountId());
    if (optionalNotifyMsg.isPresent()) {
      return optionalNotifyMsg.get();
    }
    return null;
  }

  @Override
  public NotifyMsg update(NotifyMsg notifyMsg) {
    notifyMsgRepository.save(notifyMsg);
    Optional<NotifyMsg> optionalNotifyMsg = notifyMsgRepository.findById(notifyMsg.getAccountId());
    if (optionalNotifyMsg.isPresent()) {
      return optionalNotifyMsg.get();
    }
    return null;
  }

  @Override
  public void delete(String accountId) {
    notifyMsgRepository.deleteById(accountId);
  }

  @Override
  public NotifyMsg find(String accountId) {
    Optional<NotifyMsg> optionalNotifyMsg = notifyMsgRepository.findById(accountId);
    if (optionalNotifyMsg.isPresent()) {
      return optionalNotifyMsg.get();
    }
    return null;
  }

  @Override
  public NotifyMsg notAccept(String accountId) {
    Optional<NotifyMsg> optionalNotifyMsg = notifyMsgRepository.findById(accountId);
    if (optionalNotifyMsg.isPresent()) {
      NotifyMsg originNotifyMsg = optionalNotifyMsg.get();
      originNotifyMsg.setIsAccept(false);
      notifyMsgRepository.save(originNotifyMsg);
      return originNotifyMsg;
    } else {
      throw new PermissionFailException("没有该回调地址记录");
    }
  }

  @Override
  public NotifyMsg accept(String accountId) {
    Optional<NotifyMsg> optionalNotifyMsg = notifyMsgRepository.findById(accountId);
    if (optionalNotifyMsg.isPresent()) {
      NotifyMsg originNotifyMsg = optionalNotifyMsg.get();
      originNotifyMsg.setIsAccept(true);
      notifyMsgRepository.save(originNotifyMsg);
      return originNotifyMsg;
    } else {
      throw new PermissionFailException("没有该回调地址记录");
    }
  }
}
