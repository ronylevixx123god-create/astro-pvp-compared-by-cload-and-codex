/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.compilex.CompilexPermissions.event.PermissionGroupAddEvent
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.google.common.collect.MapMaker
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Sound
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package org.arkhamnetwork.Arkkit.patches;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

final class EssentialsAddHomeItemListener
        implements Listener {
    //private Map<UUID, Long> cooldown = new MapMaker().expireAfterWrite(10L, TimeUnit.SECONDS).makeMap();
    private final Cache<UUID, Long> cooldown = CacheBuilder.newBuilder().expireAfterWrite(10L, TimeUnit.SECONDS).build();

    EssentialsAddHomeItemListener() {
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerUseItem(PlayerInteractEvent e) {
        if (e.hasItem() && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && EssentialsAddHomes.isAddHomeItem(e.getItem()) && !e.getPlayer().getWorld().getName().contains("duels")) {
            e.setCancelled(true);
            e.setUseInteractedBlock(Event.Result.DENY);
            e.setUseItemInHand(Event.Result.DENY);
            Player p = e.getPlayer();
            if (!p.hasPermission("enchantments.max.20")) {
                p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "(!) " + ChatColor.RED + "Home expanders can only be applied by players with a Tier 5 (or higher) rank.");
                p.sendMessage(ChatColor.GRAY + "Upgrade your rank to unlock more homes.");
                return;
            }
            Long expiration = this.cooldown.asMap().get(p.getUniqueId());
            if (expiration != null && expiration > System.currentTimeMillis()) {
                e.setCancelled(true);
                e.setUseItemInHand(Event.Result.DENY);
                p.sendMessage(CC.Red + "Please wait at least 10 seconds before applying another home expansion!");
                return;
            }
            this.cooldown.put(p.getUniqueId(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(8L));
            ItemStack i = e.getItem();
            if (i.getAmount() > 1) {
                i.setAmount(i.getAmount() - 1);
                p.setItemInHand(i);
            } else {
                p.setItemInHand(null);
            }
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "addhomes " + p.getName() + " 1");
            p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + " +1 extra /home!");
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 0.7f, 1.0f);
        }
    }


}

