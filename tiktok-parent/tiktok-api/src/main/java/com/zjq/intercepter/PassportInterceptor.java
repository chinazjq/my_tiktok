package com.zjq.intercepter;

import com.zjq.constant.RedisKeyDefinedConstant;
import com.zjq.exceptions.GraceException;
import com.zjq.result.ResponseStatusEnum;
import com.zjq.utils.IPUtil;
import com.zjq.utils.RedisOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @version: java version 1.8
 * @Author: zjq
 * @description:
 * @date: 2022-11-10 10:14
 */
@Slf4j
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisOperator redisOperator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userIp = IPUtil.getRequestIp(request);
        boolean keyIsExist = redisOperator.keyIsExist(RedisKeyDefinedConstant.MOBILE_SMSCODE + ":" + userIp);
        if (keyIsExist) {
            // 短信发送频率过快
            GraceException.display(ResponseStatusEnum.SMS_NEED_WAIT_ERROR);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

}
