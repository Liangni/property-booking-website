package com.penny.controller;

import com.penny.service.DistrictService;
import com.penny.vo.DistrictVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/districts")
@RequiredArgsConstructor
public class DistrictController {
    private final DistrictService districtService;

    /**
     * 根據關鍵字獲取行政區列表。
     *
     * @param keyword 關鍵字
     * @return ResponseEntity<List<DistrictVo>> 符合關鍵字的行政區列表
     */
    @GetMapping("search")
    public ResponseEntity<List<DistrictVo>> getDistrictsByKeyword(
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(districtService.getDistrictsByKeyword(keyword));
    }
}
