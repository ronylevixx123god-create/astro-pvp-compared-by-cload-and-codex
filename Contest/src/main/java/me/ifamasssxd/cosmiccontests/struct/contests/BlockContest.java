/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.event.block.BlockEvent
 */
package me.ifamasssxd.cosmiccontests.struct.contests;

import me.ifamasssxd.cosmiccontests.struct.contests.BlockEventType;
import me.ifamasssxd.cosmiccontests.struct.contests.Contest;
import me.ifamasssxd.cosmiccontests.struct.user.ContestUser;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockEvent;

public abstract class BlockContest
extends Contest {
    private BlockEventType blockEventType;

    public BlockContest(BlockEventType type, Material material) {
        super(material);
        this.blockEventType = type;
    }

    public abstract void onBlockEvent(ContestUser var1, BlockEvent var2);

    public abstract boolean isBlockApplicable(Block var1);

    public BlockEventType getBlockEventType() {
        return this.blockEventType;
    }
}

