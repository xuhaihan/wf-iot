package com.warpfuture.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/** @Auther: fido @Date: 2018/5/26 22:37 @Description: */
@Data
@ToString
public class HistoryDataPageModel<T> {
  private List<T> data; // 返回数据
  private Long rowCount; // 总记录数
  private Integer pageSize; // 每页显示多少条
  private Integer pageIndex = 1; // 当前第几页
  private Integer skip; // 跳过第几页，mongoDB分页需要
}
