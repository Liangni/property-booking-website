package com.penny.controller;

import com.penny.request.CreatePropertyDiscountRequest;
import com.penny.request.UpdatePropertyPictureRequest;
import com.penny.service.AmenityService;
import com.penny.service.DiscountService;
import com.penny.service.PictureService;
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

    private final PictureService pictureService;

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

    /**
     * 取得物業圖片下載 URL 地圖列表。
     *
     * @param propertyId 房源 ID
     * @param sizeNum 圖片尺寸數量
     * @return ResponseEntity 包含房源圖片下載 URL map 列表 的 ResponseEntity
     */
    @GetMapping("{propertyId}/image-download-urls")
    public ResponseEntity<List<Map<String, Object>>> getPropertyImageDownloadUrlMap(
            @PathVariable Long propertyId,
            @RequestParam Integer sizeNum
    ) {
        return ResponseEntity.ok(pictureService.listPropertyImageDownloadUrl(propertyId, sizeNum));
    }

    /**
     * 獲取房源圖片的上傳URL。
     *
     * @param propertyId 房源 ID
     * @param fileExtension 檔案副檔名
     * @return 包含圖片上傳 URL 的 Map 的 ResponseEntity
     */
    @GetMapping("{propertyId}/image-upload-urls")
    public ResponseEntity<Map<String, Object>> getPropertyImageUploadUrlMap(
            @PathVariable Long propertyId,
            @RequestParam String fileExtension
    ) {
        return ResponseEntity.ok(pictureService.getPropertyImageUploadUrlMap(propertyId, fileExtension));
    }

    /**
     * 更新房源圖片信息。
     *
     * @param propertyId 房源 ID
     * @param updateRequest 更新圖片請求。
     * @return ResponseEntity，表示請求已成功處理。
     */
    @PostMapping("{propertyId}/pictures")
    public ResponseEntity<String> updatePropertyPicture(
            @PathVariable Long propertyId,
            @RequestBody UpdatePropertyPictureRequest updateRequest
    ) {
        pictureService.updatePropertyPicture(propertyId, updateRequest);
        return ResponseEntity.ok("ok");
    }
}
