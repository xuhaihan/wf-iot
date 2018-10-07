package com.warpfuture.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

/** Created by fido on 2018/4/19. 标签 */
@Document(collection = "iot_tag_info")
@ToString
@Data
public class Tag implements Serializable {
  private String accountId; // 账户Id
  @Id private String tagId; // 标签Id
  private String tagName; // 标签名
  private String tagDesc; // 标签描述
  private Boolean isDelete; // 是否删除

  private Long createTime; // 创建时间
  private Long updateTime; // 更新时间

  private List<String> deviceList;

  public Tag() {
  }

  public Tag(String accountId, String tagId) {
    this.accountId = accountId;
    this.tagId = tagId;
  }
}
