package com.warpfuture.iot.oauth.repository.impl;

import com.warpfuture.iot.oauth.repository.base.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public abstract class BaseRepositoryImpl<T> implements BaseRepository<T> {
  @Autowired private MongoTemplate mongoTemplate;

  protected abstract Class<T> getEntityClass();

  @Override
  public void save(T t) {
    mongoTemplate.save(t);
  }

  @Override
  public void save(T t, String collectionName) {
    mongoTemplate.save(t, collectionName);
  }

  @Override
  public void update(Query query, Update update) {
    mongoTemplate.upsert(query, update, getEntityClass());
  }

  @Override
  public void updateFirst(Query query, Update update) {
    mongoTemplate.updateFirst(query, update, getEntityClass());
  }

  @Override
  public void updateFirst(Query query, Update update, String collectionName) {
    mongoTemplate.updateFirst(query, update, getEntityClass(), collectionName);
  }

  @Override
  public void updateMulti(Query query, Update update) {
    mongoTemplate.updateMulti(query, update, getEntityClass());
  }

  @Override
  public void updateMulti(Query query, Update update, String collectionName) {
    mongoTemplate.updateMulti(query, update, getEntityClass(), collectionName);
  }

  @Override
  public List<T> find(Query query) {
    return mongoTemplate.find(query, getEntityClass());
  }

  @Override
  public List<T> find(Query query, String collectionName) {
    return mongoTemplate.find(query, getEntityClass(), collectionName);
  }

  @Override
  public List<T> findAll() {
    return mongoTemplate.findAll(getEntityClass());
  }

  @Override
  public List<T> findAll(String collectionName) {
    return mongoTemplate.findAll(getEntityClass(), collectionName);
  }

  @Override
  public List<T> findAllAndRemove(Query query) {
    return mongoTemplate.findAllAndRemove(query, getEntityClass());
  }

  @Override
  public List<T> findAllAndRemove(Query query, String collectionName) {
    return mongoTemplate.findAllAndRemove(query, getEntityClass(), collectionName);
  }

  @Override
  public T findById(Object id) {
    return mongoTemplate.findById(id, getEntityClass());
  }

  @Override
  public T findById(Object id, String collectionName) {
    return mongoTemplate.findById(id, getEntityClass(), collectionName);
  }

  @Override
  public T findOne(Query query) {

    return mongoTemplate.findOne(query, getEntityClass());
  }

  @Override
  public T findOne(Query query, String collectionName) {
    return mongoTemplate.findOne(query, getEntityClass(), collectionName);
  }

  @Override
  public void insert(T t) {
    mongoTemplate.insert(t);
  }

  @Override
  public void insert(T t, String collectionName) {
    mongoTemplate.insert(t, collectionName);
  }

  @Override
  public void insertAll(Collection<?> collection) {
    insertAll(collection);
  }

  @Override
  public void remove(T t) {
    mongoTemplate.remove(t);
  }

  @Override
  public void remove(T t, String collectionName) {
    mongoTemplate.remove(t, collectionName);
  }

  @Override
  public void remove(Query query) {
    mongoTemplate.remove(query, getEntityClass());
  }

  @Override
  public void remove(Query query, String collectionName) {
    mongoTemplate.remove(query, getEntityClass(), collectionName);
  }

  public void setMongoTemplate(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }
}
