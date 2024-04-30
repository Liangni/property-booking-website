package com.penny.controller;

import com.penny.service.AmenityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/properties")
@RequiredArgsConstructor
public class PropertyController {
    private final AmenityService amenityService;

    /**
     * 創建房源設施。
     *
     * @param propertyId 房源 ID
     * @param amenityId 設施 ID
     * @return ResponseEntity 包含 "ok" 字串的 ResponseEntity。
     */
    @PostMapping("{propertyId}/amenities/{amenityId}")
    public ResponseEntity<String> createPropertyAmenities(
            @PathVariable Long propertyId,
            @PathVariable Long amenityId
    ) {
        amenityService.createPropertyAmenity(propertyId, amenityId);
        return ResponseEntity.ok("ok");
    }

    /**
     * 獲取房源的設施列表。
     *
     * @param propertyId 房源的 ID
     * @return 包含設施分類詳細資訊的 map 列表的 ResponseEntity
     */
    @GetMapping("{propertyId}/amenities")
    public ResponseEntity<List<Map<String, Object>>> getPropertyAmenities(
            @PathVariable Long propertyId
    ) {
        return ResponseEntity.ok(amenityService.listPropertyAmenityMap(propertyId));
    }

    /**
     * 刪除房源設施。
     *
     * @param propertyId 房源 ID
     * @param amenityId 設施 ID
     * @return 包含 "ok" 字串的 ResponseEntity
     */
    @DeleteMapping("{propertyId}/amenities/{amenityId}")
    public ResponseEntity<String> deletePropertyAmenities(
            @PathVariable Long propertyId,
            @PathVariable Long amenityId
    ) {
        amenityService.deletePropertyAmenity(propertyId, amenityId);
        return ResponseEntity.ok("ok");
    }
}
