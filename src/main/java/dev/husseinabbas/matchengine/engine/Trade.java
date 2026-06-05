package dev.husseinabbas.matchengine.engine;

import java.time.Instant;

public record Trade(long id,
                    String symbol,
                    long price,
                    long quantity,
                    long restingOrderId,
                    long aggressingOrderId,
                    Side takerSide,
                    Instant timeStamp) {
}
