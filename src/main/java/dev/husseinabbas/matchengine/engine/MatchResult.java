package dev.husseinabbas.matchengine.engine;

import java.util.List;

public record MatchResult(List<Trade> trades,
                          OrderStatus status,
                          long remainingQuantity) {
}
