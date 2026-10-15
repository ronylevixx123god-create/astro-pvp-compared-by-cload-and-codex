/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.FoodLevelChangeEvent
 *  org.bukkit.event.entity.PotionSplashEvent
 *  org.bukkit.event.entity.ProjectileLaunchEvent
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryType$SlotType
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.ifamasssxd.cosmicduel.listeners;

import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicduel.CosmicDuel;
import me.ifamasssxd.cosmicduel.DuelAPI;
import me.ifamasssxd.cosmicduel.struct.Arena;
import me.ifamasssxd.cosmicduel.struct.enums.DuelState;
import me.ifamasssxd.cosmicduel.struct.enums.Setting;
import me.ifamasssxd.cosmicduel.struct.newduel.NewDuel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class SpectatorListener
implements Listener {
    List<String> allowed = Lists.newArrayList("/mute", "/warn", "/kick", "/unmute", "/tempmute", "/ban", "/bminfo");

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && (this.isSpectator((Player)e.getEntity()) || this.isDuelEnding((Player)e.getEntity()))) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPotionSplash(PotionSplashEvent e) {
        for (Entity ent : e.getAffectedEntities()) {
            if (!(ent instanceof Player) || !this.isSpectator((Player)ent)) continue;
            e.setIntensity((LivingEntity)ent, 0.0);
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerPreProcess(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (this.isSpectator(p) && !p.isOp()) {
            if (e.getMessage().endsWith("duel leave")) {
                return;
            }
            if (p.hasPermission("bm.command.bminfo")) {
                for (String cmd : this.allowed) {
                    if (!e.getMessage().startsWith(cmd)) continue;
                    return;
                }
            }
            e.setMessage("/null");
            e.setCancelled(true);
            p.sendMessage(String.format("%sYou cannot use commands while spectating.", ChatColor.RED));
            p.sendMessage(String.format("%sYou can leave the duel with /duel leave.", ChatColor.GRAY));
        }
    }

    @EventHandler
    public void onFoodLoss(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();
            if (event.getFoodLevel() < player.getFoodLevel() && this.isSpectator(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onProjectile(ProjectileLaunchEvent e) {
        Player p;
        if (e.getEntity().getShooter() instanceof Player && this.isSpectator(p = (Player)e.getEntity().getShooter())) {
            e.setCancelled(true);
            e.getEntity().remove();
        }
    }

    @EventHandler
    public void onPlayerDealDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && (this.isSpectator((Player)e.getDamager()) || this.isDuelEnding((Player)e.getDamager()))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onSpectateDrop(PlayerDropItemEvent e) {
        if (this.isSpectator(e.getPlayer()) || this.isDuelEnding(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (this.isSpectator(e.getPlayer()) || this.isDuelEnding(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onPlayerPickupItem(PlayerPickupItemEvent e) {
        if (this.isSpectator(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        if (this.isSpectator((Player)event.getWhoClicked()) && event.getSlotType() == InventoryType.SlotType.ARMOR) {
            event.setCancelled(true);
        }
    }

    public void checkArmor(final Player p) {
        NewDuel d;
        if (DuelAPI.hasDuel(p) && DuelAPI.isDuelValid(d = DuelAPI.getDuel(p)) && !d.getSettings().hasSetting(Setting.ARMOR)) {
            new BukkitRunnable(){

                public void run() {
                    ItemStack[] armor = p.getInventory().getArmorContents();
                    for (int i = 0; i < armor.length; ++i) {
                        ItemStack is = armor[i];
                        if (is == null || is.getType() == Material.AIR) continue;
                        armor[i] = new ItemStack(Material.AIR);
                        p.getInventory().setArmorContents(armor);
                        p.getInventory().addItem(is);
                        p.updateInventory();
                        p.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "(!) " + ChatColor.YELLOW + "Your duel has Armor " + ChatColor.RED + ChatColor.UNDERLINE + "disabled.");
                        for (PotionEffect pe : p.getActivePotionEffects()) {
                            p.removePotionEffect(pe.getType());
                        }
                        p.playSound(p.getLocation(), Sound.ITEM_BREAK, 1.0f, 1.0f);
                    }
                }
            }.runTaskLater(CosmicDuel.get(), 2L);
        }
    }

    public boolean isDuelEnding(Player p) {
        return DuelAPI.hasDuel(p) && DuelAPI.getDuel(p).getDuelState() == DuelState.ENDING;
    }

    public boolean isSpectator(Player p) {
        boolean hasSpectatorMeta = p.hasMetadata("spectator");
        return hasSpectatorMeta || !DuelAPI.hasDuel(p) && p.getWorld().getName().equals(Arena.getDuelWorldName());
    }
}

