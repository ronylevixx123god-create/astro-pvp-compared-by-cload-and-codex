/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 */
package net.astropvp.lootboxes.config;

import net.astropvp.lootboxes.AstroLootboxes;
import net.astropvp.lootboxes.pojo.Rarity;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class RarityConfig {
    private static final Pattern SPLIT_PATTERN = Pattern.compile("\\|");
    private static final String PATH = "rarities";
    private static Rarity DEFAULT_RARITY;
    private static List<Rarity> RARITIES;
    private static Map<String, Rarity> RARITY_MAP;

    public static void initialize(AstroLootboxes plugin) {
        RARITIES = Lists.newArrayList();
        RARITY_MAP = Maps.newHashMap();
        plugin.getConfig().getStringList(PATH).forEach(string -> {
            try {
                String[] values = SPLIT_PATTERN.split(string);
                String id = values[0];
                String displayName = values[1];
                double chance = Double.parseDouble(values[2]);
                boolean isDefault = Boolean.parseBoolean(values[3]);
                RARITIES.add(new Rarity(id, displayName, chance, isDefault));
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                System.out.println("Error initializing rarity string [" + string + "], ignoring!");
            }
        });
        RARITIES.sort(Comparator.comparingDouble(Rarity::getChance).reversed());
        RARITIES.forEach(rarity -> RARITY_MAP.put(rarity.getId(), rarity));
        DEFAULT_RARITY = RARITIES.stream().filter(Rarity::isDefault).findFirst().orElse(new Rarity("commmon", "&f&lCOMMON", 10.0, true));
    }

    public static Rarity getRarityOrDefault(String rarity) {
        return RARITY_MAP.getOrDefault(rarity, DEFAULT_RARITY);
    }

    public static Rarity getDefaultRarity() {
        return DEFAULT_RARITY;
    }

    public static List<Rarity> getRarities() {
        return Collections.unmodifiableList(RARITIES);
    }
}

