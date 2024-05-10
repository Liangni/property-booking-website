package com.penny.controller;

import com.penny.request.CreateAddressRequest;
import com.penny.service.AddressService;
import com.penny.vo.base.AddressBaseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    /**
     * 創建新地址。
     *
     * @param request 地址創建請求
     * @return ResponseEntity 如果成功創建，返回 "ok"
     */
    @PostMapping
    public ResponseEntity<String> createAddresses(
            @RequestBody CreateAddressRequest request
            ) {
        addressService.createAddress(request);
        return ResponseEntity.ok("ok");
    }

    /**
     * 根據地址 ID 獲取地址基本資訊。
     *
     * @param addressId 地址的 ID
     * @return ResponseEntity 包含地址基本資訊的 ResponseEntity
     */
    @GetMapping("{addressId}")
    public ResponseEntity<AddressBaseVo> getAddress(
            @PathVariable Long addressId
    ) {
        return ResponseEntity.ok(addressService.getAddress(addressId));
    }
}
