package com.penny.service;



import com.penny.dao.DistrictVoMapper;
import com.penny.exception.ResourceNotFoundException;
import com.penny.vo.DistrictVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DistrictService {

    private final DistrictVoMapper districtVoMapper;

    /**
     * 取得嵌套的地區資訊，包括父地區及其子地區列表。
     *
     * @return 嵌套的地區資訊，表示為包含父地區及其子地區列表的列表。
     */
    public  List<Map<String, Object>> getNestedDistricts() {
        // 取得父地區列表
        List<DistrictVo> parentDistrictList = districtVoMapper.listParentDistricts();

        // 遍歷父地區列表，取得每個父地區及其子地區列表的 map，並將映射結果收集為列表
        return parentDistrictList
                .stream()
                .map((parentDistrict) -> {
                    // 取得當前父地區的子地區列表
                    List<DistrictVo> childDistrictList = districtVoMapper.listByParentDistrictId(parentDistrict.getDistrictId());
                    // 準備當前父地區及其子地區列表的 map
                    return prepareDistrictMap(parentDistrict, childDistrictList);
                }).toList();
    }

    /**
     * 根據關鍵字獲取行政區列表。
     *
     * @param keyword 關鍵字
     * @return List<DistrictVo> 符合關鍵字的行政區列表
     * @throws ResourceNotFoundException 若未找到符合關鍵字的行政區，拋出此異常
     */
    public List<DistrictVo> getDistrictsByKeyword(String keyword){
        String replaced = keyword.replace("台", "臺");

        return Optional.ofNullable(districtVoMapper.listByNameKeyword(replaced))
                .orElseThrow(() -> new ResourceNotFoundException(String.format("district with name [%s] not found", keyword)));
    }

    private Map<String, Object> prepareDistrictMap(DistrictVo districtVo, List<DistrictVo> childDistrictVoList) {
        return Map.ofEntries(
                Map.entry("districtId", districtVo.getDistrictId()),
                Map.entry("districtName", districtVo.getDistrictName()),
                Map.entry("administrativeAreaId", districtVo.getAdministrativeAreaId()),
                Map.entry("childDistricts", childDistrictVoList)
        );
    }

}
