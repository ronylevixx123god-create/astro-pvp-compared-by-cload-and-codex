/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.arkhamnetwork.Arkkit.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;

public class ExpOrbs
        extends ArkPlugin {
    public ExpOrbs(Plugin p) {
        super(p);
    }

    public static ItemStack getXpOrb(int xpAmount, String enchanter) {
        return ItemUtils.buildItem(Material.SLIME_BALL, (short) 0, ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "Experience Orb" + ChatColor.GRAY + " (Right Click)", ChatColor.YELLOW + "Value " + ChatColor.GRAY + xpAmount + " XP", ChatColor.YELLOW + "Summoner " + ChatColor.GRAY + enchanter);
    }

    public static boolean isXpBottle(ItemStack i) {
        return i != null && i.getType() == Material.SLIME_BALL && i.hasItemMeta() && i.getItemMeta().hasLore() && i.getItemMeta().getLore().get(0).startsWith(ChatColor.YELLOW + "Value ");
    }

    public static int getXpAmount(ItemStack xpBottle) {
        try {
            return Integer.parseInt(ChatColor.stripColor(xpBottle.getItemMeta().getLore().get(0).split(" ")[1].replace("xp", "")));
        } catch (Exception err) {
            return 0;
        }
    }

    @Override
    public void enable() {
        this.registerListener(new ExpOrbsListener());
        this.registerCommand(new ExpOrbsCommand("xporb", "/<command>", "Converts your vanilla levels into an XP Orb.", new ArrayList<String>(Arrays.asList("exporb", "withdrawxp"))));
    }

    @Override
    public void disable() {
        if (!Arkkit.get().shutting_down) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.removeMetadata("customXpOrbValue", Arkkit.get());
            }
        }
    }
}

