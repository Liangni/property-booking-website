package com.penny.controller;

import com.penny.request.ConfirmPaymentRequest;
import com.penny.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/payment-callbacks")
@RequiredArgsConstructor
public class PaymentCallbackController {
    private final PaymentService paymentService;

    /**
     * POST 請求，確認付款並發送相應的消息至 RabbitMQ。
     *
     * @param confirmRequest 確認付款請求的 JSON 對象
     * @return ResponseEntity 若操作成功，返回 "ok"
     */
    @PostMapping()
    public ResponseEntity<String> confirmPayment(@RequestBody @Valid ConfirmPaymentRequest confirmRequest) {

        paymentService.produceMessage(confirmRequest);

        return ResponseEntity.ok("ok");
    }
}
