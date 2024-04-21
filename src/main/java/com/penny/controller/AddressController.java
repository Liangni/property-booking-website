package com.penny.controller;

import com.penny.request.address.AddressCreateRequest;
import com.penny.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    /**
     * 創建新地址。
     *
     * @param request 地址創建請求
     * @return ResponseEntity<String> 如果成功創建，返回 "ok"
     */
    @PostMapping
    public ResponseEntity<String> createAddresses(
            @RequestBody AddressCreateRequest request
            ) {
        addressService.createAddress(request);
        return ResponseEntity.ok("ok");
    }
}
