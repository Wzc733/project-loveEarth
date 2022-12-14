package com.fwzc.rbcollect.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * @Auther:wzc
 * @Data:2021/12/3 - 12 - 03 - 22:01
 * 解决gataway跨域问题
 * 会和@CrossOrigin冲突
 */
@Configuration
public class CorsConfig {
    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        //是否允许携带cookie
        config.setAllowCredentials(true);
        //可接受的域，是一个具体域名或者*（代表任意域名）
        config.addAllowedOrigin("*");
        //允许携带的头
        config.addAllowedHeader("*");
        //允许访问的方式
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
