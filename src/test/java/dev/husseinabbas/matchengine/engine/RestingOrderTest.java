package dev.husseinabbas.matchengine.engine;

import org.assertj.core.api.ThrowableAssertAlternative;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import java.time.Instant;

class RestingOrderTest {

    private final long orderId = 12345L;
    private final Side side = Side.BUY; // Assuming Side is an enum with BUY/SELL
    private final long initialPrice = 15000L;
    private final long initialQty = 100L;
    private final Instant timestamp = Instant.now();

    private RestingOrder order;

    @BeforeEach
    void setUp() {
        // Initializes a fresh order before each test to ensure isolation
        order = new RestingOrder(orderId, side, initialPrice, initialQty, timestamp);
    }

    @Test
    void reduce_decreasesRemainingQuantity() {
        order.reduce(20L);
        assertThat(order.getRemainingQty()).isEqualTo(80L);
    }

    @Test
    void reduce_ExactRemainingQuantity_FillsTheOrder(){
        order.reduce(100L);
        assertThat(order.isFilled()).isTrue();
    }

    @Test
    void reduce_ZeroQuantity_ThrowsIllegalArgumentException(){
        assertThatThrownBy(() -> order.reduce(0L))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(order.getRemainingQty()).isEqualTo(100L);
    }

    @Test
    void reduce_ExceedingQuantity_ThrowsIllegalArgumentException(){
        assertThatThrownBy(() -> order.reduce(120L))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(order.getRemainingQty()).isEqualTo(100L);
    }

    @Test
    void isFilled_QuantityRemains(){
        assertThat(order.isFilled()).isFalse();

    }
}