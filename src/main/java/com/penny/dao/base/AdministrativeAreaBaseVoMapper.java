package com.penny.dao.base;

import com.penny.vo.base.AdministrativeAreaBaseVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdministrativeAreaBaseVoMapper {
    int deleteByPrimaryKey(Long administrativeAreaId);

    int insert(AdministrativeAreaBaseVo record);

    int insertSelective(AdministrativeAreaBaseVo record);

    AdministrativeAreaBaseVo selectByPrimaryKey(Long administrativeAreaId);

    int updateByPrimaryKeySelective(AdministrativeAreaBaseVo record);

    int updateByPrimaryKey(AdministrativeAreaBaseVo record);
}