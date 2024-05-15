package com.penny.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.penny.request.ConfirmPaymentRequest;
import com.penny.util.JsonConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final RabbitTemplate rabbitTemplate;

    private final JsonConverter jsonConverter;
    /**
     * 發送確認付款請求消息至 RabbitMQ。
     *
     * @param confirmRequest 確認付款請求
     */
    public void produceMessage(ConfirmPaymentRequest confirmRequest) {
        String json = jsonConverter.convertToJson(confirmRequest);

        rabbitTemplate.convertAndSend("paymentExchange", "zxc",json);
    }
}
