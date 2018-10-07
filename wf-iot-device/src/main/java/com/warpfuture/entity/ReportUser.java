package com.warpfuture.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "iot_user_info")
@Data
@ToString
public class ReportUser {
  private String accountId; // 企业id
  private String applicationId; // 应用id
  @Id private String userId; // 用户id
}
