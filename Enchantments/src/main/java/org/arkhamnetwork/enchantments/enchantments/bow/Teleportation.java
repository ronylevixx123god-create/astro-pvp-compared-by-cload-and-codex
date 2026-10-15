/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  com.rit.sucy.EnchantmentAPI
 *  com.rit.sucy.enchanting.EListener
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.arkhamnetwork.Arkkit.net.astropvp.armorsets.utils.WorldGuardUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.World$Environment
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityShootBowEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.enchantments.enchantments.bow;

import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import com.rit.sucy.enchanting.EListener;
import de.inventivegames.particle.ParticleEffect;
import org.arkhamnetwork.Arkkit.utils.WorldGuardUtils;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class Teleportation
extends CustomEnchantment {
    public Teleportation() {
        super("Teleportation", new Material[]{Material.BOW}, 3);
        this.max = 5;
        this.base = 20.0;
        this.interval = 10.0;
        Bukkit.getPluginManager().registerEvents((Listener)new TeleportationEvents(), (Plugin)Enchantments.getInstance());
    }

    private static class TeleportationEvents
    implements Listener {
        private TeleportationEvents() {
        }

        @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=false)
        public void onArrowHit(EntityDamageByEntityEvent e) {
            if (e.getDamager() instanceof Projectile && e.getDamager().hasMetadata("teleportation_level")) {
                Player player = null;
                Player playerHit = null;
                if (((Projectile)e.getDamager()).getShooter() instanceof Player) {
                    player = (Player)((Projectile)e.getDamager()).getShooter();
                }
                if (e.getEntity() instanceof Player) {
                    playerHit = (Player)e.getEntity();
                }
                if (player == null || playerHit == null) {
                    return;
                }
                int level = ((MetadataValue)e.getDamager().getMetadata("teleportation_level").get(0)).asInt();
                if (player.equals((Object)playerHit)) {
                    e.setCancelled(true);
                    if (player instanceof Player) {
                        player.playSound(player.getLocation(), Sound.CAT_HISS, 0.85f, 0.2f);
                    }
                    return;
                }
                if (!(player instanceof Player) || !(playerHit instanceof Player)) {
                    return;
                }
                if (GeneralUtil.isAlly(player, playerHit)) {
                    if (player.getWorld().getName().equals("cosmic-station-1")) {
                        return;
                    }
                    e.setCancelled(true);
                    if (e.getDamager() instanceof Projectile) {
                        e.getDamager().remove();
                    }
                    Location tp = playerHit.getLocation();
                    tp.setPitch(player.getLocation().getPitch());
                    tp.setYaw(player.getLocation().getYaw());
                    if (tp.distanceSquared(player.getLocation()) > Math.pow(level * 6, 2.0)) {
                        player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.RED + "Your ally is too far away to teleport to with this level of Teleporation.");
                        return;
                    }
                    if ((player.getWorld().getEnvironment() == World.Environment.THE_END || player.getWorld().getName().equals("world_koth")) && !WorldGuardUtils.isPvPDisabled((Location)player.getLocation()) && WorldGuardUtils.isPvPDisabled((Location)tp)) {
                        player.sendMessage((Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + "(!) " + (Object)ChatColor.RED + "You cannot teleport from PvP-enabled to PvP-disabled with Teleportation in The End or KOTH.");
                        return;
                    }
                    player.setMetadata("ignore_combat_tag", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)tp));
                    player.teleport(tp, PlayerTeleportEvent.TeleportCause.UNKNOWN);
                    player.removeMetadata("ignore_combat_tag", (Plugin)Enchantments.getInstance());
                    if (playerHit instanceof Player) {
                        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 0.75f, 0.341f);
                    }
                    Location loc = playerHit.getLocation().add(0.0, 0.5, 0.0);
                    try {
                        ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), loc, (float)Math.random(), (float) Math.random(), (float)Math.random(), 0.5f, 35);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }

        @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
        public void onPlayerShootBow(EntityShootBowEvent e) {
            if (e.getEntity() instanceof Player) {
                Player p = (Player)e.getEntity();
                if (e.getBow() != null && EListener.getValidEnchantments((ItemStack)e.getBow(), (Player)p).containsKey((Object)EnchantmentAPI.getEnchantment((String)"Teleportation"))) {
                    Location loc = e.getProjectile().getLocation();
                    e.getProjectile().setMetadata("teleportation_level", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), EnchantmentAPI.getEnchantments((ItemStack)e.getBow()).get((Object)EnchantmentAPI.getEnchantment((String)"Teleportation"))));
                    try {
                        ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), loc, (float)Math.random(), (float) Math.random(), (float)Math.random(), 0.5f, 35);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                }
            }
        }
    }
}

