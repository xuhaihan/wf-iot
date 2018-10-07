package com.warpfuture.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 接收前端的应用产品关联规则
 */
@Data
@ToString
public class RouteFront {

    private String accountId;

    private String applicationId;

    private List<Productions> productions;
}


