package org.demonicpvp.demonictrials.trial.modifier.stackable;

import org.bukkit.Material;

public enum StackableModifier {
    FRAGILE(
            "Fragile",
            "+10% Incoming Damage per stack.",
            10,
            Material.GLASS
    ),
    SUGAR_RUSH(
            "Sugar Rush",
            "All mobs are given Speed +10% per stack.",
            10,
            Material.SUGAR
    ),
    TIKTOK(
            "TikTok",
            "-8% Extra Room Completion Time per stack.",
            10,
            Material.WATCH
    ),
    WET_NOODLE(
            "Wet Noodle",
            "-10% Outgoing Damage per stack.",
            5,
            Material.STRING
    );

    private final String name;
    private final String description;
    private final int maxStacks;
    private final Material material;

    StackableModifier(String name, String description, int maxStacks, Material material) {
        this.name = name;
        this.description = description;
        this.maxStacks = maxStacks;
        this.material = material;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public int getMaxStacks() {
        return this.maxStacks;
    }

    public Material getMaterial() {
        return this.material;
    }
}
