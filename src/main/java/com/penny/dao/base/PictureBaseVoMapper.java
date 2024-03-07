package com.penny.dao.base;

import com.penny.vo.base.PictureBaseVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PictureBaseVoMapper {
    int deleteByPrimaryKey(Long pictureId);

    int insert(PictureBaseVo record);

    int insertSelective(PictureBaseVo record);

    PictureBaseVo selectByPrimaryKey(Long pictureId);

    int updateByPrimaryKeySelective(PictureBaseVo record);

    int updateByPrimaryKey(PictureBaseVo record);
}