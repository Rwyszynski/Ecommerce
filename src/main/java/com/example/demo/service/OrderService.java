package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderRequestDto;
import com.example.demo.event.OrderEvent;
import com.example.demo.kafka.OrderEventProducer;
import com.example.demo.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventProducer orderEventProducer;

    public Order registerOrder(OrderRequestDto dto) {
        Order order = new Order();
        order.setParcelNumber(dto.parcelNumber());
        order.setEmailReceiver(dto.emailReceiver());
        order.setReceiverCountryCode(dto.receiverCountryCode());
        order.setSenderCountryCode(dto.senderCountryCode());
        order.setStatusCode(dto.statusCode());

        Order saved = orderRepository.save(order);

        OrderEvent event = new OrderEvent(
                saved.getParcelNumber(),
                saved.getEmailReceiver(),
                saved.getReceiverCountryCode(),
                saved.getSenderCountryCode(),
                saved.getStatusCode()
        );
        orderEventProducer.publish(event);

        return saved;
    }

    public Optional<Order> getLatestByParcelNumber(String parcelNumber) {
        return orderRepository.findTopByParcelNumberOrderByIdDesc(parcelNumber);
    }

}
