package pizzeria.order.domain.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OrderTests {
    @Test
    public void orderEquals_worksCorrectly() {
        Order order = new Order(2L, List.of(), 3L, "Mocked Id", LocalDateTime.now(), 100.0, List.of());
        Order secondOrder = new Order(2L, List.of(), 6L, "Mocked Id", LocalDateTime.now(), 100.0, List.of());

        assertThat(order.equals(secondOrder)).isTrue();
    }
    @Test
    public void orderEquals_isNotEqual() {
        Order order = new Order(2L, List.of(), 3L, "Mocked Id", LocalDateTime.now(), 100.0, List.of());
        Order secondOrder = new Order(5L, List.of(), 3L, "Mocked Id", LocalDateTime.now(), 100.0, List.of());

        assertThat(order.equals(secondOrder)).isFalse();
    }

    @Test
    public void orderEquals_isEqualObject() {
        Order order = new Order(2L, List.of(), 3L, "Mocked Id", LocalDateTime.now(), 100.0, List.of());
        Object secondOrder = new Order(2L, List.of(), 3L, "Mocked Id", LocalDateTime.now(), 100.0, List.of());

        assertThat(order.equals(secondOrder)).isTrue();
    }

    @Test
    public void orderEquals_isEqualToItself() {
        Order order = new Order(2L, List.of(), 3L, "Mocked Id", LocalDateTime.now(), 100.0, List.of());

        assertThat(order.equals(order)).isTrue();
    }

    @Test
    public void orderEquals_otherObject() {
        Order order = new Order(2L, List.of(), 3L, "Mocked Id", LocalDateTime.now(), 100.0, List.of());
        Object otherObject = "Random String";

        assertThat(order.equals(otherObject)).isFalse();
    }

    @Test
    public void orderEquals_hashCode() {
        Order order = new Order(2L, List.of(), 3L, "Mocked Id", LocalDateTime.now(), 100.0, List.of());
        Order order2 = new Order(2L, List.of(), 3L, "Mocked Id", LocalDateTime.now(), 100.0, List.of());

        assertThat(order.hashCode()).isEqualTo(order2.hashCode());
    }

    @Test
    public void orderEquals_hashCodeNotEqual() {
        Order order = new Order(2L, List.of(), 3L, "Mocked Id", LocalDateTime.now(), 100.0, List.of());
        Order order2 = new Order(4L, List.of(), 3L, "Mocked Id", LocalDateTime.now(), 100.0, List.of());

        assertThat(order.hashCode()).isNotEqualTo(order2.hashCode());
    }
}
