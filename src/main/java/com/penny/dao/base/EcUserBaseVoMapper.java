package com.penny.dao.base;

import com.penny.vo.base.EcUserBaseVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EcUserBaseVoMapper {
    int deleteByPrimaryKey(Long ecUserId);

    int insert(EcUserBaseVo record);

    int insertSelective(EcUserBaseVo record);

    EcUserBaseVo selectByPrimaryKey(Long ecUserId);

    int updateByPrimaryKeySelective(EcUserBaseVo record);

    int updateByPrimaryKey(EcUserBaseVo record);
}