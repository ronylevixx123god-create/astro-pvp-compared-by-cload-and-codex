/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.potion.PotionType
 */
package com.cosmicpvp.CosmicSpaceChests.generators;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.generators.ArmorGenerator;
import com.cosmicpvp.CosmicSpaceChests.generators.WeaponGenerator;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.cosmicpvp.CosmicSpaceChests.utils.ItemBuilder;
import com.cosmicpvp.CosmicSpaceChests.utils.PotionBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

public class ItemGenerator {
    private static List<ItemBuilder> SIMPLE = new ArrayList<ItemBuilder>();
    private static List<ItemBuilder> UNIQUE = new ArrayList<ItemBuilder>();
    private static List<ItemBuilder> ELITE = new ArrayList<ItemBuilder>();
    private static List<ItemBuilder> ULTIMATE = new ArrayList<ItemBuilder>();
    private static Random r = new Random();

    public static ItemStack generate(ItemRarity ir) {
        if (r.nextInt(40) == 0 && ir != ItemRarity.ULTIMATE) {
            if (ir == ItemRarity.SIMPLE) {
                ir = ItemRarity.UNIQUE;
            } else if (ir == ItemRarity.UNIQUE) {
                ir = ItemRarity.ELITE;
            } else if (ir == ItemRarity.ELITE) {
                ir = ItemRarity.ULTIMATE;
            }
        }
        if (ir == ItemRarity.SIMPLE) {
            if (!r.nextBoolean()) {
                return SIMPLE.get(r.nextInt(SIMPLE.size())).build();
            }
            return r.nextBoolean() ? WeaponGenerator.generate(ir) : ArmorGenerator.generate(ir);
        }
        if (ir == ItemRarity.UNIQUE) {
            if (!r.nextBoolean()) {
                return UNIQUE.get(r.nextInt(UNIQUE.size())).build();
            }
            return r.nextBoolean() ? WeaponGenerator.generate(ir) : ArmorGenerator.generate(ir);
        }
        if (ir == ItemRarity.ELITE) {
            return r.nextBoolean() ? WeaponGenerator.generate(ir) : ArmorGenerator.generate(ir);
        }
        if (ir == ItemRarity.ULTIMATE) {
            return r.nextBoolean() ? WeaponGenerator.generate(ir) : ArmorGenerator.generate(ir);
        }
        return null;
    }

    public static ItemStack generate(ItemRarity ir, ItemRarity slotRarity) {
        int slotRarity_ID = CosmicSpaceChestsAPI.getItemRarity(slotRarity);
        if (r.nextInt(10) == 0 && ir != ItemRarity.ULTIMATE) {
            if (ir == ItemRarity.SIMPLE) {
                ir = ItemRarity.UNIQUE;
            } else if (ir == ItemRarity.UNIQUE) {
                ir = ItemRarity.ELITE;
            } else if (ir == ItemRarity.ELITE) {
                ir = ItemRarity.ULTIMATE;
            }
        }
        if (ir == ItemRarity.SIMPLE) {
            if (!r.nextBoolean() && slotRarity_ID < 3) {
                return SIMPLE.get(r.nextInt(SIMPLE.size())).build();
            }
            return r.nextBoolean() || slotRarity_ID < 3 ? ArmorGenerator.generate(ir) : WeaponGenerator.generate(ir);
        }
        if (ir == ItemRarity.UNIQUE) {
            if (!r.nextBoolean() && slotRarity_ID < 3) {
                return UNIQUE.get(r.nextInt(UNIQUE.size())).build();
            }
            return r.nextBoolean() || slotRarity_ID < 3 ? ArmorGenerator.generate(ir) : WeaponGenerator.generate(ir);
        }
        if (ir == ItemRarity.ELITE) {
            return r.nextBoolean() || slotRarity_ID < 3 ? ArmorGenerator.generate(ir) : WeaponGenerator.generate(ir);
        }
        if (ir == ItemRarity.ULTIMATE) {
            return r.nextBoolean() || slotRarity_ID < 3 ? ArmorGenerator.generate(ir) : WeaponGenerator.generate(ir);
        }
        return null;
    }

    public static ItemStack getRandomFood() {
        Material m = null;
        int x = r.nextInt(6);
        if (x == 0) {
            m = Material.APPLE;
        }
        if (x == 1) {
            m = Material.RAW_BEEF;
        }
        if (x == 2) {
            m = Material.RAW_FISH;
        }
        if (x == 3) {
            m = Material.RAW_CHICKEN;
        }
        if (x == 4) {
            m = Material.POTATO_ITEM;
        }
        if (x == 5) {
            m = Material.CARROT_ITEM;
        }
        return new ItemStack(m, 1);
    }

    static {
        block6: for (Lootable c : Lootable.values()) {
            switch (c.rarity) {
                case SIMPLE: {
                    SIMPLE.add(c.builder);
                    continue block6;
                }
                case UNIQUE: {
                    UNIQUE.add(c.builder);
                    continue block6;
                }
                case ELITE: {
                    ELITE.add(c.builder);
                    continue block6;
                }
                case ULTIMATE: {
                    ULTIMATE.add(c.builder);
                }
            }
        }
    }

