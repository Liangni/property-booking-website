package com.penny.rabbitMq;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

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
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "deadLetterExchange"); // 死信交換器的名稱
        Queue paymentQueue = new Queue("paymentQueue", true, false, false, arguments);
        amqpAdmin.declareQueue(paymentQueue);

        // 創建名稱為 "paymentExchange" 的 Fanout 交換器
        FanoutExchange paymentFanoutExchange = new FanoutExchange("paymentExchange");
        amqpAdmin.declareExchange(paymentFanoutExchange);

        // 將 "paymentQueue" 列隊與 "paymentExchange" 交換器進行綁定
        Binding binding = BindingBuilder.bind(paymentQueue).to(paymentFanoutExchange);
        amqpAdmin.declareBinding(binding);

        // 創建死信列隊
        Queue deadLetterQueue = new Queue("deadLetterQueue");
        amqpAdmin.declareQueue(deadLetterQueue);

        // 創建死信的 Fanout 交換器
        FanoutExchange deadLetterFanoutExchange = new FanoutExchange("deadLetterExchange");
        amqpAdmin.declareExchange(deadLetterFanoutExchange);

        // 將死信列隊與交換器進行綁定
        Binding deadLetterBinding = BindingBuilder.bind(deadLetterQueue).to(deadLetterFanoutExchange);
        amqpAdmin.declareBinding(deadLetterBinding);
    }
}
