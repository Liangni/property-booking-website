package com.penny.rabbitMq;

import com.fasterxml.jackson.core.type.TypeReference;
import com.penny.request.ConfirmPaymentRequest;
import com.penny.service.BookingOrderService;
import com.penny.util.JsonConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class PaymentRabbitListener {
    private final BookingOrderService bookingOrderService;

    private final JsonConverter jsonConverter;

    /**
     * RabbitMQ 監聽器，用於處理支付列隊中的訊息。
     *
     * @param message RabbitMQ 的消息
     */
    @RabbitListener(queues = "paymentQueue")
    void completeBookingOrder(Message message) {
        // 將 Message 的 body 轉換為 ConfirmPaymentRequest 物件
        ConfirmPaymentRequest confirmPaymentRequest = parseMessageContent(message, ConfirmPaymentRequest.class);

        // 更新 booking order 狀態
        bookingOrderService.updateBookingOrderStatus(confirmPaymentRequest);
    }

    /**
     * 解析消息的內容並將其轉換為指定類型的物件。
     *
     * @param message    要解析的消息
     * @param valueType 目標類型的 Type
     * @param <T>         目標類型
     * @return 解析後的目標物件
     */
    private <T> T parseMessageContent(Message message, Class<T> valueType) {
        // 獲取 Message 的內容，並轉換為字串
        String jsonContent = new String(message.getBody());

        return jsonConverter.convertJsonToObject(jsonContent, valueType);
    }

}
