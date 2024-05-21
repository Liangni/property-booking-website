package com.penny.response;

import com.penny.vo.PropertyVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

@Service
public class ReadInitialPagePropertyResponseMapper implements BiFunction<PropertyVo, List<Map<String, Object>>, ReadInitialPagePropertyResponse> {
    @Override
    public ReadInitialPagePropertyResponse apply(PropertyVo propertyVo, List<Map<String, Object>> propertyImageUrlList) {

        return ReadInitialPagePropertyResponse
                .builder()
                .propertyId(propertyVo.getPropertyId())
                .propertyTitle(propertyVo.getPropertyTitle())
                .priceOnWeekends(propertyVo.getPriceOnWeekends())
                .priceOnWeekdays(propertyVo.getPriceOnWeekdays())
                .averageRating(propertyVo.getAverageRating())
                .reviewCount(propertyVo.getReviewCount())
                .districtId(propertyVo.getDistrictId())
                .districtName(propertyVo.getDistrictName())
                .parentDistrictId(propertyVo.getParentDistrictId())
                .parentDistrictName(propertyVo.getParentDistrictName())
                .propertyImageList(propertyImageUrlList)
                .startAvailableDate(propertyVo.getStartAvailableDate())
                .endAvailableDate(propertyVo.getEndAvailableDate())
                .build();
    }

}
