package com.penny.dao;

import com.penny.vo.AddressVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressVoMapper {
    /**
     * 根據地區 ID 和街道名稱選擇地址。
     *
     * @param districtId 地區 ID
     * @param street 街道名稱
     * @return 符合條件的地址
     */
    AddressVo selectByDistrictIdAndStreet(Long districtId, String street);

    AddressVo selectWithDistrictByAddressId(Long addressId);
}
