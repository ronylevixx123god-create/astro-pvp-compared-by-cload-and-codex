/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.compilex.CosmicCustomCreepers.me.astropvp.cratekeys.enums.CreeperTypes
 *  com.compilex.CosmicDungeons.DungeonsAPI
 *  com.cosmicpvp.CosmicRankQuests.CosmicRankQuestsAPI
 *  com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI
 *  com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity
 *  com.cosmicpvp.CosmicSpaceChests.me.astropvp.cratekeys.net.astropvp.armorsets.utils.ItemUtils
 *  com.cosmicpvp.cosmicutils.itembuilder.BoosterBuilder
 *  com.cosmicpvp.cosmicutils.itembuilder.CustomCrateType
 *  com.cosmicpvp.cosmicutils.itembuilder.CustomItemBuilder
 *  com.cosmicpvp.cosmicutils.itembuilder.CustomItemBuilder$CustomItemType
 *  com.cosmicpvp.cosmicutils.itembuilder.CustomTitleBuilder
 *  com.cosmicpvp.cosmicutils.itembuilder.EssentialsItemBuilder
 *  com.cosmicpvp.cosmicutils.itembuilder.ItemBuilder
 *  com.cosmicpvp.cosmicutils.itembuilder.KitGemBuilder
 *  com.cosmicpvp.cosmicutils.itembuilder.SpawnerItemBuilder
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CustomItemUtils
 *  com.gmail.nossr50.datatypes.skills.SkillType
 *  com.google.common.collect.Lists
 *  me.ifamasssxd.cosmicconquest.CosmicConquestAPI
 *  me.ifamasssxd.cosmicskycustoms.net.astropvp.armorsets.struct.CustomItemBuilder
 *  me.ifamasssxd.killtracker.KillTrackerAPI
 *  me.ifamasssxd.killtracker.me.astropvp.cratekeys.net.astropvp.armorsets.listeners.Tracker
 *  org.arkhamnetwork.Arkkit.patches.EquipmentLootboxes
 *  org.arkhamnetwork.Arkkit.patches.EssentialsAddHomes
 *  org.arkhamnetwork.Arkkit.patches.ItemID
 *  org.arkhamnetwork.Arkkit.patches.McmmoCredits
 *  org.arkhamnetwork.Arkkit.patches.ShowcaseExpander
 *  org.arkhamnetwork.Arkkit.plugins.ArkPlugins
 *  org.arkhamnetwork.Arkkit.me.astropvp.cratekeys.net.astropvp.armorsets.utils.ItemUtils
 *  org.arkhamnetwork.enchantments.EnchantmentOrb
 *  org.arkhamnetwork.enchantments.libs.OrbType
 *  org.arkhamnetwork.enchantments.util.TieredEnchantments
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.EnchantmentStorageMeta
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 */
package com.compilex.CosmicKOTH.utils;

