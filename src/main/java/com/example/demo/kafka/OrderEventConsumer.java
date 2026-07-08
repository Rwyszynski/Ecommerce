package com.example.demo.kafka;

import com.example.demo.email.MockEmailSender;
import com.example.demo.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {

    private final MockEmailSender mockEmailSender;

    @KafkaListener(
            topics = "${app.kafka.topic.order-events}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(OrderEvent event) {
        log.info("Consumed order event for parcelNumber={}", event.parcelNumber());
        mockEmailSender.send(event);
    }
}