    public static enum Lootable {
        PORK_16(new ItemBuilder(Material.GRILLED_PORK, 16), ItemRarity.SIMPLE),
        PORK_32(new ItemBuilder(Material.GRILLED_PORK, 32), ItemRarity.SIMPLE),
        COOKIE_16(new ItemBuilder(Material.COOKIE, 16), ItemRarity.SIMPLE),
        BAKED_POTATO(new ItemBuilder(Material.BAKED_POTATO, Material.BAKED_POTATO.getMaxStackSize()), ItemRarity.SIMPLE),
        GOLDEN_CARROT(new ItemBuilder(Material.GOLDEN_CARROT, 16), ItemRarity.SIMPLE),
        CHICKEN_16(new ItemBuilder(Material.COOKED_CHICKEN, 16), ItemRarity.SIMPLE),
        GOLDEN_APPLE(new ItemBuilder(Material.GOLDEN_APPLE, 10), ItemRarity.SIMPLE),
        ENDERPEARL_8(new ItemBuilder(Material.ENDER_PEARL, 8), ItemRarity.SIMPLE),
        ARROWS(new ItemBuilder(Material.ARROW, 64), ItemRarity.SIMPLE),
        TNT(new ItemBuilder(Material.TNT, 16), ItemRarity.SIMPLE),
        OBSIDIAN(new ItemBuilder(Material.OBSIDIAN, 16), ItemRarity.SIMPLE),
        BUCKET(new ItemBuilder(Material.BUCKET, 16), ItemRarity.SIMPLE),
        WATER_BREATHING(new ItemBuilder(Material.POTION).setPotion(new PotionBuilder(PotionType.WATER_BREATHING, 1).build()), ItemRarity.SIMPLE),
        HEAL_0(new ItemBuilder(Material.POTION, 10).setPotion(new PotionBuilder(PotionType.INSTANT_HEAL, 1).build()), ItemRarity.SIMPLE),
        REGEN_0(new ItemBuilder(Material.POTION).setPotion(new PotionBuilder(PotionType.REGEN, true, 1).build()), ItemRarity.SIMPLE),
        NIGHT_VISION(new ItemBuilder(Material.POTION).setPotion(new PotionBuilder(PotionType.NIGHT_VISION, 1).build()), ItemRarity.SIMPLE),
        STRENGTH_0(new ItemBuilder(Material.POTION).setPotion(new PotionBuilder(PotionType.STRENGTH, 2).build()), ItemRarity.SIMPLE),
        PORK_64(new ItemBuilder(Material.GRILLED_PORK, 64), ItemRarity.UNIQUE),
        CHICKEN_32(new ItemBuilder(Material.COOKED_CHICKEN, 32), ItemRarity.SIMPLE),
        GOLDEN_APPLE_24(new ItemBuilder(Material.GOLDEN_APPLE, 24), ItemRarity.SIMPLE),
        DIAMOND(new ItemBuilder(Material.DIAMOND, 32), ItemRarity.UNIQUE),
        EMERALD(new ItemBuilder(Material.EMERALD, 16), ItemRarity.UNIQUE),
        TNT_32(new ItemBuilder(Material.TNT, 32), ItemRarity.SIMPLE),
        OBSIDIAN_32(new ItemBuilder(Material.OBSIDIAN, 32), ItemRarity.SIMPLE),
        EMERALD_BLOCK(new ItemBuilder(Material.EMERALD_BLOCK, 4), ItemRarity.UNIQUE),
        ENDERPEARL_16(new ItemBuilder(Material.ENDER_PEARL, 16), ItemRarity.UNIQUE),
        SPEED_1(new ItemBuilder(Material.POTION, 5).setPotion(new PotionBuilder(PotionType.SPEED, 2).build()), ItemRarity.UNIQUE),
        STRENGTH_1(new ItemBuilder(Material.POTION, 5).setPotion(new PotionBuilder(PotionType.STRENGTH, 2).build()), ItemRarity.UNIQUE),
        REGEN_1(new ItemBuilder(Material.POTION, 6).setPotion(new PotionBuilder(PotionType.REGEN, 2).build()), ItemRarity.UNIQUE),
        HEAL_1(new ItemBuilder(Material.POTION, 10).setPotion(new PotionBuilder(PotionType.INSTANT_HEAL, 2).build()), ItemRarity.UNIQUE);

        ItemBuilder builder;
        ItemRarity rarity;

        private Lootable(ItemBuilder builder, ItemRarity rarity) {
            this.builder = builder == null ? new ItemBuilder(Material.getMaterial((String)this.name())) : builder;
            this.rarity = rarity;
        }

        public static ArrayList<Lootable> valuesByNetwork() {
            ArrayList<Lootable> networkItems = new ArrayList<Lootable>();
            networkItems.addAll(Arrays.asList(Lootable.values()));
            return networkItems;
        }
    }
}

