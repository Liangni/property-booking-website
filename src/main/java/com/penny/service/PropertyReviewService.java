package com.penny.service;

import com.penny.dao.PropertyReviewVoMapper;
import com.penny.vo.PropertyReviewVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyReviewService {
    private final PropertyReviewVoMapper propertyReviewVoMapper;

    /**
     * 獲取特定房源的評論列表。
     *
     * @param propertyId 房源ID
     * @return 房源評論列表
     */
    public List<PropertyReviewVo> listPropertyReview(Long propertyId) {
        return propertyReviewVoMapper.listByPropertyId(propertyId);
    }
}
