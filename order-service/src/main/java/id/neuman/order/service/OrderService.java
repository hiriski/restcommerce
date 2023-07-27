package id.neuman.order.service;

import id.neuman.common.dto.response.PaymentResponse;
import id.neuman.order.dto.request.CreateOrderRequest;
import id.neuman.order.entities.Order;
import id.neuman.order.repositories.OrderRepository;
import id.neuman.order.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final PaymentRepository paymentRepository;

    public Order createOrder(CreateOrderRequest request) {

        PaymentResponse payment = paymentRepository.findById(request.getPaymentId());

        Order order = new Order();
        BeanUtils.copyProperties(request, order);
        order.setPaymentName(payment.getName());

        return orderRepository.save(order);
    }
}