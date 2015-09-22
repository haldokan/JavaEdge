package org.haldokan.edge.lunchbox.misc;

public class Quote {
    private final String symbol;
    private final long time;

    public Quote(String symbol, long time) {
        this.symbol = symbol;
        this.time = time;
    }

    public String getSymbol() {
        return symbol;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Quote [symbol=" + symbol + ", time=" + time + "]";
    }
}
