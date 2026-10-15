/*
 * Decompiled with CFR 0.150.
 */
package me.ifamasssxd.cosmicduel.struct.enums;

public enum DuelState {
    IN_QUEUE,
    WAITING,
    STARTING,
    PLAYING,
    ENDING,
    ENDED;


    public boolean isValid() {
        return this == PLAYING || this == STARTING;
    }
}

