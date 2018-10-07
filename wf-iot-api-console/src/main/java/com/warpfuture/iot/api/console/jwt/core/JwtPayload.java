package com.warpfuture.iot.api.console.jwt.core;

import lombok.*;

/**
 * jwt 负载
 *
 * @author scolia
 */
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@ToString
public class JwtPayload {

    /**
     * 用户id
     */
    @NonNull
    private String userId;

}
