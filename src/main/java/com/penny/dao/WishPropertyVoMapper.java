package com.penny.dao;

import com.penny.vo.WishPropertyVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WishPropertyVoMapper {
    /**
     * 根據房源 ID 選擇願望房源。
     *
     * @param propertyId 房源 ID
     * @return 返回願望房源
     */
    WishPropertyVo selectByPropertyId(Long propertyId);

    /**
     * 根據使用者 ID 列出願望房源列表。
     *
     * @param ecUserId  使用者的 ID
     * @return 返回願望房源列表
     */
    List<WishPropertyVo> listByEcUserId(Long ecUserId);
}
