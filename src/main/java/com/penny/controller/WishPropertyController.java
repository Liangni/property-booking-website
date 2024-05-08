package com.penny.controller;

import com.penny.request.CreateWishPropertyRequest;
import com.penny.service.WishPropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/wish-properties")
@RequiredArgsConstructor
public class WishPropertyController {
    private final WishPropertyService wishPropertyService;

    /**
     * 創建願望房源。
     *
     * @param createRequest 創建願望房源的請求
     * @return ResponseEntity 包含 "ok" 字串的 ResponseEntity
     */
    @PostMapping
    public ResponseEntity<String> createWishProperty(
            @RequestBody @Valid CreateWishPropertyRequest createRequest
    ) {
        wishPropertyService.createWishProperty(createRequest);
        return ResponseEntity.ok("ok");
    }

    /**
     * 根據願望房源 ID 刪除願望房源。
     *
     * @param wishPropertyId 要刪除的願望房源 ID
     * @return ResponseEntity 包含 "ok" 字串的 ResponseEntity
     */
    @DeleteMapping("{wishPropertyId}")
    public ResponseEntity<String> deleteWishProperty(
            @PathVariable Long wishPropertyId
    ) {
        wishPropertyService.deleteWishPropertyById(wishPropertyId);
        return ResponseEntity.ok("ok");
    }
}
