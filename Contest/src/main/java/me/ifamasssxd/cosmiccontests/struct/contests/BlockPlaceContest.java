/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.event.block.BlockEvent
 *  org.bukkit.event.block.BlockPlaceEvent
 */
package me.ifamasssxd.cosmiccontests.struct.contests;

import me.ifamasssxd.cosmiccontests.struct.contests.BlockContest;
import me.ifamasssxd.cosmiccontests.struct.contests.BlockEventType;
import me.ifamasssxd.cosmiccontests.struct.contests.Contests;
import me.ifamasssxd.cosmiccontests.struct.user.ContestUser;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceContest
extends BlockContest {
    public BlockPlaceContest() {
        super(BlockEventType.PLACE, Material.WORKBENCH);
    }

    @Override
    public String getName() {
        return "Blocks Placed";
    }

    @Override
    public String getFormattedData(ContestUser user) {
        return "%s" + user.getInt(Contests.BLOCKS_PLACED, "placed");
    }

    @Override
    public String getFormattedLine(ContestUser user) {
        return "%sBlocks Placed\\ " + String.format(this.getFormattedData(user), new Object[]{ChatColor.GRAY});
    }

    @Override
    public void onBlockEvent(ContestUser user, BlockEvent event) {
        if (event instanceof BlockPlaceEvent) {
            BlockPlaceEvent e = (BlockPlaceEvent)event;
            if (!(e.getItemInHand() == null || e.getItemInHand().getType() != Material.TNT && e.getItemInHand().getType().isSolid())) {
                return;
            }
            user.incrementValue(Contests.BLOCKS_PLACED, "placed", 1.0);
        }
    }

    @Override
    public String getFormattedSuffix(ContestUser user) {
        return String.format(this.getFormattedData(user), new Object[]{ChatColor.GRAY}) + " Blocks Placed";
    }

    @Override
    public boolean isBlockApplicable(Block block) {
        return true;
    }
}

