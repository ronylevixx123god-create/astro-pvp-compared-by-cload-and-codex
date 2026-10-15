/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.NBTWrapper
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.block.BrewingStand
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.BrewEvent
 *  org.bukkit.event.inventory.InventoryAction
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryMoveItemEvent
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemStack
 */
package org.arkhamnetwork.Arkkit.patches;

import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

final class PotionStackListener
        implements Listener {
    PotionStackListener() {
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.hasItem() && e.getItem().getType() == Material.POTION && e.getItem().getDurability() != 0 && e.getItem().getAmount() > 1) {
            Player pl = e.getPlayer();
            ItemStack is = e.getItem();
            boolean approvedStack = false;
            if (e.getItem().getAmount() <= this.getPotionMaxStackSize(is)) {
                approvedStack = true;
            }
            e.setCancelled(true);
            e.setUseItemInHand(Event.Result.DENY);
            if (!approvedStack) {
                pl.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!)" + ChatColor.RED + " You have too many potions in this stack!");
                pl.sendMessage(ChatColor.RED + "Purchase stackable potions from " + ChatColor.UNDERLINE + "/shop" + ChatColor.RED + " to use in combat!");
            }
            int i = is.getAmount();
            ItemStack is_single = is.clone();
            is_single.setAmount(1);
            pl.setItemInHand(is_single);
            --i;
            while (i > 0) {
                if (pl.getInventory().firstEmpty() != -1) {
                    --i;
                    pl.getInventory().addItem(is_single);
                    continue;
                }
                ItemStack is_remainder = is_single.clone();
                is_remainder.setAmount(i);
                if (approvedStack) {
                    is_remainder.setAmount(i + 1);
                    pl.setItemInHand(is_remainder);
                    break;
                }
                pl.getWorld().dropItem(pl.getLocation(), is_remainder);
                pl.sendMessage(ChatColor.GRAY + "Your extra illegally stacked potions have been dropped on the ground as you did not have enough inventory space for them to be automatically unstacked.");
                break;
            }
            pl.updateInventory();
            block1:
            for (int invSlot = 0; invSlot < pl.getInventory().getSize(); ++invSlot) {
                int x;
                ItemStack itemInInv = pl.getInventory().getItem(invSlot);
                if (itemInInv == null || itemInInv.getType() != Material.POTION || itemInInv.getDurability() == 0 || itemInInv.getAmount() <= 1 || (x = itemInInv.getAmount()) <= this.getPotionMaxStackSize(itemInInv))
                    continue;
                ItemStack itemInInv_single = itemInInv.clone();
                itemInInv_single.setAmount(1);
                pl.getInventory().setItem(invSlot, itemInInv_single);
                --x;
                while (x > 0) {
                    if (pl.getInventory().firstEmpty() != -1) {
                        --x;
                        pl.getInventory().addItem(itemInInv_single);
                        continue;
                    }
                    ItemStack is_remainder = itemInInv_single.clone();
                    is_remainder.setAmount(x);
                    pl.getWorld().dropItem(pl.getLocation(), is_remainder);
                    continue block1;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() != null && e.getInventory().getHolder() instanceof BrewingStand) {
            if (e.getAction() == InventoryAction.HOTBAR_SWAP) {
                e.setCancelled(true);
                return;
            }
            if (e.getCursor() != null && e.getCursor().getType() == Material.POTION && e.getCursor().getAmount() > 1 || e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.POTION && e.getCurrentItem().getAmount() > 1) {
                e.setCancelled(true);
                e.setResult(Event.Result.DENY);
                Player p = (Player) e.getWhoClicked();
                p.updateInventory();
                p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!)" + ChatColor.RED + " You cannot brew with stacked water bottles!");
                p.sendMessage(ChatColor.GRAY + "Stacked potions are " + ChatColor.UNDERLINE + "not" + ChatColor.GRAY + " allowed, if you attempt to use them in combat they will forcibly unstack and be dropped on the ground.");
                p.closeInventory();
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onItemMoveInventory(InventoryMoveItemEvent e) {
        if (e.getDestination().getType() == InventoryType.BREWING && e.getItem().getType() == Material.POTION && e.getItem().getAmount() > 1) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBrewEvent(BrewEvent e) {
        for (ItemStack i : e.getContents().getContents()) {
            if (i == null || i.getType() != Material.POTION || i.getAmount() <= 1) continue;
            e.setCancelled(true);
            return;
        }
        if (e.getContents().getIngredient().getType() == Material.POTION && e.getContents().getIngredient().getAmount() > 1) {
            e.setCancelled(true);
        }
    }

    private int getPotionMaxStackSize(ItemStack i) {
        NBTWrapper nbtData = new NBTWrapper(i);
        boolean stackable = nbtData.getString("cosmicType").equalsIgnoreCase("stackable");
        if (stackable) {
            int maxStackSize = nbtData.getCompound("conaxData").getInt("maxStackSize");
            return maxStackSize;
        }
        return 1;
    }
}

