/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryAction
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.inventory.InventoryDragEvent
 *  org.bukkit.event.inventory.InventoryOpenEvent
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.server.PluginDisableEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.cosmicpvp.cosmicutils.inventory;


import com.cosmicpvp.cosmicutils.inventory.content.InventoryContents;
import com.cosmicpvp.cosmicutils.inventory.opener.ChestInventoryOpener;
import com.cosmicpvp.cosmicutils.inventory.opener.InventoryOpener;
import com.cosmicpvp.cosmicutils.inventory.opener.SpecialInventoryOpener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class InventoryManager {
    private final JavaPlugin plugin;
    private final PluginManager pluginManager;
    private final Map<Player, SmartInventory> inventories;
    private final Map<Player, InventoryContents> contents;
    private final List<InventoryOpener> defaultOpeners;
    private final List<InventoryOpener> openers;

    public InventoryManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.pluginManager = Bukkit.getPluginManager();
        this.inventories = new HashMap<Player, SmartInventory>();
        this.contents = new HashMap<Player, InventoryContents>();
        this.defaultOpeners = Arrays.asList(new ChestInventoryOpener(), new SpecialInventoryOpener());
        this.openers = new ArrayList<InventoryOpener>();
    }

    public void init() {
        this.pluginManager.registerEvents(new InvListener(), this.plugin);
        new InvTask().runTaskTimer(this.plugin, 1L, 1L);
    }

    public Optional<InventoryOpener> findOpener(InventoryType type) {
        Optional<InventoryOpener> opInv = this.openers.stream().filter(opener -> opener.supports(type)).findAny();
        if (!opInv.isPresent()) {
            opInv = this.defaultOpeners.stream().filter(opener -> opener.supports(type)).findAny();
        }
        return opInv;
    }

    public void registerOpeners(InventoryOpener... openers) {
        this.openers.addAll(Arrays.asList(openers));
    }

    public List<Player> getOpenedPlayers(SmartInventory inv) {
        ArrayList<Player> list = new ArrayList<Player>();
        this.inventories.forEach((player, playerInv) -> {
            if (inv.equals(playerInv)) {
                list.add(player);
            }
        });
        return list;
    }

    public Optional<SmartInventory> getInventory(Player p) {
        return Optional.ofNullable(this.inventories.get(p));
    }

    protected void setInventory(Player p, SmartInventory inv) {
        if (inv == null) {
            this.inventories.remove(p);
        } else {
            this.inventories.put(p, inv);
        }
    }

    public Optional<InventoryContents> getContents(Player p) {
        return Optional.ofNullable(this.contents.get(p));
    }

    protected void setContents(Player p, InventoryContents contents) {
        if (contents == null) {
            this.contents.remove(p);
        } else {
            this.contents.put(p, contents);
        }
    }

    class InvTask
            extends BukkitRunnable {
        InvTask() {
        }

        public void run() {
            new HashMap<Player, SmartInventory>(InventoryManager.this.inventories).forEach((player, inv) -> inv.getProvider().update(player, InventoryManager.this.contents.get(player)));
        }
    }

    class InvListener
            implements Listener {
        InvListener() {
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onInventoryClick(InventoryClickEvent e) {
            Player p = (Player) e.getWhoClicked();
            if (!InventoryManager.this.inventories.containsKey(p)) {
                return;
            }
            SmartInventory inv = InventoryManager.this.inventories.get(p);
            if (!inv.isClickable()) {
                e.setCancelled(true);
            }
            if (!(inv.isClickable() || e.getAction() != InventoryAction.COLLECT_TO_CURSOR && e.getAction() != InventoryAction.MOVE_TO_OTHER_INVENTORY && e.getAction() != InventoryAction.NOTHING)) {
                e.setCancelled(true);
                return;
            }
            if (inv.isListenForPlayerInventoryClick() || e.getClickedInventory() == p.getOpenInventory().getTopInventory()) {
                if (!inv.isClickable()) {
                    e.setCancelled(true);
                }
                int row = e.getSlot() / 9;
                int column = e.getSlot() % 9;
                if (row < 0 || column < 0) {
                    return;
                }
                if (row >= inv.getRows() || column >= inv.getColumns()) {
                    return;
                }
                inv.getListeners().stream().filter(listener -> listener.getType() == InventoryClickEvent.class).forEach(listener -> ((InventoryListener<InventoryClickEvent>)(listener)).accept(e));
                if (e.getClickedInventory() == p.getOpenInventory().getTopInventory()) {
                    InventoryManager.this.contents.get(p).get(row, column).ifPresent(item -> item.run(e));
                }
            }
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onInventoryDrag(InventoryDragEvent e) {
            Player p = (Player) e.getWhoClicked();
            if (!InventoryManager.this.inventories.containsKey(p)) {
                return;
            }
            SmartInventory inv = InventoryManager.this.inventories.get(p);
            Iterator iterator = e.getRawSlots().iterator();
            while (iterator.hasNext()) {
                int slot = (Integer) iterator.next();
                if (slot >= p.getOpenInventory().getTopInventory().getSize()) continue;
                if (inv.isClickable()) break;
                e.setCancelled(true);
                break;
            }
            inv.getListeners().stream().filter(listener -> listener.getType() == InventoryDragEvent.class).forEach(listener -> ((InventoryListener<InventoryDragEvent>)(listener)).accept(e));
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onInventoryOpen(InventoryOpenEvent e) {
            Player p = (Player) e.getPlayer();
            if (!InventoryManager.this.inventories.containsKey(p)) {
                return;
            }
            SmartInventory inv = InventoryManager.this.inventories.get(p);
            inv.getListeners().stream().filter(listener -> listener.getType() == InventoryOpenEvent.class).forEach(listener -> ((InventoryListener<InventoryOpenEvent>)(listener)).accept(e));
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onInventoryClose(InventoryCloseEvent e) {
            Player p = (Player) e.getPlayer();
            if (!InventoryManager.this.inventories.containsKey(p)) {
                return;
            }
            SmartInventory inv = InventoryManager.this.inventories.get(p);
            inv.getListeners().stream().filter(listener -> listener.getType() == InventoryCloseEvent.class).forEach(listener -> ((InventoryListener<InventoryCloseEvent>)(listener)).accept(e));
            if (inv.isCloseable()) {
                e.getInventory().clear();
                InventoryManager.this.inventories.remove(p);
                InventoryManager.this.contents.remove(p);
            } else {
                Bukkit.getScheduler().runTask(InventoryManager.this.plugin, () -> p.openInventory(e.getInventory()));
            }
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerQuit(PlayerQuitEvent e) {
            Player p = e.getPlayer();
            if (!InventoryManager.this.inventories.containsKey(p)) {
                return;
            }
            SmartInventory inv = InventoryManager.this.inventories.get(p);
            inv.getListeners().stream().filter(listener -> listener.getType() == PlayerQuitEvent.class).forEach(listener -> ((InventoryListener<PlayerQuitEvent>)(listener)).accept(e));
            InventoryManager.this.inventories.remove(p);
            InventoryManager.this.contents.remove(p);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPluginDisable(PluginDisableEvent e) {
            new HashMap<Player, SmartInventory>(InventoryManager.this.inventories).forEach((player, inv) -> {
                inv.getListeners().stream().filter(listener -> listener.getType() == PluginDisableEvent.class).forEach(listener -> ((InventoryListener<PluginDisableEvent>)(listener)).accept(e));
                inv.close(player);
            });
            InventoryManager.this.inventories.clear();
            InventoryManager.this.contents.clear();
        }
    }
}

