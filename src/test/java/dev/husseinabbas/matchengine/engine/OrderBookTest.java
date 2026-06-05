package dev.husseinabbas.matchengine.engine;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import java.time.Instant;

public class OrderBookTest {
    private final RestingOrder buyOrder = new RestingOrder(
            1001L,Side.BUY,50L,100L, Instant.now());
    private final RestingOrder sellOrder = new RestingOrder(
            1002L,Side.SELL,50L,100L, Instant.now());
    private OrderBook book = new OrderBook("AMZN");

    @Test
    void bestBid_successfullyReturnsBid(){
        book.addResting(buyOrder);
        var o = book.bestBid().orElseThrow();
        assertThat(o.peekFirst()).isEqualTo(buyOrder);
        assertThat(o.getTotalVolume()).isEqualTo(100L);
    }

    @Test
    void bestBid_returnsBestBid(){
        book.addResting(
                new RestingOrder(1L, Side.BUY, 50L, 10L, Instant.now()));
        book.addResting(
                new RestingOrder(2L, Side.BUY, 55L, 10L, Instant.now()));
        assertThat(book.bestBid().orElseThrow().getPrice()).isEqualTo(55L);
    }

    @Test
    void bestAsk_successfullyReturnsBestAsk(){
        book.addResting(sellOrder);
        var o = book.bestAsk().orElseThrow();
        assertThat(o.peekFirst()).isEqualTo(sellOrder);
        assertThat(o.getTotalVolume()).isEqualTo(100L);
    }

    @Test
    void cancel_success(){
        book.addResting(sellOrder);
        book.addResting(new RestingOrder(
                1003L,Side.SELL,50L,100L, Instant.now()));
        assertThat(book.cancel(1002)).isTrue();
        assertThat(book.bestAsk().orElseThrow().getTotalVolume()).isEqualTo(100L);
    }

    @Test
    void cancel_unknownIdFalse(){
        book.addResting(sellOrder);
        assertThat(book.cancel(100)).isFalse();


    }
}
