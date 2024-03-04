package com.penny.dao.base;

import com.penny.vo.base.AddressBaseVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBaseVoMapper {
    int deleteByPrimaryKey(Long addressId);

    int insert(AddressBaseVo record);

    int insertSelective(AddressBaseVo record);

    AddressBaseVo selectByPrimaryKey(Long addressId);

    int updateByPrimaryKeySelective(AddressBaseVo record);

    int updateByPrimaryKey(AddressBaseVo record);
}