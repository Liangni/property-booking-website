package com.penny.controller;

import com.penny.service.DiscountService;
import com.penny.vo.DiscountVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/discounts")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;
    @GetMapping
    List<DiscountVo> getPropertyDiscount(
            @RequestParam Long propertyId
    ){
        return discountService.getPropertyDiscount(propertyId);
    }
}
