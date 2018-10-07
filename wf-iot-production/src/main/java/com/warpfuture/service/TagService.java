package com.warpfuture.service;

import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.Tag;
import com.warpfuture.vo.TagDevInfo;

import java.util.List;

/**
 * Created by fido on 2018/4/19.
 */
public interface TagService {
    public Tag createTag(Tag tag);

    public Tag updateTag(Tag tag);

    public void deleteTag(String tagId, String accountId);

    public PageModel<Tag> queryTagByAccountId(String accountId, Integer pageSize, Integer pageIndex);

    public Tag queryTag(String accountId, String tagId);

    public TagDevInfo addDevs(String accountId, String tagId, List<String> deviceList);

    public void removeDevs(String accountId, String tagId, List<String> deviceList);

    public TagDevInfo getDevs(String accountId, String tagId);
}
