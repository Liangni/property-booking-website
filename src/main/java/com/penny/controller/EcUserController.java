package com.penny.controller;

import com.penny.service.BookingOrderService;
import com.penny.service.EcUserService;
import com.penny.vo.BookingOrderVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/ec-users")
@RequiredArgsConstructor
public class EcUserController {
    private final BookingOrderService bookingOrderService;

    /**
     * 根據是否為房東取得使用者的預訂訂單列表。
     *
     * @param ecUserId 使用者ID
     * @param isHost 是否為房東
     * @return 包含符合條件的預訂訂單列表的 ResponseEntity
     */
    @GetMapping("{ecUserId}/booking-orders")
    public ResponseEntity<List<BookingOrderVo>> getEcUserBookingOrders(
            @PathVariable Long ecUserId,
            @RequestParam Boolean isHost
    ) {
        return ResponseEntity.ok(bookingOrderService.getUserBookingOrders(ecUserId, isHost));
    }
}
