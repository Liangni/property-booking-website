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

    /**
     * 通過關鍵字搜尋行政區的端點。
     *
     * @param districtSearchRequest 行政區搜尋請求物件，包含搜尋關鍵字、分頁等資訊。
     * @return 包含符合條件的行政區資訊和分頁資訊的 map。
     */
    @PostMapping("search")
    public Map<String, Object> getDistrictsByKeyword(
            @RequestBody DistrictSearchRequest districtSearchRequest
    ) {
        return districtService.getDistrictsByKeyword(districtSearchRequest);
    }
}
