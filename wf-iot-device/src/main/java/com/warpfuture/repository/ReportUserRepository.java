package com.warpfuture.repository;

import com.warpfuture.entity.ReportUser;
import org.apache.catalina.User;

import java.util.List;

public interface ReportUserRepository {

  public List<ReportUser> getUserIdList(String applicationId);
}
