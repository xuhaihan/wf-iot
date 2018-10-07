package com.warpfuture.repository;

import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.Tag;

/** Created by fido on 2018/4/19. */
public interface TagRepository {

  public void createTag(Tag tag);

  public void updateTag(Tag tag);

  public void deleteTag(String tagId);

  public PageModel<Tag> queryByAccountId(String accountId, Integer pageIndex, Integer pageSize);

  public Tag query(String tagId);

  public Tag queryByName(String tagName);
}
