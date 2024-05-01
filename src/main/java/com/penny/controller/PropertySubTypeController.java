package com.penny.controller;

import com.penny.service.PropertySubTypeService;
import com.penny.vo.PropertySubTypeVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/property-sub-types")
@RequiredArgsConstructor
public class PropertySubTypeController {
    private final PropertySubTypeService propertyTypeService;

    /**
     * 根據房源主類型 ID 取得房源子類型清單。
     *
     * @param propertyMainTypeId 房源主類型 ID
     * @return 返回包含房源子類型清單的 ResponseEntity
     */
    @GetMapping
    public ResponseEntity<List<PropertySubTypeVo>> getPropertyTypesByPropertyMainTypeId(
            @RequestParam  Long propertyMainTypeId
    ){
        return ResponseEntity.ok(propertyTypeService.getByPropertyMainTypId(propertyMainTypeId));
    }
}
