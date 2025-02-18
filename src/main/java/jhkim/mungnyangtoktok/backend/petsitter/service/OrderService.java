package jhkim.mungnyangtoktok.backend.petsitter.service;

import jakarta.transaction.Transactional;
import jhkim.mungnyangtoktok.backend.petsitter.dto.OrderRequest;
import jhkim.mungnyangtoktok.backend.petsitter.entity.Order;
import jhkim.mungnyangtoktok.backend.petsitter.repository.OrderRepository;
import jhkim.mungnyangtoktok.backend.user.entity.User;
import jhkim.mungnyangtoktok.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    // 모든 주문 조회
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // 새로운 주문 생성 (DTO 사용)
    @Transactional
    public Order createOrder(OrderRequest orderRequest) {
        User sender = userRepository.findById(orderRequest.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("Sender ID가 존재하지 않습니다."));
        User receiver = userRepository.findById(orderRequest.getReceiverId())
                .orElseThrow(() -> new IllegalArgumentException("Receiver ID가 존재하지 않습니다."));

        Order order = Order.builder()
                .sender(sender)
                .receiver(receiver)
                .startTime(orderRequest.getStartTime())
                .endTime(orderRequest.getEndTime())
                .cost(orderRequest.getCost())
                .impUid(orderRequest.getImpUid())
                .status(orderRequest.getStatus())
                .build();

        return orderRepository.save(order);
    }
}
