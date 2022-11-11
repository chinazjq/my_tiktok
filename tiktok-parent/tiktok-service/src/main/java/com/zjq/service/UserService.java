package com.zjq.service;

import com.zjq.bo.UpdatedUserBO;
import com.zjq.pojo.Users;

/**
 * @version: java version 1.8
 * @Author: zjq
 * @description:
 * @date: 2022-11-10 13:53
 */
public interface UserService {

    Users createUser(String mobile);

    Users queryMobileIsExist(String mobile);

    Users getUser(String userId);

    Users updateUserInfo(UpdatedUserBO updatedUserBO, Integer type);

    Users updateUserInfo(UpdatedUserBO updatedUserBO);

}
