package com.warpfuture.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;

/** 关于某笔订单的回调通知次数，存进cassandra里，便于后面查找分析 */
@Data
@ToString
@Table("wf_iot_notifyrecord")
public class NotifyRecord {
  @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
  private String orderId; // 订单id

  @Column private String accountId; // 账户Id
  @Column private String merchantId; // 商户id
  @Column private Integer nums; // 次数

  @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordinal = 1)
  private Integer type; // 下单，退款..

  @Column private Date recordTime; // 记录时间
}
