package dev.husseinabbas.matchengine.engine;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.Clock;
public class MatchingEngine {
    private final Map<String, OrderBook> books = new HashMap<>();
    private long nextTradeId = 1;
    private final Clock clock = Clock.systemUTC();

    private MatchResult match(OrderBook book, Order incomingOrder){
        List<Trade> trades = new ArrayList<>();
        boolean stopFlag = true;
        long remaining = incomingOrder.quantity();
        while(stopFlag){
            if (remaining >0){
                switch (incomingOrder.side()){
                    case Side.BUY:
                        if (book.bestAsk().isEmpty()) {
                            stopFlag = false;
                            break;
                        }
                        var ask_side = book.bestAsk().get();
                        if(ask_side.getPrice()<= incomingOrder.price()){
                            var resting = ask_side.peekFirst();
                            var fillQty = Math.min(remaining,resting.getRemainingQty());
                            ask_side.fill(fillQty);
                            if (ask_side.isEmpty()) {
                                book.removeEmptyLevel(resting.getSide(), ask_side.getPrice());
                            }
                            remaining -= fillQty;
                            var trade = new Trade(
                                    nextTradeId++,
                                    book.getSymbol(),
                                    resting.getPrice(),
                                    fillQty,
                                    resting.getId(),
                                    incomingOrder.id(),
                                    incomingOrder.side(),
                                    clock.instant()
                                    );
                            trades.add(trade);
                            break;
                        }
                        stopFlag = false;
                        break;
                    case Side.SELL:
                        if (book.bestBid().isEmpty()) {
                            stopFlag = false;
                            break;
                        }
                        var bid_side = book.bestBid().get();
                        if(bid_side.getPrice()>= incomingOrder.price()) {
                            var resting = bid_side.peekFirst();
                            var fillQty = Math.min(remaining,resting.getRemainingQty());
                            bid_side.fill(fillQty);
                            if (bid_side.isEmpty()) {
                                book.removeEmptyLevel(resting.getSide(), bid_side.getPrice());
                            }
                            remaining -= fillQty;
                            var trade = new Trade(
                                    nextTradeId++,
                                    book.getSymbol(),
                                    resting.getPrice(),
                                    fillQty,
                                    resting.getId(),
                                    incomingOrder.id(),
                                    incomingOrder.side(),
                                    clock.instant()
                            );
                            trades.add(trade);
                            break;
                        }
                        stopFlag = false;
                        break;


                }
            }
            else stopFlag = false;

        }
        OrderStatus status;
        if (remaining == 0) {
            status = OrderStatus.FILLED;
        } else {
            book.addResting(new RestingOrder(
                    incomingOrder.id(), incomingOrder.side(),
                    incomingOrder.price(), remaining, incomingOrder.timestamp()));
            status = trades.isEmpty() ? OrderStatus.NEW : OrderStatus.PARTIALLY_FILLED;
        }

        return new MatchResult(trades,status,remaining);


    }
    public MatchResult submit(Order o){
        OrderBook book = books.computeIfAbsent(o.symbol(), OrderBook::new);
        return this.match(book,o);
    }

    public boolean cancel(String symbol, long orderId) {
        OrderBook book = books.get(symbol);
        if (book == null) return false;
        return book.cancel(orderId);
    }
}
