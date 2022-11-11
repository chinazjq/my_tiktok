package com.zjq.controller;

import com.zjq.pojo.Users;
import com.zjq.result.GraceJSONResult;
import com.zjq.service.UserService;
import com.zjq.utils.SMSUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SMSUtils smsUtils;

    @Autowired
    private UserService userService;

    @GetMapping("hello")
    @ApiOperation(value = "hello-测试hello接口")
    public GraceJSONResult hello() {
        return GraceJSONResult.ok("hello, tiktok!!!");
    }

    @GetMapping("testSendSms")
    @ApiOperation(value = "testSendSms-测试发送短信接口")
    public GraceJSONResult testSendSms() throws Exception {
        smsUtils.sendSMS("14790079097", "123456");
        return GraceJSONResult.ok();
    }

    @GetMapping("testSaveUser")
    @ApiOperation(value = "testSaveUser-测试保存用户")
    public GraceJSONResult testSaveUser(String mobile) throws Exception {
        Users user = userService.createUser(mobile);
        return GraceJSONResult.ok(user);
    }


}
