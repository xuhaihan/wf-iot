package com.warpfuture.iot.api.enterprise.service.impl;

import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.Tag;
import com.warpfuture.iot.api.enterprise.feign.service.TagEnterpriseFeignService;
import com.warpfuture.iot.api.enterprise.service.TagEnterpriseService;
import com.warpfuture.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TagEnterpriseServiceImpl implements TagEnterpriseService {

    @Autowired
    private TagEnterpriseFeignService tagEnterpriseFeignService;

    @Override
    public ResultVO createTag(Tag tag) {
        ResultVO<Tag> result;
        try {
            result = tagEnterpriseFeignService.createTag(tag);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO<Tag>().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO updateTag(Tag tag) {
        ResultVO<Tag> result;
        try {
            result = tagEnterpriseFeignService.updateTag(tag);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO<Tag>().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO queryTag(Tag tag) {
        ResultVO result;
        try {
            result = tagEnterpriseFeignService.queryTag(tag);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO<Tag>().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO queryTagByAccountId(String accountId, Integer pageSize, Integer pageIndex) {
        ResultVO result;
        try {
            result = tagEnterpriseFeignService.queryTagsByAccountId(accountId, pageSize, pageIndex);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO<PageModel<Tag>>().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO deleteTag(Tag tag) {
        ResultVO<Tag> result;
        try {
            result = tagEnterpriseFeignService.deleteTag(tag);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO<Tag>().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO addDeviceTag(Tag tag, List<String> deviceList) {
        ResultVO result;
        try {
            result = tagEnterpriseFeignService.addDeviceTag(tag, deviceList);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO removeDeviceTag(Tag tag, List<String> deviceList) {
        ResultVO result;
        try {
            result = tagEnterpriseFeignService.removeDeviceTag(tag, deviceList);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }
}
