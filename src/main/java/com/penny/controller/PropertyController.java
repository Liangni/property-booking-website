package com.penny.controller;

import com.penny.request.CreatePropertyDiscountRequest;
import com.penny.service.AmenityService;
import com.penny.service.DiscountService;
import com.penny.vo.DiscountVo;
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

    private final DiscountService discountService;

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
    public ResponseEntity<String> deletePropertyAmenity(
            @PathVariable Long propertyId,
            @PathVariable Long amenityId
    ) {
        amenityService.deletePropertyAmenity(propertyId, amenityId);
        return ResponseEntity.ok("ok");
    }

    /**
     * 根據房源 ID 取得房源折扣列表的方法。
     *
     * @param propertyId 房源 ID，用於指定要查詢折扣的房源
     * @return 返回包含房源折扣列表的 ResponseEntity
     */
    @GetMapping("{propertyId}/discounts")
    ResponseEntity<List<DiscountVo>> getPropertyDiscount(
            @PathVariable Long propertyId
    ){
        return ResponseEntity.ok(discountService.getPropertyDiscount(propertyId));
    }

    /**
     * 新增房源折扣的方法。
     *
     * @param propertyId 房源 ID
     * @param createRequest 房源折扣創建請求
     * @return 返回表示成功的 ResponseEntity
     */
    @PostMapping("{propertyId}/discounts")
    ResponseEntity<String> getPropertyDiscount(
            @PathVariable Long propertyId,
            @RequestBody CreatePropertyDiscountRequest createRequest
            ){
        discountService.createPropertyDiscount(propertyId, createRequest);
        return ResponseEntity.ok("ok");
    }

    /**
     * 刪除房源折扣。
     *
     * @param propertyId 房源 ID
     * @param discountId 折扣 ID
     * @return 包含 "ok" 字串的 ResponseEntity
     */
    @DeleteMapping("{propertyId}/discounts/{discountId}")
    public ResponseEntity<String> deletePropertyDiscount(
            @PathVariable Long propertyId,
            @PathVariable Long discountId
    ) {
        discountService.deletePropertyDiscount(propertyId, discountId);
        return ResponseEntity.ok("ok");
    }
}
