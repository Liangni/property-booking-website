package com.penny.controller;

import com.penny.service.DistrictService;
import com.penny.vo.DistrictVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/districts")
@RequiredArgsConstructor
public class DistrictController {
    private final DistrictService districtService;

    /**
     * 取得地區資訊，包括父地區及其子地區列表。
     *
     * @return 地區資訊，表示為包含父地區及其子地區列表的列表的 ResponseEntity。
     */
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getDistricts() {
        return ResponseEntity.ok(districtService.getNestedDistricts());
    }

    /**
     * 根據關鍵字獲取行政區列表。
     *
     * @param keyword 關鍵字
     * @return ResponseEntity<List<DistrictVo>> 包含符合關鍵字的行政區列表的 ResponseEntity
     */
    @GetMapping("search")
    public ResponseEntity<List<DistrictVo>> getDistrictsByKeyword(
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(districtService.getDistrictsByKeyword(keyword));
    }
}
