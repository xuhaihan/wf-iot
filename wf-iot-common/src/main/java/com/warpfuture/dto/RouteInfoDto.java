package com.warpfuture.dto;

import com.warpfuture.entity.Route;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/** Created by fido on 2018/5/16. */
@ToString
@Data
public class RouteInfoDto {
  private String accountId;
  private String applicationId;
  private List<Route> productions;
}
