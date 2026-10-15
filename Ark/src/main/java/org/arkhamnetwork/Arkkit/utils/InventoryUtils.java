/*
 * Decompiled with CFR 0.150.
 */
package org.arkhamnetwork.Arkkit.utils;

public class InventoryUtils {
    public static int fitInventorySize(int size) {
        for (int i = 9; i < 54; i += 9) {
            if (size > i) continue;
            return i;
        }
        return 54;
    }
}

