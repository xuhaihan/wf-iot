package com.warpfuture.service;

import com.warpfuture.entity.locationInfo.DashBoardInfo;

import java.util.List;

/** Created by fido on 2018/4/20. */
public interface DashBoardService {
  public List<DashBoardInfo> dashBoard(String accountId);

  public DashBoardInfo getDashBoardInfo(String city);
}
