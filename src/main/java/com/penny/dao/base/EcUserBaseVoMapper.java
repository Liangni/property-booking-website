package com.penny.dao.base;

import com.penny.vo.base.EcUserBaseVo;

public interface EcUserBaseVoMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(EcUserBaseVo record);

    int insertSelective(EcUserBaseVo record);

    EcUserBaseVo selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(EcUserBaseVo record);

    int updateByPrimaryKey(EcUserBaseVo record);
}