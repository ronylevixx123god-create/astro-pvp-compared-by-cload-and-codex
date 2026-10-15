/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockEvent
 */
package me.ifamasssxd.cosmiccontests.contests;

import me.ifamasssxd.cosmiccontests.struct.contests.BlockContest;
import me.ifamasssxd.cosmiccontests.struct.contests.BlockEventType;
import me.ifamasssxd.cosmiccontests.struct.contests.Contests;
import me.ifamasssxd.cosmiccontests.struct.user.ContestUser;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;

public class BlocksMinedContest
extends BlockContest {
    public BlocksMinedContest(BlockEventType type, Material material) {
        super(type, material);
    }

    @Override
    public void onBlockEvent(ContestUser user, BlockEvent event) {
        BlockBreakEvent e;
        if (event instanceof BlockBreakEvent && (e = (BlockBreakEvent)event).getPlayer().getItemInHand() != null && e.getPlayer().getItemInHand().getType().name().endsWith("_PICKAXE")) {
            user.incrementValue(Contests.BLOCKS_MINED, "broken", 1.0);
        }
    }

    @Override
    public String getFormattedLine(ContestUser user) {
        return "%sBlocks Broken\\ " + String.format(this.getFormattedData(user), new Object[]{ChatColor.GRAY});
    }

    @Override
    public boolean isBlockApplicable(Block block) {
        return block.getType() != Material.TNT;
    }

    @Override
    public String getName() {
        return "Blocks Mined Using Pick";
    }

    @Override
    public String getFormattedSuffix(ContestUser user) {
        return String.format(this.getFormattedData(user), new Object[]{ChatColor.GRAY}) + " " + this.getName();
    }

    @Override
    public String getFormattedData(ContestUser user) {
        return "%s" + user.getInt(Contests.BLOCKS_MINED, "broken");
    }
}

