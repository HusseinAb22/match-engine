package dev.husseinabbas.matchengine.engine;

import java.util.Comparator;
import java.util.HashMap;
import java.util.NavigableMap;
import java.util.TreeMap;

public class OrderBook {
    private String symbol;
    private final NavigableMap<Long, PriceLevel> bids = new TreeMap<>(Comparator.reverseOrder());
    private final NavigableMap<Long, PriceLevel> asks = new TreeMap<>();
    private final HashMap<Long,RestingOrder > index = new HashMap<>();

    public OrderBook(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public PriceLevel bestBid(){
        return bids.firstEntry().getValue();
    }

    public PriceLevel bestAsk(){
        return asks.firstEntry().getValue();
    }

    public void addResting(RestingOrder o ){
        NavigableMap<Long, PriceLevel> book = (o.getSide() == Side.BUY) ? bids : asks;
        PriceLevel level = book.computeIfAbsent(o.getPrice(), PriceLevel::new);
        level.add(o);

        index.put(o.getId(),o);
    }

    public boolean cancel(long orderId) {
        RestingOrder order = index.get(orderId);
        if (order == null) return false;
        NavigableMap<Long, PriceLevel> book = (order.getSide() == Side.BUY) ? bids : asks;
        PriceLevel level = book.get(order.getPrice());
        boolean removed = level.remove(order);
        if (removed) {
            index.remove(orderId);
            if (level.isEmpty()) book.remove(order.getPrice());
        }
        return removed;
    }
}
