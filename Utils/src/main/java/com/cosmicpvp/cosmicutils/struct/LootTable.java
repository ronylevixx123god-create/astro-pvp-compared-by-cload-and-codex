/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.struct;

import java.util.*;

public class LootTable<T> {
    private final NavigableMap<Double, T> table = new TreeMap<Double, T>();
    private final Map<T, Double> lootRates = new HashMap<T, Double>();
    Random random = new Random();
    double total = 0.0;
    private List<T> loot = new ArrayList<T>();

    public void addItem(T loot, double chance) {
        this.loot.add(loot);
        if (chance <= 0.0) {
            return;
        }
        this.total += chance;
        this.table.put(this.total, loot);
        this.lootRates.put(loot, chance);
    }

    public double getTotalWeight() {
        return this.total;
    }

    public T getRandomLoot() {
        double value = this.random.nextDouble() * this.total;
        return this.table.ceilingEntry(value).getValue();
    }

    public Map<T, Double> getLootRates() {
        return this.lootRates;
    }

    public List<T> getLoot() {
        return this.loot;
    }
}

