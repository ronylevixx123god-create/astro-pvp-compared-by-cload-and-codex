/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockEvent
 */
package me.ifamasssxd.cosmiccontests.contests;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import me.ifamasssxd.cosmiccontests.contests.Rerollable;
import me.ifamasssxd.cosmiccontests.struct.contests.BlockContest;
import me.ifamasssxd.cosmiccontests.struct.contests.BlockEventType;
import me.ifamasssxd.cosmiccontests.struct.contests.Contests;
import me.ifamasssxd.cosmiccontests.struct.user.ContestUser;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;

public class SpecificOreMinedContest
extends BlockContest
implements Rerollable {
    private Material ore;
    private List<Material> ores = Lists.newArrayList(Material.REDSTONE_ORE, Material.LAPIS_ORE, Material.DIAMOND_ORE, Material.GOLD_ORE, Material.IRON_ORE, Material.COAL_ORE, Material.EMERALD_ORE, Material.QUARTZ_ORE);

    public SpecificOreMinedContest() {
        super(BlockEventType.BREAK, Material.DIAMOND_ORE);
        this.reroll();
    }

    @Override
    public Material getShowMaterial() {
        return this.ore;
    }

    @Override
    public String getName() {
        return this.getMaterialName() + " Mined";
    }

    @Override
    public String getFormattedLine(ContestUser user) {
        return "%s" + this.getMaterialName() + " Mined\\ " + String.format(this.getFormattedData(user), ChatColor.GRAY);
    }

    @Override
    public String getFormattedData(ContestUser user) {
        return "%s" + user.getInt(Contests.SPECIFIC_MINED, "broken");
    }

    @Override
    public String getFormattedSuffix(ContestUser user) {
        return String.format(this.getFormattedData(user), ChatColor.GRAY) + " " + this.getName();
    }

    @Override
    public void onBlockEvent(ContestUser user, BlockEvent event) {
        BlockBreakEvent eve;
        Player player;
        if (event instanceof BlockBreakEvent && (player = (eve = (BlockBreakEvent)event).getPlayer()).getItemInHand() != null && player.getItemInHand().getType().name().endsWith("_PICKAXE")) {
            user.incrementValue(Contests.SPECIFIC_MINED, "broken", 1.0);
        }
    }

    @Override
    public boolean isBlockApplicable(Block block) {
        return block.getType().name().endsWith(this.ore.name());
    }

    public String getMaterialName() {
        return StringUtils.capitaliseAllWords((String)this.ore.name().toLowerCase().replace("_", " "));
    }

    @Override
    public void reroll() {
        this.ore = this.ores.get(ThreadLocalRandom.current().nextInt(this.ores.size()));
    }

    @Override
    public void loadData(String data) {
        this.ore = Material.getMaterial(data);
    }

    @Override
    public String getExtraData() {
        return "&" + this.ore.name();
    }
}

