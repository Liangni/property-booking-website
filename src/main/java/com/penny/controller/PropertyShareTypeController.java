package com.penny.controller;

import com.penny.service.PropertyShareTypeService;
import com.penny.vo.PropertyShareTypeVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/property-share-types")
@RequiredArgsConstructor
public class PropertyShareTypeController {
    private final PropertyShareTypeService propertyShareTypeService;

    /**
     * 獲取所有房屋共享類型。
     *
     * @return 包含所有房屋共享類型的 ResponseEntity
     */
    @GetMapping
    public ResponseEntity<List<PropertyShareTypeVo>> getPropertyShareTypes(
    ) {
        return ResponseEntity.ok(propertyShareTypeService.getPropertyShareTypes());
    }
}
