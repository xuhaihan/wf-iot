package com.warpfuture.repository.impl;

import com.warpfuture.constant.AliveConstant;
import com.warpfuture.dto.TagInfo;
import com.warpfuture.entity.Device;
import com.warpfuture.entity.PageModel;
import com.warpfuture.repository.DeviceInfoRepository;
import com.warpfuture.util.PageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Created by fido on 2018/4/23. */
@Repository
public class DeviceInfoRepositoryImpl implements DeviceInfoRepository {

  @Autowired private MongoTemplate mongoTemplate;
  private Logger logger = LoggerFactory.getLogger(DeviceInfoRepositoryImpl.class);

  @Override
  public PageModel<Device> getOnlineDevicesByProd(
      String productionId, Integer pageIndex, Integer pageSize) {
    return this.getDevicesByProd(productionId, pageIndex, pageSize, true);
  }

  @Override
  public PageModel<Device> getOffLineDevicesByProd(
      String productionId, Integer pageIndex, Integer pageSize) {
    return this.getDevicesByProd(productionId, pageIndex, pageSize, false);
  }

  private PageModel<Device> getDevicesByProd(
      String productionId, Integer pageIndex, Integer pageSize, boolean status) {
    Criteria criatira = new Criteria();
    // 查找在线
    if (status == true) {
      logger.info("查找在线设备的当前时间" + System.currentTimeMillis());
      criatira.andOperator(
          Criteria.where("productionId").is(productionId),
          Criteria.where("sendMsgTime")
              .gt(System.currentTimeMillis() - AliveConstant.ALIVE_CONFIRM));
    }
    // 查找离线
    else {
      criatira.andOperator(
          Criteria.where("productionId").is(productionId),
          Criteria.where("sendMsgTime")
              .lt(System.currentTimeMillis() - AliveConstant.ALIVE_CONFIRM));
    }
    Query query = new Query(criatira);
    long rowCount = mongoTemplate.count(query, Device.class); // 总记录数
    PageModel pageModel = PageUtils.dealOverPage(pageIndex, pageSize, (int) rowCount);
    query.skip(pageModel.getSkip()).limit(pageSize);
    List<Device> result = mongoTemplate.find(query, Device.class);
    pageModel.setData(result);
    return pageModel;
  }

  @Override
  public void addTag(String accountId, String deviceId, String tagId) {
    Criteria criteria = new Criteria();
    criteria.andOperator(
        Criteria.where("accountId").is(accountId), Criteria.where("deviceId").is(deviceId));
    Query query = new Query(criteria);
    Device device = mongoTemplate.findOne(query, Device.class);
    if (device != null) {
      Set<TagInfo> tagList = device.getTagList();
      if (tagList == null) {
        tagList = new HashSet<>();
      }
      TagInfo tagInfo = new TagInfo(tagId);
      tagList.add(tagInfo);
      device.setTagList(tagList);
      device.setUpdateTime(System.currentTimeMillis());
      mongoTemplate.save(device);
    }
  }

  @Override
  public List<Device> queryDeviceList(Query query) {
    List<Device> devices = mongoTemplate.find(query, Device.class);
    return devices;
  }

  @Override
  public void removeTag(String accountId, String deviceId, String tagId) {
    Criteria criteria = new Criteria();
    criteria.andOperator(
        Criteria.where("accountId").is(accountId), Criteria.where("deviceId").is(deviceId));
    Query query = new Query(criteria);
    Device device = mongoTemplate.findOne(query, Device.class);
    if (device != null) {
      Set<TagInfo> tagList = device.getTagList();
      if (tagList == null) {
        return;
      }
      TagInfo tagInfo = new TagInfo(tagId);
      tagList.remove(tagInfo);
      device.setTagList(tagList);
      device.setUpdateTime(System.currentTimeMillis());
      mongoTemplate.save(device);
    }
  }

