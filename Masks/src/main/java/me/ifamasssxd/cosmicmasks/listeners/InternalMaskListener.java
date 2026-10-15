/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.GeneralUtil
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryClickEvent
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryType
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryType$SlotType
 *  org.bukkit.event.player.PlayerChangedWorldEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.ifamasssxd.cosmicmasks.listeners;

import com.cosmicpvp.cosmicutils.utils.GeneralUtil;
import me.ifamasssxd.cosmicmasks.MaskType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class InternalMaskListener
implements Listener {
    public void onPlayerEquip(PlayerInteractEvent event) {
        if (event.hasItem() && event.getItem() != null && GeneralUtil.isArmor((ItemStack)event.getItem())) {
            MaskType.scheduleRescan(event.getPlayer());
        }
    }

    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
    public void onPlayerInventoryClick(InventoryClickEvent event) {
        if ((event.isShiftClick() || event.getSlotType() == InventoryType.SlotType.ARMOR || event.getClick().isKeyboardClick()) && event.getInventory().getType() == InventoryType.PLAYER) {
            MaskType.scheduleRescan((Player)event.getWhoClicked());
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            MaskType.rescanMask((Player)event.getEntity(), false);
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
        MaskType.scheduleRescan(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        MaskType equipped = MaskType.equippedMasks.remove(event.getPlayer().getUniqueId());
        if (equipped != null && equipped.getMaskData().getInternalMask() != null) {
            equipped.getMaskData().getInternalMask().onUnequip(event.getPlayer());
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        MaskType.rescanMask(event.getPlayer(), false);
    }
}

