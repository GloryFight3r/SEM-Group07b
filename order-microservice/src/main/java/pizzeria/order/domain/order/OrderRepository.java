package pizzeria.order.domain.order;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findById(Long orderId);

    List<Order> findByUserId(Long userId);

    Order update(Order order);
}
