package jhkim.mungnyangtoktok.backend.petsitter.repository;

import jhkim.mungnyangtoktok.backend.petsitter.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
