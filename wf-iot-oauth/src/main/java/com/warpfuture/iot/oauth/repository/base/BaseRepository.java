package com.warpfuture.iot.oauth.repository.base;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Collection;
import java.util.List;

/** Created by 徐海瀚 on 2018/4/17. */
public interface BaseRepository<T> {
  public void save(T t);

  public void save(T t, String collectionName);

  public void update(Query query,Update update);

  public void updateFirst(Query query, Update update);

  public void updateFirst(Query query, Update update, String collectionName);

  public void updateMulti(Query query, Update update);

  public void updateMulti(Query query, Update update, String collectionName);


  public List<T> find(Query query);

  public List<T> find(Query query, String collectionName);

  public List<T> findAll();

  public List<T> findAll(String collectionName);


  public List<T> findAllAndRemove(Query query);

  public List<T> findAllAndRemove(Query query, String collectionName);

    public T findById(Object id);

    public T findById(Object id, String collectionName);

  public T findOne(Query query);

  public T findOne(Query query, String collectionName);

  public void insert(T t);

  public void insert(T t, String collectionName);

  public void insertAll(Collection<? extends Object> collection);

  public void remove(T t);

  public void remove(T t,String collectionName);

  public void remove(Query query);

  public void remove(Query query,  String collectionName);

}
