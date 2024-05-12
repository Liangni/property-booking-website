package com.penny.rabbitMq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.penny.request.ConfirmPaymentRequest;
import com.penny.service.BookingOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;


@Component
@RequiredArgsConstructor
public class PaymentRabbitListener {
    private final BookingOrderService bookingOrderService;

    /**
     * RabbitMQ 監聽器，用於處理支付列隊中的訊息。
     *
     * @param message RabbitMQ 的消息
     */
    @RabbitListener(queues = "paymentQueue")
    void completeBookingOrder(Message message) {

        try {
            // 將 Message 的 body 轉換為 ConfirmPaymentRequest 物件
            ConfirmPaymentRequest confirmPaymentRequest = parseMessageContent(message);

            // 更新 booking order 狀態
            bookingOrderService.updateBookingOrderStatus(confirmPaymentRequest);

        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }

    /**
     * 解析 RabbitMQ 消息的內容，將其轉換為 ConfirmPaymentRequest 物件。
     *
     * @param message RabbitMQ 的消息
     * @return 解析後的 ConfirmPaymentRequest 物件
     */
    private ConfirmPaymentRequest parseMessageContent(Message message) {

        try {
            // 獲取 Message 的內容，並轉換為字串
            String jsonContent = new String(message.getBody());

            // 使用 Jackson 將 JSON 轉換為 ConfirmPaymentRequest 物件
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonContent, ConfirmPaymentRequest.class);

        } catch (Exception e) {
            throw new RuntimeException("Error parsing message content: " + e.getMessage());
        }

    }

}
