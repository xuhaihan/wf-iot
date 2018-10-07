package com.warpfuture.config;

import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

public class CorsFilterUtils {

    public static CorsFilter getAllowAllCorsFilter() {
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Collections.singletonList("*"));
        config.setAllowedMethods(Arrays.asList(HttpMethod.GET.name(), HttpMethod.HEAD.name(),
                HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name()));
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(1800L);
        configSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(configSource);
    }


}
