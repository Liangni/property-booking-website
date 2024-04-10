package com.penny.service;

import com.penny.dao.DiscountVoMapper;
import com.penny.vo.DiscountVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountVoMapper discountVoMapper;

    /**
     * 獲取特定房源的折扣列表。
     *
     * @param propertyId 房源ID
     * @return 房源折扣列表
     */
    public List<DiscountVo> getPropertyDiscount(Long propertyId) {
        return discountVoMapper
                .listByPropertyId(propertyId)
                .stream()
                .filter(DiscountVo::getDiscountIsActive)
                .collect(Collectors.toList());
    }
}
