package jhkim.mungnyangtoktok.backend.petsitter.entity;

import jakarta.persistence.*;
import jhkim.mungnyangtoktok.backend.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders") // 테이블명 설정
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 자동 생성되는 기본 키

    private String impUid; // 아임포트 결제 고유번호

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender; // 요청을 보낸 유저

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver; // 요청을 받는 유저

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime; // 시작 시간

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime; // 종료 시간

    @Column(nullable = false)
    private int cost; // 비용

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status; // 주문 상태 (REJECT, ACCEPT, 대기중)
}