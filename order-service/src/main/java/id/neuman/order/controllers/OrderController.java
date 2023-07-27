package id.neuman.order.controllers;

import id.neuman.order.dto.request.CreateOrderRequest;
import id.neuman.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    Object createOrder(@RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request);
    }
}