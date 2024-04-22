package com.penny.dao;

import com.penny.vo.DistrictVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DistrictVoMapper {

    /**
     * 返回所有上級行政區的列表。
     *
     * @return List<DistrictVo> 上級行政區列表
     */
    List<DistrictVo> listParentDistricts();

    /**
     * 根據指定的上級行政區ID返回行政區列表。
     *
     * @param parentDistrictId 上級行政區ID
     * @return List<DistrictVo> 符合指定上級行政區ID的行政區列表
     */
    List<DistrictVo> listByParentDistrictId(Long parentDistrictId);

    /**
     * 根據行政區名稱關鍵字列出行政區。
     *
     * @param districtName 行政區名稱的關鍵字。
     * @return 符合條件的行政區列表。
     */
    List<DistrictVo> listByNameKeyword(String districtName);

    /**
     * 根據行政區 ID 查詢行政劃分層級。
     *
     * @param districtId 行政區 ID
     * @return 行政劃分層級
     */
    Long selectAdminAreaLevelByPrimaryKey(Long districtId);
}
