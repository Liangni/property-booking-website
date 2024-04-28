package com.penny.dao;

import com.penny.vo.BookingOrderVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookingOrderVoMapper {
    /**
     * 根據顧客ID列出相應的預訂訂單。
     *
     * @param customerId 顧客ID
     * @return 符合條件的預訂訂單列表
     */
    List<BookingOrderVo> listByCustomerId(Long customerId);

    /**
     * 根據房東ID列出相應的預訂訂單。
     *
     * @param hostId 房東ID
     * @return 符合條件的預訂訂單列表
     */
    List<BookingOrderVo> listByHostId(Long hostId);
}
