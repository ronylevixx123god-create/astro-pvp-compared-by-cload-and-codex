package com.astropvp.wildpvp.queue;


import com.astropvp.wildpvp.event.QueueCreateEvent;
import com.astropvp.wildpvp.event.QueueRemoveEvent;
import com.astropvp.wildpvp.util.InventoryUpdateTask;
import com.astropvp.wildpvp.util.KrizzUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.*;

import com.astropvp.wildpvp.AstroWildPvP;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class QueueManager {
    private List<WildQueue> queues = Lists.newArrayList();
    private Set<UUID> acceptingPlayers = Sets.newHashSet();
    private AstroWildPvP plugin;

    public QueueManager(AstroWildPvP plugin) {
        this.plugin = plugin;
    }

    public void openQueueInventory(final Player player) {
        InventoryUpdateTask var10001 = new InventoryUpdateTask(player, this.plugin.getQueueManager().getQueueInventory(player)) {
            public ItemStack[] getUpdatedContents() {
                return QueueManager.this.plugin.getQueueManager().getQueueInventory(player).getContents();
            }
        };
        KrizzUtil.playSound(player, Sound.NOTE_PLING);
    }

    public Inventory getQueueInventory(Player viewer) {
        Inventory queueInventory = Bukkit.createInventory((InventoryHolder)null, 9, ChatColor.DARK_GRAY + "Wild PvP Queue");
        this.queues.stream().filter((queue) -> {
            return queue.getHost() != viewer;
        }).forEach((queue) -> {
            queueInventory.addItem(queue.getQueueDisplayItem(viewer));
        });
        queueInventory.setItem(8, KrizzUtil.createItem(Material.STAINED_GLASS_PANE, "&a&lEnter Wild PvP Queue", 1, 5, Arrays.asList("", "&7Click to create a &nWild PvP", "&7match at your current location.", "", "&a&lWhat is Wild PvP?", "&7Wild PvP is when a player or", "&7players fight inside Wilderness", "&7chunks (unclaimed land) to", "&7ensure the most fair fight.", "", "&7Once an opponent accepts your Wild PvP", "&7match they will be teleported to your", "&7location and you will each receive", "&77 seconds of immunity - then, you fight!")));
        queueInventory.setItem(7, KrizzUtil.createItem(Material.IRON_FENCE, "&6&l1v1 Cage Match", 1, 5, Arrays.asList("", "&7Click to create a &n1v1 Cage PvP Match", "&7match at your current location.", "", "&6&lWhat is a 1v1 Cage Match?", "&71v1 Cage PvP is where", "&72 players fight in the wilderness", "&7in a chunk until a player", "&7defeats the other one.", "", "&7Once an opponent accepts your 1v1 Cage PvP", "&7match they will be teleported to your", "&7location and you will each receive", "&77 seconds of immunity - then, you fight!")));

        return queueInventory;
    }

    public WildQueue getQueueByItem(ItemStack itemStack) {
        if (itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            Player player = Bukkit.getPlayer(ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()));
            return player == null ? null : this.getQueueByHost(player);
        } else {
            return null;
        }
    }

    public WildQueue getQueueByChunk(Chunk chunk) {
        return (WildQueue)this.queues.stream().filter((queue) -> {
            return queue.getCreationLocation().getChunk().equals(chunk);
        }).findFirst().orElse(null);
    }

    public WildQueue getQueueByHost(Player player) {
        return (WildQueue)this.queues.stream().filter((queue) -> {
            return queue.getHost().equals(player);
        }).findFirst().orElse(null);
    }

    public WildQueue getQueueContainingPlayer(Player player) {
        return (WildQueue)this.queues.stream().filter((queue) -> {
            return queue.containsPlayer(player);
        }).findFirst().orElse(null);
    }

    public void createNewQueue(Player host) {
        WildQueue queue = new WildQueue(host);
        queue.setTimeInitiated(System.currentTimeMillis());
        QueueCreateEvent event = new QueueCreateEvent(host, queue);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            List<String> messages = Lists.newArrayList("&a&l(!)&a You have entered into the Wild PvP queue!", "&7Please wait for an opponent to join.", "", "&c&lGlass Border &7- &cThe glass border displays the outside of the current chunk you are standing in, this chunk will not be claimable until after player invincibility is removed.");
            KrizzUtil.sendMessage((CommandSender)host, messages);
            queue.showChunkBorder();
            this.queues.add(queue);
        }
    }

    public void removeExisitingQueue(WildQueue queue) {
        QueueRemoveEvent event = new QueueRemoveEvent(queue);
        Bukkit.getPluginManager().callEvent(event);
        queue.hideChunkBorder();
        queue.setHost((Player)null);
        queue.setCreationLocation((Location)null);
        queue.setOpponent((Player)null);
        queue.setState((QueueState)null);
        queue.setPreviousLocationMap((Map)null);
        this.queues.remove(queue);
    }

    public Set<UUID> getAcceptingPlayers() {
        return this.acceptingPlayers;
    }

    public List<WildQueue> getQueues() {
        return this.queues;
    }

    public AstroWildPvP getPlugin() {
        return this.plugin;
    }
}
