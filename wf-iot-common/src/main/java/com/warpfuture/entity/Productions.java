package com.warpfuture.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * 接收前端产品应用关联规则
 */
@Data
@ToString
@AllArgsConstructor
public class Productions {

    private String productionId;

    private Integer[] relation;

    private boolean broadcast;

}