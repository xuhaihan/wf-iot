package com.warpfuture.entity;

import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/** Created by 徐海瀚 on 2018/4/17. 应用实体 */
@Document(collection = "iot_application_info")
@ToString
public class ApplicationEntity implements Serializable {
  private String accountId;
  @Id private String applicationId; // 应用id
  private String applicationName; // 应用名称
  private String applicationDescribe; // 应用描述
  private List<String> oauthType; // 支持的认证类型
  private Map<String, Object> oauthData; // key [wx,qq,wb] ;value [h5,app,web]
  private Map<String, Object> expand; // 拓展字段
  private Long createTime; // 创建时间
  private Long updateTime; // 更新时间
  private Boolean isDelete; // 用于假删除;false 表示未删除,true 表示删除

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public String getApplicationId() {
    return applicationId;
  }

  public void setApplicationId(String applicationId) {
    this.applicationId = applicationId;
  }

  public String getApplicationName() {
    return applicationName;
  }

  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }

  public String getApplicationDescribe() {
    return applicationDescribe;
  }

  public void setApplicationDescribe(String applicationDescribe) {
    this.applicationDescribe = applicationDescribe;
  }

  public Map<String, Object> getOauthData() {
    return oauthData;
  }

  public void setOauthData(Map<String, Object> oauthData) {
    this.oauthData = oauthData;
  }

  public List<String> getOauthType() {
    return oauthType;
  }

  public void setOauthType(List<String> oauthType) {
    this.oauthType = oauthType;
  }

  public Map<String, Object> getExpand() {
    return expand;
  }

  public void setExpand(Map<String, Object> expand) {
    this.expand = expand;
  }

  public Long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Long createTime) {
    this.createTime = createTime;
  }

  public Long getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Long updateTime) {
    this.updateTime = updateTime;
  }

  public Boolean getDelete() {
    return isDelete;
  }

  public void setDelete(Boolean delete) {
    isDelete = delete;
  }
}
