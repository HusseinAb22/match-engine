package dev.husseinabbas.matchengine.engine;

import java.util.ArrayDeque;
import java.util.Deque;

public class PriceLevel {
    private long price;
    private final Deque<RestingOrder> orders =new ArrayDeque<>();
    private long totalVolume = 0;

    public PriceLevel(long price) {
        this.price = price;
    }

    public long getPrice() {
        return price;
    }

    public long getTotalVolume() {
        return totalVolume;
    }

    public void add(RestingOrder o){
        orders.add(o);
        this.totalVolume += o.getRemainingQty();
    }
    public RestingOrder peekFirst(){
        return orders.peek();
    }
    private RestingOrder pollFirst(){
        return orders.poll();
    }
    public boolean isEmpty(){
        return orders.isEmpty();
    }
    public void fill(long qty){
        RestingOrder first = this.peekFirst();
        if (first == null) {
            throw new IllegalStateException("cannot fill an empty price level");
        }
        first.reduce(qty);
        this.totalVolume -= qty;

        if (first.isFilled()){
            this.pollFirst();
        }
    }
    public boolean remove(RestingOrder o ) {
        boolean flag = false;
        if (orders.remove(o)) {
            totalVolume -= o.getRemainingQty();
            flag = true;
        }
        return flag;
    }
}
