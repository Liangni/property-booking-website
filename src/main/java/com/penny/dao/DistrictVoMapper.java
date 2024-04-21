package com.penny.dao;

import com.penny.vo.DistrictVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

    /**
     * 根據行政區 ID 查詢行政劃分層級。
     *
     * @param districtId 行政區 ID
     * @return 行政劃分層級
     */
    Long selectAdminAreaLevelByPrimaryKey(Long districtId);
}
