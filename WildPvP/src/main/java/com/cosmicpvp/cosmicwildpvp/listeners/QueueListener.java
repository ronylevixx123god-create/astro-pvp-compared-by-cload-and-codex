package com.cosmicpvp.cosmicwildpvp.listeners;

import com.cosmicpvp.cosmicwildpvp.Cages;
import com.cosmicpvp.cosmicwildpvp.WildPvPPlugin;
import com.cosmicpvp.cosmicwildpvp.event.QueueCreateEvent;
import com.cosmicpvp.cosmicwildpvp.event.QueueRemoveEvent;
import com.cosmicpvp.cosmicwildpvp.menus.MatchSettingsMenu;
import com.cosmicpvp.cosmicwildpvp.queue.QueueState;
import com.cosmicpvp.cosmicwildpvp.queue.SingleWildQueue;
import com.cosmicpvp.cosmicwildpvp.util.BryUtil;
import com.cosmicpvp.cosmicwildpvp.util.PlayerInventoryDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Inventory;

import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import org.bukkit.Bukkit;
import com.massivecraft.factions.Board;
import org.bukkit.OfflinePlayer;
import com.massivecraft.factions.FPlayers;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.Material;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import java.util.Iterator;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.EventPriority;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.event.LandClaimEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.Listener;

public class QueueListener implements Listener
{
    private WildPvPPlugin plugin;