import com.compilex.CosmicCustomCreepers.enums.CreeperTypes;
import com.compilex.CosmicDungeons.DungeonsAPI;
import com.compilex.CosmicKOTH.struct.KothWorldType;
import com.compilex.CosmicKOTH.utils.KothItemUtils;
import com.compilex.CosmicKOTH.utils.KothItemUtilsVanilla;
import com.cosmicpvp.CosmicRankQuests.CosmicRankQuestsAPI;
import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.cosmicpvp.cosmicutils.itembuilder.BoosterBuilder;
import com.cosmicpvp.cosmicutils.itembuilder.CustomItemBuilder;
import com.cosmicpvp.cosmicutils.itembuilder.CustomTitleBuilder;
import com.cosmicpvp.cosmicutils.itembuilder.EssentialsItemBuilder;
import com.cosmicpvp.cosmicutils.itembuilder.ItemBuilder;
import com.cosmicpvp.cosmicutils.itembuilder.KitGemBuilder;
import com.cosmicpvp.cosmicutils.utils.CustomItemUtils;
import com.google.common.collect.Lists;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import me.ifamasssxd.cosmicconquest.CosmicConquestAPI;
import me.ifamasssxd.killtracker.KillTrackerAPI;
import me.ifamasssxd.killtracker.listeners.Tracker;
import org.arkhamnetwork.Arkkit.patches.EquipmentLootboxes;
import org.arkhamnetwork.Arkkit.patches.EssentialsAddHomes;
import org.arkhamnetwork.Arkkit.patches.ItemID;
import org.arkhamnetwork.Arkkit.patches.ShowcaseExpander;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.arkhamnetwork.Arkkit.utils.ItemUtils;
import org.arkhamnetwork.enchantments.EnchantmentOrb;
import org.arkhamnetwork.enchantments.libs.OrbType;
import org.arkhamnetwork.enchantments.util.TieredEnchantments;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class RewardUtils {
    private static Integer cachedMapIndex;
    private static long mapIndexUpdate;
    private static List<ItemBuilder> vanillaKothRewardsTier1;
    private static List<ItemBuilder> vanillaKothRewardsTier2;
    private static List<ItemBuilder> vanillaKothRewardsTier3;

    public static ItemStack getKOTHLootbag(Player winner) {
        return ItemUtils.buildItem((Material)Material.ENDER_CHEST, (short)0, (String)(ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "CosmicPvP KOTH Lootbag"), (String[])new String[]{(Object)ChatColor.GRAY + "A legendary loot filled satchel,", (Object)ChatColor.GRAY + "only obtainable by completing an", (Object)ChatColor.GRAY + "official CosmicPvP KOTH event.", "", (Object)ChatColor.GRAY + "Right-Click to open.", "", (Object)ChatColor.GOLD + "Captured by: " + (Object)ChatColor.UNDERLINE + (winner != null ? winner.getName() : "???")});
    }

    public static boolean isKOTHLootbag(ItemStack i) {
        ItemStack lootbag = RewardUtils.getKOTHLootbag(null);
        return i.getType() == lootbag.getType() && i.hasItemMeta() && i.getItemMeta().hasDisplayName() && i.getItemMeta().getDisplayName().equals(lootbag.getItemMeta().getDisplayName()) && i.getItemMeta().hasLore() && ((String)i.getItemMeta().getLore().get(0)).equals(lootbag.getItemMeta().getLore().get(0));
    }

    public static List<ItemStack> getKOTHLoot(Player winner) {
        return RewardUtils.getKOTHLoot(winner.getName());
    }

    public static ItemStack getKothArmorItem(String playerName) {
        int index = RewardUtils.getMapIndex();
        if (index == 1) {
            return KothItemUtils.getKothHelmet(playerName);
        }
        if (index == 2) {
            return KothItemUtils.getKothChestplate(playerName);
        }
        if (index == 3) {
            return KothItemUtils.getKothLeggings(playerName);
        }
        if (index == 4) {
            return KothItemUtils.getKothBoots(playerName);
        }
        if (index == 5) {
            int rand = ThreadLocalRandom.current().nextInt(4);
            if (rand == 0) {
                return KothItemUtils.getKothHelmet(playerName);
            }
            if (rand == 1) {
                return KothItemUtils.getKothChestplate(playerName);
            }
            if (rand == 2) {
                return KothItemUtils.getKothLeggings(playerName);
            }
            if (rand == 3) {
                return KothItemUtils.getKothBoots(playerName);
            }
        }
        return null;
    }

    public static int getMapIndex() {
        if (cachedMapIndex != null && mapIndexUpdate > System.currentTimeMillis()) {
            return cachedMapIndex;
        }
        for (int i = 1; i <= KothWorldType.values().length; ++i) {
            if (!new File("koth_id_" + i).exists()) continue;
            cachedMapIndex = i;
            mapIndexUpdate = System.currentTimeMillis() + 60000L;
            return cachedMapIndex;
        }
        return 0;
    }

    public static List<ItemStack> getKOTHLoot(String winner) {
        ItemStack item;
        ItemStack mkitClaim;
        ItemStack gem;
        int i;
        ArrayList<ItemStack> rewards = new ArrayList<ItemStack>();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        rewards.add(CosmicSpaceChestsAPI.getSpaceFlare((ItemRarity)ItemRarity.GOD));
        for (i = 7; i > 0; --i) {
            rewards.add(CosmicSpaceChestsAPI.getItems((ItemRarity)ItemRarity.GOD, (int)1)[0]);
        }
        if (Math.random() < 0.3) {
            rewards.add(com.cosmicpvp.CosmicSpaceChests.utils.ItemUtils.setAmount((ItemStack)ShowcaseExpander.getShowcaseExpanderItem(), (int)(1 + new Random().nextInt(3))));
        } else {
            rewards.add(com.cosmicpvp.CosmicSpaceChests.utils.ItemUtils.setAmount((ItemStack)EssentialsAddHomes.getAddHomeItem(), (int)(1 + new Random().nextInt(2))));
        }
        rewards.add(EnchantmentOrb.getEnchantmentOrb((int)90, (int)(11 + ((Random)random).nextInt(2) + (Math.random() <= 0.2 ? 1 : 0)), (OrbType)(Math.random() < 0.35 ? OrbType.WEAPON : OrbType.ARMOR)));
        i = new Random().nextInt(7);
        if (i == 0) {
            rewards.add(com.cosmicpvp.CosmicSpaceChests.utils.ItemUtils.getTitleVoucher((String)"#KOTH"));
        } else if (i == 1) {
            rewards.add(com.cosmicpvp.CosmicSpaceChests.utils.ItemUtils.getTitleVoucher((String)"69cps"));
        } else if (i == 2) {
            rewards.add(com.cosmicpvp.CosmicSpaceChests.utils.ItemUtils.getTitleVoucher((String)"ssMeBro"));
        } else if (i == 3) {
            rewards.add(com.cosmicpvp.CosmicSpaceChests.utils.ItemUtils.getTitleVoucher((String)"VapeOP"));
        } else if (i == 4) {
            rewards.add(com.cosmicpvp.CosmicSpaceChests.utils.ItemUtils.getTitleVoucher((String)"#Cleared"));
        } else if (i == 5) {
            rewards.add(com.cosmicpvp.CosmicSpaceChests.utils.ItemUtils.getTitleVoucher((String)"#StackinForSanta"));
        } else if (Math.random() < 0.04) {
            rewards.add(com.cosmicpvp.CosmicSpaceChests.utils.ItemUtils.getTitleVoucher((String)"(\u3064\u2580\u00af\u2580)\u3064"));
        }
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta ebm = (EnchantmentStorageMeta)book.getItemMeta();
        ebm.addStoredEnchant(Enchantment.DEPTH_STRIDER, 3, true);
        book.setItemMeta((ItemMeta)ebm);
        rewards.add(book);
        if (Math.random() < 0.33) {
            rewards.add(com.cosmicpvp.CosmicSpaceChests.utils.ItemUtils.setAmount((ItemStack)DungeonsAPI.get().getEnchantedObsidian(), (int)(1 + new Random().nextInt(3))));
        }
        if (Math.random() < 0.2) {
            rewards.add(DungeonsAPI.get().getCosmonautFuel());
        }
        if (Math.random() < 0.1) {
            rewards.add(DungeonsAPI.get().getDimensionWeb());
        }
        if (Math.random() < 0.01) {
            rewards.add(DungeonsAPI.get().getDungeon(0).getDungeonPortal().clone());
        }
        if (Math.random() < 0.25) {
            rewards.add(CosmicConquestAPI.createHeroicToken((int)(2 + ThreadLocalRandom.current().nextInt(9))));
        }
        if (Math.random() <= 0.05) {
            rewards.add(DungeonsAPI.get().getTeleportationCrystal().clone());
        }
        if (Math.random() <= 0.1) {
            rewards.add(CustomItemUtils.createRandomEvolutionKitGem());
        }
        if (Math.random() <= 0.03) {
            rewards.add(ItemID.removeItemID((ItemStack)com.cosmicpvp.CosmicSpaceChests.utils.ItemUtils.setAmount((ItemStack)TieredEnchantments.getMysteryEnchantmentBook((int)7), (int)1)));
        }
        if (Math.random() <= 0.02) {
            rewards.add(KillTrackerAPI.buildTracker((Tracker)Tracker.Godly));
        }
        if (Math.random() <= 0.04) {
            rewards.add(new me.ifamasssxd.cosmicskycustoms.struct.CustomItemBuilder(Math.random() < 0.7 ? "wildcard_gkit" : "wildcard_vkit", new Object[0]).build());
        }
        if (Math.random() <= 0.15) {
            rewards.add(new me.ifamasssxd.cosmicskycustoms.struct.CustomItemBuilder("mystery_fallen_hero", new Object[0]).amount(1, 2).build());
        }
        if (Math.random() <= 0.01) {
            rewards.add(new me.ifamasssxd.cosmicskycustoms.struct.CustomItemBuilder("mystery_lootbox", new Object[0]).build());
        }
        if (Math.random() <= 0.25 && (gem = new KitGemBuilder(false, true, true).build()) != null) {
            rewards.add(gem);
        }
        if (Math.random() <= 0.15 && (mkitClaim = new KitGemBuilder(Math.random() <= 0.4 ? "NECROMANCER" : (Math.random() < 0.6 ? "DEATH_KNIGHT" : "GHOST"), false, true).build()) != null) {
            rewards.add(mkitClaim);
        }
        if (Math.random() <= 0.075 && (item = new CustomItemBuilder(CustomItemBuilder.CustomItemType.RANDOM_MASK, 1).build()) != null) {
            rewards.add(item);
        }
        return rewards;
    }

    public static List<ItemStack> getVanillaKOTHLoot(Player winner) {
        int i;
        ArrayList<ItemStack> rewards = new ArrayList<ItemStack>();
        rewards.add(KothItemUtilsVanilla.getRandomKothItem(winner));
        for (i = 6; i > 0; --i) {
            int tier = ThreadLocalRandom.current().nextInt(3) + 1;
            if (tier == 2 && Math.random() <= 0.01) {
                rewards.addAll(KothItemUtilsVanilla.getArcherSet());
                continue;
            }
            if (tier == 3 && Math.random() <= 0.01) {
                rewards.addAll(KothItemUtilsVanilla.getBardSet());
                continue;
            }
            if (tier == 3 && Math.random() <= 1.0E-4) {
                rewards.add(new CustomTitleBuilder("(/\u00b0^\u00b0)/L").build());
                continue;
            }
            rewards.add(RewardUtils.getRandomVanillaKOTHItem(tier));
        }
        i = new Random().nextInt(5);
        for (ItemStack item : rewards) {
            ItemMeta im = item.getItemMeta();
            if (!im.hasLore()) continue;
            boolean modified = false;
            List lore = im.getLore();
            for (int index = 0; index < lore.size(); ++index) {
                String line = (String)lore.get(index);
                if (winner != null && line.contains("{PLAYER}")) {
                    lore.set(index, line.replace("{PLAYER}", winner.getName()));
                    modified = true;
                    continue;
                }
                if (!line.contains("{DATE}")) continue;
                lore.set(index, line.replace("{DATE}", KothItemUtils.format.format(new Date())));
                modified = true;
            }
            if (!modified) continue;
            im.setLore(lore);
            item.setItemMeta(im);
        }
        return rewards;
    }

    private static ItemBuilder build(ItemStack original) {
        return new ItemBuilder(original);
    }

    public static ItemStack getRandomVanillaKOTHItem(int tier) {
        if (vanillaKothRewardsTier1.isEmpty()) {
            vanillaKothRewardsTier1.add(RewardUtils.build(RewardUtils.getVanillaEnchantmentBook(Enchantment.LOOT_BONUS_MOBS, 3)));
            vanillaKothRewardsTier1.add(RewardUtils.build(RewardUtils.setAmount(CreeperTypes.getCCItem((CreeperTypes)CreeperTypes.Lucky), 3)));
            vanillaKothRewardsTier1.add(new ItemBuilder(CreeperTypes.getCCItem((CreeperTypes)CreeperTypes.Lucky)));
            vanillaKothRewardsTier1.add(RewardUtils.build(new ItemStack(Material.MONSTER_EGG, 32, EntityType.CREEPER.getTypeId())));
            vanillaKothRewardsTier1.add((ItemBuilder)new EssentialsItemBuilder("373:16425 16 effect:strength power:2 duration:134 name:&f&k;_&c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH_&f&k;_&4&lStrength_II_Ext. lore:&7Please_mom,_just_a_little_bit_longer!"));
            vanillaKothRewardsTier1.add((ItemBuilder)new CustomTitleBuilder("#Cleared"));
        }
        if (vanillaKothRewardsTier2.isEmpty()) {
            vanillaKothRewardsTier2.add((ItemBuilder)new BoosterBuilder("EXP", 1800, 2.0));
            vanillaKothRewardsTier2.add((ItemBuilder)new BoosterBuilder("MCMMO", 1800, 2.0));
            vanillaKothRewardsTier2.add((ItemBuilder)new BoosterBuilder("EXP", 900, 5.0));
            vanillaKothRewardsTier2.add(new ItemBuilder(RewardUtils.getVanillaEnchantmentBook(Enchantment.DURABILITY, 4)));
            vanillaKothRewardsTier2.add(new ItemBuilder(RewardUtils.getVanillaEnchantmentBook(Enchantment.DURABILITY, 3)).setAmount(2));
            vanillaKothRewardsTier2.add(new ItemBuilder(RewardUtils.getVanillaEnchantmentBook(Enchantment.LOOT_BONUS_MOBS, 4)));
            vanillaKothRewardsTier2.add(new ItemBuilder(RewardUtils.getVanillaEnchantmentBook(Enchantment.LOOT_BONUS_MOBS, 5)));
            vanillaKothRewardsTier2.add((ItemBuilder)new CustomItemBuilder(CustomItemBuilder.CustomItemType.MYSTERY_MOBSPAWNER, 2));
            vanillaKothRewardsTier2.add(new ItemBuilder(RewardUtils.getVanillaEnchantmentBook(Enchantment.DEPTH_STRIDER, 3)).setAmount(2));
            vanillaKothRewardsTier2.add((ItemBuilder)new EssentialsItemBuilder("373:16448 16 effect:speed power:2 duration:134 name:&f&k;_&c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH_&f&k;_&b&l Speed_Ext. lore:&7What_a_&5&lmagical_&7splash!"));
            vanillaKothRewardsTier2.add((ItemBuilder)new EssentialsItemBuilder("373:8233 16 effect:strength power:2 duration:180 name:&f&k;_&c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH_&f&k;_&4&l Strength_II_Ext. lore:&7Please_mom,_just_a_little_bit_longer!"));
            vanillaKothRewardsTier2.add((ItemBuilder)new EssentialsItemBuilder("373:8259 16 effect:fireresist power:1 duration:720 name:&f&k;_&c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH_&f&k;_&c&l Fire_Resistance_Ext."));
            vanillaKothRewardsTier2.add((ItemBuilder)new EssentialsItemBuilder("373:5408 9 effect:healthboost power:1 duration:180 name:&f&k;_&c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH_&f&k;_&e&l Health_Boost_Ext."));
            vanillaKothRewardsTier2.add((ItemBuilder)new EssentialsItemBuilder("373:5408 9 effect:absorption power:1 duration:180 name:&f&k;_&c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH_&f&k;_&6&l Absorption_Ext."));
            vanillaKothRewardsTier2.add((ItemBuilder)new EssentialsItemBuilder("373:5408 9 effect:resistance power:1 duration:44 name:&f&k;_&c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH_&f&k;_&8&l Resistance_Ext."));
            vanillaKothRewardsTier2.add((ItemBuilder)new CustomTitleBuilder("Unlucky"));
            vanillaKothRewardsTier2.add((ItemBuilder)new CustomTitleBuilder("Oof"));
        }
        if (vanillaKothRewardsTier3.isEmpty()) {
            ItemStack rankQuest1 = CosmicRankQuestsAPI.getRankQuestItem((int)1);
            ItemStack rankQuest2 = CosmicRankQuestsAPI.getRankQuestItem((int)2);
            ItemStack rankQuest3 = CosmicRankQuestsAPI.getRankQuestItem((int)3);
            if (rankQuest1 != null) {
                vanillaKothRewardsTier3.add(new ItemBuilder(rankQuest1));
            }
            if (rankQuest2 != null) {
                vanillaKothRewardsTier3.add(new ItemBuilder(rankQuest2));
            }
            if (rankQuest3 != null) {
                vanillaKothRewardsTier3.add(new ItemBuilder(rankQuest3));
            }

            vanillaKothRewardsTier3.add((ItemBuilder)new EssentialsItemBuilder("373:5408 9 effect:healthboost power:2 duration:90 name:&f&k;_&c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH_&f&k;_&e&lHealth_Boost_II_Ext."));
            vanillaKothRewardsTier3.add((ItemBuilder)new EssentialsItemBuilder("373:5408 9 effect:healthboost power:2 duration:90 name:&f&k;_&c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH_&f&k;_&6&lAbsorption_II_Ext."));
            vanillaKothRewardsTier3.add((ItemBuilder)new EssentialsItemBuilder("373:5408 9 effect:healthboost power:2 duration:90 name:&f&k;_&c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH_&f&k;_&6&lAbsorption_II_Ext."));
            vanillaKothRewardsTier3.add((ItemBuilder)new EssentialsItemBuilder("373:5408 9 effect:resistance power:2 duration:22 name:&f&k;_&c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH_&f&k;_&8&lResistance_II_Ext."));
        }
        List<ItemBuilder> toCheck = tier <= 1 ? vanillaKothRewardsTier1 : (tier == 2 ? vanillaKothRewardsTier2 : (tier == 3 ? vanillaKothRewardsTier3 : vanillaKothRewardsTier1));
        return toCheck.get(ThreadLocalRandom.current().nextInt(toCheck.size())).build().clone();
    }

    private static ItemStack getMobSpawner(EntityType entityType) {
        return ItemUtils.buildItem((Material)Material.MOB_SPAWNER, (short)entityType.getTypeId(), (String)(ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + RewardUtils.getEntityName(entityType) + ChatColor.GOLD.toString() + " Spawner"), (String[])new String[]{(Object)ChatColor.GRAY + "Be careful where you place this!"});
    }

    private static String getEntityName(EntityType et) {
        if (et == EntityType.IRON_GOLEM) {
            return "Iron Golem";
        }
        if (et == EntityType.MAGMA_CUBE) {
            return "Magma Cube";
        }
        return et.getName();
    }

    private static ItemStack setAmount(ItemStack i, int amount) {
        i.setAmount(amount);
        return i;
    }

    private static ItemStack getVanillaEnchantmentBook(Enchantment ench, int level) {
        ItemStack i = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta esm = (EnchantmentStorageMeta)i.getItemMeta();
        esm.addStoredEnchant(ench, level, true);
        i.setItemMeta((ItemMeta)esm);
        return i;
    }

    static {
        vanillaKothRewardsTier1 = Lists.newArrayList();
        vanillaKothRewardsTier2 = Lists.newArrayList();
        vanillaKothRewardsTier3 = Lists.newArrayList();
    }
}

