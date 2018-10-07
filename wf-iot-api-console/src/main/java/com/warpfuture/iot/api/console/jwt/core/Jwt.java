package com.warpfuture.iot.api.console.jwt.core;

import java.lang.annotation.*;


@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Jwt {

}
