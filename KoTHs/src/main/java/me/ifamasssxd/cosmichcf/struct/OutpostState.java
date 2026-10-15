/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 */
package me.ifamasssxd.cosmichcf.struct;

import com.cosmicpvp.cosmicutils.utils.CC;

public enum OutpostState {
    OPEN(CC.GrayB + "IDLE"),
    CAPTURING(CC.GreenB + "BEING CAPTURED"),
    CONTESTED(CC.YellowB + "CONTESTED"),
    BLOCKED(CC.RedB + "BLOCKED"),
    CONTROLLED(CC.GreenB + "CONTROLLED"),
    UNDER_ATTACK(CC.RedB + "UNDER ATTACK");

    private String name;

    private OutpostState(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}

