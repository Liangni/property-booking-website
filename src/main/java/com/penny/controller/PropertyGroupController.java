package com.penny.controller;

import com.penny.service.PropertyGroupService;
import com.penny.vo.PropertyGroupVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/property-groups")
@RequiredArgsConstructor
public class PropertyGroupController {
    private final PropertyGroupService propertyGroupService;

    @GetMapping
    public ResponseEntity<List<PropertyGroupVo>> getPropertyGroups(
    ) {
        return ResponseEntity.ok(propertyGroupService.getPropertyGroups());
    }
}
