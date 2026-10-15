/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.Acrobot.ChestShop.Signs.ChestShopSign
 *  com.massivecraft.factions.Board
 *  com.massivecraft.factions.FLocation
 *  org.bukkit.Bukkit
 *  org.bukkit.Chunk
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockState
 *  org.bukkit.block.Chest
 *  org.bukkit.block.DoubleChest
 *  org.bukkit.block.Sign
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.world.ChunkLoadEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.Inventory
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package org.arkhamnetwork.Arkkit.patches;

import com.Acrobot.ChestShop.Signs.ChestShopSign;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

final class TileEntityCleanerListener
        implements Listener {
    private final boolean chest_shop = Bukkit.getPluginManager().isPluginEnabled("ChestShopSign");

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkLoad(ChunkLoadEvent e) {
        FLocation ploc;
        Chunk c = e.getChunk();
        if (Bukkit.getPluginManager().isPluginEnabled("Factions") && !FactionUtils.getFactionAt(ploc = new FLocation(c.getBlock(0, 0, 0).getLocation())).isWarZone()) {
            return;
        }
        int count = 0;
        int tile_entity = c.getTileEntities().length;
        if (tile_entity > 5) {
            for (BlockState bs : c.getTileEntities()) {
                Sign s;
                if (bs instanceof Sign && (s = (Sign) bs).getLine(0).equals("") && s.getLine(1).equals("") && s.getLine(2).equals("") && s.getLine(3).equals("")) {
                    if (s.getBlock().getType() == Material.SIGN_POST) {
                        s.setType(Material.AIR);
                    }
                    ++count;
                }
                if (bs instanceof DoubleChest) {
                    DoubleChest dc = (DoubleChest) bs;
                    if (this.chest_shop && ChestShopSign.isShopChest(dc.getLocation().getBlock()) || this.getChestItemCount(dc.getLeftSide().getInventory()) > 0 || this.getChestItemCount(dc.getRightSide().getInventory()) > 0)
                        continue;
                    dc.getLocation().getBlock().setType(Material.AIR);
                    ++count;
                    continue;
                }
                if (!(bs instanceof Chest)) continue;
                Chest ch = (Chest) bs;
                if (this.chest_shop && ChestShopSign.isShopChest(ch.getLocation().getBlock()) || this.getChestItemCount(ch.getBlockInventory()) > 0)
                    continue;
                ch.getBlock().setType(Material.AIR);
                ++count;
            }
            if (count > 10) {
                Bukkit.getLogger().info("Cleared " + count + " empty tile entities from " + c.getX() + "," + c.getZ() + "(" + tile_entity + ")");
            }
        }
    }

    public int getChestItemCount(Inventory inv) {
        int i = 0;
        for (ItemStack is : inv) {
            if (is == null || is.getType() == Material.AIR) continue;
            ++i;
        }
        return i;
    }
}

