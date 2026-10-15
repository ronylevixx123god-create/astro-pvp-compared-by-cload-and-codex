/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
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
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;

public class OreMinedContest
extends BlockContest {
    public OreMinedContest() {
        super(BlockEventType.BREAK, Material.DIAMOND_ORE);
    }

    @Override
    public String getName() {
        return "Ore Mined";
    }

    @Override
    public String getFormattedLine(ContestUser user) {
        return "%sOres Mined\\ " + String.format(this.getFormattedData(user), new Object[]{ChatColor.GRAY});
    }

    @Override
    public String getFormattedData(ContestUser user) {
        return "%s" + user.getInt(Contests.ORE_MINED, "broken");
    }

    @Override
    public String getFormattedSuffix(ContestUser user) {
        return String.format(this.getFormattedData(user), new Object[]{ChatColor.GRAY}) + " " + this.getName();
    }

    @Override
    public void onBlockEvent(ContestUser user, BlockEvent event) {
        BlockBreakEvent eve;
        Player player;
        if (event instanceof BlockBreakEvent && (player = (eve = (BlockBreakEvent)event).getPlayer()).getItemInHand() != null && player.getItemInHand().getType().name().endsWith("_PICKAXE")) {
            user.incrementValue(Contests.ORE_MINED, "broken", 1.0);
        }
    }

    @Override
    public boolean isBlockApplicable(Block block) {
        return block.getType().name().endsWith("_ORE");
    }
}

