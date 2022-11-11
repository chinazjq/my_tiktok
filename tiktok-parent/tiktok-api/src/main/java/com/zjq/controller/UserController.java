package com.zjq.controller;

import com.zjq.bo.UpdatedUserBO;
import com.zjq.config.MinIOConfig;
import com.zjq.constant.RedisKeyDefinedConstant;
import com.zjq.enums.FileTypeEnum;
import com.zjq.enums.UserInfoModifyType;
import com.zjq.pojo.Users;
import com.zjq.result.GraceJSONResult;
import com.zjq.result.ResponseStatusEnum;
import com.zjq.service.UserService;
import com.zjq.utils.MinIOUtils;
import com.zjq.utils.RedisOperator;
import com.zjq.vo.UsersVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @version: java version 1.8
 * @Author: zjq
 * @description:
 * @date: 2022-11-11 9:33
 */
@Slf4j
@Api(tags = "用户信息接口模块")
@RequestMapping("userInfo")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redisOperator;

    @Autowired
    private MinIOConfig minIOConfig;

    @GetMapping("query")
    public GraceJSONResult query(@RequestParam String userId) {
        Users user = userService.getUser(userId);
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(user, usersVO);

        // 我的关注博主总数量
        String myFollowsCountsStr = redisOperator.get(RedisKeyDefinedConstant.REDIS_MY_FOLLOWS_COUNTS + ":" + userId);
        // 我的粉丝总数
        String myFansCountsStr = redisOperator.get(RedisKeyDefinedConstant.REDIS_MY_FANS_COUNTS + ":" + userId);
        // 用户获赞总数，视频博主（点赞/喜欢）总和
        String likedVlogerCountsStr = redisOperator.get(RedisKeyDefinedConstant.REDIS_VLOGER_BE_LIKED_COUNTS + ":" + userId);

        Integer myFollowsCounts = 0;
        Integer myFansCounts = 0;
        Integer likedVlogCounts = 0;
        Integer likedVlogerCounts = 0;
        Integer totalLikeMeCounts = 0;

        if (StringUtils.isNotBlank(myFollowsCountsStr)) {
            myFollowsCounts = Integer.valueOf(myFollowsCountsStr);
        }
        if (StringUtils.isNotBlank(myFansCountsStr)) {
            myFansCounts = Integer.valueOf(myFansCountsStr);
        }
        if (StringUtils.isNotBlank(likedVlogerCountsStr)) {
            likedVlogerCounts = Integer.valueOf(likedVlogerCountsStr);
        }
        totalLikeMeCounts = likedVlogCounts + likedVlogerCounts;

        usersVO.setMyFollowsCounts(myFollowsCounts);
        usersVO.setMyFansCounts(myFansCounts);
        usersVO.setTotalLikeMeCounts(totalLikeMeCounts);
        return GraceJSONResult.ok(usersVO);
    }

    @PostMapping("modifyUserInfo")
    public GraceJSONResult modifyUserInfo(@RequestBody UpdatedUserBO updatedUserBO,
                                          @RequestParam Integer type) throws Exception {
        UserInfoModifyType.checkUserInfoTypeIsRight(type);
        Users newUserInfo = userService.updateUserInfo(updatedUserBO, type);
        return GraceJSONResult.ok(newUserInfo);
    }


    @PostMapping("modifyImage")
    public GraceJSONResult modifyImage(@RequestParam String userId,
                                       @RequestParam Integer type,
                                       MultipartFile file) throws Exception {
        if (type != FileTypeEnum.BGIMG.type && type != FileTypeEnum.FACE.type) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }
        String fileName = file.getOriginalFilename();
        MinIOUtils.uploadFile(minIOConfig.getBucketName(), fileName, file.getInputStream());
        StringBuilder imgUrl = new StringBuilder();
        imgUrl.append(minIOConfig.getFileHost()).append("/").append(minIOConfig.getBucketName()).append("/").append(fileName);
        // 修改图片地址到数据库
        UpdatedUserBO updatedUserBO = new UpdatedUserBO();
        updatedUserBO.setId(userId);
        if (type == FileTypeEnum.BGIMG.type) {
            updatedUserBO.setBgImg(imgUrl.toString());
        } else {
            updatedUserBO.setFace(imgUrl.toString());
        }
        Users users = userService.updateUserInfo(updatedUserBO);
        return GraceJSONResult.ok(users);
    }

}
