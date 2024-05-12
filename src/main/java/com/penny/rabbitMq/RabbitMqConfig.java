package com.penny.rabbitMq;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMqConfig {
    private final AmqpAdmin amqpAdmin;

    /**
     * 在初始化時設定 RabbitMQ 的交換器、列隊及綁定關係。
     */
    @PostConstruct
    void init() {
        // 創建名稱為 "paymentQueue" 的列隊
        Queue paymentQueue = new Queue("paymentQueue");
        amqpAdmin.declareQueue(paymentQueue);

        // 創建名稱為 "paymentExchange" 的 Fanout 交換器
        FanoutExchange paymentFanoutPayment = new FanoutExchange("paymentExchange");
        amqpAdmin.declareExchange(paymentFanoutPayment);

        // 將 "paymentQueue" 列隊與 "paymentExchange" 交換器進行綁定
        Binding binding = BindingBuilder.bind(paymentQueue).to(paymentFanoutPayment);
        amqpAdmin.declareBinding(binding);
    }
}
