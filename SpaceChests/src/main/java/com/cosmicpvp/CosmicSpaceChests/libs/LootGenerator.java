package com.cosmicpvp.CosmicSpaceChests.libs;


import com.compilex.CosmicCustomCreepers.enums.CreeperTypes;
import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsPlugin;
import com.cosmicpvp.CosmicSpaceChests.generators.ItemGenerator;
import com.cosmicpvp.CosmicSpaceChests.generators.LegendaryGenerator;
import com.cosmicpvp.CosmicSpaceChests.utils.ItemUtils;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import me.ifamasssxd.cosmicconquest.CosmicConquestAPI;
import org.arkhamnetwork.Arkkit.patches.*;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.arkhamnetwork.enchantments.EnchantmentOrb;
import org.arkhamnetwork.enchantments.RandomizationScroll;
import org.arkhamnetwork.enchantments.libs.OrbType;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class LootGenerator {
    private static HashMap<ItemStack, Double> commonExtras;
    private static HashMap<ItemStack, Double> uncommonExtras;
    private static HashMap<ItemStack, Double> rareExtras;
    private static HashMap<ItemStack, Double> ultraRareExtras;
    private static HashMap<ItemStack, Double> legendaryExtras;
    private static HashMap<ItemStack, Double> godExtras;
    private static List<ItemStack> possibleCommonExtras;
    private static List<ItemStack> possibleUncommonExtras;
    private static List<ItemStack> possibleRareExtras;
    private static List<ItemStack> possibleUltraRareExtras;
    private static List<ItemStack> possibleLegendaryExtras;
    private static List<ItemStack> possibleGodExtras;

    public static void generateLootTables() {
        ItemStack item;
        boolean rankItems = CosmicSpaceChestsPlugin.plugin.getConfig().getBoolean("rankItems");
        boolean moneyNotes = CosmicSpaceChestsPlugin.plugin.getConfig().getBoolean("moneyNoteRanks");
        commonExtras = new HashMap();
        commonExtras.put(ItemUtils.getMoneyNoteNoIID("Cosmic Chest", 10000), 0.03);
        commonExtras.put(CosmicSpaceChestsAPI.getSpaceFlare(ItemRarity.SIMPLE), 0.02);
        commonExtras.put(CosmicSpaceChestsAPI.getSpaceFlare(ItemRarity.UNIQUE), 0.01);
        commonExtras.put(ItemUtils.getEnchantmentBook(ItemRarity.SIMPLE), 0.3);
        commonExtras.put(ItemUtils.getXpBottle(100, "Cosmic Chest"), 0.05);
        commonExtras.put(ItemUtils.getMobSpawner(EntityType.SHEEP), 0.01);
        commonExtras.put(ItemUtils.getTitleVoucher("Wasted"), 0.005);
        commonExtras.put(ItemUtils.getTitleVoucher("Tilted"), 0.0025);
        commonExtras.put(ItemUtils.getTitleVoucher("Trollolol"), 0.0025);
        commonExtras.put(ItemUtils.getTitleVoucher("Noob"), 1.0E-5);
        commonExtras.put(ItemUtils.getTitleVoucher("#poor"), 1.0E-5);
        commonExtras.put(ItemUtils.setAmount(EnchantmentBook.getSecretDust(ItemRarity.SIMPLE, 50), 5), 0.02);
        commonExtras.put(ItemUtils.setAmount(RandomizationScroll.getRandomizationScroll(ItemRarity.SIMPLE), 1), 0.02);
        uncommonExtras = new HashMap();
        uncommonExtras.put(ItemUtils.getMoneyNoteNoIID("Cosmic Chest", 50000), 0.03);
        uncommonExtras.put(CosmicSpaceChestsAPI.getSpaceFlare(ItemRarity.UNIQUE), 0.02);
        uncommonExtras.put(CosmicSpaceChestsAPI.getSpaceFlare(ItemRarity.ELITE), 0.01);
        uncommonExtras.put(ItemUtils.getEnchantmentBook(ItemRarity.UNIQUE), 0.3);
        uncommonExtras.put(ItemUtils.getXpBottle(2000, "Cosmic Chest"), 0.05);
        uncommonExtras.put(ItemUtils.getXpBottle(5000, "Cosmic Chest"), 0.01);
        uncommonExtras.put(ItemUtils.getMobSpawner(EntityType.SHEEP), 0.02);
        uncommonExtras.put(ItemUtils.getMobSpawner(EntityType.PIG), 0.01);
        uncommonExtras.put(ItemUtils.getMobSpawner(EntityType.CHICKEN), 0.01);
        uncommonExtras.put(ItemUtils.getTitleVoucher("#21"), 0.0025);
        uncommonExtras.put(ItemUtils.getTitleVoucher("RIP"), 0.005);
        uncommonExtras.put(ItemUtils.getTitleVoucher("Getmed"), 0.005);
        uncommonExtras.put(new ItemStack(Material.HOPPER, 1), 0.01);
        uncommonExtras.put(ItemUtils.setAmount(EnchantmentBook.getSecretDust(ItemRarity.UNIQUE, 50), 5), 0.02);
        uncommonExtras.put(ItemUtils.setAmount(RandomizationScroll.getRandomizationScroll(ItemRarity.UNIQUE), 1), 0.01);
        rareExtras = new HashMap();
        rareExtras.put(ItemUtils.getMoneyNoteNoIID("Cosmic Chest", 100000), 0.03);
        rareExtras.put(CosmicSpaceChestsAPI.getSpaceFlare(ItemRarity.ELITE), 0.02);
        rareExtras.put(CosmicSpaceChestsAPI.getSpaceFlare(ItemRarity.ULTIMATE), 0.01);
        rareExtras.put(ItemUtils.getEnchantmentBook(ItemRarity.ELITE), 0.3);
        rareExtras.put(ItemUtils.getXpBottle(5000, "Cosmic Chest"), 0.05);
        rareExtras.put(ItemUtils.getXpBottle(20000, "Cosmic Chest"), 0.01);
        rareExtras.put(ItemUtils.getMobSpawner(EntityType.SPIDER), 0.01);
        rareExtras.put(ItemUtils.getMobSpawner(EntityType.SKELETON), 0.01);
        rareExtras.put(ItemUtils.getMobSpawner(EntityType.ZOMBIE), 0.01);
        rareExtras.put(ItemUtils.getMobSpawner(EntityType.CAVE_SPIDER), 0.01);
        rareExtras.put(ItemUtils.getMobSpawner(EntityType.WOLF), 0.02);
        rareExtras.put(ItemUtils.getTitleVoucher("Poofless"), 0.01);
        rareExtras.put(ItemUtils.getTitleVoucher("ezpz"), 0.0025);
        rareExtras.put(ItemUtils.getTitleVoucher("OneShot"), 0.01);
        rareExtras.put(ItemUtils.getTitleVoucher("Staysalty"), 0.005);
        rareExtras.put(ItemUtils.getTitleVoucher("Schmoked"), 0.01);


        rareExtras.put(new ItemStack(Material.getMaterial(383), 1, (short) 50), 0.01);
        rareExtras.put(new ItemStack(Material.HOPPER, 5), 0.01);
        rareExtras.put(ItemUtils.setAmount(EnchantmentBook.getSecretDust(ItemRarity.ELITE, 50), 5), 0.02);
        rareExtras.put(ItemUtils.setAmount(RandomizationScroll.getRandomizationScroll(ItemRarity.ELITE), 1), 0.01);
        ultraRareExtras = new HashMap();
        ultraRareExtras.put(ItemUtils.getNewBossEgg(BossType.BROOD_MOTHER), 0.0025);
        ultraRareExtras.put(ItemUtils.getNewBossEgg(BossType.KING_SLIME), 5.0E-4);
        ultraRareExtras.put(ItemUtils.getNewBossEgg(BossType.HOLIDAY_SPIRIT), 5.0E-4);
        ultraRareExtras.put(ItemUtils.getMoneyNoteNoIID("Cosmic Chest", 250000), 0.03);
        ultraRareExtras.put(CosmicSpaceChestsAPI.getSpaceFlare(ItemRarity.ULTIMATE), 0.02);
        ultraRareExtras.put(CosmicSpaceChestsAPI.getSpaceFlare(ItemRarity.LEGENDARY), 0.01);
        ultraRareExtras.put(ItemUtils.getEnchantmentBook(ItemRarity.ULTIMATE), 0.3);
        ultraRareExtras.put(ItemUtils.getWhitescroll(), 0.02);
        ultraRareExtras.put(ItemUtils.getTransmogScroll(), 0.01);
        ultraRareExtras.put(ItemUtils.getItemNametag(), 0.01);
        ultraRareExtras.put(ItemUtils.getBlackscroll(), 0.05);
        ultraRareExtras.put(ItemUtils.getXpBottle(25000, "Cosmic Chest"), 0.05);
        ultraRareExtras.put(ItemUtils.getXpBottle(75000, "Cosmic Chest"), 0.01);
        ultraRareExtras.put(ItemUtils.getMobSpawner(EntityType.BLAZE), 0.01);
        ultraRareExtras.put(ItemUtils.getMobSpawner(EntityType.IRON_GOLEM), 0.01);
        ultraRareExtras.put(ItemUtils.getMobSpawner(EntityType.ENDERMAN), 0.03);
        ultraRareExtras.put(ItemUtils.getMobSpawner(EntityType.COW), 0.05);
        ultraRareExtras.put(ItemUtils.getTitleVoucher("Gank"), 0.01);
        ultraRareExtras.put(ItemUtils.getTitleVoucher("Scrub"), 0.01);
        ultraRareExtras.put(ItemUtils.getTitleVoucher("#AllmcMMO"), 0.005);
        ultraRareExtras.put(ItemUtils.getTitleVoucher("1v1me"), 0.001);
        ultraRareExtras.put(ItemUtils.getTitleVoucher("Vapenation"), 0.01);
        ultraRareExtras.put(ItemUtils.getMysterySoulEnchantmentBook(), 0.0025);
        ultraRareExtras.put(new ItemStack(Material.getMaterial(383), 2, (short) 50), 0.01);
        ultraRareExtras.put(new ItemStack(Material.HOPPER, 16), 0.01);
        ultraRareExtras.put(ItemUtils.setAmount(EnchantmentBook.getSecretDust(ItemRarity.ULTIMATE, 50), 5), 0.02);
        ultraRareExtras.put(ItemUtils.setAmount(RandomizationScroll.getRandomizationScroll(ItemRarity.ULTIMATE), 2), 0.01);
        ultraRareExtras.put(ShowcaseExpander.getShowcaseExpanderItem(), 0.01);
        ultraRareExtras.put(EssentialsAddHomes.getAddHomeItem(), 0.005);
        legendaryExtras = new HashMap();
        legendaryExtras.put(ItemUtils.getNewBossEgg(BossType.SKELETON_HORSE), 0.01);
        legendaryExtras.put(ItemUtils.getNewBossEgg(BossType.PLAGUE_BLOATER), 0.005);
        legendaryExtras.put(ItemUtils.getNewBossEgg(BossType.DEMONIC_WITHER), 2.5E-4);
        legendaryExtras.put(ItemUtils.getMoneyNoteNoIID("Cosmic Chest", 1000000), 0.01);
        legendaryExtras.put(CosmicSpaceChestsAPI.getSpaceFlare(ItemRarity.LEGENDARY), 0.01);
        legendaryExtras.put(CosmicSpaceChestsAPI.getSpaceFlare(ItemRarity.GOD), 0.005);
        legendaryExtras.put(ItemUtils.getEnchantmentBook(ItemRarity.LEGENDARY), 0.3);
        legendaryExtras.put(ItemUtils.getMysterySoulEnchantmentBook(), 0.01);
        legendaryExtras.put(ItemUtils.getMobSpawner(EntityType.MUSHROOM_COW), 0.01);
        legendaryExtras.put(ItemUtils.getMobSpawner(EntityType.MAGMA_CUBE), 0.0075);
        legendaryExtras.put(ItemUtils.getMobSpawner(EntityType.GHAST), 0.005);
        legendaryExtras.put(ItemUtils.getWhitescroll(), 0.1);
        legendaryExtras.put(ItemUtils.getBlackscroll(75), 0.075);
        legendaryExtras.put(ItemUtils.getEnvoySummoner(), 0.01);
        legendaryExtras.put(ItemUtils.getItemNametag(), 0.01);
        legendaryExtras.put(ItemUtils.getTransmogScroll(), 0.01);
        legendaryExtras.put(ItemUtils.getXpBottle(50000, "Cosmic Chest"), 0.05);
        legendaryExtras.put(ItemUtils.getXpBottle(100000, "Cosmic Chest"), 0.01);
        legendaryExtras.put(ItemUtils.getMobSpawner(EntityType.BLAZE), 0.05);
        legendaryExtras.put(ItemUtils.getMobSpawner(EntityType.IRON_GOLEM), 0.02);
        legendaryExtras.put(ItemUtils.getMobSpawner(EntityType.ENDERMAN), 0.05);
        legendaryExtras.put(ItemUtils.getTitleVoucher("Ez"), 0.005);
        legendaryExtras.put(ItemUtils.getTitleVoucher("GetRekt"), 0.01);
        legendaryExtras.put(ItemUtils.getTitleVoucher("weewoo"), 0.01);
        legendaryExtras.put(ItemUtils.getTitleVoucher("Shiva-d"), 0.01);

        legendaryExtras.put(new ItemStack(Material.getMaterial(383), 16, (short) 50), 0.02);
        legendaryExtras.put(new ItemStack(Material.HOPPER, 64), 0.02);
        legendaryExtras.put(ItemUtils.setAmount(ItemUtils.getCustomCreeperEgg(CreeperTypes.Huge), 4), 0.015);
        legendaryExtras.put(ItemUtils.setAmount(ItemUtils.getCustomCreeperEgg(CreeperTypes.Lucky), 2), 0.015);
        legendaryExtras.put(ItemUtils.setAmount(EnchantmentBook.getSecretDust(ItemRarity.LEGENDARY, 50), 5), 0.02);
        legendaryExtras.put(ItemUtils.setAmount(RandomizationScroll.getRandomizationScroll(ItemRarity.LEGENDARY), 2), 0.01);

        legendaryExtras.put(EnchantmentOrb.getEnchantmentOrb(75, 10, OrbType.WEAPON), 0.0025);
        legendaryExtras.put(EnchantmentOrb.getEnchantmentOrb(75, 10, OrbType.ARMOR), 0.005);
        legendaryExtras.put(ItemUtils.getDepthStriderBook(1), 0.01);
        legendaryExtras.put(ItemUtils.getDepthStriderBook(2), 0.005);
        legendaryExtras.put(ItemUtils.getDepthStriderBook(3), 0.0025);
        legendaryExtras.put(ItemUtils.setAmount(ShowcaseExpander.getShowcaseExpanderItem(), 2), 0.01);
        legendaryExtras.put(ItemUtils.setAmount(EssentialsAddHomes.getAddHomeItem(), 3), 0.005);
        legendaryExtras.put(CosmicConquestAPI.createFactionToken(20), 0.01);
        godExtras = new HashMap();
        godExtras.put(ItemUtils.getNewBossEgg(BossType.PLAGUE_BLOATER), 0.005);
        godExtras.put(ItemUtils.getNewBossEgg(BossType.DEMONIC_WITHER), 2.5E-4);
        godExtras.put(ItemUtils.getNewBossEgg(BossType.SOUL_REAPER), 4.5E-4);
        godExtras.put(ItemUtils.getMoneyNoteNoIID("Cosmic Chest", 4000000), 0.01);
        godExtras.put(ItemUtils.setAmount(CosmicSpaceChestsAPI.getSpaceFlare(ItemRarity.LEGENDARY), 2), 0.01);
        godExtras.put(ItemUtils.setAmount(ItemUtils.getEnchantmentBook(ItemRarity.LEGENDARY), 3), 0.3);
        godExtras.put(ItemUtils.getMysterySoulEnchantmentBook(), 0.01);
        godExtras.put(ItemUtils.setAmount(ItemUtils.getWhitescroll(), 2), 0.1);
        godExtras.put(ItemUtils.setAmount(ItemUtils.getBlackscroll(95), 3), 0.075);
        godExtras.put(ItemUtils.getEnvoySummoner(), 0.01);
        godExtras.put(ItemUtils.getItemNametag(), 0.02);
        godExtras.put(ItemUtils.getTransmogScroll(), 0.025);
        godExtras.put(ItemUtils.getXpBottle(100000, "Cosmic Chest"), 0.05);
        godExtras.put(ItemUtils.getXpBottle(200000, "Cosmic Chest"), 0.01);
        godExtras.put(ItemUtils.getMobSpawner(EntityType.GHAST), 0.02);
        godExtras.put(ItemUtils.getMobSpawner(EntityType.MUSHROOM_COW), 0.05);
        godExtras.put(ItemUtils.getMobSpawner(EntityType.MAGMA_CUBE), 0.05);
        godExtras.put(ItemUtils.getTitleVoucher("TheLongestTitleOnCosmicPvP"), 0.01);
        godExtras.put(ItemUtils.getTitleVoucher("Godly"), 0.01);
        godExtras.put(ItemUtils.getTitleVoucher("#BANNED"), 0.01);
        godExtras.put(ItemUtils.getTitleVoucher("Epic"), 0.01);
        godExtras.put(ItemUtils.getTitleVoucher("#GetSplit"), 0.01);

        godExtras.put(new ItemStack(Material.getMaterial(383), 48, (short) 50), 0.02);
        godExtras.put(new ItemStack(Material.HOPPER, 64), 0.02);
        godExtras.put(ItemUtils.setAmount(ItemUtils.getCustomCreeperEgg(CreeperTypes.Huge), 8), 0.015);
        godExtras.put(ItemUtils.setAmount(ItemUtils.getCustomCreeperEgg(CreeperTypes.Lucky), 5), 0.015);
        godExtras.put(ItemUtils.setAmount(EnchantmentBook.getSecretDust(ItemRarity.SOUL, 50), 5), 0.02);
        godExtras.put(ItemUtils.setAmount(RandomizationScroll.getRandomizationScroll(ItemRarity.GOD), 2), 0.01);

        godExtras.put(EnchantmentOrb.getEnchantmentOrb(100, 10, OrbType.WEAPON), 0.0025);
        godExtras.put(EnchantmentOrb.getEnchantmentOrb(100, 10, OrbType.ARMOR), 0.005);
        godExtras.put(EnchantmentOrb.getEnchantmentOrb(75, 11, OrbType.WEAPON), 1.0E-4);
        godExtras.put(EnchantmentOrb.getEnchantmentOrb(75, 11, OrbType.ARMOR), 2.0E-4);
        godExtras.put(ItemUtils.getDepthStriderBook(2), 0.02);
        godExtras.put(ItemUtils.getDepthStriderBook(3), 0.01);
        godExtras.put(ItemUtils.setAmount(ShowcaseExpander.getShowcaseExpanderItem(), 2), 0.01);
        godExtras.put(ItemUtils.setAmount(EssentialsAddHomes.getAddHomeItem(), 5), 0.005);
        godExtras.put(CosmicConquestAPI.createFactionToken(50), 0.01);

        godExtras.put(CosmicConquestAPI.createHeroicToken(2 + ThreadLocalRandom.current().nextInt(6)), 0.0015);
        possibleCommonExtras = new ArrayList<ItemStack>(commonExtras.keySet());
        possibleUncommonExtras = new ArrayList<ItemStack>(uncommonExtras.keySet());
        possibleRareExtras = new ArrayList<ItemStack>(rareExtras.keySet());
        possibleUltraRareExtras = new ArrayList<ItemStack>(ultraRareExtras.keySet());
        possibleLegendaryExtras = new ArrayList<ItemStack>(legendaryExtras.keySet());
        possibleGodExtras = new ArrayList<ItemStack>(godExtras.keySet());
    }

    public static ItemStack getRandomCommonLoot(ItemRarity slotRarity) {
        boolean uniqueLoot;
        ItemStack is = null;
        ItemRarity chestRarity = ItemRarity.SIMPLE;
        boolean bl = uniqueLoot = new Random().nextInt(100) > 35;
        if (uniqueLoot) {
            if (Math.random() <= 0.02) {
                Collections.shuffle(possibleCommonExtras);
            }
            for (ItemStack item : possibleCommonExtras) {
                double chance = commonExtras.get(item);
                if (!(Math.random() * LootGenerator.getSlotRarityModifier(slotRarity) <= chance)) continue;
                is = item.clone();
                break;
            }
        }
        if (is == null) {
            is = ItemGenerator.generate(chestRarity, slotRarity);
        }
        if (is == null || is.getType() == Material.AIR) {
            return null;
        }
        return is;
    }

    public static ItemStack getRandomUncommonLoot(ItemRarity slotRarity) {
        ItemStack is = null;
        ItemRarity chestRarity = ItemRarity.UNIQUE;
        boolean uniqueLoot = new Random().nextInt(100) > 35;
        int slotRarity_ID = CosmicSpaceChestsAPI.getItemRarity(slotRarity);
        if (uniqueLoot) {
            if (Math.random() <= 0.02) {
                Collections.shuffle(possibleUncommonExtras);
            }
            for (ItemStack item : possibleUncommonExtras) {
                double chance = uncommonExtras.get(item);
                if (!(Math.random() * LootGenerator.getSlotRarityModifier(slotRarity) <= chance)) continue;
                is = item.clone();
                break;
            }
        }
        if (is == null) {
            CustomEnchantment ce;
            is = ItemGenerator.generate(chestRarity, slotRarity);
            if (CosmicSpaceChestsPlugin.plugin.getServer().getPluginManager().isPluginEnabled("Enchantments") && (slotRarity_ID > 2 || new Random().nextBoolean()) && (ce = CosmicSpaceChestsAPI.getRandomCustomEnchant(is.getType())) != null && ce.canEnchantOnto(is)) {
                is = ce.addToItem(is, new Random().nextInt(ce.getMaxLevel() == 1 ? ce.getMaxLevel() : ce.getMaxLevel() - 1) + 1);
            }
        }
        if (is == null || is.getType() == Material.AIR) {
            return null;
        }
        return is;
    }

    public static ItemStack getRandomRareLoot(ItemRarity slotRarity) {
        ItemStack is = null;
        ItemRarity chestRarity = ItemRarity.ELITE;
        boolean uniqueLoot = new Random().nextInt(100) > 35;
        int slotRarity_ID = CosmicSpaceChestsAPI.getItemRarity(slotRarity);
        if (uniqueLoot) {
            if (Math.random() <= 0.02) {
                Collections.shuffle(possibleRareExtras);
            }
            for (ItemStack item : possibleRareExtras) {
                double chance = rareExtras.get(item);
                if (!(Math.random() * LootGenerator.getSlotRarityModifier(slotRarity) <= chance)) continue;
                is = item.clone();
                break;
            }
        }
        if (is == null && EnchantmentAPI.getEnchantments(is = ItemGenerator.generate(chestRarity, slotRarity)).size() < 5) {
            int attempts = 0;
            for (int id = 0; id < 2 && attempts++ <= 100; ++id) {
                CustomEnchantment ce = CosmicSpaceChestsAPI.getRandomCustomEnchant(is.getType());
                if (ce == null) continue;
                if (ce.canEnchantOnto(is)) {
                    is = ce.addToItem(is, new Random().nextInt(ce.getMaxLevel()) + 1);
                    continue;
                }
                if (!LootGenerator.canBeEnchanted(is)) continue;
                --id;
            }
        }
        if (is == null || is.getType() == Material.AIR) {
            return null;
        }
        return is;
    }

    public static ItemStack getRandomUltraRareLoot(ItemRarity slotRarity) {
        ItemStack is = null;
        ItemRarity chestRarity = ItemRarity.ULTIMATE;
        boolean uniqueLoot = new Random().nextInt(100) > 35;
        int slotRarity_ID = CosmicSpaceChestsAPI.getItemRarity(slotRarity);
        if (uniqueLoot) {
            if (Math.random() <= 0.02) {
                Collections.shuffle(possibleUltraRareExtras);
            }
            for (ItemStack item : possibleUltraRareExtras) {
                double chance = ultraRareExtras.get(item);
                if (!(Math.random() * LootGenerator.getSlotRarityModifier(slotRarity) <= chance)) continue;
                is = item.clone();
                break;
            }
        }
        if (is == null && EnchantmentAPI.getEnchantments(is = ItemGenerator.generate(chestRarity, slotRarity)).size() < 5) {
            int x = new Random().nextInt(2) + 3;
            if (slotRarity_ID > 3) {
                ++x;
            }
            if (slotRarity_ID > 4) {
                ++x;
            }
            if (slotRarity_ID > 5) {
                ++x;
            }
            int attempts = 0;
            for (int id = 0; id < x && attempts++ <= 100; ++id) {
                CustomEnchantment ce = CosmicSpaceChestsAPI.getRandomCustomEnchant(is.getType());
                if (ce == null) continue;
                if (ce.canEnchantOnto(is)) {
                    is = ce.addToItem(is, new Random().nextInt(ce.getMaxLevel()) + 1);
                    continue;
                }
                if (!LootGenerator.canBeEnchanted(is)) continue;
                --id;
            }
        }
        if (is == null || is.getType() == Material.AIR) {
            return null;
        }
        return is;
    }

    public static ItemStack getRandomLegendaryLoot(ItemRarity slotRarity) {
        double chance;
        ItemStack is = null;
        ItemRarity chestRarity = ItemRarity.LEGENDARY;
        boolean uniqueLoot = new Random().nextInt(100) > 35;
        int slotRarity_ID = CosmicSpaceChestsAPI.getItemRarity(slotRarity);
        if (uniqueLoot) {
            if (Math.random() <= 0.02) {
                Collections.shuffle(possibleLegendaryExtras);
            }
            for (ItemStack item : possibleLegendaryExtras) {
                double d = chance = legendaryExtras.containsKey(item) ? legendaryExtras.get(item) : 0.0;
                if (!(Math.random() * LootGenerator.getSlotRarityModifier(slotRarity) <= chance)) continue;
                is = item.clone();
                break;
            }
        }
        if (is == null) {
            if (new Random().nextInt(10 - slotRarity_ID) != 0 || slotRarity_ID < 3) {
                is = LootGenerator.convertToGoldName(ItemGenerator.generate(ItemRarity.ULTIMATE, ItemRarity.LEGENDARY));
                if (is != null) {
                    if (is.getType() == Material.DIAMOND_HOE || is.getType() == Material.DIAMOND_SPADE) {
                        for (ItemStack item : possibleLegendaryExtras) {
                            double d = chance = legendaryExtras.containsKey(item) ? legendaryExtras.get(item) : 0.0;
                            if (!(Math.random() * LootGenerator.getSlotRarityModifier(slotRarity) <= chance)) continue;
                            is = item.clone();
                            return is;
                        }
                        return possibleLegendaryExtras.get(new Random().nextInt(possibleLegendaryExtras.size()));
                    }
                }
            } else {
                is = LegendaryGenerator.getRandomLegendary();
            }
            if (EnchantmentAPI.getEnchantments(is).size() < 5) {
                int x = new Random().nextInt(2) + 5;
                int attempts = 0;
                for (int id = 0; id < x && attempts++ <= 100; ++id) {
                    CustomEnchantment ce = CosmicSpaceChestsAPI.getRandomCustomEnchant(is.getType());
                    if (ce == null) continue;
                    if (ce != null && ce.canEnchantOnto(is)) {
                        is = ce.addToItem(is, new Random().nextInt(ce.getMaxLevel()) + 1);
                        continue;
                    }
                    if (!LootGenerator.canBeEnchanted(is)) continue;
                    --id;
                }
            }
        }
        if (is == null || is.getType() == Material.AIR) {
            return null;
        }
        return is;
    }

    public static ItemStack getRandomGodLoot(ItemRarity slotRarity) {
        return LootGenerator.getRandomGodLoot(slotRarity, 0);
    }

    public static ItemStack getRandomGodLoot(ItemRarity slotRarity, int attempt) {
        ItemStack is = null;
        ItemRarity chestRarity = ItemRarity.GOD;
        boolean uniqueLoot = true;
        int slotRarity_ID = CosmicSpaceChestsAPI.getItemRarity(slotRarity);
        if (uniqueLoot) {
            if (Math.random() <= 0.02) {
                Collections.shuffle(possibleGodExtras);
            }
            for (ItemStack item : possibleGodExtras) {
                double chance = godExtras.get(item);
                if (!(Math.random() * LootGenerator.getSlotRarityModifier(slotRarity) <= chance)) continue;
                is = item.clone();
                break;
            }
        }
        if (is == null) {
            if (attempt >= 10) {
                Bukkit.getLogger().info("[CosmicSpaceChests] Returning NULL for getRandomGodLoot due to attempt=" + attempt);
                return null;
            }
            return LootGenerator.getRandomGodLoot(slotRarity, attempt + 1);
        }
        if (is == null || is.getType() == Material.AIR) {
            return null;
        }
        return is;
    }

    public static double getSlotRarityModifier(ItemRarity slotRarity) {
        if (slotRarity == ItemRarity.SIMPLE) {
            return 1.0;
        }
        if (slotRarity == ItemRarity.UNIQUE) {
            return 0.79;
        }
        if (slotRarity == ItemRarity.ELITE) {
            return 0.65;
        }
        if (slotRarity == ItemRarity.ULTIMATE) {
            return 0.45;
        }
        if (slotRarity == ItemRarity.LEGENDARY) {
            return 0.25;
        }
        return 1.0;
    }

    private static ItemStack convertToGoldName(ItemStack i) {
        if (i == null || !i.hasItemMeta() || !i.getItemMeta().hasDisplayName()) {
            return i;
        }
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(im.getDisplayName().replace(ChatColor.YELLOW.toString(), ChatColor.GOLD.toString()));
        i.setItemMeta(im);
        return i;
    }

    private static boolean canBeEnchanted(ItemStack i) {
        return i != null && (GeneralUtil.isArmor(i) || GeneralUtil.isTool(i) || GeneralUtil.isWeapon(i));
    }
}

