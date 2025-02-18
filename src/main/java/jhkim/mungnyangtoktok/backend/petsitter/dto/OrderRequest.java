package jhkim.mungnyangtoktok.backend.petsitter.dto;

import jhkim.mungnyangtoktok.backend.petsitter.entity.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderRequest {
    private Long senderId;
    private Long receiverId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int cost;
    private OrderStatus status;
    private String impUid;
}