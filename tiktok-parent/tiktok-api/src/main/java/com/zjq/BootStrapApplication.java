package com.zjq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @version: java version 1.8
 * @Author: zjq
 * @description:
 * @date: 2022-11-09 14:52
 */
@SpringBootApplication
@MapperScan(basePackages = "com.zjq.mapper")
public class BootStrapApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootStrapApplication.class, args);
    }

}
