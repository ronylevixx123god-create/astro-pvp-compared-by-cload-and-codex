/*
 * Decompiled with CFR 0.150.
 */
package net.astropvp.core.cosmiccoinflip.utils;

public class SlotUtils {
    public static int fitSlots(int size) {
        return size <= 9 ? 9 : (size <= 18 ? 18 : (size <= 27 ? 27 : (size <= 36 ? 36 : (size <= 45 ? 45 : (size <= 54 ? 54 : 54)))));
    }
}

