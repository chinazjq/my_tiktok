package com.zjq.controller;

import com.zjq.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version: java version 1.8
 * @Author: zjq
 * @description:
 * @date: 2022-11-09 10:16
 */
@RestController
@RequestMapping("test")
@Api(tags = "测试接口")
public class TestController {

    @GetMapping("hello")
    @ApiOperation(value = "hello-测试接口方法")
    public GraceJSONResult hello() {
        return GraceJSONResult.ok("hello, tiktok!!!");
    }

}