  @Override
  public Device findByDeviceId(String accountId, String productionId, String deviceId) {
    Criteria criteria = new Criteria();
    criteria.andOperator(
        Criteria.where("accountId").is(accountId),
        Criteria.where("productionId").is(productionId),
        Criteria.where("deviceId").is(deviceId));
    Query query = new Query(criteria);
    List<Device> list = mongoTemplate.find(query, Device.class);
    if (list != null & list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  @Override
  public void update(Device device) {
    mongoTemplate.save(device);
  }

  @Override
  public PageModel<Device> getAllDevicesByProd(
      String productionId, Integer pageIndex, Integer pageSize) {
    Criteria criatira = new Criteria();
    criatira.andOperator(Criteria.where("productionId").is(productionId));
    Query query = new Query(criatira);
    long rowCount = mongoTemplate.count(query, Device.class); // 总记录数
    PageModel pageModel = PageUtils.dealOverPage(pageIndex, pageSize, (int) rowCount);
    query.skip(pageModel.getSkip()).limit(pageSize);
    List<Device> result = mongoTemplate.find(query, Device.class);
    pageModel.setData(result);
    return pageModel;
  }

  @Override
  public PageModel<Device> getOnlineDevicesByAccount(
      String accountId, Integer pageIndex, Integer pageSize) {
    return this.getDevicesByAccount(accountId, pageIndex, pageSize, true);
  }

  @Override
  public PageModel<Device> getOffLineDeviceByAccount(
      String accountId, Integer pageIndex, Integer pageSize) {
    return this.getDevicesByAccount(accountId, pageIndex, pageSize, false);
  }

  @Override
  public PageModel<Device> getAllDevicesByAccount(
      String accountId, Integer pageIndex, Integer pageSize) {
    Criteria criatira = new Criteria();
    criatira.andOperator(Criteria.where("accountId").is(accountId));
    Query query = new Query(criatira);
    long rowCount = mongoTemplate.count(query, Device.class); // 总记录数
    PageModel pageModel = PageUtils.dealOverPage(pageIndex, pageSize, (int) rowCount);
    query.skip(pageModel.getSkip()).limit(pageSize);
    List<Device> result = mongoTemplate.find(query, Device.class);
    pageModel.setData(result);
    return pageModel;
  }

  private PageModel<Device> getDevicesByAccount(
      String accountId, Integer pageIndex, Integer pageSize, boolean status) {
    Criteria criatira = new Criteria();
    // 查找在线
    if (status == true) {
      logger.info("查找在线设备的当前时间" + System.currentTimeMillis());
      criatira.andOperator(
          Criteria.where("accountId").is(accountId),
          Criteria.where("sendMsgTime")
              .gt(System.currentTimeMillis() - AliveConstant.ALIVE_CONFIRM));
    }
    // 查找离线
    else {
      criatira.andOperator(
          Criteria.where("accountId").is(accountId),
          Criteria.where("sendMsgTime")
              .lt(System.currentTimeMillis() - AliveConstant.ALIVE_CONFIRM));
    }
    Query query = new Query(criatira);
    long rowCount = mongoTemplate.count(query, Device.class); // 总记录数
    PageModel pageModel = PageUtils.dealOverPage(pageIndex, pageSize, (int) rowCount);
    query.skip(pageModel.getSkip()).limit(pageSize);
    List<Device> result = mongoTemplate.find(query, Device.class);
    pageModel.setData(result);
    return pageModel;
  }

  @Override
  public void updateOTAInfo(
      String accountId, String productionId, String deviceId, String newOTAVersion) {
    Device device = this.findByDeviceId(accountId, productionId, deviceId);
    if (device != null) {
      device.setOriginOtaVersion(newOTAVersion);
      mongoTemplate.save(device);
    }
  }

  @Override
  public Device getDeviceById(String deviceCloudId) {
    return mongoTemplate.findById(deviceCloudId, Device.class);
  }
}
