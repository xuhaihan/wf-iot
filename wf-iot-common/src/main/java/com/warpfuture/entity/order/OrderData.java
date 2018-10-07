package com.warpfuture.entity.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

/** Created by fido on 2018/4/18. */
@ToString
@Data
public class OrderData {
  private Integer realAmount; // 应结订单总额
  private Integer totalAmount; // 总金额

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Integer refundAmount; // 退款金额
}
