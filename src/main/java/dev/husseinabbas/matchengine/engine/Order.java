package dev.husseinabbas.matchengine.engine;

import java.time.Instant;

public record Order(long id,
                    String symbol,
                    Side side,
                    OrderType type,
                    long price,
                    long quantity,
                    Instant timestamp ) {
}
