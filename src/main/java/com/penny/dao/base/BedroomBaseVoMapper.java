package com.penny.dao.base;

import com.penny.vo.base.BedroomBaseVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BedroomBaseVoMapper {
    int deleteByPrimaryKey(Long bedroomId);

    int insert(BedroomBaseVo record);

    int insertSelective(BedroomBaseVo record);

    BedroomBaseVo selectByPrimaryKey(Long bedroomId);

    int updateByPrimaryKeySelective(BedroomBaseVo record);

    int updateByPrimaryKey(BedroomBaseVo record);
}