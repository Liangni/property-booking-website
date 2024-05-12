package com.penny.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.penny.request.ConfirmPaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final RabbitTemplate rabbitTemplate;

    /**
     * 發送確認付款請求消息至 RabbitMQ。
     *
     * @param confirmRequest 確認付款請求
     */
    public void produceMessage(ConfirmPaymentRequest confirmRequest) {
        ObjectMapper mapper = new ObjectMapper();
        String json;

        try {
           json = mapper.writeValueAsString(confirmRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        rabbitTemplate.convertAndSend("paymentExchange", "zxc",json);
    }
}
