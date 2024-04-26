package com.penny.controller;

import com.penny.service.AmenityTypeService;
import com.penny.vo.AmenityTypeVo;
import com.penny.vo.AmenityVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/amenity-types")
@RequiredArgsConstructor
public class AmenityTypeController {
    private final AmenityTypeService amenityTypeService;

    /**
     * 取得設施類型列表。
     *
     * @return ResponseEntity 包含設施類型列表的 ResponseEntity
     */
    @GetMapping
    public ResponseEntity<List<AmenityTypeVo>> getAmenityType(
    ) {
        return ResponseEntity.ok(amenityTypeService.getAmenityTypes());
    }

}
