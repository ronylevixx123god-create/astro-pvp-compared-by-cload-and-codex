package org.arkhamnetwork.enchantments.enchantments.tools;

import com.massivecraft.factions.*;
import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;

import java.util.Arrays;
import java.util.List;

public class Tray extends CustomEnchantment {
    public static List<Material> trayWhiteListedBlocks = Arrays.asList(Material.NETHERRACK, Material.DIRT);

    public Tray() {
        super("Tray", GeneralUtil.tools, 6);
        this.max = 1;
        this.base = 10.0;
        this.interval = 10.0;
    }

    public void applyToolEffect(final Player player, final Block block, final int enchantLevel, final BlockEvent event) {
        if (event instanceof BlockBreakEvent) {
            trayEffect(block, 6, player);
        }
    }

    public void trayEffect(Block block, int r, Player p) {
        FPlayer fplayer = FPlayers.getInstance().getByPlayer(p);
        Block target;
        Faction locFac;

        double borderRadius = block.getWorld().getWorldBorder().getSize() / 2;

        for (int x = -r; x <= r; x++) {
            for (int z = -r; z <= r; z++) {
                target = block.getRelative(x,0, z);
                if (Math.abs(getAccurateCoord(target.getX())) >= borderRadius || Math.abs(getAccurateCoord(target.getZ())) >= borderRadius) continue;
                locFac = Board.getInstance().getFactionAt(new FLocation(target.getLocation()));

                if (!fplayer.getFaction().equals(locFac) && !locFac.isWilderness()) continue;

                if (trayWhiteListedBlocks.contains(target.getType())) target.setType(Material.AIR);
            }
        }
    }

    private double getAccurateCoord(double coord) {
        if (coord < 0) return coord + 1.0;
        return coord;
    }



}
