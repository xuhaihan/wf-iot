package com.warpfuture.entity;

import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by 徐海瀚 on 2018/4/17. 账户实体
 */
@Document(collection = "iot_user_info")
@ToString
public class UserEntity implements Serializable {

    private String accountId; // 企业id
    private String applicationId; // 应用id
    @Id
    private String userId; // 用户id
    private String password; // 账号密码
    private String phone; // 电话
    private String email; // 邮件
    private String nickname; // 昵称
    private List<String> oauthType; // 认证类型
    private Map<String, Object> qqData; // qq认证的数据
    private Map<String, Object> wxData; // 微信认证的数据
    private Map<String, Object> wbData; // 微博认证的数据
    private Long createTime; // 创建时间
    private Long updateTime; // 更新时间
    private Long lastLoginTime; // 最后一次登陆时间
    private Boolean isDelete; // 是否删除标记
    private Map<String, Object> expand; // 拓展字段

    public UserEntity() {
    }

    public UserEntity(String accountId, String applicationId, String userId) {
        this.accountId = accountId;
        this.applicationId = applicationId;
        this.userId = userId;
    }

    public UserEntity(String accountId, String userId) {
        this.accountId = accountId;
        this.userId = userId;
    }

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<String> getOauthType() {
        return oauthType;
    }

    public void setOauthType(List<String> oauthType) {
        this.oauthType = oauthType;
    }

    public Map<String, Object> getQqData() {
        return qqData;
    }

    public void setQqData(Map<String, Object> qqData) {
        this.qqData = qqData;
    }

    public Map<String, Object> getWxData() {
        return wxData;
    }

    public void setWxData(Map<String, Object> wxData) {
        this.wxData = wxData;
    }

    public Map<String, Object> getWbData() {
        return wbData;
    }

    public void setWbData(Map<String, Object> wbData) {
        this.wbData = wbData;
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

    public Long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    public Map<String, Object> getExpand() {
        return expand;
    }

    public void setExpand(Map<String, Object> expand) {
        this.expand = expand;
    }
}
