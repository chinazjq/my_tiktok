package com.zjq.controller;

import com.zjq.bo.RegistLoginBO;
import com.zjq.constant.RedisKeyDefinedConstant;
import com.zjq.pojo.Users;
import com.zjq.result.GraceJSONResult;
import com.zjq.result.ResponseStatusEnum;
import com.zjq.service.UserService;
import com.zjq.utils.IPUtil;
import com.zjq.utils.RedisOperator;
import com.zjq.utils.SMSUtils;
import com.zjq.vo.UsersVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

/**
 * @version: java version 1.8
 * @Author: zjq
 * @description:
 * @date: 2022-11-10 9:30
 */
@RestController
@RequestMapping("passport")
public class LoginController {

    @Autowired
    private SMSUtils smsUtils;

    @Autowired
    private RedisOperator redisOperator;

    @Autowired
    private UserService userService;

    /**
     * 获取验证码
     * @param mobile
     * @param request
     * @return
     */
    @PostMapping("getSMSCode")
    public GraceJSONResult getSMSCode(@RequestParam("mobile") String mobile,
                                      HttpServletRequest request) {
        if (StringUtils.isBlank(mobile)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.EMPTY_PARAMS_ERROR);
        }
        // 获取用户ip地址，存入redis，控制60秒内重复请求
        String requestIp = IPUtil.getRequestIp(request);
        redisOperator.setnx60s(RedisKeyDefinedConstant.MOBILE_SMSCODE + ":" + requestIp, requestIp);
        // 生成随机六位数验证码
        String code = (int) ( ( Math.random() * 9 + 1 ) * 100000 ) + "";
        smsUtils.sendSMS(mobile, code);
        redisOperator.set(RedisKeyDefinedConstant.MOBILE_SMSCODE + ":" + mobile, code, 60 * 30);
        return GraceJSONResult.ok();
    }

    /**
     * 登录
     * @param registLoginBO
     * @return
     */
    @PostMapping("login")
    public GraceJSONResult login(@RequestBody @Valid RegistLoginBO registLoginBO) {
        // 校验验证码是否匹配
        String redisKey = RedisKeyDefinedConstant.MOBILE_SMSCODE + ":" + registLoginBO.getMobile();
        String redisCode = redisOperator.get(redisKey);
        if (StringUtils.isBlank(redisCode) || !redisCode.equals(registLoginBO.getSmsCode())) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }
        // 存储用户信息
        Users user = userService.queryMobileIsExist(registLoginBO.getMobile());
        if (user == null) {
            user = userService.createUser(registLoginBO.getMobile());
        }
        String uToken = UUID.randomUUID().toString();
        redisOperator.set(RedisKeyDefinedConstant.REDIS_USER_TOKEN + ":" + user.getId(), uToken);
        redisOperator.del(RedisKeyDefinedConstant.MOBILE_SMSCODE + ":" + registLoginBO.getMobile());

        UsersVO userVO = new UsersVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setUserToken(uToken);
        return GraceJSONResult.ok(userVO);
    }

    /**
     * 退出登录
     * @param userId
     * @return
     */
    @PostMapping("logout")
    public GraceJSONResult logout(String userId) {
        // 清楚token信息
        redisOperator.del(RedisKeyDefinedConstant.REDIS_USER_TOKEN + ":" + userId);
        return GraceJSONResult.ok();
    }

}
