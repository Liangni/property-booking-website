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
}
