package com.penny.dao.base;

import com.penny.vo.base.EcUserPictureBaseVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EcUserPictureBaseVoMapper {
    int deleteByPrimaryKey(Long ecUserPictureId);

    int insert(EcUserPictureBaseVo record);

    int insertSelective(EcUserPictureBaseVo record);

    EcUserPictureBaseVo selectByPrimaryKey(Long ecUserPictureId);

    int updateByPrimaryKeySelective(EcUserPictureBaseVo record);

    int updateByPrimaryKey(EcUserPictureBaseVo record);
}