package com.penny.dao;

import com.penny.vo.WishPropertyVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WishPropertyVoMapper {
    WishPropertyVo selectByPropertyId(Long propertyId);
}
