package com.penny.dao.base;

import com.penny.vo.base.PictureDtBaseVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PictureDtBaseVoMapper {
    int deleteByPrimaryKey(Long pictureDtId);

    int insert(PictureDtBaseVo record);

    int insertSelective(PictureDtBaseVo record);

    PictureDtBaseVo selectByPrimaryKey(Long pictureDtId);

    int updateByPrimaryKeySelective(PictureDtBaseVo record);

    int updateByPrimaryKey(PictureDtBaseVo record);
}