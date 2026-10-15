/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  me.ifamasssxd.killtracker.KillTrackerAPI
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.ProjectileLaunchEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.enchantments.enchantments.bow;

import com.rit.sucy.CustomEnchantment;
import de.inventivegames.particle.ParticleEffect;
import net.astropvp.armorsets.AstroArmorSetsAPI;
import net.astropvp.armorsets.struct.ArmorSetType;
import net.astropvp.core.killtracker.KillTrackerAPI;
import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class Teleblock
extends CustomEnchantment {
    public Teleblock() {
        super("Teleblock", new Material[]{Material.BOW}, 2);
        this.max = 5;
        this.base = 20.0;
        this.interval = 0.0;
        Bukkit.getPluginManager().registerEvents((Listener)new TeleblockListener(), (Plugin)Enchantments.getInstance());
    }

    public void applyProjectileEffect(LivingEntity player, int level, ProjectileLaunchEvent e) {
        Player p = (Player)player;
        int soulCost = level * 6;
        if (KillTrackerAPI.hasGodMode((Player)p) && KillTrackerAPI.getAllSouls((Player)p) >= soulCost) {
            KillTrackerAPI.removeSoulsFromGems((Player)p, (int)soulCost);
            try {
                ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), p.getLocation().add(0.0, 1.0, 0.0), 0.5f, 0.5f, 0.5f, 0.1f, 65);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            p.playSound(p.getLocation(), Sound.EAT, 0.4f, 0.2f);
            if (KillTrackerAPI.getAllSouls((Player)p) % 100 == 0) {
                p.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "** SOULS: " + (Object)ChatColor.UNDERLINE + KillTrackerAPI.getAllSouls((Player)p) + ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + " **");
            }
            e.getEntity().setMetadata("teleblockEnchantment", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)level));
        }
    }
}

