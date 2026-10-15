package org.arkhamnetwork.enchantments.libs;

import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

public enum EnchanterRarity {
    SIMPLE("Simple", 0.0, DyeColor.WHITE, ChatColor.WHITE, 1),
    UNIQUE("Unique", 0.0, DyeColor.LIME, ChatColor.GREEN, 2),
    ELITE("Elite", 0.0, DyeColor.LIGHT_BLUE, ChatColor.AQUA, 3),
    ULTIMATE("Ultimate", 0.0, DyeColor.YELLOW, ChatColor.YELLOW, 4),
    LEGENDARY("Legendary", 0.0, DyeColor.ORANGE, ChatColor.GOLD, 5),
    SOUL("Soul", 0.0, DyeColor.RED, ChatColor.RED, 6),
    HEROIC("Heroic", 0.0, DyeColor.PINK, ChatColor.LIGHT_PURPLE, 7),
    MASTERY("Mastery", 250000.0, DyeColor.RED, ChatColor.DARK_RED, 8);

    private String name;
    private double price;
    private DyeColor paneColor;
    private ChatColor color;
    private int tier;

    private EnchanterRarity(final String name, final double price, final DyeColor paneColor, final ChatColor color, final int tier) {
        this.name = name;
        this.price = price;
        this.paneColor = paneColor;
        this.color = color;
        this.tier = tier;
    }

    public static EnchanterRarity getFromItemRarity(final ItemRarity rarity) {
        for (final EnchanterRarity enc : values()) {
            if (enc.name().equals(rarity.name())) {
                return enc;
            }
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }

    public DyeColor getPaneColor() {
        return this.paneColor;
    }

    public ChatColor getColor() {
        return this.color;
    }

    public int getTier() {
        return this.tier;
    }
}
