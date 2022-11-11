package com.zjq.config;

import com.zjq.intercepter.PassportInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @version: java version 1.8
 * @Author: zjq
 * @description:
 * @date: 2022-11-10 11:09
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public PassportInterceptor passportInterceptor() {
        return new PassportInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor())
                // 指定拦截路由
                .addPathPatterns("/passport/getSMSCode");
    }

}
