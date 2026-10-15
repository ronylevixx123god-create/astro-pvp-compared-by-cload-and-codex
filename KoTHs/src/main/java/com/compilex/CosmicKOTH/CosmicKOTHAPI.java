package com.compilex.CosmicKOTH;

import com.compilex.CosmicArmorSets.enums.HeroicWeaponType;
import com.compilex.CosmicKOTH.customfiles.CustomConfig;
import com.compilex.CosmicKOTH.tasks.KothEventTask;
import com.compilex.CosmicKOTH.utils.KothItemUtils;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.*;

import com.google.common.collect.Maps;
import com.massivecraft.factions.Faction;
import net.minecraft.server.v1_8_R3.NBTBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class CosmicKOTHAPI {

    public static Map<UUID, Integer> playerLivesUsed = new HashMap<>();

    public static boolean isKOTHRunning() {
        return (CosmicKOTHPlugin.get_().getKoth() != null && CosmicKOTHPlugin.get_().getKoth().isRunning());
    }

    public static boolean canTeleportToKOTH() {
        return (CosmicKOTHPlugin.get_().getKoth() != null && !CosmicKOTHPlugin.get_().getKoth().isCapped());
    }

    public static ItemStack applyKOTHWeaponSetBonus(ItemStack item, String winner, HeroicWeaponType weaponType, String ability) {
        ItemMeta im = item.getItemMeta();
        List<String> lore = im.getLore();
        if (lore == null) {
            lore = Lists.newArrayList();
        }
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes((char) '&', (String) ("&c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH " + CC.WhiteB + "WEAPON BONUS")));
        lore.add(CC.WhiteB + " * " + CC.White + ability);
        lore.add(CC.GrayI + "(Requires all 4 koth armor items)");
        lore.add("");
        lore.add(CC.AquaB + "(!) " + (Object) ChatColor.AQUA + "Claimed by " + CC.LightPurpleB + winner + (Object) ChatColor.AQUA + " on " + CC.LightPurpleU + KothItemUtils.format.format(new Date()));
        im.setLore(lore);
        item.setItemMeta(im);
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder builder = wrapper.getCompoundOrCreate("conaxData");
        builder.setString("KOTH_WEAPON", weaponType.name());
        wrapper.setNBTBase("conaxData", (NBTBase) builder.get());
        return wrapper.build();
    }

    public static void resetKOTH() {
        int oldTaskId;
        playerLivesUsed.clear();
        if (CosmicKOTHPlugin.get_().getKoth() != null && (oldTaskId = CosmicKOTHPlugin.get_().getKoth().getTaskId()) != -1) {
            Bukkit.getLogger().info("[CosmicKOTH] Resetting KOTH, cancelling task ID #" + oldTaskId);
            Bukkit.getScheduler().cancelTask(oldTaskId);
        }
        KothEventTask KET = new KothEventTask();
        KET.running = true;
        KET.setTaskId(Bukkit.getScheduler().runTaskTimer((Plugin) CosmicKOTHPlugin.get_(), (Runnable) KET, 20L, 20L).getTaskId());
        CosmicKOTHPlugin.get_().setKoth(KET);
        Bukkit.getLogger().info("[CosmicKOTH] Launched new KOTH with task ID #" + KET.getTaskId());
    }

    public static int getFactionKothWins(Faction faction) throws IOException {
        String factionId = faction.getId();
        return CosmicKOTHPlugin.get_().getKothWins().getOrDefault(factionId, 0);

    }

}
