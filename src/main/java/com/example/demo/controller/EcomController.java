package com.example.demo.controller;

import com.example.demo.entity.OrderRequestDto;
import com.example.demo.entity.OrderRequestSuccessDto;
import com.example.demo.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class EcomController {

    private final OrderService orderService;

    @PostMapping("/")
    ResponseEntity<OrderRequestSuccessDto> sendOrder(@Valid @RequestBody OrderRequestDto orderRequestDto) {
        orderService.registerOrder(orderRequestDto);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(new OrderRequestSuccessDto("Order accepted"));
    }

    @GetMapping("/{parcelNumber}")
    ResponseEntity<String> getOrder(@PathVariable String parcelNumber) {
        return orderService.getLatestByParcelNumber(parcelNumber)
                .map(order -> ResponseEntity.ok(order.toString()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
