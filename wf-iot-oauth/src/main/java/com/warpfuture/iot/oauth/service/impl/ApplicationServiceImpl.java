package com.warpfuture.iot.oauth.service.impl;

import com.warpfuture.entity.ApplicationEntity;
import com.warpfuture.iot.oauth.repository.ApplicationRepository;
import com.warpfuture.iot.oauth.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Created by 徐海瀚 on 2018/4/17. */
@Service
public class ApplicationServiceImpl extends BaseServiceImpl<ApplicationEntity>
    implements ApplicationService {

  private ApplicationRepository applicationRepository;

  @Autowired
  public void setApplicationRepository(ApplicationRepository applicationRepository) {
    this.applicationRepository = applicationRepository;
    this.setBaseRepository(applicationRepository);
  }
}
