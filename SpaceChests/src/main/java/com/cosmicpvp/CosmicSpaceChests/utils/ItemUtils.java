package com.cosmicpvp.CosmicSpaceChests.utils;

import com.compilex.CosmicCustomCreepers.enums.CreeperTypes;
import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.cosmicpvp.CosmicSpaceChests.libs.RankItems;
import com.cosmicpvp.CosmicSpaceChests.libs.TitleItems;
import com.cosmicpvp.CosmicSpaceChests.listeners.EnvoySummonItemListener;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import me.ifamasssxd.cosmicbosses.utils.BossEggUtils;
import me.ifamasssxd.cosmicguishop.ArkhamMoneyNotesAPI;
import org.arkhamnetwork.Arkkit.patches.ItemNametag;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.arkhamnetwork.enchantments.util.TieredEnchantments;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Random;

public class ItemUtils {
    public static ItemStack buildItem(Material m, short metaData, String name, String ... description) {
        ItemStack is = new ItemStack(m, 1, metaData);
        ItemMeta im = is.getItemMeta();
        if (name != null) {
            im.setDisplayName(name);
        }
        if (description != null && description.length > 0) {
            im.setLore(Arrays.asList(description));
        }
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack getXpBottle(int xpAmount, String enchanter) {
        return ItemUtils.buildItem(Material.EXP_BOTTLE, (short)0, ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "Experience Bottle" + (Object)ChatColor.GRAY + " (Throw)", (Object)ChatColor.LIGHT_PURPLE + "Value " + (Object)ChatColor.WHITE + xpAmount + " XP", (Object)ChatColor.LIGHT_PURPLE + "Enchanter " + (Object)ChatColor.WHITE + enchanter);
    }

    public static ItemStack getMoneyNote(String signer, int amount) {
        return ArkhamMoneyNotesAPI.getMoneyNote((String)signer, (long)amount);
    }

    public static ItemStack getMoneyNoteNoIID(String string, int i) {
        return ArkhamMoneyNotesAPI.getMoneyNoteNoIID((String)string, (long)i);
    }

    public static ItemStack getEnchantmentBook(CustomEnchantment ce) {
        return EnchantmentBook.getEnchantmentBook((CustomEnchantment)ce, (int)(new Random().nextInt(ce.getMaxLevel()) + 1));
    }

    public static ItemStack getEnchantmentBook(CustomEnchantment ce, int level) {
        return EnchantmentBook.getEnchantmentBook((CustomEnchantment)ce, (int)level);
    }

    public static ItemStack getRandomEnchantmentBook() {
        CustomEnchantment ce = EnchantmentAPI.getEnchantments().toArray(new CustomEnchantment[EnchantmentAPI.getEnchantments().size()])[new Random().nextInt(EnchantmentAPI.getEnchantments().size())];
        return EnchantmentBook.getEnchantmentBook((CustomEnchantment)ce, (int)(new Random().nextInt(ce.getMaxLevel()) + 1));
    }

    public static ItemStack getMysterySoulEnchantmentBook() {
        return TieredEnchantments.getMysteryEnchantmentBook((int)6);
    }

    public static ItemStack getEnchantmentBook(ItemRarity ir) {
        return TieredEnchantments.getMysteryEnchantmentBook((int)CosmicSpaceChestsAPI.getItemRarity(ir));
    }

    public static ItemStack getMysteryDust(ItemRarity ir, int successIncrease) {
        return EnchantmentBook.getMagicDust((ItemRarity)ir, (int)successIncrease);
    }

    public static ItemStack getEnchantmentBook(CustomEnchantment ce, int level, int success, int failure) {
        return EnchantmentBook.getEnchantmentBook((CustomEnchantment)ce, (int)level, (int)success, (int)failure);
    }

    public static ItemStack getEnvoySummoner() {
        return EnvoySummonItemListener.buildEnvoySpawnItem();
    }

    public static ItemStack getWhitescroll() {
        return EnchantmentBook.getWhiteScroll();
    }

    public static ItemStack getBlackscroll() {
        return EnchantmentBook.getBlackScroll();
    }

    public static ItemStack getBlackscroll(int successRate) {
        return EnchantmentBook.getBlackScroll((int)successRate);
    }

    public static ItemStack getTitleVoucher(String title) {
        return TitleItems.getTitleItem(title);
    }


    public static ItemStack getMobSpawner(EntityType entityType) {
        return ItemUtils.buildItem(Material.MOB_SPAWNER, entityType.getTypeId(), ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + ItemUtils.getEntityName(entityType) + ChatColor.GOLD.toString() + " Spawner", new String[0]);
    }

    public static ItemStack getRankItem(ItemRarity ir) {
        return RankItems.getRankItem(RankItems.getRank(ItemUtils.getItemRarityID(ir)));
    }

    public static ItemStack getDepthStriderBook(int level) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta ebm = (EnchantmentStorageMeta)book.getItemMeta();
        ebm.addStoredEnchant(Enchantment.DEPTH_STRIDER, level, true);
        book.setItemMeta((ItemMeta)ebm);
        return book;
    }


    public static ItemStack getCustomCreeperEgg(CreeperTypes creeperType) {
        return CreeperTypes.getCCItem((CreeperTypes)creeperType);
    }

    public static ItemStack setAmount(ItemStack i, int amount) {
        i.setAmount(amount);
        return i;
    }

    public static ItemStack getLootCrate(ItemRarity ir) {
        return CosmicSpaceChestsAPI.getDropPackageChest(ir);
    }

    public static ItemStack getTransmogScroll() {
        return EnchantmentBook.getTransmogScroll();
    }

    public static ItemStack getItemNametag() {
        return ItemNametag.getNametagItem();
    }

    public static ItemStack getNewBossEgg(BossType bossType) {
        return BossEggUtils.getBossEgg((BossType)bossType);
    }

    public static String getEntityName(EntityType et) {
        if (et == EntityType.IRON_GOLEM) {
            return "Iron Golem";
        }
        if (et == EntityType.MAGMA_CUBE) {
            return "Magma Cube";
        }
        return et.getName();
    }

    public static int getItemRarityID(ItemRarity ir) {
        if (ir == ItemRarity.SIMPLE) {
            return 1;
        }
        if (ir == ItemRarity.UNIQUE) {
            return 2;
        }
        if (ir == ItemRarity.ELITE) {
            return 3;
        }
        if (ir == ItemRarity.ULTIMATE) {
            return 4;
        }
        if (ir == ItemRarity.LEGENDARY) {
            return 5;
        }
        return 1;
    }

    public static void giveItemToPlayer(Player p, ItemStack i) {
        if (p.getInventory().firstEmpty() != -1) {
            p.getInventory().addItem(new ItemStack[]{i});
        } else {
            Bukkit.getLogger().info("[CosmicDropPackages] " + p.getName() + " did not have inventory space for " + (Object)i + ", dropping on ground!");
            p.getWorld().dropItem(p.getLocation(), i);
        }
    }
}

