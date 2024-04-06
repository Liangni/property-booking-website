package com.penny.controller;

import com.amazonaws.Response;
import com.penny.request.property.PropertySearchRequest;
import com.penny.request.property.PropertyUploadImageRequest;
import com.penny.service.AmenityService;
import com.penny.service.BedroomService;
import com.penny.service.PropertyService;
import com.penny.vo.AmenityVo;
import com.penny.vo.BedroomVo;
import com.penny.vo.base.AmenityBaseVo;
import com.penny.vo.base.BedroomBaseVo;
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

    @PostMapping("search")
    public ResponseEntity<Map<String, Object>> getPropertiesByFilter(
            @RequestBody PropertySearchRequest propertySearchRequest
    ) {
        return ResponseEntity.ok(propertyService.getPropertiesByFilter(propertySearchRequest));
    }

    @PostMapping("upload-image-url")
    public ResponseEntity<Map<String, String>> getPropertyImageUploadUrl(
            @RequestBody PropertyUploadImageRequest uploadImageRequest
            ) {
        return ResponseEntity.ok(propertyService.getImageUploadUrl(uploadImageRequest));
    }

    @GetMapping("{propertyId}/download-image-url")
    public ResponseEntity<List<String>> getPropertyImageDownloadUrl(
            @PathVariable Long propertyId,
            @RequestParam Integer sizeNum
    ) {
        return ResponseEntity.ok(propertyService.getImageDownloadUrl(propertyId, sizeNum));
    }

    @GetMapping("{propertyId}")
    public ResponseEntity<PropertyBaseVo> getProperty(
            @PathVariable("propertyId") Long propertyId
    ) {
        return ResponseEntity.ok(propertyService.getProperty(propertyId));
    }

    @GetMapping("{propertyId}/rooms")
    public  ResponseEntity<List<BedroomVo>> getPropertyRoom(
            @PathVariable("propertyId") Long propertyId
    ) {
        return ResponseEntity.ok(bedroomService.getPropertyBedroomList(propertyId));
    }

    @GetMapping("{propertyId}/amenity")
    public ResponseEntity<Map<String, List<AmenityVo>>> getPropertyAmenity(
            @PathVariable("propertyId") Long propertyId
    ) {
        return ResponseEntity.ok(amenityService.getPropertyAmenity(propertyId));
    }
}
