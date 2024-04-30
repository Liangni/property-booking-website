package com.penny.controller;

import com.penny.service.AmenityService;
import com.penny.vo.AmenityVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}
