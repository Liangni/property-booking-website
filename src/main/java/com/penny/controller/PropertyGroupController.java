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

    /**
     * 獲取所有房屋分組。
     *
     * @return 房屋分組列表
     */
    @GetMapping
    public ResponseEntity<List<PropertyGroupVo>> getPropertyGroups(
    ) {
        return ResponseEntity.ok(propertyGroupService.getPropertyGroups());
    }
}
