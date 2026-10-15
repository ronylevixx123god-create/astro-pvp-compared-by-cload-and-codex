/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.arkhamnetwork.Arkkit.patches.ItemID
 *  org.arkhamnetwork.Arkkit.plugins.ArkPlugins
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.ifamasssxd.cosmicguishop;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import me.ifamasssxd.cosmicguishop.utils.ItemUtils;
import me.ifamasssxd.cosmicguishop.utils.NumberUtils;
import org.arkhamnetwork.Arkkit.patches.ItemID;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ArkhamMoneyNotesAPI {
    public static final double MAX_CASH = 1.0E15;

    public static ItemStack getMoneyNote(String signer, long amount) {
        ItemStack i = ItemUtils.buildItem(Material.PAPER, (short)0, String.valueOf(ChatColor.AQUA.toString()) + ChatColor.BOLD.toString() + "Astro Note" + (Object)ChatColor.GRAY + " (Right Click)", String.valueOf(ChatColor.LIGHT_PURPLE.toString()) + "Value " + (Object)ChatColor.WHITE + "$" + ArkhamMoneyNotesAPI.insertCommas(NumberUtils.formatMoney(amount)), (Object)ChatColor.LIGHT_PURPLE + "Signer " + (Object)ChatColor.GRAY + signer);
        if (ArkPlugins.ITEM_ID.getArkPlugin().isRunning()) {
            ItemID.setItemID((ItemStack)i, (long)ItemID.generateNewItemID());
        }
        return i;
    }

    public static ItemStack getMoneyNoteNoIID(String signer, long amount) {
        ItemStack i = ItemUtils.buildItem(Material.PAPER, (short)0, String.valueOf(ChatColor.AQUA.toString()) + ChatColor.BOLD.toString() + "Astro Note" + (Object)ChatColor.GRAY + " (Right Click)", String.valueOf(ChatColor.LIGHT_PURPLE.toString()) + "Value " + (Object)ChatColor.WHITE + "$" + ArkhamMoneyNotesAPI.insertCommas(NumberUtils.formatMoney(amount)), (Object)ChatColor.LIGHT_PURPLE + "Signer " + (Object)ChatColor.GRAY + signer);
        return i;
    }

    public static long getMoneyNoteVal(ItemStack is) {
        if (!ArkhamMoneyNotesAPI.isMoneyNote(is)) {
            return 0L;
        }
        return ArkhamMoneyNotesAPI.getMoneyValue(((String)is.getItemMeta().getLore().get(0)).split(" ")[1]);
    }

    public static long getMoneyValue(String formatted_value) {
        String numb_val = ChatColor.stripColor((String)formatted_value.replaceAll(",", "").replace("$", "")).toLowerCase();
        numb_val = numb_val.replace("k", "000");
        numb_val = numb_val.replace("million", "000000");
        numb_val = numb_val.replace("billion", "000000000");
        numb_val = numb_val.replace("trillion", "000000000000");
        numb_val = numb_val.replace("mil", "000000");
        numb_val = numb_val.replace("bil", "000000000");
        numb_val = numb_val.replace("tril", "000000000000");
        numb_val = numb_val.replace("m", "000000");
        numb_val = numb_val.replace("b", "000000000");
        numb_val = numb_val.replace("t", "000000000000");
        return Long.parseLong(numb_val);
    }

    public static boolean isMoneyNote(ItemStack is) {
        return is != null && is.getType() == Material.PAPER && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().hasLore() && ((String)is.getItemMeta().getLore().get(0)).contains(String.valueOf(ChatColor.LIGHT_PURPLE.toString()) + "Value");
    }

    public static long getAllMoneyNoteValues(Player pl) {
        long val = 0L;
        for (ItemStack is : pl.getInventory()) {
            if (!ArkhamMoneyNotesAPI.isMoneyNote(is)) continue;
            val += ArkhamMoneyNotesAPI.getMoneyNoteVal(is);
        }
        return val;
    }

    private static String insertCommas(BigDecimal number) {
        DecimalFormat df = new DecimalFormat("#,##0");
        return df.format(number);
    }

    private static String insertCommas(String number) {
        try {
            return ArkhamMoneyNotesAPI.insertCommas(new BigDecimal(number));
        }
        catch (Exception err) {
            return number;
        }
    }
}

