package com.penny.dao;

import com.penny.vo.DistrictVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DistrictVoMapper {

    /**
     * 根據行政區名稱關鍵字列出行政區。
     *
     * @param districtName 行政區名稱的關鍵字。
     * @param offset       查詢的偏移量。
     * @param limit        查詢的限制數量。
     * @return 符合條件的行政區列表。
     */
    List<DistrictVo> listByNameKeyword(String districtName, Integer offset, Integer limit);

    /**
     * 根據行政區名稱關鍵字計算符合條件的行政區數量。
     *
     * @param districtName 行政區名稱的關鍵字。
     * @return 符合條件的行政區數量。
     */
    Integer countByNameKeyword(String districtName);
}
