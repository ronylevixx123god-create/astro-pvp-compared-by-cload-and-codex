/*
 * Decompiled with CFR 0.150.
 */
package com.compilex.CosmicDungeons.items;

import com.compilex.CosmicDungeons.dungeons.libs.DungeonType;

public class KeyPair {
    DungeonType type;
    long num;

    public KeyPair(DungeonType type, long num) {
        this.type = type;
        this.num = num;
    }

    public DungeonType getKey() {
        return this.type;
    }

    public long getValue() {
        return this.num;
    }
}

