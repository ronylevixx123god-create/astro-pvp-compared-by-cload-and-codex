/*
 * Decompiled with CFR 0.150.
 */
package com.compilex.CosmicDungeons.struct;

public enum DungeonChallenge {
    FLAWLESS("Flawless"),
    NIMBLE_FEET("Nimble Feet"),
    SPEED_RACER("Speed Racer"),
    GUARDIAN("Guardian"),
    OVERLORD("Overlord"),
    TAG_TEAM("Tag Team"),
    WELL_OILED_MACHINE("Well Oiled Machine"),
    ON_A_ROLL("On a Roll"),
    UP_CLOSE_AND_PERSONAL("Up Close and Personal"),
    PLATINUM_PACKAGE("Platinum Package"),
    PARKOUR_EXPERT("Parkour Expert"),
    LUCKY("Lucky"),
    PETLESS("Petless"),
    TWINKE_TOES("Twinkle Toes"),
    PATH_FINDER("Path Finder"),
    APESTOS("Apestos"),
    CRUSADER("Crusader"),
    DUNGEON_WHISPERER("Dungeon Whisperer"),
    HALF_HEART_WARRIOR("Half a heart warrior");

    private String name;

    public String getName() {
        return this.name;
    }

    private DungeonChallenge(String name) {
        this.name = name;
    }
}

