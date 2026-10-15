package com.astropvp.wildpvp.listener;// =============================================== //
// Recompile disabled. Please run Recaf with a JDK //
// =============================================== //

// Decompiled with: FernFlower
// Class Version: 8

import com.astropvp.wildpvp.AstroWildPvP;
import com.astropvp.wildpvp.inventory.AcceptInventory;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.event.LandClaimEvent;
import com.astropvp.wildpvp.event.QueueCreateEvent;
import com.astropvp.wildpvp.event.QueueRemoveEvent;
import com.astropvp.wildpvp.queue.QueueManager;
import com.astropvp.wildpvp.queue.QueueState;
import com.astropvp.wildpvp.queue.WildQueue;
import com.astropvp.wildpvp.util.KrizzUtil;
import com.astropvp.wildpvp.util.PlayerInventoryDisplay;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class QueueListener implements Listener {
    private AstroWildPvP plugin;

    public QueueListener(AstroWildPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getFrom().getChunk() != event.getTo().getChunk()) {
            Player player = event.getPlayer();
            WildQueue queue = this.plugin.getQueueManager().getQueueByHost(player);
            if (queue != null) {
                Chunk toChunk = event.getTo().getChunk();
                Chunk queueChunk = queue.getCreationLocation().getChunk();
                if (toChunk != queueChunk) {
                    queue.hideChunkBorder();
                    queue.cancelQueue(player, false);
                }

            }
        }
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String cmd = event.getMessage().toLowerCase();
        WildQueue queue = this.plugin.getQueueManager().getQueueContainingPlayer(player);
        if (queue != null) {
            if (!cmd.contains("spawn") && !cmd.contains("pvp leave")) {
                KrizzUtil.sendMessage((LivingEntity) player, (String) "&c&l(!) Command Not Permitted (!)");
                KrizzUtil.sendMessage((LivingEntity) player, (String) "&7You must use &c/spawn&7 or &c/pvp leave&7 to exit.");
                event.setCancelled(true);
            } else {
                queue.cancelQueue(player, true);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(
            ignoreCancelled = true
    )
    public void onLandClaim(LandClaimEvent event) {
        FPlayer fPlayer = event.getfPlayer();
        Location location = fPlayer.getPlayer().getLocation();
        if (location != null) {
            Chunk chunk = location.getChunk();
            if (this.plugin.getQueueManager().getQueueByChunk(chunk) != null) {
                KrizzUtil.sendMessage((LivingEntity) fPlayer.getPlayer(), (String) "&c&l(!)&c You cannot claim this land during a &lPvP Queue.");
                event.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            WildQueue queue = this.plugin.getQueueManager().getQueueContainingPlayer(player);
            if (queue == null) {
                return;
            }

            if (queue.getState() == QueueState.INVINCIBILITY) {
                event.setCancelled(true);
            }
        }

    }



    @EventHandler
    public void onQueueRemove(QueueRemoveEvent event) {
        WildQueue queue = event.getQueue();
        if (queue.getState() == QueueState.INVINCIBILITY) {
            queue.removeInvincibility();
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        WildQueue queue = this.plugin.getQueueManager().getQueueContainingPlayer(player);
        if (queue != null) {
            queue.cancelQueue(player, true);
        }
    }

    @EventHandler(
            ignoreCancelled = true
    )
    public void onQueueCreate(QueueCreateEvent event) {
        Player player = event.getHost();
        FPlayer fPlayer = (FPlayer) FPlayers.getInstance().getByPlayer(player);
        Faction factionAt = FactionUtils.getFactionAt(player.getLocation());
        if (this.plugin.getQueueManager().getQueueByHost(player) != null) {
            KrizzUtil.sendMessage((LivingEntity) player, (String) "&c&l(!)&c You are already in the Wild PvP Queue.");
            KrizzUtil.sendMessage((LivingEntity) player, (String) "&7You may use &c/pvp leave&7 to exit the queue.");
            event.setCancelled(true);
        } else {
            if (factionAt != null && factionAt.isNone()) {
                Bukkit.broadcastMessage(KrizzUtil.color("&c&l>&f " + player.getName() + "&7 has created a &n/pvp match&7. &cDefeat them!"));
            } else {
                player.sendMessage(CC.translate("&c&l(!) &cYou must be inside a Wilderness Chunk to enter the Wild PvP queue!"));
                event.setCancelled(true);
            }

        }
    }

    @EventHandler
    public void onQueueInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        FPlayer fPlayer = (FPlayer) FPlayers.getInstance().getByPlayer(player);
        Inventory inventory = event.getInventory();
        ItemStack item = event.getCurrentItem();
        if (inventory.getTitle().endsWith(ChatColor.DARK_GRAY + "Wild PvP Queue") && inventory.getSize() == 9) {
            event.setCancelled(true);
            QueueManager queueManager = AstroWildPvP.getInstance().getQueueManager();
            if (event.getRawSlot() == 8) {
                if (queueManager.getAcceptingPlayers().contains(player.getUniqueId())) {
                    KrizzUtil.sendMessage(player, "&c&l(!) &cYou cannot create a pvp while being reviewed for another /pvp match!");
                } else {
                    this.plugin.getQueueManager().createNewQueue(player);
                    player.closeInventory();
                }
            } else if (event.getRawSlot() == 7) {
                player.closeInventory();
                player.chat("/1v1");
            } else {
                WildQueue queue = this.plugin.getQueueManager().getQueueByItem(item);
                if (queue != null) {
                    if (queueManager.getAcceptingPlayers().contains(player.getUniqueId())) {
                        KrizzUtil.sendMessage(player, "&c&l(!) &cYou cannot join a pvp while being reviewed for another /pvp match!");
                    } else if (event.isRightClick()) {
                        (new PlayerInventoryDisplay(queue.getHost())).show(player);
                    } else if (this.plugin.getQueueManager().getQueueContainingPlayer(player) != null) {
                        KrizzUtil.sendMessage((LivingEntity) player, (String) "&c&l(!)&c You are already in a pvp queue.");
                    } else if (queue.getDenied().contains(player.getUniqueId())) {
                        KrizzUtil.sendMessage((LivingEntity) player, (String) "&c&l(!)&c You cannot join this queue as you have been denied.");
                    } else if (!queue.getState().isJoinable()) {
                        KrizzUtil.sendMessage((LivingEntity) player, (String) "&c&l(!)&c This queue currently cannot be joined.");
                    } else {
                        new AcceptInventory(queue.getHost(), player, queue);
                        KrizzUtil.sendMessage(player, "&a&l(!) &aYou have sent a /pvp request to &f" + queue.getHost().getName() + "&a.");
                        player.closeInventory();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPreviewInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        if (inventory.getTitle().endsWith(ChatColor.GREEN + "'s Inventory")) {
            event.setCancelled(true);
            if (event.getRawSlot() == 45 && event.getCurrentItem().getType() == Material.ARROW) {
                this.plugin.getQueueManager().openQueueInventory(player);
            }
        }

    }
}
