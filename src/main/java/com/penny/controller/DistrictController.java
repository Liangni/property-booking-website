package com.penny.controller;

import com.penny.request.district.DistrictSearchRequest;
import com.penny.service.DistrictService;
import com.penny.vo.DistrictVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/districts")
public class DistrictController {
    private final DistrictService districtService;

    @Autowired
    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }

    @PostMapping("search")
    public List<DistrictVo> getDistrictsByKeyword(
            @RequestBody DistrictSearchRequest districtSearchRequest,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit
    ) {
        return districtService.getDistrictsByKeyword(districtSearchRequest.getKeyword(), page, limit);
    }
}
