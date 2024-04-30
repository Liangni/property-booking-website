package com.penny.controller;

import com.penny.request.CreatePropertyDiscountRequest;
import com.penny.service.DiscountService;
import com.penny.vo.DiscountVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/discounts")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;

    /**
     * 根據房源 ID 取得物業折扣列表的方法。
     *
     * @param propertyId 房源 ID，用於指定要查詢折扣的房源
     * @return 返回包含房源折扣列表的 ResponseEntity
     */
    @GetMapping("property-discounts")
    ResponseEntity<List<DiscountVo>> getPropertyDiscount(
            @RequestParam Long propertyId
    ){
        return ResponseEntity.ok(discountService.getPropertyDiscount(propertyId));
    }

    /**
     * 新增房源折扣的方法。
     *
     * @param createRequest 建立房源折扣的請求物件
     * @return 返回表示成功的 ResponseEntity
     */
    @PostMapping("property-discounts")
    ResponseEntity<String> getPropertyDiscount(
            @RequestBody CreatePropertyDiscountRequest createRequest
    ){
        discountService.createPropertyDiscount(createRequest);
        return ResponseEntity.ok("ok");
    }
}
