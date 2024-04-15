package com.penny.controller;

import com.penny.request.property.PropertySearchParam;
import com.penny.request.property.PropertyUploadImageRequest;
import com.penny.service.*;
import com.penny.vo.*;
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

        private final BookingAvailabilityService bookingAvailabilityService;

        private final PropertyReviewService propertyReviewService;

        private final PictureService pictureService;

        private final DiscountService discountService;

    /**
     * 獲取所有房源。
     *
     * @param request 房源搜索參數
     * @return 包含房源列表的 ResponseEntity
     */
    @GetMapping
    public ResponseEntity<List<PropertyVo>> getProperties(
            @ModelAttribute PropertySearchParam request
    ) {
        return ResponseEntity.ok(propertyService.getPublishedProperties(request));
    }

    /**
     * 獲取房源圖片的上傳URL。
     *
     * @param request 上傳圖片請求
     * @return 包含圖片上傳 URL 的 Map 的 ResponseEntity
     */
    @PostMapping("upload-image-url")
    public ResponseEntity<Map<String, String>> getPropertyImageUploadUrlMap(
            @RequestBody PropertyUploadImageRequest request
            ) {
        return ResponseEntity.ok(pictureService.getPropertyImageUploadUrlMap(request));
    }

    /**
     * 獲取房源圖片的下載URL。
     *
     * @param propertyId 房源ID
     * @param sizeNum 圖片大小編號
     * @return 包含圖片下載 URL 列表的 ResponseEntity
     */
    @GetMapping("{propertyId}/download-image-url")
    public ResponseEntity<List<String>> getPropertyImageDownloadUrls(
            @PathVariable Long propertyId,
            @RequestParam Integer sizeNum
    ) {
        return ResponseEntity.ok(pictureService.listPublishedPropertyImageDownloadUrls(propertyId, sizeNum));
    }

    /**
     * 獲取單個房源的資訊。
     *
     * @param propertyId 房源ID
     * @return 包含房源資訊的 ResponseEntity
     */
    @GetMapping("{propertyId}")
    public ResponseEntity<PropertyBaseVo> getProperty(
            @PathVariable("propertyId") Long propertyId
    ) {
        return ResponseEntity.ok(propertyService.getPublishedProperty(propertyId));
    }

    /**
     * 獲取房源的房間列表。
     *
     * @param propertyId 房源ID
     * @return 包含房間列表的 ResponseEntity
     */
    @GetMapping("{propertyId}/rooms")
    public  ResponseEntity<List<BedroomVo>> getPropertyRooms(
            @PathVariable("propertyId") Long propertyId
    ) {
        return ResponseEntity.ok(bedroomService.listPublishedPropertyBedrooms(propertyId));
    }

    /**
     * 獲取房源的設施列表。
     *
     * @param propertyId 房源ID
     * @return 包含設施列表的 Map 的 ResponseEntity
     */
    @GetMapping("{propertyId}/amenity")
    public ResponseEntity<Map<String, List<AmenityVo>>> getPropertyAmenityMap(
            @PathVariable("propertyId") Long propertyId
    ) {
        return ResponseEntity.ok(amenityService.getPublishedPropertyAmenityMap(propertyId));
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
        return ResponseEntity.ok(bookingAvailabilityService.listPublishedPropertyBookingAvailability(propertyId));
    }

    /**
     * 根據房源ID獲取房源評論。
     *
     * @param propertyId 房源ID
     * @return 包含房源評論的 ResponseEntity
     */
    @GetMapping("{propertyId}/property-reviews")
    public ResponseEntity<List<PropertyReviewVo>> getPropertyReviews(
            @PathVariable("propertyId") Long propertyId
    ) {
        return ResponseEntity.ok(propertyReviewService.listPublishedPropertyReview(propertyId));
    }

    /**
     * 獲取特定房源的折扣列表。
     *
     * @param propertyId 房源ID
     * @return 包含房源折扣列表的 ResponseEntity
     */
    @GetMapping("{propertyId}/discounts")
    public ResponseEntity<List<DiscountVo>> getPropertyDiscounts(
            @PathVariable("propertyId") Long propertyId
    ) {
        return ResponseEntity.ok(discountService.getPublishedPropertyDiscount(propertyId));
    }
}
