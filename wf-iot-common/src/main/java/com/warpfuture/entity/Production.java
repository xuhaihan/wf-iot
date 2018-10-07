package com.warpfuture.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/** Created by fido on 2018/4/13. 产品类 */
@Document(collection = "iot_production_info") // 指定文档名为iot_production_info
@Data
@ToString
public class Production implements Serializable {
  @Id private String productionId; // 产品Id
  private String accountId; // 账户Id
  private String productionName; // 产品名称
  private int[] encryptRole; // 加密规则
  private String productionLogo; // 产品图片地址
  private String productionDesc; // 产品描述
  private Integer productionStatus; // 产品状态 0：未发布 1：已发布 2：已下架
  private String productionKey; // 产品密码
  private String productionPublicSecure; // 公钥
  private String productionPrivateSecure; // 密钥
  private Map<String, String> extensions; // 拓展属性
  private Boolean isDelete = false; // 是否删除
  private Long createTime; // 创建时间
  private Long updateTime; // 更新时间
  private List<String> devList; //设备id列表

  public Production() {
  }

  public Production(String productionId, String accountId) {
    this.productionId = productionId;
    this.accountId = accountId;
  }
}
