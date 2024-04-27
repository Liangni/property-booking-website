package com.penny.controller;

import com.penny.service.AmenityService;
import com.penny.vo.AmenityVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/amenities")
@RequiredArgsConstructor
public class AmenityController {
    private final AmenityService amenityService;

    /**
     * 取得設施列表。
     *
     * @param amenityTypeId 設施類型 ID
     * @return ResponseEntity 包含設施列表的 ResponseEntity
     */
    @GetMapping
    public ResponseEntity<List<AmenityVo>> getAmenities(
            @RequestParam Long amenityTypeId
    ) {
        return ResponseEntity.ok(amenityService.getAmenities(amenityTypeId));
    }

    /**
     * 獲取房源的設施列表。
     *
     * @param propertyId 房源的 ID
     * @return 包含設施分類詳細資訊的 map 列表的 ResponseEntity
     */
    @GetMapping("property-amenities")
    public ResponseEntity<List<Map<String, Object>>> getPropertyAmenities(
            @RequestParam Long propertyId
    ) {
        return ResponseEntity.ok(amenityService.listPropertyAmenityMap(propertyId));
    }



}
