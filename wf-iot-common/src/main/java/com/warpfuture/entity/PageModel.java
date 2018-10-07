package com.warpfuture.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/** Created by fido on 2018/4/19. 分页展示模型 */
@Data
@ToString
public class PageModel<T> {
  private List<T> data; // 返回数据
  private Integer rowCount; // 总记录数
  private Integer pageSize; // 每页显示多少条
  private Integer pageIndex = 1; // 当前第几页
  @JsonIgnore
  private Integer skip; // 跳过第几页，mongoDB分页需要
}
