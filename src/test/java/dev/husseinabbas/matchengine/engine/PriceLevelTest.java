package dev.husseinabbas.matchengine.engine;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import java.time.Instant;
public class PriceLevelTest {
    private final RestingOrder order = new RestingOrder(
            1001L,Side.BUY,50L,100L,Instant.now());
    private PriceLevel level = new PriceLevel(50);

    @Test
    void add_raisesTotalVolumeByQuantity(){
        level.add(order);
        assertThat(level.getTotalVolume()).isEqualTo(100L);
    }

    @Test
    void fill_lowersTotalVolumeByAmount(){
        level.add(order);
        level.fill(50);
        assertThat(level.getTotalVolume()).isEqualTo(50);
    }

    @Test
    void fill_completeOrderRemovesIt(){
        level.add(order);
        level.fill(100L);
        assertThat(level.isEmpty()).isTrue();
    }

    @Test
    void fill_partiallyFillsTopOrderKeepsIt(){
        level.add(order);
        level.add(new RestingOrder(
                1002L,Side.BUY,50L,110L,Instant.now()));
        level.fill(40);
        assertThat(level.peekFirst().getId()).isEqualTo(order.getId());
        assertThat(order.getRemainingQty()).isEqualTo(60L);
    }

    @Test
    void fill_emptyLevel_ThrowsIllegalStateException(){
        assertThatThrownBy(() -> level.fill(50))
                .isInstanceOf(IllegalStateException.class);

    }
}
