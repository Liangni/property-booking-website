package com.penny.service;

import com.penny.dao.AmenityTypeVoMapper;
import com.penny.dao.base.AmenityTypeBaseVoMapper;
import com.penny.vo.AmenityTypeVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AmenityTypeService {
    private final AmenityTypeVoMapper amenityTypeVoMapper;

    /**
     * 取得所有設施類型。
     *
     * @return 設施類型列表
     */
    public List<AmenityTypeVo> getAmenityTypes() {
        return amenityTypeVoMapper.listAll();
    }
}
