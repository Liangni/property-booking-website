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

    private final BookingOrderService bookingOrderService;

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

    /**
     * 根據指定的 ecUserId 和 isHost 參數，獲取相應的預訂訂單列表。
     *
     * @param ecUserId 使用者 ID，必須提供以查詢相關的預訂訂單。
     * @param isHost 是否為出租人（可選）。默認值為 false，表示查詢租客的訂單；如果設置為 true，則查詢出租人的訂單。
     *
     * @return ResponseEntity 包含預訂訂單列表的 ResponseEntity。
     */
    @GetMapping("{ecUserId}/booking-orders")
    public ResponseEntity<List<BookingOrderVo>> listEcUserBookingOrders(
            @PathVariable Long ecUserId,
            @RequestParam(defaultValue = "false") Boolean isHost
    ) {
        return ResponseEntity.ok(bookingOrderService.getBookingOrders(ecUserId, isHost));
    }
}
