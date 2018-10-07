package com.warpfuture.repository.impl;

import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.Tag;
import com.warpfuture.repository.TagRepository;
import com.warpfuture.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Created by fido on 2018/4/19. */
@Repository
public class TagRepositoryImpl implements TagRepository {

  @Autowired private MongoTemplate mongoTemplate;

  @Override
  public void createTag(Tag tag) {
    mongoTemplate.insert(tag);
  }

  @Override
  public void updateTag(Tag tag) {
    mongoTemplate.save(tag);
  }

  @Override
  public void deleteTag(String tagId) {
    Query query = new Query();
    query.addCriteria(Criteria.where("tagId").is(tagId));
    Update update =
        new Update().set("isDelete", true).set("updateTime", System.currentTimeMillis());
    mongoTemplate.updateFirst(query, update, Tag.class);
  }

  @Override
  public PageModel<Tag> queryByAccountId(String accountId, Integer pageSize, Integer pageIndex) {
    Criteria criatira = new Criteria();
    criatira.andOperator(
        Criteria.where("accountId").is(accountId), Criteria.where("isDelete").is(false));
    Query query = new Query(criatira);
    long rowCount = mongoTemplate.count(query, Tag.class); // 总记录数
    PageModel pageModel = new PageModel();
    pageModel.setRowCount((int) rowCount);
    pageModel.setPageIndex(pageIndex);
    pageModel.setPageSize(pageSize);
    pageModel.setSkip(PageUtils.getSkip(pageIndex, pageSize));
    query.skip(pageModel.getSkip()).limit(pageSize);
    List<Tag> result = mongoTemplate.find(query, Tag.class);
    pageModel.setData(result);
    return pageModel;
  }

  @Override
  public Tag query(String tagId) {
    return mongoTemplate.findById(tagId, Tag.class);
  }

  @Override
  public Tag queryByName(String tagName) {
    Query query = new Query();
    query.addCriteria(Criteria.where("tagName").is(tagName));
    Tag tag = mongoTemplate.findOne(query, Tag.class);
    return tag;
  }
}
