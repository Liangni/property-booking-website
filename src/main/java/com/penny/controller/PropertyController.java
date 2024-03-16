package com.penny.controller;

import com.penny.request.property.PropertySearchRequest;
import com.penny.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
