package com.warpfuture.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Objects;

/** Created by fido on 2018/4/14. 应用与产品的关系 */
@Document(collection = "iot_route_info")
@Data
@ToString
public class Route implements Serializable {
  private String accountId; // 账户Id
  @Id private String routeId; // 路由规则Id
  private String applicationId; // 应用Id
  private String productionId; // 产品Id
  private Integer relation; // 关系 0：上报 1：下发 2：双向通信
  private Boolean broadcast; // 广播模式 true:允许 false：不允许
  private Boolean isDelete; // 是否删除
  private Long createTime; // 创建时间
  private Long updateTime; // 更新时间

  public Route(
      String accountId,
      String applicationId,
      String productionId,
      Integer relation,
      Boolean broadcast) {
    this.accountId = accountId;
    this.applicationId = applicationId;
    this.productionId = productionId;
    this.relation = relation;
    this.broadcast = broadcast;
  }

  public Route() {}

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Route)) {
      return false;
    }
    Route route = (Route) o;
    return applicationId.equals(((Route) o).getApplicationId())
        && productionId.equals(((Route) o).getProductionId())
        && (isDelete == ((Route) o).isDelete);
  }

  @Override
  public int hashCode() {
    return Objects.hash(applicationId, productionId, isDelete);
  }
}
