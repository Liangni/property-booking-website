package com.penny.controller;

import com.penny.dao.PropertyTypeVoMapper;
import com.penny.service.PropertyTypeService;
import com.penny.vo.PropertyTypeVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/property-types")
@RequiredArgsConstructor
public class PropertyTypeController {
    private final PropertyTypeService propertyTypeService;

    @GetMapping
    public ResponseEntity<List<PropertyTypeVo>> getPropertyTypesByGroupId(
            @RequestParam  Long propertyGroupId
    ){
        return ResponseEntity.ok(propertyTypeService.getByPropertyGroupId(propertyGroupId));
    }
}
