package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderRequestDto;
import com.example.demo.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order registerOrder(OrderRequestDto dto) {
        Order order = new Order();
        order.setParcelNumber(dto.parcelNumber());
        order.setEmailReceiver(dto.emailReceiver());
        order.setReceiverCountryCode(dto.receiverCountryCode());
        order.setSenderCountryCode(dto.senderCountryCode());
        order.setStatusCode(dto.statusCode());

        Order saved = orderRepository.save(order);

        return saved;
    }

    public Optional<Order> getLatestByParcelNumber(String parcelNumber) {
        return orderRepository.findTopByParcelNumberOrderByCreatedAtDesc(parcelNumber);
    }

}
