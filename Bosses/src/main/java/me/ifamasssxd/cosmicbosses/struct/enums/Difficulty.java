package me.ifamasssxd.cosmicbosses.struct.enums;

import org.bukkit.ChatColor;

public enum Difficulty {
    ULTIMATE(ChatColor.DARK_PURPLE, "Ultimate"),
    ULTIMATE_PLUS(ChatColor.YELLOW, "Ultimate+"),
    LEGENDARY(ChatColor.RED, "Legendary"),
    LEGIT(ChatColor.DARK_GREEN, "Legendary+"),
    LEGENDARY_PLUS(ChatColor.GOLD, "Legendary++"),
    HEROIC(ChatColor.RED, "Heroic"),
    END(ChatColor.DARK_RED, "End"),
    OUTPOST(ChatColor.LIGHT_PURPLE, "Outpost Exclusive");

    ChatColor color;
    String name;

    private Difficulty(final ChatColor color, final String name) {
        this.color = color;
        this.name = name;
    }

    public ChatColor getColor() {
        return this.color;
    }

    public String getName() {
        return this.name;
    }
}
