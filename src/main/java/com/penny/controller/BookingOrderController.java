package com.penny.controller;

import com.penny.request.CreateBookingOrderRequest;
import com.penny.service.BookingOrderService;
import com.penny.vo.BookingOrderVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/booking-orders")
@RequiredArgsConstructor
public class BookingOrderController {

    private final BookingOrderService bookingOrderService;

    /**
     * 創建預訂訂單。
     *
     * @param request 預訂訂單創建請求
     * @return ResponseEntity 創建預訂訂單的回應
     */
    @PostMapping()
    public ResponseEntity<String> createBookingOrders(
            @RequestBody @Valid CreateBookingOrderRequest request
    ) {
        bookingOrderService.createBookingOrder(request);
        return ResponseEntity.ok("ok");
    }

    /**
     * 根據指定的 ecUserId 和 isHost 參數，獲取相應的預訂訂單列表。
     *
     * @param ecUserId 使用者 ID，必須提供以查詢相關的預訂訂單。
     * @param isHost 是否為出租人（可選）。默認值為 false，表示查詢租客的訂單；如果設置為 true，則查詢出租人的訂單。
     *
     * @return ResponseEntity 包含預訂訂單列表的 ResponseEntity。
     */
    @GetMapping
    public ResponseEntity<List<BookingOrderVo>> getBookingOrders(
            @RequestParam Long ecUserId,
            @RequestParam(defaultValue = "false") Boolean isHost
    ) {
        return ResponseEntity.ok(bookingOrderService.getBookingOrders(ecUserId, isHost));
    }
}
