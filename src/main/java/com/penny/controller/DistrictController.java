package com.penny.controller;

import com.amazonaws.Response;
import com.penny.request.district.DistrictSearchRequest;
import com.penny.service.DistrictService;
import com.penny.vo.DistrictVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
     * 通過關鍵字搜尋行政區的端點。
     *
     * @param districtSearchRequest 行政區搜尋請求物件。
     * @return 包含符合條件的行政區資訊的 Map 的 ResponseEntity。
     */
    @PostMapping("search")
    public ResponseEntity<Map<String, Object>> getDistrictsByKeyword(
            @RequestBody DistrictSearchRequest districtSearchRequest
    ) {
        return ResponseEntity.ok(districtService.getDistrictsByKeyword(districtSearchRequest));
    }
}
