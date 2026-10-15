package me.venir.astrocollectors.collector;

import lombok.Getter;

@Getter
public class CollectorSellLog {

    private final long sellDate;
    private final String sellerName;
    private final SellLogType sellLogType;
    private final double worth;

    public CollectorSellLog(String sellerName, SellLogType sellLogType) {
        this.sellerName = sellerName;
        this.sellLogType = sellLogType;
        this.sellDate = System.currentTimeMillis();
        this.worth = 0;
    }

    public CollectorSellLog(String sellerName, SellLogType sellLogType, double worth) {
        this.sellerName = sellerName;
        this.sellLogType = sellLogType;
        this.sellDate = System.currentTimeMillis();
        this.worth = worth;
    }

    public enum SellLogType {
        TNT,
        MOB_DROPS
    }
}