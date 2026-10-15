package com.bryside.astroattributes.listeners;

import com.bryside.astroattributes.enums.RuneType;
import com.bryside.astroattributes.objects.RuneCacher;
import com.bryside.astroattributes.utils.Utils;
import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class RuneDamagerListener implements Listener {

    @EventHandler
    public void damageEvent(EntityDamageByEntityEvent e) throws Exception {
        if (e.getDamager() instanceof Player) {
            Player damager = (Player) e.getDamager();
            ItemStack item = damager.getItemInHand();
            if (item == null || !Utils.isWeapon(item)) {
                return;
            }
            if (e.isCancelled()) return;

            RuneCacher cacher = RuneCacher.getRuneCacher(damager);
            if (cacher == null) {
                cacher = new RuneCacher(item, damager);
                if (cacher.getRuneType() == null) {
                    return;
                }
            }

            int level = cacher.getLevel();
            RuneType type = cacher.getRuneType();
            if (!cacher.isStillCached(item)) {
                RuneCacher.removeCache(cacher);
                cacher = new RuneCacher(item, damager);
                if (cacher.getRuneType() == null) {
                    return;
                }
            }
            if (e.getEntity() instanceof Player) {
                if (AstroItemSkinsAPI.hasSkinEquipped((Player) e.getEntity(), SkinType.CHRISTMAS_2022_HELMET_ACTIVE) && Math.random() < 0.5) {
                    return;
                }
            }

                type.getRune().procDamageEvent(e, level, damager);
        }

    }

    @EventHandler
    public void logoutEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        RuneCacher cacher = RuneCacher.getRuneCacher(player);
        if (cacher != null) {
            RuneCacher.removeCache(cacher);
            Bukkit.getLogger().info("Removing Titan Cache for " + player.getName() + ".");
        }

    }

    @EventHandler
    public void placeRune(BlockPlaceEvent e) {
        RuneCacher cacher = new RuneCacher(e.getItemInHand());
        if (cacher.getRuneType() != null) {
            e.setCancelled(true);
        }

    }
}
