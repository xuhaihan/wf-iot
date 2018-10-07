package com.warpfuture.iot.api.enterprise.dto;

import lombok.Data;

@Data
public class UserLogin {
    private String userAccount;
    private String password;
}
