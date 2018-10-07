package com.warpfuture.service.impl;

import com.warpfuture.entity.Device;
import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.Tag;
import com.warpfuture.exception.NameExistException;
import com.warpfuture.exception.PermissionFailException;
import com.warpfuture.repository.DeviceInfoRepository;
import com.warpfuture.repository.TagRepository;
import com.warpfuture.service.TagService;
import com.warpfuture.util.IdUtils;
import com.warpfuture.vo.TagDevInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/** Created by fido on 2018/4/19. */
@Service
public class TagServiceImpl implements TagService {
  @Autowired private TagRepository tagRepository;
  @Autowired private DeviceInfoRepository deviceInfoRepository;

  @Override
  public Tag createTag(Tag tag) {
    if (this.ifHasName(tag.getTagName())) {
      throw new NameExistException("标签名字已经存在");
    }
    tag.setIsDelete(false);
    tag.setTagId(IdUtils.getId());
    tag.setCreateTime(System.currentTimeMillis());
    tag.setUpdateTime(System.currentTimeMillis());
    tagRepository.createTag(tag);
    return tagRepository.query(tag.getTagId());
  }

  @Override
  public Tag updateTag(Tag tag) {
    if (ifHasName(tag.getTagName())) {
      throw new NameExistException("标签名字已经存在");
    }
    Tag originTag = tagRepository.query(tag.getTagId());
    if (!permission(originTag, tag.getAccountId())) {
      throw new PermissionFailException("更新标签权限错误");
    }
    // 更新名称，更新描述
    originTag.setTagDesc(tag.getTagDesc());
    originTag.setTagName(tag.getTagName());
    originTag.setUpdateTime(System.currentTimeMillis());
    tagRepository.updateTag(tag);
    return tagRepository.query(tag.getTagId());
  }

  @Override
  public void deleteTag(String tagId, String accountId) {
    Tag tag = tagRepository.query(tagId);
    if (!permission(tag, accountId)) {
      throw new PermissionFailException("删除标签权限错误");
    }
    tagRepository.deleteTag(tagId);
    // 删除标签的时候，还需要把设备相关的标签信息更新
    Query query = Query.query(Criteria.where("tagList.tagId").is(tagId));
    List<Device> devices = deviceInfoRepository.queryDeviceList(query);
    for (Device device : devices) {
      deviceInfoRepository.removeTag(accountId, device.getDeviceId(), tagId);
    }
  }

  @Override
  public PageModel<Tag> queryTagByAccountId(String accountId, Integer pageSize, Integer pageIndex) {
    return tagRepository.queryByAccountId(accountId, pageSize, pageIndex);
  }

  @Override
  public Tag queryTag(String tagId, String accountId) {
    Tag tag = tagRepository.query(tagId);
    System.out.println("查到的tag" + tag);
    if (!permission(tag, accountId)) {
      throw new PermissionFailException("查询标签权限错误");
    }
    return tag;
  }

  private boolean permission(Tag tag, String accountId) {
    if (tag == null) {
      return false;
    }
    return tag.getAccountId().equals(accountId);
  }

  private boolean ifHasName(String tagName) {
    Tag tag = tagRepository.queryByName(tagName);
    if (tag == null) {
      return false;
    }
    return true;
  }

  @Override
  public TagDevInfo addDevs(String accountId, String tagId, List<String> deviceList) {
    Tag tag = tagRepository.query(tagId);
    if (!permission(tag, accountId)) {
      throw new PermissionFailException("标签新增设备权限错误");
    }
    for (String deviceId : deviceList) {
      deviceInfoRepository.addTag(accountId, deviceId, tagId);
    }
    return this.getDevs(accountId, tagId);
  }

  @Override
  public TagDevInfo getDevs(String accountId, String tagId) {
    Tag tag = tagRepository.query(tagId);
    if (!permission(tag, accountId)) {
      throw new PermissionFailException("查询标签分组详情权限错误");
    }
    Query query = Query.query(Criteria.where("tagList.tagId").is(tagId));
    List<Device> devices = deviceInfoRepository.queryDeviceList(query);
    TagDevInfo tagDevInfo = new TagDevInfo();
    tagDevInfo.setTag(tag);
    tagDevInfo.setDeviceList(devices);
    return tagDevInfo;
  }

  @Override
  public void removeDevs(String accountId, String tagId, List<String> deviceList) {
    Tag tag = tagRepository.query(tagId);
    if (!permission(tag, accountId)) {
      throw new PermissionFailException("标签移除设备权限错误");
    }
    for (String deviceId : deviceList) {
      deviceInfoRepository.removeTag(accountId, deviceId, tagId);
    }
  }
}
