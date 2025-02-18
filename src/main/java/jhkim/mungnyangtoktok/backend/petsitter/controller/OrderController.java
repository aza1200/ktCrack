package jhkim.mungnyangtoktok.backend.petsitter.controller;

import jhkim.mungnyangtoktok.backend.petsitter.dto.OrderRequest;
import jhkim.mungnyangtoktok.backend.petsitter.entity.Order;
import jhkim.mungnyangtoktok.backend.petsitter.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 모든 주문 조회 (GET)
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // 새로운 주문 생성 (POST)
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest orderRequest) {
        Order createdOrder = orderService.createOrder(orderRequest);
        return ResponseEntity.ok(createdOrder);
    }
}
