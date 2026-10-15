/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  me.ifamasssxd.cosmicmasks.MaskType
 *  net.minecraft.server.v1_7_R4.MinecraftServer
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.block.Chest
 *  org.bukkit.block.Dispenser
 *  org.bukkit.block.Dropper
 *  org.bukkit.block.Furnace
 *  org.bukkit.block.Hopper
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.PlayerInventory
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.ifamasssxd.cosmicmasks.MaskType;
import net.astropvp.armorsets.AstroArmorSetsAPI;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;

final class HotbarArmorSwapListener
        implements Listener {
    HotbarArmorSwapListener() {
    }

    public boolean isArmor(Material m) {
        String n = m.toString().toLowerCase();
        if (m == Material.AIR) {
            return false;
        }
        return n.contains("helmet") || n.contains("chestplate") || n.contains("leggings") || n.contains("boots");
    }

    public void equipArmor(Player player, ItemStack is) {
        if (is == null || is.getType() == Material.AIR) {
            return;
        }
        String n = is.getType().toString().toLowerCase();
        PlayerInventory inv = player.getInventory();
        if (n.contains("helmet")) {
            ItemStack old = inv.getHelmet();
            inv.setHelmet(is);
            player.setItemInHand(null);
            if (old != null && old.getType() != Material.AIR) {
                for (SkinType skinType : AstroItemSkinsAPI.getItemSkinByItem(old)) {
                    skinType.getItemSkin().onUnequip(player);
                }
                inv.setItemInHand(old);
            }
        } else if (n.contains("chestplate")) {
            ItemStack old = inv.getChestplate();
            inv.setChestplate(is);
            player.setItemInHand(null);
            if (old != null && old.getType() != Material.AIR) {
                for (SkinType skinType : AstroItemSkinsAPI.getItemSkinByItem(old)) {
                    skinType.getItemSkin().onUnequip(player);
                }
                inv.setItemInHand(old);
            }
        } else if (n.contains("leggings")) {
            ItemStack old = inv.getLeggings();
            inv.setLeggings(is);
            player.setItemInHand(null);
            if (old != null && old.getType() != Material.AIR) {
                for (SkinType skinType : AstroItemSkinsAPI.getItemSkinByItem(old)) {
                    skinType.getItemSkin().onUnequip(player);
                }
                inv.setItemInHand(old);
            }
        } else if (n.contains("boots")) {
            ItemStack old = inv.getBoots();
            inv.setBoots(is);
            player.setItemInHand(null);
            if (old != null && old.getType() != Material.AIR) {
                for (SkinType skinType : AstroItemSkinsAPI.getItemSkinByItem(old)) {
                    skinType.getItemSkin().onUnequip(player);
                }
                inv.setItemInHand(old);
            }
        }
        player.updateInventory();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (event.getPlayer().getItemInHand() == null || !this.isArmor(event.getPlayer().getItemInHand().getType())) {
            return;
        }
        if (event.hasBlock() && (event.getClickedBlock().getState() instanceof Chest || event.getClickedBlock().getState() instanceof Dispenser || event.getClickedBlock().getState() instanceof Dropper || event.getClickedBlock().getState() instanceof Furnace || event.getClickedBlock().getState() instanceof Hopper || event.getClickedBlock().getType() == Material.GOLD_BLOCK || event.getClickedBlock().getType() == Material.IRON_BLOCK)) {
            return;
        }
        String worldName = event.getPlayer().getWorld().getName();

        Player player = event.getPlayer();
        if (player.hasMetadata("lastArmorSwap") && (long) MinecraftServer.currentTick - player.getMetadata("lastArmorSwap").get(0).asLong() <= 15L) {
            return;
        }
        if (event.isCancelled() && player.hasMetadata("noArmorSwap")) {
            return;
        }
        event.setCancelled(true);
        event.setUseInteractedBlock(Event.Result.DENY);
        event.setUseItemInHand(Event.Result.DENY);
        player.setMetadata("lastArmorSwap", new FixedMetadataValue(Arkkit.get(), MinecraftServer.currentTick));
        ItemStack piece = event.getPlayer().getItemInHand().clone();
        System.out.println("tries to update");
        this.equipArmor(player, player.getItemInHand());
        if (HotbarArmorSwap.masksEnabled && event.hasItem() && event.getItem().getType().toString().toLowerCase().contains("helmet")) {
            MaskType.rescanMask(player, true);
        }
        AstroArmorSetsAPI.refreshArmorSets(player, false);
        //AstroItemSkinsAPI.updateSkins(player, true, piece);
        player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 10.0f, 0.95f);
    }

    public ItemStack getOldArmorPiece(Player player, ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return null;
        }
        String n = itemStack.getType().toString().toLowerCase();
        PlayerInventory inv = player.getInventory();
        if (n.contains("helmet")) {
            return player.getInventory().getHelmet();
        }
        if (n.contains("chestplate")) {
            return player.getInventory().getChestplate();
        }
        if (n.contains("leggings")) {
            return player.getInventory().getLeggings();

        }
        if (n.contains("boots")) {
            return player.getInventory().getBoots();
        }
        return null;
    }


}

