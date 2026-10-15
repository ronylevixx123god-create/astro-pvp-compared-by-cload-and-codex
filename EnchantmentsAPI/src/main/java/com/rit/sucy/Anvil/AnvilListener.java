/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryClickEvent
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryCloseEvent
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryOpenEvent
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryType
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package com.rit.sucy.Anvil;

import com.rit.sucy.Anvil.AnvilTask;
import com.rit.sucy.Anvil.AnvilView;
import com.rit.sucy.Anvil.CustomAnvil;
import com.rit.sucy.Anvil.MainAnvil;
import com.rit.sucy.EUpdateTask;
import java.util.Hashtable;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class AnvilListener
implements Listener {
    private final Plugin plugin;
    private final Hashtable<String, AnvilTask> tasks = new Hashtable();

    public AnvilListener(Plugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents((Listener)this, plugin);
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        if (event.getInventory().getType() == InventoryType.ANVIL) {
            Player player = this.plugin.getServer().getPlayer(event.getPlayer().getName());
            if (this.plugin.getServer().getVersion().contains("MC: 1.7.2")) {
                MainAnvil anvil = new MainAnvil(this.plugin, event.getInventory(), player);
                this.tasks.put(player.getName(), new AnvilTask(this.plugin, anvil));
            } else {
                event.setCancelled(true);
                CustomAnvil anvil = new CustomAnvil(this.plugin, player);
                this.tasks.put(player.getName(), new AnvilTask(this.plugin, anvil));
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (this.tasks.containsKey(event.getPlayer().getName())) {
            this.tasks.get(event.getPlayer().getName()).getView().close();
            this.tasks.get(event.getPlayer().getName()).cancel();
            this.tasks.remove(event.getPlayer().getName());
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = this.plugin.getServer().getPlayer(event.getWhoClicked().getName());
        if (this.tasks.containsKey(player.getName()) && !this.plugin.getServer().getVersion().contains("MC: 1.7.2") && this.tasks.get(player.getName()).getView().getInventory().getName().equals(event.getInventory().getName())) {
            boolean top;
            AnvilView view = this.tasks.get(player.getName()).getView();
            ItemStack[] inputs = view.getInputSlots();
            boolean bl = top = event.getRawSlot() < view.getInventory().getSize();
            if (event.getSlot() == -999) {
                return;
            }
            if (event.isShiftClick()) {
                if (event.getRawSlot() == view.getResultSlotID() && this.isFilled(view.getResultSlot())) {
                    if (player.getGameMode() != GameMode.CREATIVE && (view.getRepairCost() > player.getLevel() || view.getRepairCost() >= 40)) {
                        event.setCancelled(true);
                    } else {
                        view.clearInputs();
                        if (player.getGameMode() != GameMode.CREATIVE) {
                            player.setLevel(player.getLevel() - view.getRepairCost());
                        }
                    }
                } else if (top && !view.isInputSlot(event.getSlot())) {
                    event.setCancelled(true);
                } else if (!top && this.areFilled(inputs[0], inputs[1])) {
                    event.setCancelled(true);
                }
            } else if (event.isLeftClick()) {
                if (event.getRawSlot() == view.getResultSlotID() && !this.isFilled(event.getCursor()) && this.isFilled(view.getResultSlot())) {
                    if (player.getGameMode() != GameMode.CREATIVE && (view.getRepairCost() > player.getLevel() || view.getRepairCost() >= 40)) {
                        event.setCancelled(true);
                    } else {
                        view.clearInputs();
                        if (player.getGameMode() != GameMode.CREATIVE) {
                            player.setLevel(player.getLevel() - view.getRepairCost());
                        }
                    }
                } else if (top && !view.isInputSlot(event.getSlot())) {
                    event.setCancelled(true);
                }
            } else if (event.isRightClick() && top) {
                event.setCancelled(true);
            }
            new EUpdateTask(this.plugin, player);
        }
    }

    private boolean isFilled(ItemStack item) {
        return item != null && item.getType() != Material.AIR;
    }

    private boolean areFilled(ItemStack item1, ItemStack item2) {
        return this.isFilled(item1) && this.isFilled(item2);
    }
}

