package com.conaxgames.customskulls.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;

@Getter
@RequiredArgsConstructor
public enum AbilityType {
    HALLOWEEN_CAULDRON("HALLOWEEN_CAULDRON",
            "HALLOWEEN_CAULDRON",
            1000000, 300, Material.NOTE_BLOCK),
    THANKSGIVING_PIE("THANKSGIVING_PIE",
            "THANKSGIVING_PIE",
            1000000, 300, Material.NOTE_BLOCK),
    TREASURE_CHEST("TREASURE_CHEST",
            "TREASURE_CHEST",
            1000000, 300, Material.NOTE_BLOCK),
    COSMIC_TROPHY("COSMIC_TROPHY",
            "COSMIC_TROPHY",
            1000000, 300, Material.NOTE_BLOCK),
    LEVIATHAN_TOTEM("LEVIATHAN_TOTEM",
            "LEVIATHAN_TOTEM",
            1000000, 300, Material.NOTE_BLOCK),
    HALLOWEEN_CANDLES("HALLOWEEN_CANDLES",
                            "HALLOWEEN_CANDLES",
            1000000, 300, Material.NOTE_BLOCK),
    COSMIC_TOKEN_GENERATOR("COSMIC_TOKEN_GENERATOR",
                                    "COSMIC_TOKEN_GENERATOR",
            1500000, 300, Material.NOTE_BLOCK),
    SECURITY_SYSTEM("SECURITY_SYSTEM",
                                   "SECURITY_SYSTEM",
            1500000, 300, Material.NOTE_BLOCK),
    BLAZE_STONE_TOTEM("BLAZE_STONE_TOTEM",
            "BLAZE_STONE_TOTEM",
            1500000, 500000, Material.NOTE_BLOCK),
    COSMICCRATE("COSMICCRATE", "COSMICCRATE", 1500000, 0, Material.NOTE_BLOCK),
    ;
    private final String name; // we will use this as a indexing name and for metadata.
    private final String customBlockSkin; // this is the skin texture for the item
    private final int pickupPrice, pricePerAction;
    private final Material material;
}


//COSMIC_TOKEN_GENERATOR
