package com.example.demo.kafka;

import com.example.demo.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    private final KafkaTopicsConfig kafkaTopicsConfig;

    public void publish(OrderEvent event) {
        String topic = kafkaTopicsConfig.getOrderEventsTopic();
        String key = event.parcelNumber();

        kafkaTemplate.send(topic, key, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish order event for parcelNumber={}", key, ex);
                    } else {
                        log.info("Published order event for parcelNumber={} to partition={}",
                                key, result.getRecordMetadata().partition());
                    }
                });
    }

}
