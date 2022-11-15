package com.zjq.service.impl;

import com.github.pagehelper.PageHelper;
import com.zjq.bo.VlogBO;
import com.zjq.constant.RedisKeyDefinedConstant;
import com.zjq.enums.MessageEnum;
import com.zjq.enums.YesOrNo;
import com.zjq.mapper.MyLikedVlogMapper;
import com.zjq.mapper.VlogMapper;
import com.zjq.mapper.VlogMapperCustom;
import com.zjq.pojo.MyLikedVlog;
import com.zjq.pojo.Vlog;
import com.zjq.service.FansService;
import com.zjq.service.VlogService;
import com.zjq.utils.PagedGridResult;
import com.zjq.utils.RedisOperator;
import com.zjq.vo.IndexVlogVO;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VlogServiceImpl implements VlogService {
    @Autowired
    private VlogMapper vlogMapper;
    @Autowired
    private VlogMapperCustom vlogMapperCustom;
    @Autowired
    private MyLikedVlogMapper myLikedVlogMapper;
    @Autowired
    private FansService fansService;
    @Autowired
    private Sid sid;
    @Autowired
    private RedisOperator redis;

    @Override
    public void createVlog(VlogBO vlogBO) {
        String vid = sid.nextShort();
        Vlog vlog = new Vlog();
        BeanUtils.copyProperties(vlogBO, vlog);
        vlog.setId(vid);
        vlog.setLikeCounts(0);
        vlog.setCommentsCounts(0);
        vlog.setIsPrivate(YesOrNo.NO.type);
        vlog.setCreatedTime(new Date());
        vlog.setUpdatedTime(new Date());
        vlogMapper.insert(vlog);
    }

    @Override
    public PagedGridResult getIndexVlogList(String userId, String search, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(search)) {
            map.put("search", search);
        }
        List<IndexVlogVO> list = vlogMapperCustom.getIndexVlogList(map);
        for (IndexVlogVO v : list) {
            String vlogerId = v.getVlogerId();
            String vlogId = v.getVlogId();
            if (StringUtils.isNotBlank(userId)) {
                // 用户是否关注该博主
                boolean doIFollowVloger = fansService.queryDoIFollowVloger(userId, vlogerId);
                v.setDoIFollowVloger(doIFollowVloger);
                // 判断当前用户是否点赞过视频
                v.setDoILikeThisVlog(doILikeVlog(userId, vlogId));
            }
            // 获得当前视频被点赞过的总数
            v.setLikeCounts(getVlogBeLikedCounts(vlogId));
        }
        return PagedGridResult.setterPagedGrid(list, page);
    }

    @Override
    public IndexVlogVO getVlogDetailById(String userId, String vlogId) {
        Map<String, Object> map = new HashMap<>();
        map.put("vlogId", vlogId);
        List<IndexVlogVO> list = vlogMapperCustom.getVlogDetailById(map);
        if (list != null && list.size() > 0 && !list.isEmpty()) {
            IndexVlogVO vlogVO = list.get(0);
            return setterVO(vlogVO, userId);
        }
        return null;
    }

    @Override
    public void changeToPrivateOrPublic(String userId, String vlogId, Integer yesOrNo) {
        Example example = new Example(Vlog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", vlogId);
        criteria.andEqualTo("vlogerId", userId);
        Vlog pendingVlog = new Vlog();
        pendingVlog.setIsPrivate(yesOrNo);
        vlogMapper.updateByExampleSelective(pendingVlog, example);
    }

    @Override
    public PagedGridResult queryMyVlogList(String userId, Integer page, Integer pageSize, Integer yesOrNo) {
        Example example = new Example(Vlog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("vlogerId", userId);
        criteria.andEqualTo("isPrivate", yesOrNo);
        PageHelper.startPage(page, pageSize);
        List<Vlog> list = vlogMapper.selectByExample(example);
        return PagedGridResult.setterPagedGrid(list, page);
    }

    @Override
    public PagedGridResult getMyLikedVlogList(String userId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        List<IndexVlogVO> list = vlogMapperCustom.getMyLikedVlogList(map);
        return PagedGridResult.setterPagedGrid(list, page);
    }

    @Override
    public void userLikeVlog(String userId, String vlogId) {

    }

    @Override
    public void userUnLikeVlog(String userId, String vlogId) {

    }

    @Override
    public Integer getVlogBeLikedCounts(String vlogId) {
        return null;
    }

    @Override
    public PagedGridResult getMyFollowVlogList(String myId, Integer page, Integer pageSize) {
        return null;
    }

    @Override
    public PagedGridResult getMyFriendVlogList(String myId, Integer page, Integer pageSize) {
        return null;
    }

    private boolean doILikeVlog(String myId, String vlogId) {
        String doILike = redis.get(RedisKeyDefinedConstant.REDIS_USER_LIKE_VLOG + ":" + myId + ":" + vlogId);
        boolean isLike = false;
        if (StringUtils.isNotBlank(doILike) && doILike.equalsIgnoreCase("1")) {
            isLike = true;
        }
        return isLike;
    }

    private IndexVlogVO setterVO(IndexVlogVO v, String userId) {
        String vlogerId = v.getVlogerId();
        String vlogId = v.getVlogId();
        if (StringUtils.isNotBlank(userId)) {
            // 用户是否关注该博主
            boolean doIFollowVloger = fansService.queryDoIFollowVloger(userId, vlogerId);
            v.setDoIFollowVloger(doIFollowVloger);
            // 判断当前用户是否点赞过视频
            v.setDoILikeThisVlog(doILikeVlog(userId, vlogId));
        }
        // 获得当前视频被点赞过的总数
        v.setLikeCounts(getVlogBeLikedCounts(vlogId));
        return v;
    }
}
