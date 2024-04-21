package com.penny.service;



import com.penny.dao.DistrictVoMapper;
import com.penny.exception.ResourceNotFoundException;
import com.penny.vo.DistrictVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DistrictService {

    private final DistrictVoMapper districtVoMapper;

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

}
