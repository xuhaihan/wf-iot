package com.warpfuture.iot.oauth.service.impl;

import com.warpfuture.iot.oauth.repository.base.BaseRepository;
import com.warpfuture.iot.oauth.service.base.BaseService;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import java.util.Collection;
import java.util.List;

public abstract class BaseServiceImpl<T> implements BaseService<T> {

  private BaseRepository<T> baseRepository;

  protected void setBaseRepository(BaseRepository<T> baseRepository) {
    this.baseRepository = baseRepository;
  }

  @Override
  public void save(T t) {
    baseRepository.save(t);
  }

  @Override
  public void save(T t, String collectionName) {
    baseRepository.save(t, collectionName);
  }

  @Override
  public void update(Query query, Update update) {
    baseRepository.update(query, update);
  }

  @Override
  public void updateFirst(Query query, Update update) {
    baseRepository.updateFirst(query, update);
  }

  @Override
  public void updateFirst(Query query, Update update, String collectionName) {
    baseRepository.updateFirst(query, update, collectionName);
  }

  @Override
  public void updateMulti(Query query, Update update) {
    baseRepository.updateFirst(query, update);
  }

  @Override
  public void updateMulti(Query query, Update update, String collectionName) {
    baseRepository.updateMulti(query, update, collectionName);
  }

  @Override
  public List<T> find(Query query) {
    return baseRepository.find(query);
  }

  @Override
  public List<T> find(Query query, String collectionName) {

    return baseRepository.find(query, collectionName);
  }

  @Override
  public List<T> findAll() {

    return baseRepository.findAll();
  }

  @Override
  public List<T> findAll(String collectionName) {

    return baseRepository.findAll(collectionName);
  }

  @Override
  public List<T> findAllAndRemove(Query query) {

    return baseRepository.findAllAndRemove(query);
  }

  @Override
  public List<T> findAllAndRemove(Query query, String collectionName) {
    return baseRepository.findAllAndRemove(query, collectionName);
  }

  @Override
  public T findById(Object id) {

    return baseRepository.findById(id);
  }

  @Override
  public T findById(Object id, String collectionName) {
    return baseRepository.findById(id, collectionName);
  }

  @Override
  public T findOne(Query query) {

    return baseRepository.findOne(query);
  }

  @Override
  public T findOne(Query query, String collectionName) {
    return baseRepository.findOne(query, collectionName);
  }

  @Override
  public void insert(T t) {
    baseRepository.insert(t);
  }

  @Override
  public void insert(T t, String collectionName) {
    baseRepository.insert(t, collectionName);
  }

  @Override
  public void insertAll(Collection<?> collection) {
    baseRepository.insertAll(collection);
  }

  @Override
  public void remove(T t) {
    baseRepository.remove(t);
  }

  @Override
  public void remove(T t, String collectionName) {
    baseRepository.remove(t, collectionName);
  }

  @Override
  public void remove(Query query) {
    baseRepository.remove(query);
  }

  @Override
  public void remove(Query query, String collectionName) {
    baseRepository.remove(query, collectionName);
  }
}
