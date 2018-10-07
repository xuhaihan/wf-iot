package com.warpfuture.iot.api.enterprise.service;

import com.warpfuture.entity.Tag;
import com.warpfuture.vo.ResultVO;

import java.util.List;

public interface TagEnterpriseService {

    ResultVO createTag(Tag tag);

    ResultVO updateTag(Tag tag);

    ResultVO queryTag(Tag tag);

    ResultVO queryTagByAccountId(String accountId, Integer pageSize, Integer pageIndex);

    ResultVO deleteTag(Tag tag);

    ResultVO addDeviceTag(Tag tag, List<String> deviceList);

    ResultVO removeDeviceTag(Tag tag, List<String> deviceList);
}
