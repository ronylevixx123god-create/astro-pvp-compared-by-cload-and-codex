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

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import me.ifamasssxd.cosmiccontests.contests.Rerollable;
import me.ifamasssxd.cosmiccontests.struct.contests.BlockContest;
import me.ifamasssxd.cosmiccontests.struct.contests.BlockEventType;
import me.ifamasssxd.cosmiccontests.struct.contests.Contests;
import me.ifamasssxd.cosmiccontests.struct.user.ContestUser;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;

public class TreeCutterContest
extends BlockContest
implements Rerollable {
    private Material logMaterial;
    private byte logType;

    public TreeCutterContest() {
        super(BlockEventType.BREAK, Material.LOG);
        this.reroll();
    }

    @Override
    public String getName() {
        return this.getName(this.logType) + " Logs Cut";
    }

    @Override
    public Material getShowMaterial() {
        return this.logMaterial;
    }

    @Override
    public short getData() {
        return this.logType;
    }

    @Override
    public String getFormattedLine(ContestUser user) {
        return "%s" + this.getName(this.logType) + " Logs Broken\\ " + String.format(this.getFormattedData(user), new Object[]{ChatColor.GRAY});
    }

    @Override
    public String getFormattedSuffix(ContestUser user) {
        return String.format(this.getFormattedData(user), new Object[]{ChatColor.GRAY}) + this.getName(this.logType) + " Logs Broken";
    }

    @Override
    public String getFormattedData(ContestUser user) {
        return "%s" + user.getInt(Contests.TREE_CUTTER, "broken");
    }

    @Override
    public void onBlockEvent(ContestUser user, BlockEvent event) {
        Block logType;
        if (event instanceof BlockBreakEvent && (logType = event.getBlock()).getType() == this.logMaterial && logType.getData() == this.logType) {
            user.incrementValue(Contests.TREE_CUTTER, "broken", 1.0);
        }
    }

    public String getName(byte type) {
        if (this.logMaterial == Material.LOG) {
            if (type == 0) {
                return "Oak";
            }
            if (type == 1) {
                return "Spruce";
            }
            if (type == 2) {
                return "Birch";
            }
            if (type == 3) {
                return "Jungle Wood";
            }
        }
        return "New Wood";
    }

    @Override
    public boolean isBlockApplicable(Block block) {
        return block.getType().name().startsWith("LOG");
    }

    @Override
    public void reroll() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        this.logMaterial = Material.LOG;
        this.logType = Byte.parseByte(String.valueOf(((Random)random).nextInt(4)));
    }

    @Override
    public void loadData(String data) {
        if (data.contains("@")) {
            this.logType = Byte.parseByte(data.split("@")[1]);
            this.logMaterial = Material.getMaterial((String)data.split("@")[0]);
        } else {
            this.logMaterial = Material.getMaterial((String)data);
        }
    }

    @Override
    public String getExtraData() {
        return "&" + this.logMaterial.name() + "@" + this.logType;
    }

    public Material getLogMaterial() {
        return this.logMaterial;
    }

    public void setLogMaterial(Material logMaterial) {
        this.logMaterial = logMaterial;
    }

    public byte getLogType() {
        return this.logType;
    }
}

