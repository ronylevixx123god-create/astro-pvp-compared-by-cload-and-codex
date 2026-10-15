/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.math;

import org.bukkit.Chunk;

public class LongHash {
    public static long toLong(int msw, int lsw) {
        return ((long) msw << 32) + (long) lsw - Integer.MIN_VALUE;
    }

    public static int msw(long l) {
        return (int) (l >> 32);
    }

    public static int lsw(long l) {
        return (int) l + Integer.MIN_VALUE;
    }

    public static Long createLongHash(int x, int z) {
        return ((long) x << 32) + (long) z - Integer.MIN_VALUE;
    }

    public static int getXFromLongHash(long val) {
        return LongHash.msw(val);
    }

    public static int getZFromLongHash(long val) {
        return LongHash.lsw(val);
    }

    public static boolean isEqualToChunk(long val, Chunk chunk) {
        return val == LongHash.toLong(chunk.getX(), chunk.getZ());
    }
}

