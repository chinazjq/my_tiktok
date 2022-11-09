package com.zjq.mapper;

import com.zjq.base.BaseMapper;
import com.zjq.pojo.Fans;
import com.zjq.vo.FansVO;
import com.zjq.vo.VlogerVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface FansMapperCustom extends BaseMapper<Fans> {

    public List<VlogerVO> queryMyFollows(@Param("paramMap") Map<String, Object> map);

    public List<FansVO> queryMyFans(@Param("paramMap") Map<String, Object> map);

}