    public QueueListener(final WildPvPPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final Chunk fromChunk = event.getFrom().getChunk();
        final Chunk toChunk = event.getTo().getChunk();
        final SingleWildQueue queue = this.plugin.getQueueManager().getQueueContainingPlayer(player);
        if (fromChunk == toChunk || queue == null) {
            return;
        }
        final Chunk queueChunk = queue.getCreationLocation().getChunk();
        if (toChunk != queueChunk) {
            if (player == queue.getOpponent()) {
                queue.removeOpponentAndRequeue();
            }
            else if (player == queue.getHost()) {
                queue.cancelQueue(player);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final Chunk chunk = player.getLocation().getChunk();
        if (Cages.noClaimUntil.containsKey(chunk)) {
            final long expire = Cages.noClaimUntil.get(chunk);
            final long now = System.currentTimeMillis();
            if (expire > now) {
                WildPvPPlugin.tpNow(player, WildPvPPlugin.getInstance().getSpawnLocation());
            }
        }
    }

    @EventHandler
    public void onPlayerCommand(final PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        final String cmd = event.getMessage().toLowerCase();
        final SingleWildQueue queue = this.plugin.getQueueManager().getQueueContainingPlayer(player);
        if (queue == null) {
            return;
        }
        if (cmd.contains("spawn") || cmd.contains("pvp leave")) {
            queue.cancelQueue(player);
            event.setCancelled(true);
            return;
        }
        BryUtil.sendMessage((CommandSender)player, "&c&l(!) Command Not Permitted (!)");
        BryUtil.sendMessage((CommandSender)player, "&7You must use &c/spawn&7 or &c/pvp leave&7 to exit.");
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onLandClaim(final LandClaimEvent event) {
        final FPlayer fPlayer = event.getfPlayer();
        final Chunk chunk = event.getLocation().getChunk();
        if (this.plugin.getQueueManager().getQueueByChunk(chunk) != null) {
            BryUtil.sendMessage((CommandSender)fPlayer.getPlayer(), "&c&l(!)&c You cannot claim this land during a &lPvP Queue.");
            event.setCancelled(true);
            return;
        }
        if (Cages.noClaimUntil.containsKey(chunk)) {
            final long expire = Cages.noClaimUntil.get(chunk);
            final long now = System.currentTimeMillis();
            if (expire > now) {
                event.setCancelled(true);
                BryUtil.sendMessage((CommandSender)fPlayer.getPlayer(), "&c&l(!)&c You cannot claim this land during a &lPvP Queue.");
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Chunk chunk = event.getPlayer().getLocation().getChunk();
        if (event.hasItem() && (event.getItem().getType().name().contains("BANNER") || event.getItem().getType().name().contains("PICKAXE")) && (Cages.noClaimUntil.containsKey(chunk) || this.plugin.getQueueManager().getQueueByChunk(chunk) != null)) {
            final long expire = Cages.noClaimUntil.get(chunk);
            final long now = System.currentTimeMillis();
            if (expire > now) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityExplode(final EntityExplodeEvent event) {
        boolean shouldClear = false;
        for (final Block block : event.blockList()) {
            final Chunk chunk = block.getLocation().getChunk();
            if (Cages.noClaimUntil.containsKey(chunk)) {
                final long expire = Cages.noClaimUntil.get(chunk);
                final long now = System.currentTimeMillis();
                if (expire > now) {
                    event.setYield(0.0f);
                    event.setCancelled(true);
                    shouldClear = true;
                    break;
                }
                continue;
            }
        }
        if (shouldClear) {
            event.blockList().clear();
        }
    }

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        final Chunk chunk = event.getBlock().getChunk();
        if (Cages.noClaimUntil.containsKey(chunk)) {
            final long expire = Cages.noClaimUntil.get(chunk);
            final long now = System.currentTimeMillis();
            if (expire > now) {
                BryUtil.sendMessage((CommandSender)player, "&c&l(!)&c You cannot build in this land during a &lPvP Match.");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final Chunk chunk = event.getBlock().getChunk();
        if (Cages.noClaimUntil.containsKey(chunk)) {
            final long expire = Cages.noClaimUntil.get(chunk);
            final long now = System.currentTimeMillis();
            if (expire > now) {
                BryUtil.sendMessage((CommandSender)player, "&c&l(!)&c You cannot build in this land during a &lPvP Match.");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(final EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player player = (Player)event.getEntity();
            final SingleWildQueue queue = this.plugin.getQueueManager().getQueueContainingPlayer(player);
            if (queue == null) {
                return;
            }
            if (queue.getState() == QueueState.INVINCIBILITY) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onQueueRemove(final QueueRemoveEvent event) {
        final SingleWildQueue queue = event.getQueue();
        if (queue.getState() == QueueState.INVINCIBILITY) {
            queue.removeInvincibility();
        }
        if (!queue.isCageMatch() || queue.getState() == QueueState.CANCELLED) {}
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onFactionBanner(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Chunk chunk = player.getLocation().getChunk();
        if (Cages.noClaimUntil.containsKey(chunk) && event.hasItem() && (event.getItem().getType().name().contains("BANNER") || event.getItem().getType() == Material.DIAMOND_PICKAXE)) {
            final long expire = Cages.noClaimUntil.get(chunk);
            final long now = System.currentTimeMillis();
            if (expire > now) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerTpIn(final PlayerTeleportEvent event) {
        final Player player = event.getPlayer();
        final Chunk chunk = event.getTo().getChunk();
        final SingleWildQueue queue = this.plugin.getQueueManager().getQueueByChunk(chunk);
        if (player.isOp() || player.hasPermission("factions.teleport.bypass")) {
            return;
        }
        if (Cages.noClaimUntil.containsKey(chunk) && (queue == null || queue.getState() == QueueState.END)) {
            final long expire = Cages.noClaimUntil.get(chunk);
            final long now = System.currentTimeMillis();
            if (expire > now) {
                final String state = (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) ? "enderpearl" : "teleport";
                BryUtil.sendMessage((CommandSender)player, "&c&l(!)&c You cannot " + state + " in this chunk while there is a cage match active.");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final SingleWildQueue queue = this.plugin.getQueueManager().getQueueContainingPlayer(player);
        if (queue == null) {
            return;
        }
        queue.cancelQueue(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onQueueCreate(final QueueCreateEvent event) {
        final Player player = event.getHost();
        final FPlayer fPlayer = (FPlayer)FPlayers.getInstance().getByPlayer(player);
        if (this.plugin.getQueueManager().getQueueByHost(player) != null) {
            BryUtil.sendMessage((CommandSender)player, "&c&l(!)&c You are already in the Wild PvP Queue.");
            BryUtil.sendMessage((CommandSender)player, "&7You may use &c/pvp leave&7 to exit the queue.");
            event.setCancelled(true);
            return;
        }
        if (!Board.getInstance().getFactionAt(fPlayer.getLastStoodAt()).isNone()) {
            BryUtil.sendMessage((CommandSender)player, "&c&l(!)&c You must be in &2Wilderness&c to create a queue.");
            event.setCancelled(true);
            return;
        }
        if (!fPlayer.getFaction().isWilderness()) {
            Bukkit.broadcastMessage(BryUtil.color("&c&lWildPvP &7- &f" + player.getName() + "&7 (&a&n" + FactionUtils.getFaction(player).getOnlinePlayers().size() + "&f Online&7) has created a &n/pvp match&7. &cDefeat them!"));
        }
        else {
            Bukkit.broadcastMessage(BryUtil.color("&c&lWildPvP &7- &f" + player.getName() + " has created a &n/pvp match&7. &cDefeat them!"));
        }
    }

    @EventHandler
    public void onQueueInventoryClick(final InventoryClickEvent event) {
        final Player player = (Player)event.getWhoClicked();
        final FPlayer fPlayer = (FPlayer)FPlayers.getInstance().getByPlayer(player);
        final Inventory inventory = event.getInventory();
        final ItemStack item = event.getCurrentItem();
        if (!inventory.getTitle().endsWith(ChatColor.DARK_GRAY + "Wild PvP Queue")) {
            return;
        }
        event.setCancelled(true);
        if (item != null && item.hasItemMeta() && item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&a&lEnter Wild PvP Queue"))) {
            player.openInventory(new MatchSettingsMenu(player, this.plugin).getInventory());
            return;
        }

        if (item != null && item.hasItemMeta() && item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&6&l1v1 Cage Match"))) {
            //player.openInventory(new MatchSettingsMenu(player, this.plugin).getInventory());
            player.closeInventory();
            player.chat("/1v1");
            return;
        }
        final SingleWildQueue queue = this.plugin.getQueueManager().getQueueByItem(item);
        if (queue == null) {
            return;
        }
        if (event.isRightClick()) {
            new PlayerInventoryDisplay(queue.getHost()).show(player);
            return;
        }
        if (this.plugin.getQueueManager().getQueueContainingPlayer(player) != null) {
            BryUtil.sendMessage((CommandSender)player, "&c&l(!)&c You are already in a pvp queue.");
            return;
        }
        if (!queue.getState().isJoinable()) {
            BryUtil.sendMessage((CommandSender)player, "&c&l(!)&c This queue currently cannot be joined.");
            return;
        }
        queue.addOpponentToQueue(player);
    }

    @EventHandler
    public void onPreviewInventoryClick(final InventoryClickEvent event) {
        final Player player = (Player)event.getWhoClicked();
        final Inventory inventory = event.getInventory();
        if (inventory.getTitle().endsWith(ChatColor.GREEN + "'s Inventory")) {
            event.setCancelled(true);
            if (event.getRawSlot() == 45) {
                this.plugin.getQueueManager().openQueueInventory(player);
            }
        }
    }
}
