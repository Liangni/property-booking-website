package com.penny.controller;

import com.penny.exception.AuthorizationException;
import com.penny.service.BookingOrderService;
import com.penny.service.EcUserService;
import com.penny.service.WishPropertyService;
import com.penny.vo.BookingOrderVo;
import com.penny.vo.WishPropertyVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/ec-users")
@RequiredArgsConstructor
public class EcUserController {

    private final WishPropertyService wishPropertyService;

    /**
     * 根據使用者 ID 列出願望房源列表。
     *
     * @param ecUserId 使用者 ID
     * @return ResponseEntity 包含願望房源列表的 ResponseEntity
     */
    @GetMapping("{ecUserId}/wish-properties")
    public ResponseEntity<List<WishPropertyVo>> listEcUserWishProperty(
            @PathVariable Long ecUserId
    ) {
        return ResponseEntity.ok(wishPropertyService.listWishPropertyByEcUserId(ecUserId));
    }
}
