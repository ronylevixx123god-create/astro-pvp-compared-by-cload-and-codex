package me.ifamasssxd.cosmicbosses.listeners;

import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.tasks.BleedTask;
import me.ifamasssxd.cosmicbosses.utils.ScoreboardUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerListener implements Listener {
    public static ConcurrentHashMap<UUID, Player> aSyncPlayerMap;

    @EventHandler
    public void onPlayerLogon(final PlayerJoinEvent e) {
        PlayerListener.aSyncPlayerMap.put(e.getPlayer().getUniqueId(), e.getPlayer());
        ScoreboardUtils.clearBossScoreboard(e.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        PlayerListener.aSyncPlayerMap.remove(p.getUniqueId());
        if (BleedTask.isBleeding(p)) {
            BleedTask.stopBleed(p);
        }
        ScoreboardUtils.clearBossScoreboard(p);
    }

    @EventHandler
    public void onPLayerDeath(final PlayerDeathEvent e) {
        if (BleedTask.isBleeding(e.getEntity())) {
            BleedTask.stopBleed(e.getEntity());
        }
        ScoreboardUtils.clearBossScoreboard(e.getEntity());
    }

    @EventHandler
    public void onPlayerREspawn(final PlayerRespawnEvent e) {
        e.getPlayer().setWalkSpeed(0.2f);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerPickup(final PlayerPickupItemEvent e) {
        if (e.getItem().hasMetadata("whitelisted_item") && e.getItem().getMetadata("whitelisted_item").size() > 0) {
            final String name = e.getItem().getMetadata("whitelisted_item").get(0).asString();
            if (!e.getPlayer().getName().equals(name)) {
                e.setCancelled(true);
            }
        } else if (e.getItem().hasMetadata("removeDurability") && !e.isCancelled()) {
            final ItemStack item = e.getItem().getItemStack();
            final ItemMeta im = item.getItemMeta();
            e.getItem().removeMetadata("removeDurability", (Plugin) CosmicBosses.getInstance());
            final List<String> lore = (List<String>) im.getLore();
            if (lore == null || lore.size() <= 0) {
                return;
            }
            lore.remove(lore.size() - 1);
            im.setLore((List) lore);
            item.setItemMeta(im);
            e.getItem().setItemStack(item);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerPickup(final InventoryPickupItemEvent e) {
        if (e.getItem().hasMetadata("whitelisted_item") && e.getItem().getMetadata("whitelisted_item").size() > 0) {
            e.setCancelled(true);
        }
    }

    static {
        PlayerListener.aSyncPlayerMap = new ConcurrentHashMap<UUID, Player>();
    }
}
