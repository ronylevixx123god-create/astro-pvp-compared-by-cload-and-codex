/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.NBTWrapper
 *  com.google.common.collect.Lists
 *  net.minecraft.server.v1_7_R4.NBTBase
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 */
package net.astropvp.core.utils;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.google.common.collect.Lists;
import java.util.LinkedList;
import java.util.List;

import net.astropvp.core.AstroCore;
import net.minecraft.server.v1_8_R3.NBTBase;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HolyWhiteScrollUtils {
    private static String corruptLoreLine = CC.RedB + "CORRUPT (" + CC.Gray + "cannot be protected" + CC.RedB + ")";

    public static int getHolyScrollsApplied(NBTWrapper wrapper) {
        NBTTagCompoundBuilder data = wrapper.getCompoundOrCreate("conaxData");
        return data.getInt("holyScrollsApplied");
    }

    public static boolean isCorrupt(NBTWrapper wrapper) {
        NBTTagCompoundBuilder data = wrapper.getCompoundOrCreate("conaxData");
        return data.getBoolean("corrupted");
    }

    public static ItemStack applyCorruptionToItem(ItemStack item) {
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder builder = wrapper.getCompoundOrCreate("conaxData");
        builder.setBoolean("corrupted", Boolean.TRUE);
        wrapper.setNBTBase("conaxData", (NBTBase)builder.get());
        item = wrapper.build();
        ItemMeta im = item.getItemMeta();
        List lore = im.getLore();
        if (lore == null) {
            lore = Lists.newArrayList();
        }
        if (!lore.contains(corruptLoreLine)) {
            lore.add(corruptLoreLine);
        }
        im.setLore(lore);
        item.setItemMeta(im);
        return item;
    }

    public static NBTWrapper setHolyWhiteScrollsApplied(NBTWrapper wrapper, int applied) {
        NBTTagCompoundBuilder data = wrapper.getCompoundOrCreate("conaxData");
        data.setInt("holyScrollsApplied", Integer.valueOf(applied));
        wrapper.setNBTBase("conaxData", (NBTBase)data.get());
        return wrapper;
    }

    public static void removeCorrupt(ItemStack i, NBTWrapper wrapper) {
        List<String> lore = i.getItemMeta().getLore();
        NBTTagCompoundBuilder data = wrapper.getCompoundOrCreate("conaxData");

        LinkedList<String> newLore = new LinkedList<>();
        boolean found = false;
        for (String s : lore) {
            if (s.startsWith(CC.RedB + "CORRUPT")) {
                found = true;
            } else {
                newLore.add(s);
            }
        }
        if (found) {
            ItemMeta im = i.getItemMeta();
            im.setLore(newLore);
            i.setItemMeta(im);
            data.setBoolean("corrupted", Boolean.FALSE);
            data.remove("corrupted");
            wrapper.setNBTBase("conaxData", data.get());

        }
    }

    public static void applyHolyWhitescroll(ItemStack i, int applied, int max, boolean fart) {
        List<String> lore = i.getItemMeta().getLore();
        LinkedList<String> newLore = new LinkedList<>();
        boolean found = false;
        String newCorrupt = CC.RedB + "SEMI CORRUPT (" + CC.Gray + applied + CC.Red + "/" + CC.Gray + max + " holy whitescrolls applied" + CC.RedB + ")";
        for (String s : lore) {
            if (s.equals(ChatColor.WHITE + "" + ChatColor.BOLD + "PROTECTED") && fart) {
                newLore.add(ChatColor.WHITE.toString() + ChatColor.BOLD + "PROTECTED (" + ChatColor.GOLD + ChatColor.BOLD + "*HOLY*" + ChatColor.WHITE + ChatColor.BOLD + ")");
                continue;
            }
            if (s.startsWith(CC.RedB + "SEMI CORRUPT")) {
                newLore.add(newCorrupt);
                found = true;
                continue;
            }
            newLore.add(s);
        }
        if (!found)
            newLore.add(newCorrupt);
        ItemMeta im = i.getItemMeta();
        im.setLore(newLore);
        i.setItemMeta(im);
    }

    public static ItemStack removeHolyWhitescroll(ItemStack i) {
        NBTWrapper wrapper = new NBTWrapper(i);
        int applied = getHolyScrollsApplied(wrapper);
        boolean turnCorrupted = (applied >= getMaxHolyAllowed(i));
        List<String> lore = i.getItemMeta().getLore();
        LinkedList<String> newLore = new LinkedList<>();
        for (String s : lore) {
            if (s.equals(CC.WhiteB + "PROTECTED (" + CC.GoldB + "*HOLY*" + CC.WhiteB + ")")) {
                if (!turnCorrupted)
                    continue;
                if (newLore.contains(corruptLoreLine))
                    continue;
                newLore.add(corruptLoreLine);
                continue;
            }
            if (turnCorrupted && s.startsWith(CC.RedB + "SEMI CORRUPT"))
                continue;
            newLore.add(s);
        }
        if (turnCorrupted && !newLore.contains(corruptLoreLine))
            newLore.add(corruptLoreLine);
        ItemMeta im = i.getItemMeta();
        im.setLore(newLore);
        i.setItemMeta(im);
        if (turnCorrupted)
            i = applyCorruptionToItem(i);
        return i;
    }

    public static boolean itemHasHolyWhitescroll(ItemStack i) {
        if (i != null && i.hasItemMeta() && i.getItemMeta().hasLore()) {
            for (String s : i.getItemMeta().getLore()) {
                if (!s.equals(ChatColor.WHITE.toString() + (Object)ChatColor.BOLD + "PROTECTED (" + (Object)ChatColor.GOLD + (Object)ChatColor.BOLD + "*HOLY*" + (Object)ChatColor.WHITE + (Object)ChatColor.BOLD + ")")) continue;
                return true;
            }
        }
        return false;
    }

    public static boolean isHolyWhiteScroll(ItemStack i) {
        return AstroCore.getInstance().getHolyWhitescrollItem().isItem(i);
    }

    public static boolean isOldHolyWhiteScroll(ItemStack i) {
        return i != null && i.hasItemMeta() && i.getType() == Material.PAPER && i.getItemMeta().hasDisplayName() && i.getItemMeta().getDisplayName().equals(AstroCore.getHolyWhiteScroll().getItemMeta().getDisplayName()) && i.getItemMeta().hasLore() && ((String)i.getItemMeta().getLore().get(0)).equals(AstroCore.getHolyWhiteScroll().getItemMeta().getLore().get(0));
    }

    public static int getMaxHolyAllowed(ItemStack item) {
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder cosmicData = wrapper.getCompoundOrCreate("conaxData");
        return cosmicData.hasKey("holyWater") ? 4 : 3;
    }
}

