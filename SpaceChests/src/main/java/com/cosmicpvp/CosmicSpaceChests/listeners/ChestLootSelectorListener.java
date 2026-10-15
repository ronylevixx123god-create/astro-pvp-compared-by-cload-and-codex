/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Color
 *  org.bukkit.FireworkEffect
 *  org.bukkit.FireworkEffect$Type
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Firework
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryAction
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryClickEvent
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryCloseEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.FireworkMeta
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package com.cosmicpvp.CosmicSpaceChests.listeners;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsPlugin;
import com.cosmicpvp.CosmicSpaceChests.libs.ChestLootSelector;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.cosmicpvp.CosmicSpaceChests.utils.ItemUtils;
import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class ChestLootSelectorListener
implements Listener {
    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent e) {
        Player p = e.getPlayer();
        if (p.getOpenInventory() != null && p.getOpenInventory().getTitle().contains("Space Chest")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked().hasMetadata("chestLootSelector")) {
            ChestLootSelector cls = (ChestLootSelector)((MetadataValue)e.getWhoClicked().getMetadata("chestLootSelector").get(0)).value();
            if (!e.getInventory().getTitle().contains("Space Chest")) {
                e.getWhoClicked().removeMetadata("chestLootSelector", (Plugin)CosmicSpaceChestsPlugin.plugin);
                return;
            }
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            if (!e.getInventory().equals((Object)cls.player.getOpenInventory().getTopInventory())) {
                return;
            }
            if (e.getAction() == InventoryAction.HOTBAR_SWAP || e.getAction() == InventoryAction.SWAP_WITH_CURSOR) {
                return;
            }
            if (cls.phase == ChestLootSelector.LootSelectorPhase.GENERATING) {
                return;
            }
            if (cls.phase == ChestLootSelector.LootSelectorPhase.PICKING_PANELS) {
                int slot = e.getRawSlot();
                if (slot < 0 || slot > e.getInventory().getSize()) {
                    return;
                }
                if (cls.lootInventory.getItem(slot).getType() == Material.STAINED_GLASS_PANE) {
                    return;
                }
                if (cls.selectedSlots.contains(slot)) {
                    cls.selectedSlots.remove(new Integer(slot));
                    this.deselectSlot(cls, slot, cls.chestRarity);
                    cls.player.playSound(cls.player.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.5f);
                } else {
                    cls.selectedSlots.add(slot);
                    this.selectSlot(cls, slot);
                    cls.player.playSound(cls.player.getLocation(), Sound.ORB_PICKUP, 1.0f, 0.75f);
                }
                if (cls.selectedSlots.size() >= 5) {
                    cls.animation();
                    return;
                }
            } else if (cls.phase == ChestLootSelector.LootSelectorPhase.OPENNING_PANELS) {
                if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
                    if (e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE && e.getCurrentItem().getItemMeta().getDisplayName().contains("Mystery Item")) {
                        ItemRarity glassRarity = CosmicSpaceChestsAPI.getStainedGlassRarity(e.getCurrentItem());
                        cls.lootInventory.setItem(e.getRawSlot(), this.generateLootItem(cls.chestRarity, glassRarity));
                        cls.player.playSound(cls.player.getLocation(), Sound.BAT_TAKEOFF, 1.0f, 0.33f);
                        ++cls.lootRevealedCount;
                    } else if (e.getCurrentItem().getType() == Material.CHEST && e.getCurrentItem().getItemMeta().getDisplayName().contains("Mystery Item")) {
                        cls.lootInventory.setItem(e.getRawSlot(), CosmicSpaceChestsAPI.getRandomStainedGlassItem(cls.chestRarity));
                    }
                }
                if (cls.lootRevealedCount == 5) {
                    cls.phase = ChestLootSelector.LootSelectorPhase.DONE;
                }
            } else if (cls.phase == ChestLootSelector.LootSelectorPhase.DONE) {
                e.getWhoClicked().closeInventory();
            }
        }
    }

    private ItemStack generateLootItem(ItemRarity chestRarity, ItemRarity slotRarity) {
        return CosmicSpaceChestsAPI.getItems(chestRarity, slotRarity, 1)[0];
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getPlayer().hasMetadata("chestLootSelector")) {
            ChestLootSelector cls = (ChestLootSelector)((MetadataValue)e.getPlayer().getMetadata("chestLootSelector").get(0)).value();
            if (cls.phase == ChestLootSelector.LootSelectorPhase.ANIMATION || cls.phase == ChestLootSelector.LootSelectorPhase.OPENNING_PANELS || cls.phase == ChestLootSelector.LootSelectorPhase.LOOTING || cls.phase == ChestLootSelector.LootSelectorPhase.DONE) {
                for (int x = 0; x < cls.lootInventory.getContents().length; ++x) {
                    ItemStack i = cls.lootInventory.getItem(x);
                    if (i == null || i.getType() == Material.AIR || !cls.selectedSlots.contains(x)) continue;
                    if (i.getType() == Material.STAINED_GLASS_PANE) {
                        i = this.generateLootItem(cls.chestRarity, CosmicSpaceChestsAPI.getStainedGlassRarity(i));
                    } else if (i.getType() == Material.THIN_GLASS || i.getType() == Material.STAINED_GLASS) {
                        if (i.hasItemMeta() && i.getItemMeta().hasDisplayName()) {
                            i = this.generateLootItem(cls.chestRarity, CosmicSpaceChestsAPI.getRandomItemRarity());
                        }
                    } else if (i.getType() == Material.CHEST) {
                        i = this.generateLootItem(cls.chestRarity, CosmicSpaceChestsAPI.getRandomItemRarity());
                    }
                    if (e.getPlayer().getInventory().firstEmpty() != -1) {
                        e.getPlayer().getInventory().addItem(new ItemStack[]{i});
                        continue;
                    }
                    e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), i);
                }
                Bukkit.getPluginManager().callEvent((Event)new CustomCosmicPlayerEvent((Player)e.getPlayer(), "SPACE_CHEST", new Object[]{cls.chestRarity.name()}));
            } else if (e.getPlayer().getInventory().firstEmpty() != -1) {
                e.getPlayer().getInventory().addItem(new ItemStack[]{CosmicSpaceChestsAPI.getDropPackageChest(cls.chestRarity)});
                final Player p = (Player)e.getPlayer();
                Bukkit.getScheduler().runTaskLater((Plugin)CosmicSpaceChestsPlugin.plugin, new Runnable(){

                    @Override
                    public void run() {
                        p.updateInventory();
                    }
                }, 2L);
            } else {
                e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), CosmicSpaceChestsAPI.getDropPackageChest(cls.chestRarity));
            }
            ItemRarity ir = cls.chestRarity;
            ((Player)e.getPlayer()).playSound(e.getPlayer().getLocation(), Sound.CHEST_CLOSE, 1.0f, 1.0f);
            if (cls.phase != ChestLootSelector.LootSelectorPhase.PICKING_PANELS) {
                Firework fw = (Firework)e.getPlayer().getLocation().getWorld().spawnEntity(e.getPlayer().getLocation(), EntityType.FIREWORK);
                FireworkMeta fwm = fw.getFireworkMeta();
                Color c = null;
                if (ir == ItemRarity.SIMPLE) {
                    c = Color.WHITE;
                }
                if (ir == ItemRarity.UNIQUE) {
                    c = Color.GREEN;
                }
                if (ir == ItemRarity.ELITE) {
                    c = Color.AQUA;
                }
                if (ir == ItemRarity.ULTIMATE) {
                    c = Color.YELLOW;
                }
                if (ir == ItemRarity.LEGENDARY) {
                    c = Color.BLACK;
                }
                if (ir == ItemRarity.GOD) {
                    c = Color.RED;
                }
                FireworkEffect effect = FireworkEffect.builder().flicker(true).withColor(c).withFade(c).with(FireworkEffect.Type.STAR).trail(true).build();
                fwm.addEffect(effect);
                fwm.setPower(0);
                fw.setFireworkMeta(fwm);
            }
            cls.phase = ChestLootSelector.LootSelectorPhase.DONE;
            if (cls.animationTask != null) {
                cls.animationTask.cancel();
            }
            e.getPlayer().removeMetadata("chestLootSelector", (Plugin)CosmicSpaceChestsPlugin.plugin);
        }
    }

    private void selectSlot(ChestLootSelector cls, int slot) {
        cls.lootInventory.setItem(slot, ItemUtils.setAmount(ItemUtils.buildItem(Material.CHEST, (short)0, (Object)CosmicSpaceChestsAPI.getColor(cls.chestRarity) + ChatColor.BOLD.toString() + "Mystery Item " + (Object)ChatColor.UNDERLINE + "#" + (slot + 1 - 10), (Object)ChatColor.GRAY + "You have selected this mystery item."), 1));
    }

    private void deselectSlot(ChestLootSelector cls, int slot, ItemRarity chestRarity) {
        cls.lootInventory.setItem(slot, ItemUtils.setAmount(ItemUtils.buildItem(Material.STAINED_GLASS, cls.getStainedGlassBlockColor(), (Object)CosmicSpaceChestsAPI.getColor(chestRarity) + "" + (Object)ChatColor.BOLD + "??? " + (Object)ChatColor.GRAY + "#" + (slot + 1 - 10), (Object)ChatColor.GRAY + "Choose " + (Object)CosmicSpaceChestsAPI.getColor(chestRarity) + (Object)ChatColor.UNDERLINE + "5 mystery me.astropvp.cratekeys.items", (Object)ChatColor.GRAY + "and your " + (Object)CosmicSpaceChestsAPI.getColor(chestRarity) + (Object)ChatColor.BOLD + chestRarity.name + (Object)ChatColor.GRAY + " loot", (Object)ChatColor.GRAY + "will be revealed."), 1));
    }
}

