package com.penny.controller;

import com.amazonaws.Response;
import com.penny.request.property.PropertySearchRequest;
import com.penny.request.property.PropertyUploadImageRequest;
import com.penny.service.PropertyService;
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

    @PostMapping("search")
    public Map<String, Object> getPropertiesByFilter(
            @RequestBody PropertySearchRequest propertySearchRequest
    ) {
        return propertyService.getPropertiesByFilter(propertySearchRequest);
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
}
