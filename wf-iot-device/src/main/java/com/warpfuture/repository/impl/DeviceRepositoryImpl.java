package com.warpfuture.repository.impl;

import com.warpfuture.constant.AliveConstant;
import com.warpfuture.constant.DateConstant;
import com.warpfuture.entity.Device;
import com.warpfuture.entity.PageModel;
import com.warpfuture.repository.DeviceRepository;
import com.warpfuture.util.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/** Created by fido on 2018/4/15. */
@Repository
@Slf4j
public class DeviceRepositoryImpl implements DeviceRepository {

  @Autowired private MongoTemplate mongoTemplate;

  @Override
  public void save(Device device) {
    mongoTemplate.insert(device);
  }

  @Override
  public Device findById(String accountId, String productionId, String deviceId) {
    Criteria criteria = new Criteria();
    criteria.andOperator(
        Criteria.where("accountId").is(accountId),
        Criteria.where("productionId").is(productionId),
        Criteria.where("deviceId").is(deviceId));
    Query query = new Query(criteria);
    //    List<Device> list = mongoTemplate.find(query, Device.class);
    //    if (list != null && list.size() > 0) {
    //      return list.get(0);
    //    }
    //    return null;
    return mongoTemplate.findOne(query, Device.class);
  }

  @Override
  public void updateDevice(Device device) {
    mongoTemplate.save(device);
  }

  @Override
  public PageModel<Device> getOnlineByProductionId(
      String productionId, Integer pageSize, Integer pageIndex) {
    Criteria criatira = new Criteria();
    criatira.andOperator(
        Criteria.where("productionId").is(productionId),
        Criteria.where("sendMsgTime").gt(System.currentTimeMillis() - AliveConstant.ALIVE_CONFIRM));
    Query query = new Query(criatira);
    long rowCount = mongoTemplate.count(query, Device.class); // 总记录数
    PageModel pageModel = new PageModel();
    pageModel.setRowCount((int) rowCount);
    pageModel.setPageIndex(pageIndex);
    pageModel.setPageSize(pageSize);
    pageModel.setSkip(PageUtils.getSkip(pageIndex, pageSize));
    query.skip(pageModel.getSkip()).limit(pageSize);
    List<Device> result = mongoTemplate.find(query, Device.class);
    pageModel.setData(result);
    return pageModel;
  }

  @Override
  public PageModel<Device> getOffLineByProductionId(
      String productionId, Integer pageSize, Integer pageIndex) {
    Criteria criatira = new Criteria();
    criatira.andOperator(
        Criteria.where("productionId").is(productionId),
        Criteria.where("sendMsgTime").lt(System.currentTimeMillis() - AliveConstant.ALIVE_CONFIRM));
    Query query = new Query(criatira);
    long rowCount = mongoTemplate.count(query, Device.class); // 总记录数
    PageModel pageModel = new PageModel();
    pageModel.setRowCount((int) rowCount);
    pageModel.setPageIndex(pageIndex);
    pageModel.setPageSize(pageSize);
    pageModel.setSkip(PageUtils.getSkip(pageIndex, pageSize));
    query.skip(pageModel.getSkip()).limit(pageSize);
    List<Device> result = mongoTemplate.find(query, Device.class);
    pageModel.setData(result);
    return pageModel;
  }

  @Override
  public List<Device> getByAccountId(String accountId) {
    Criteria criteria = new Criteria();
    criteria.andOperator(Criteria.where("accountId").is(accountId));
    Query query = new Query(criteria);
    return mongoTemplate.find(query, Device.class);
  }

  @Override
  public Long getTodayOnlineNums(String accountId, Long startTime) {
    Criteria criteria = new Criteria();
    criteria.andOperator(
        Criteria.where("accountId").is(accountId),
        Criteria.where("sendMsgTime")
            .gt(startTime - AliveConstant.ALIVE_CONFIRM)
            .lte(startTime + DateConstant.DATE_INTERVAL + AliveConstant.ALIVE_CONFIRM));
    Query query = new Query(criteria);
    return mongoTemplate.count(query, Device.class);
  }

  @Override
  public Long getTodayActive(String accountId, Long startTime) {
    Criteria criteria = new Criteria();
    criteria.andOperator(
        Criteria.where("accountId").is(accountId),
        Criteria.where("firstConnectTime")
            .gte(startTime)
            .lte(startTime + DateConstant.DATE_INTERVAL));
    Query query = new Query(criteria);
    long count = mongoTemplate.count(query, Device.class);
    return count;
  }

  @Override
  public Long getDeviceCount(String accountId) {
    Query query = new Query(Criteria.where("accountId").is(accountId));
    long rowCount = mongoTemplate.count(query, Device.class); // 总记录数
    return rowCount;
  }

  @Override
  public Device updateExtensions(Device device) {
    Map<String, String> map = device.getExtensions();
    Device originDevice =
        this.findById(device.getAccountId(), device.getProductionId(), device.getDeviceId());
    if (originDevice != null) {
      originDevice.setExtensions(map);
      this.updateDevice(originDevice);
    }
    return this.findById(device.getAccountId(), device.getProductionId(), device.getDeviceId());
  }

  @Override
  public Long getSevenDaysActiveNums(String accountId, Long startTime) {
    Criteria criteria = new Criteria();
    criteria.andOperator(
        Criteria.where("accountId").is(accountId),
        Criteria.where("sendMsgTime")
            .gte(startTime - DateConstant.SIX_DATE_INTERVAL - AliveConstant.ALIVE_CONFIRM)
            .lte(startTime + DateConstant.DATE_INTERVAL + AliveConstant.ALIVE_CONFIRM));
    Query query = new Query(criteria);
    long count = mongoTemplate.count(query, Device.class);
    return count;
  }
}
