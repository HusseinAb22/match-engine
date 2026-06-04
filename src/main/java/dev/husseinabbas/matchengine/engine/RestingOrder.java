package dev.husseinabbas.matchengine.engine;

import java.time.Instant;
import java.util.Objects;

public class RestingOrder {
    private long id;
    private Side side;
    private long price;
    private long remainingQty;
    private Instant timeStamp;

    public RestingOrder(long id, Side side, long price, long remainingQty, Instant timeStamp) {
        this.id = id;
        this.side = side;
        this.price = price;
        this.remainingQty = remainingQty;
        this.timeStamp = timeStamp;
    }

    public long getId() {
        return id;
    }

    public Side getSide() {
        return side;
    }

    public long getPrice() {
        return price;
    }

    public long getRemainingQty() {
        return remainingQty;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }


    @Override
    public String toString() {
        return "RestingOrder{" +
                "id=" + id +
                ", side=" + side +
                ", price=" + price +
                ", remainingQty=" + remainingQty +
                ", timeStamp=" + timeStamp +
                '}';
    }

    public void reduce(long qty){
        if (qty <=0 || qty > remainingQty){
            throw new IllegalArgumentException("RestingOrder: the quantity you want to reduce is either below 0 or bigger than the remaining quantity!");

        }
        this.remainingQty -= qty;
    }

    public boolean isFilled(){
        return remainingQty==0;
    }
}
