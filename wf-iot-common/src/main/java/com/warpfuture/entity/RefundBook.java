package com.warpfuture.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** @Auther: fido @Date: 2018/6/5 21:20 @Description: */
@Document(collection = "iot_refundbook")
@ToString
@Data
public class RefundBook {
  @Id private String refundBookLocation;
  private byte[] content;
}
