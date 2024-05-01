package com.penny.controller;

import com.penny.service.PropertyMainTypeService;
import com.penny.vo.PropertyMainTypeVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/property-main-types")
@RequiredArgsConstructor
public class PropertyMainTypeController {
    private final PropertyMainTypeService propertyMainTypeService;

    /**
     * 獲取所有房屋主類型。
     *
     * @return 包含房屋主類型列表的 ResponseEntity
     */
    @GetMapping
    public ResponseEntity<List<PropertyMainTypeVo>> getPropertyMainTypes(
    ) {
        return ResponseEntity.ok(propertyMainTypeService.listPropertyMainType());
    }
}
