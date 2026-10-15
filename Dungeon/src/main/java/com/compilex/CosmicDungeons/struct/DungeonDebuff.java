/*
 * Decompiled with CFR 0.150.
 */
package com.compilex.CosmicDungeons.struct;

public enum DungeonDebuff {
    HALF_OBJECTIVE_TIME("-50% Objective Time"),
    BOSSES_TAKE_LESS_DAMAGE("Bosses take 75% less damage"),
    BOSSES_DEAL_MORE_DAMAGE("Bosses deal 2.5x outgoing damage"),
    NO_ITEM_SKINS("No Item Skins"),
    NO_TELEPORTATION_BOWS("No Teleportation Bows"),
    NO_ARMOR_SET_BONUSES("No Armor Set Bonuses"),
    NO_INVENTORY_PETS("No Inventory Pets"),
    NO_ENDER_PEARLS("No Ender Pearls"),
    NO_POTIONS("No Potions"),
    NO_BOW_DAMAGE_TO_BOSSES("No Bow damage to bosses"),
    DEAL_30HP_MAX_PER_HIT("Players deal a max of 30HP to bosses per hit");

    private String name;

    public String getName() {
        return this.name;
    }

    private DungeonDebuff(String name) {
        this.name = name;
    }
}

