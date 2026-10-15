package com.bryside.astroattributes.enums;

import com.bryside.astroattributes.objects.Rune;
import com.bryside.astroattributes.runes.*;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import net.minecraft.server.v1_8_R3.NBTBase;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public enum RuneType {
    ATLAS("ATLAS", 0, new AtlasRune()),
    OURANOS("OURANOS", 1, new OuranosRune()),
    EOS("EOS", 2, new EosRune()),
    PROMETHEUS("PROMETHEUS", 3, new PrometheusRune()),
    KRONOS("KRONOS", 4, new KronosRune());


    private Rune rune;

    RuneType(String s, int n, Rune rune) {
        this.rune = rune;
    }

    public static RuneType getType(ItemStack item) {
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            String display = item.getItemMeta().getDisplayName();
            Material material = item.getType();
            RuneType[] values;
            int length = (values = values()).length;

            for (int j = 0; j < length; ++j) {
                RuneType type = values[j];
                Rune rune = type.getRune();

                for (int i = 1; i <= rune.getMaxLevel(); ++i) {
                    if (rune.getDisplayName().contentEquals(display) && material == rune.getMaterial()) {
                        return type;
                    }
                }
            }

            return null;
        } else {
            return null;
        }
    }

    public static int getLevelOfRune(ItemStack item) {
        if (getType(item) == null) {
            return -1;
        } else {
            ItemMeta itemMeta = item.getItemMeta();
            List lore = itemMeta.getLore();
            Iterator var3 = lore.iterator();

            String l;
            do {
                if (!var3.hasNext()) {
                    return -1;
                }

                l = (String) var3.next();
            } while ((!l.contains("Level:")));

            return Integer.valueOf(l.substring(l.indexOf(":") + 4, l.length()));
        }
    }

    public static int getSuccessRateOfRune(ItemStack item) {
        if (getType(item) == null) {
            return -1;
        } else {
            ItemMeta itemMeta = item.getItemMeta();
            List lore = itemMeta.getLore();
            Iterator var3 = lore.iterator();

            String l;
            do {
                if (!var3.hasNext()) {
                    return -1;
                }

                l = (String) var3.next();
            } while ((!l.contains("Apply:")));

            return Integer.parseInt(l.substring(l.indexOf(":") + 4, l.indexOf("%")));
        }
    }

    public Rune getRune() {
        return this.rune;
    }

    public ItemStack apply(ItemStack item, RuneType type, int level) {
        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();
        lore.add(CC.RedB + "TITAN ATTRIBUTE: " + CC.DarkGrayB + name() + " " + CC.RedB + "(" + CC.DarkGray + "Level " + level + CC.RedB + ")");
        lore.add(CC.RedB + "TITAN XP: " + CC.DarkGrayB + "0 / " + CC.Red + type.getRune().getExpForLevel(level));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder compoundBuilder = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
        compoundBuilder.setString("type", type.name()).setInt("durability", 3).setDouble("dmgIncr", 3.0).setInt("maxDurability", 3).get().setInt("heroic_weapon", 1);
        item = wrapper.setNBTBase("cosmicData", (NBTBase) compoundBuilder.get()).build();
        item = wrapper.setNBTBase("cosmicData", (NBTBase) (new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData"))).setInt("titanAttributeXp", 0).setString("titanAttributeType", type.name()).setLong("titanAttributeLastLevelup", Long.valueOf(0L)).get()).build();
        return item;
    }

    public ItemStack applyHeroic(ItemStack item, RuneType type, int level) {
        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();
        lore.add(CC.LightPurpleB + "TITAN ATTRIBUTE: " + CC.DarkGrayB + name() + " " + CC.LightPurpleB + "(" + CC.DarkGray + "Level " + level + CC.LightPurpleB + ")");
        lore.add(CC.LightPurpleB + "TITAN XP: " + CC.DarkGrayB + "0 / " + CC.LightPurple + type.getRune().getExpForLevel(level));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder compoundBuilder = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
        compoundBuilder.setString("type", type.name()).setInt("durability", Integer.valueOf(3)).setDouble("dmgIncr", Double.valueOf(3)).setInt("maxDurability", Integer.valueOf(3)).setInt("heroic_weapon", 1).get();
        item = wrapper.setNBTBase("cosmicData", (NBTBase) compoundBuilder.get()).build();
        item = wrapper.setNBTBase("cosmicData", (NBTBase) (new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData"))).setInt("titanAttributeXp", Integer.valueOf(0)).setString("titanAttributeType", type.name()).setLong("titanAttributeLastLevelup", Long.valueOf(0L)).get()).build();
        return item;
    }
}
