package com.warpfuture.controller;

import com.warpfuture.constant.Constant;
import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.Tag;
import com.warpfuture.exception.NameExistException;
import com.warpfuture.exception.PermissionFailException;
import com.warpfuture.service.TagService;
import com.warpfuture.vo.ResultVO;
import com.warpfuture.vo.TagDevInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Created by fido on 2018/4/19. */
@RequestMapping("/tag")
@RestController
public class TagController {
  @Autowired private TagService tagService;

  private Logger logger = LoggerFactory.getLogger(TagController.class);

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  public ResultVO create(@RequestBody Tag tag) {
    ResultVO resultVO = null;
    try {
      Tag result = tagService.createTag(tag);
      resultVO = new ResultVO(Constant.SUCCESS, "增加标签成功", result);
    } catch (NameExistException e) {
      logger.error(e.getMessage());
      resultVO = new ResultVO(Constant.FAIL, e.getMessage(), null);
    } catch (Exception e) {
      logger.error("插入标签时异常" + e.getMessage());
      resultVO = new ResultVO(Constant.FAIL, "增加标签失败", null);
    }
    return resultVO;
  }

  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public ResultVO update(@RequestBody Tag tag) {
    ResultVO resultVO = null;
    try {
      Tag result = tagService.updateTag(tag);
      resultVO = new ResultVO(Constant.SUCCESS, "更新标签成功", result);
    } catch (NameExistException e) {
      logger.error(e.getMessage());
      resultVO = new ResultVO(Constant.FAIL, e.getMessage(), null);
    } catch (PermissionFailException e) {
      resultVO = new ResultVO(Constant.FAIL, e.getMessage(), null);
    } catch (Exception e) {
      logger.error("更新标签时异常" + e.getMessage());
      resultVO = new ResultVO(Constant.FAIL, "更新标签失败", null);
    }
    return resultVO;
  }

  @RequestMapping(value = "/account/query", method = RequestMethod.POST)
  public ResultVO queryByAccountId(
      @RequestParam String accountId,
      @RequestParam Integer pageSize,
      @RequestParam Integer pageIndex) {
    PageModel<Tag> pageModel = tagService.queryTagByAccountId(accountId, pageSize, pageIndex);
    ResultVO resultVO = new ResultVO(Constant.SUCCESS, "查询成功", pageModel);
    return resultVO;
  }

  @RequestMapping(value = "/query", method = RequestMethod.POST)
  public ResultVO query(@RequestBody Tag queryTag) {
    ResultVO resultVO = null;
    try {
      String accountId = queryTag.getAccountId();
      String tagId = queryTag.getTagId();
      Tag tag = tagService.queryTag(tagId, accountId);
      resultVO = new ResultVO(Constant.SUCCESS, "查询成功", tag);
    } catch (PermissionFailException e) {
      logger.error(e.getMessage());
      resultVO = new ResultVO(Constant.FAIL, e.getMessage(), null);
    }
    return resultVO;
  }

  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public ResultVO delete(@RequestBody Tag queryTag) {
    ResultVO resultVO = null;
    try {
      String accountId = queryTag.getAccountId();
      String tagId = queryTag.getTagId();
      tagService.deleteTag(tagId, accountId);
      resultVO = new ResultVO(Constant.SUCCESS, "删除标签成功", null);
    } catch (PermissionFailException e) {
      logger.error(e.getMessage());
      resultVO = new ResultVO(Constant.FAIL, e.getMessage(), null);
    } catch (Exception e) {
      logger.error("删除异常" + e.getMessage());
      resultVO = new ResultVO(Constant.FAIL, "删除标签失败", null);
    }
    return resultVO;
  }

  @RequestMapping(value = "/addDevs", method = RequestMethod.POST)
  public ResultVO addDevs(@RequestBody Tag tag) {
    ResultVO resultVO = null;
    try {
      String accountId = tag.getAccountId();
      String tagId = tag.getTagId();
      List<String> deviceList = tag.getDeviceList();
      TagDevInfo tagDevInfo = tagService.addDevs(accountId, tagId, deviceList);
      resultVO = new ResultVO(Constant.SUCCESS, "添加设备成功", tagDevInfo);
    } catch (PermissionFailException e) {
      logger.error(e.getMessage());
      resultVO = new ResultVO(Constant.FAIL, e.getMessage(), null);
    } catch (Exception e) {
      logger.error("删除异常" + e.getMessage());
      resultVO = new ResultVO(Constant.FAIL, "添加设备失败", null);
    }
    return resultVO;
  }

  @RequestMapping(value = "/removeDevs", method = RequestMethod.POST)
  public ResultVO removeDevs(@RequestBody Tag tag) {
    ResultVO resultVO = null;
    try {
      String accountId = tag.getAccountId();
      String tagId = tag.getTagId();
      List<String> deviceList = tag.getDeviceList();
      tagService.removeDevs(accountId, tagId, deviceList);
      resultVO = new ResultVO(Constant.SUCCESS, "移除设备成功", null);
    } catch (PermissionFailException e) {
      logger.error(e.getMessage());
      resultVO = new ResultVO(Constant.FAIL, e.getMessage(), null);
    } catch (Exception e) {
      logger.error("删除异常" + e.getMessage());
      resultVO = new ResultVO(Constant.FAIL, "移除设备失败", null);
    }
    return resultVO;
  }

  /**
   * 查询某个标签下的设备信息
   *
   * @param tag
   * @return
   */
  @RequestMapping(value = "/getDevs", method = RequestMethod.POST)
  public ResultVO<TagDevInfo> queryByTag(@RequestBody Tag tag) {
    ResultVO resultVO = null;
    try {
      String accountId = tag.getAccountId();
      String tagId = tag.getTagId();
      TagDevInfo tagDevInfo = tagService.getDevs(accountId, tagId);
      resultVO = new ResultVO(Constant.SUCCESS, "查询标签分组详情成功", tagDevInfo);
    } catch (PermissionFailException e) {
      logger.error(e.getMessage());
      resultVO = new ResultVO(Constant.FAIL, e.getMessage(), null);
    } catch (Exception e) {
      logger.error("删除异常" + e.getMessage());
      resultVO = new ResultVO(Constant.FAIL, "查询标签分组详情失败", null);
    }
    return resultVO;
  }
}
