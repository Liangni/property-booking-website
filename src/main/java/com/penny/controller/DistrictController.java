package com.penny.controller;

import com.penny.request.district.DistrictSearchRequest;
import com.penny.service.DistrictService;
import com.penny.vo.DistrictVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/districts")
@RequiredArgsConstructor
public class DistrictController {
    private final DistrictService districtService;

    @PostMapping("search")
    public Map<String, Object> getDistrictsByKeyword(
            @RequestBody DistrictSearchRequest districtSearchRequest
    ) {
        return districtService.getDistrictsByKeyword(districtSearchRequest);
    }
}
