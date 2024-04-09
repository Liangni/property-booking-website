package com.penny.controller;

import com.penny.request.property.PropertySearchParam;
import com.penny.request.property.PropertyUploadImageRequest;
import com.penny.service.AmenityService;
import com.penny.service.BedroomService;
import com.penny.service.PropertyService;
import com.penny.vo.AmenityVo;
import com.penny.vo.BedroomVo;
import com.penny.vo.BookingAvailabilityVo;
import com.penny.vo.PropertyVo;
import com.penny.vo.base.PropertyBaseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/properties")
@RequiredArgsConstructor
public class PropertyController {
        private final PropertyService propertyService;

        private final BedroomService bedroomService;

        private final AmenityService amenityService;

    /**
     * 獲取所有房源。
     *
     * @param request 房源搜索參數
     * @return 房源列表
     */
    @GetMapping
    public ResponseEntity<List<PropertyVo>> getProperties(
            @ModelAttribute PropertySearchParam request
    ) {
        return ResponseEntity.ok(propertyService.getProperties(request));
    }

    /**
     * 上傳圖片URL以設置房源的圖片。
     *
     * @param uploadImageRequest 上傳圖片請求
     * @return 圖片上傳URL的映射
     */
    @PostMapping("upload-image-url")
    public ResponseEntity<Map<String, String>> getPropertyImageUploadUrl(
            @RequestBody PropertyUploadImageRequest uploadImageRequest
            ) {
        return ResponseEntity.ok(propertyService.getImageUploadUrl(uploadImageRequest));
    }

    /**
     * 獲取房源的圖片下載URL。
     *
     * @param propertyId 房源ID
     * @param sizeNum 圖片大小編號
     * @return 圖片下載URL列表
     */
    @GetMapping("{propertyId}/download-image-url")
    public ResponseEntity<List<String>> getPropertyImageDownloadUrl(
            @PathVariable Long propertyId,
            @RequestParam Integer sizeNum
    ) {
        return ResponseEntity.ok(propertyService.getImageDownloadUrl(propertyId, sizeNum));
    }

    /**
     * 獲取單個房源的資訊。
     *
     * @param propertyId 房源ID
     * @return 房源資訊
     */
    @GetMapping("{propertyId}")
    public ResponseEntity<PropertyBaseVo> getProperty(
            @PathVariable("propertyId") Long propertyId
    ) {
        return ResponseEntity.ok(propertyService.getProperty(propertyId));
    }

    /**
     * 獲取房源的房間列表。
     *
     * @param propertyId 房源ID
     * @return 房間列表
     */
    @GetMapping("{propertyId}/rooms")
    public  ResponseEntity<List<BedroomVo>> getPropertyRoom(
            @PathVariable("propertyId") Long propertyId
    ) {
        return ResponseEntity.ok(bedroomService.getPropertyBedroomList(propertyId));
    }

    /**
     * 獲取房源的設施列表。
     *
     * @param propertyId 房源ID
     * @return 設施列表的 map
     */
    @GetMapping("{propertyId}/amenity")
    public ResponseEntity<Map<String, List<AmenityVo>>> getPropertyAmenity(
            @PathVariable("propertyId") Long propertyId
    ) {
        return ResponseEntity.ok(amenityService.getPropertyAmenity(propertyId));
    }

    /**
     * 根據房源ID獲取預定日期。
     *
     * @param propertyId 房源ID
     * @return ResponseEntity<List<BookingAvailabilityVo>> 包含房源預定日期的 ResponseEntity
     */
    @GetMapping("{propertyId}/booking-availability")
    public ResponseEntity<List<BookingAvailabilityVo>> getPropertyBookingAvailability(
            @PathVariable("propertyId") Long propertyId
    ) {
        return ResponseEntity.ok(propertyService.getPropertyBookingAvailability(propertyId));
    }
}
