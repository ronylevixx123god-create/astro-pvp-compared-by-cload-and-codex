/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityRegainHealthEvent
 *  org.bukkit.event.entity.EntityShootBowEvent
 *  org.bukkit.event.entity.FoodLevelChangeEvent
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryClickEvent
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerExpChangeEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.event.player.PlayerToggleFlightEvent
 */
package me.ifamasssxd.cosmicduel.listeners;

import me.ifamasssxd.cosmicduel.DuelAPI;
import me.ifamasssxd.cosmicduel.struct.enums.Setting;
import me.ifamasssxd.cosmicduel.struct.newduel.NewDuel;
import me.ifamasssxd.cosmicduel.tasks.DuelArenaTask;
import me.ifamasssxd.cosmicduel.utils.DuelUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class
SettingsListener
        implements Listener {
    @EventHandler
    public void onPlayerRegen(EntityRegainHealthEvent e) {
        NewDuel d;
        Player p;
        if (e.getEntity() instanceof Player && DuelAPI.hasDuel(p = (Player)e.getEntity()) && DuelAPI.isDuelValid(d = DuelAPI.getDuel(p)) && !d.getSettings().hasSetting(Setting.HEALING)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerRegen(FoodLevelChangeEvent e) {
        NewDuel d;
        Player p;
        if (e.getEntity() instanceof Player && DuelAPI.hasDuel(p = (Player)e.getEntity()) && DuelAPI.isDuelValid(d = DuelAPI.getDuel(p)) && !d.getSettings().hasSetting(Setting.FOOD_LOSS)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onExpGain(PlayerExpChangeEvent e) {
        NewDuel d;
        if (DuelAPI.hasDuel(e.getPlayer()) && DuelAPI.isDuelValid(d = DuelAPI.getDuel(e.getPlayer()))) {
            e.setAmount(0);
        }
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent e) {
        if (DuelAPI.hasDuel(e.getPlayer())) {
            NewDuel d = DuelAPI.getDuel(e.getPlayer());
            if (e.getPlayer().isOp() && !DuelArenaTask.isYoutuber(e.getPlayer())) {
                return;
            }
            if (DuelAPI.isDuelValid(d) && !d.getSettings().hasSetting(Setting.FLY)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerClickArmor(InventoryClickEvent e) {
        if (DuelUtils.isArmor(e.getCurrentItem()) || DuelUtils.isArmor(e.getCursor())) {
            Player p = (Player)e.getWhoClicked();
            DuelUtils.checkArmor(p);
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.hasItem() && DuelUtils.isArmor(e.getItem())) {
            NewDuel d;
            Player p = e.getPlayer();
            if (DuelAPI.hasDuel(p) && DuelAPI.isDuelValid(d = DuelAPI.getDuel(p)) && !d.getSettings().hasSetting(Setting.ARMOR)) {
                p.sendMessage((Object)ChatColor.YELLOW + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.YELLOW + "Your duel has Armor " + (Object)ChatColor.RED + (Object)ChatColor.UNDERLINE + "disabled.");
                p.damage(1.0);
                e.setCancelled(true);
            }
            DuelUtils.checkArmor(p);
        }
    }

    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=true)
    public void onPlayerDamagePlayer(EntityDamageByEntityEvent e) {
        NewDuel d;
        Player damager;
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player && (damager = (Player)e.getDamager()).getItemInHand() != null && DuelUtils.isWeapon(damager.getItemInHand()) && DuelAPI.hasDuel(damager) && DuelAPI.isDuelValid(d = DuelAPI.getDuel(damager)) && !d.getSettings().hasSetting(Setting.WEAPONS)) {
            damager.sendMessage((Object)ChatColor.YELLOW + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.YELLOW + "Your duel has Weapons " + (Object)ChatColor.RED + (Object)ChatColor.UNDERLINE + "disabled.");
            e.setCancelled(true);
            e.setDamage(0.0);
        }
    }

    @EventHandler
    public void onEnderPearl(PlayerInteractEvent e) {
        NewDuel d;
        Player p = e.getPlayer();
        if (e.hasItem() && (e.getItem().getType() == Material.ENDER_PEARL || e.getItem().getType() == Material.POTION) && DuelAPI.hasDuel(p) && DuelAPI.isDuelValid(d = DuelAPI.getDuel(p))) {
            if (!d.getSettings().hasSetting(Setting.ENDER_PEARLS) && e.getItem().getType() == Material.ENDER_PEARL) {
                e.setCancelled(true);
                p.sendMessage((Object)ChatColor.YELLOW + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.YELLOW + "Your duel has Ender Pearls " + (Object)ChatColor.RED + (Object)ChatColor.UNDERLINE + "disabled.");
            } else if (!d.getSettings().hasSetting(Setting.POTIONS) && e.getItem().getType() == Material.POTION) {
                e.setCancelled(true);
                p.sendMessage((Object)ChatColor.YELLOW + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.YELLOW + "Your duel has Potions " + (Object)ChatColor.RED + (Object)ChatColor.UNDERLINE + "disabled.");
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=true)
    public void onPlayerPickup(PlayerPickupItemEvent e) {
        NewDuel d;
        if (DuelAPI.hasDuel(e.getPlayer()) && DuelAPI.isDuelValid(d = DuelAPI.getDuel(e.getPlayer()))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerShootBow(EntityShootBowEvent e) {
        NewDuel d;
        Player p;
        if (e.getEntity() instanceof Player && DuelAPI.hasDuel(p = (Player)e.getEntity()) && DuelAPI.isDuelValid(d = DuelAPI.getDuel(p)) && (!d.getSettings().hasSetting(Setting.BOWS) || !d.getSettings().hasSetting(Setting.WEAPONS))) {
            e.setCancelled(true);
            p.playSound(p.getLocation(), Sound.ITEM_BREAK, 1.0f, 1.0f);
            p.sendMessage((Object)ChatColor.YELLOW + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.YELLOW + "Your duel has Bows " + (Object)ChatColor.RED + (Object)ChatColor.UNDERLINE + "disabled.");
        }
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e) {
        NewDuel d;
        Player player = e.getPlayer();
        if (DuelAPI.hasDuel(player) && DuelAPI.isDuelValid(d = DuelAPI.getDuel(player))  && d.getArena() != null && !d.getArena().getSpawnLocations().get(0).getWorld().getName().equals(player.getWorld().getName())) {
            if (e.getItemDrop().getItemStack().getType() == Material.GLASS_BOTTLE) {
                e.getItemDrop().remove();
                return;
            }
            e.setCancelled(true);
        }
    }
}
