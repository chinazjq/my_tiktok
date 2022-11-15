package com.zjq.service;

import com.zjq.bo.VlogBO;
import com.zjq.utils.PagedGridResult;
import com.zjq.vo.IndexVlogVO;

public interface VlogService {
    void createVlog(VlogBO vlogBO);

    PagedGridResult getIndexVlogList(String userId, String search, Integer page, Integer pageSize);

    IndexVlogVO getVlogDetailById(String userId, String vlogId);

    void changeToPrivateOrPublic(String userId, String vlogId, Integer type);

    PagedGridResult queryMyVlogList(String userId, Integer page, Integer pageSize, Integer type);

    PagedGridResult getMyLikedVlogList(String userId, Integer page, Integer pageSize);

    void userLikeVlog(String userId, String vlogId);

    void userUnLikeVlog(String userId, String vlogId);

    Integer getVlogBeLikedCounts(String vlogId);

    PagedGridResult getMyFollowVlogList(String myId, Integer page, Integer pageSize);

    PagedGridResult getMyFriendVlogList(String myId, Integer page, Integer pageSize);

}
