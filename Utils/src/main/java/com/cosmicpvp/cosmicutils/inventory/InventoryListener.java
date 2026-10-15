/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.inventory;

import java.util.function.Consumer;

public class InventoryListener<T> {
    private final Class<T> type;
    private final Consumer<T> consumer;

    public InventoryListener(Class<T> type, Consumer<T> consumer) {
        this.type = type;
        this.consumer = consumer;
    }

    public void accept(T t) {
        this.consumer.accept(t);
    }

    public Class<T> getType() {
        return this.type;
    }
}

