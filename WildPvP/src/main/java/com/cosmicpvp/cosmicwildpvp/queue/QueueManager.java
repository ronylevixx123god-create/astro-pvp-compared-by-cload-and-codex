package com.cosmicpvp.cosmicwildpvp.queue;


import com.cosmicpvp.cosmicwildpvp.WildPvPPlugin;
import com.cosmicpvp.cosmicwildpvp.event.QueueCreateEvent;
import com.cosmicpvp.cosmicwildpvp.event.QueueRemoveEvent;
import com.cosmicpvp.cosmicwildpvp.util.BryUtil;
import com.cosmicpvp.cosmicwildpvp.util.InventoryUpdateTask;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.entity.Player;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

public class QueueManager
{
    private List<SingleWildQueue> queues;
    private WildPvPPlugin plugin;

    public QueueManager(final WildPvPPlugin plugin) {
        this.queues = Lists.newArrayList();
        this.plugin = plugin;
    }

    public void openQueueInventory(final Player player) {
        new InventoryUpdateTask(player, this.plugin.getQueueManager().getQueueInventory(player)) {
            @Override
            public ItemStack[] getUpdatedContents() {
                return QueueManager.this.plugin.getQueueManager().getQueueInventory(player).getContents();
            }
        };
        BryUtil.playSound(player, Sound.NOTE_PLING);
    }

    public Inventory getQueueInventory(final Player viewer) {
        final int inventorySize = (this.queues.size() % 9 == 0) ? (this.queues.size() + 9) : (9 - this.queues.size() % 9 + this.queues.size());
        final Inventory queueInventory = Bukkit.createInventory((InventoryHolder)null, inventorySize, ChatColor.DARK_GRAY + "Wild PvP Queue");
        queueInventory.setItem(8, BryUtil.createItem(Material.STAINED_GLASS_PANE, "&a&lEnter Wild PvP Queue", 1, 5, "", "&7Click to create a &nWild PvP", "&7match at your current location.", "", "&a&lWhat is Wild PvP?", "&7Wild PvP is when a player or", "&7players fight inside Wilderness", "&7chunks (unclaimed land) to", "&7ensure the most fair fight.", "", "&7Once an opponent accepts your Wild PvP", "&7match they will be teleported to your", "&7location and you will each receive", "&77 seconds of immunity - then, you fight!"));
        queueInventory.setItem(7, BryUtil.createItem(Material.IRON_FENCE, "&6&l1v1 Cage Match", 1, 5, Arrays.asList("", "&7Click to create a &n1v1 Cage PvP Match", "&7match at your current location.", "", "&6&lWhat is a 1v1 Cage Match?", "&71v1 Cage PvP is where", "&72 players fight in the wilderness", "&7in a chunk until a player", "&7defeats the other one.", "", "&7Once an opponent accepts your 1v1 Cage PvP", "&7match they will be teleported to your", "&7location and you will each receive", "&77 seconds of immunity - then, you fight!")));

        this.queues.stream().filter(queue -> queue.getHost() != viewer).forEach(queue -> queueInventory.addItem(new ItemStack[] { queue.getQueueDisplayItem(viewer) }));
        return queueInventory;
    }

    public SingleWildQueue getQueueByItem(final ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) {
            return null;
        }
        final Player player = Bukkit.getPlayer(ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()));
        if (player == null) {
            return null;
        }
        return this.getQueueByHost(player);
    }

    public SingleWildQueue getQueueByChunk(final Chunk chunk) {
        return this.queues.stream().filter(queue -> queue.getCreationLocation().getChunk().equals(chunk)).findFirst().orElse(null);
    }

    public SingleWildQueue getQueueByHost(final Player player) {
        return this.queues.stream().filter(queue -> queue.getHost().equals(player)).findFirst().orElse(null);
    }

    public SingleWildQueue getQueueContainingPlayer(final Player player) {
        return this.queues.stream().filter(queue -> queue.containsPlayer(player)).findFirst().orElse(null);
    }

    public void createNewQueue(final Player host, final boolean cages) {
        for (final Player p : Bukkit.getOnlinePlayers()) {
            if (host.getLocation().getChunk() == p.getLocation().getChunk() && p.getPlayer().getName() != host.getName()) {
                host.sendMessage(CC.translate("&c&l(!)&c You cannot create a pvp match when there is someone else standing in the same chunk as you."));
                return;
            }
        }
        if (WorldGuardUtils.inRegion(host.getLocation(), "__global__") && WorldGuardUtils.getRegionsIn(host.getPlayer().getLocation()).size() != 1) {
            return;
        }
        if (!WorldGuardUtils.isPvPEnabled(host.getLocation())) {
            host.sendMessage(CC.translate("&cYou cannot create a pvp here."));
            return;
        }
        final SingleWildQueue queue = new SingleWildQueue(host);
        queue.setTimeInitiated(System.currentTimeMillis());
        queue.setCageMatch(cages);
        final QueueCreateEvent event = new QueueCreateEvent(host, queue);
        Bukkit.getPluginManager().callEvent((Event)event);
        if (event.isCancelled()) {
            return;
        }
        final List<String> messages = Lists.newArrayList("&a&l(!)&a You have entered into the Wild PvP queue!", "&7Please wait for an opponent to join.", "");
        if (cages) {
            messages.add("&5&lCage Match &7- &dThe chunk you are standing in has been enclosed with a inescapable cage, this cage will not be removed until 120s after the match begins.");
        }
        else {
            messages.add("&c&lGlass Border &7- &cThe glass border displays the outside of the current chunk you are standing in, this chunk will not be claimable until after player invincibility is removed.");
        }
        BryUtil.sendMessage((CommandSender)host, messages);
        queue.showChunkBorder();
        this.queues.add(queue);
    }

    public void removeExisitingQueue(final SingleWildQueue queue) {
        final QueueRemoveEvent event = new QueueRemoveEvent(queue);
        Bukkit.getPluginManager().callEvent((Event)event);
        queue.hideChunkBorder();
        queue.setHost(null);
        queue.setCreationLocation(null);
        queue.setOpponent(null);
        queue.setState(null);
        queue.setPreviousLocationMap(null);
        this.queues.remove(queue);
    }

    public List<SingleWildQueue> getQueues() {
        return this.queues;
    }

    public WildPvPPlugin getPlugin() {
        return this.plugin;
    }
}
