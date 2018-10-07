package com.warpfuture.entity;


import lombok.Data;
import lombok.ToString;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;

/** Created by fido on 2018/4/26. 设备历史通信数据实体 */
@Data
@ToString
@Table("wf_iot_historydata")
public class HistoricalData {
  @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
  private String historyDataId; // 记录主键,以accountId:productionId:deviceId组合

  @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordinal = 2)
  private Integer dataType; // 0：下发 1：上报

  @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordinal = 1)
  private Date dataTime; // 数据通信时间

  @Column
  private String dataContent; // 通信数据
  @Column private String deviceIp; // 设备ip
}
