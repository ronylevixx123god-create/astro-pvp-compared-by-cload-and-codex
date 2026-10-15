package net.astropvp.armorsets.tasks;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.PVPUtils;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import net.astropvp.damagehandler.AstroDamageHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class FrozenTropicsAbility extends BukkitRunnable {

    private static final int RADIUS = 7;

    private final Player player;

    private final List<Block> blockList;
    private final List<Block> blocksToClear;
    private List<Block> previousBlockList;
    private int countDown;
    private double damage;

    public FrozenTropicsAbility(Player player, int abilityDurationInSeconds, double damage) {
        this.player = player;
        this.countDown = abilityDurationInSeconds;
        this.damage = damage;
        this.blockList = new ArrayList<>();
        this.blocksToClear = new ArrayList<>();
        this.previousBlockList = new ArrayList<>();
    }

    @Override
    public void run() {

        this.previousBlockList.clear();
        this.previousBlockList.addAll(this.blockList);

        if (this.countDown <= 0) {
            this.cancel();

            for (Block block : this.blockList) {
                block.getState().update();
                block.getRelative(BlockFace.UP).getState().update();
            }

            this.blockList.clear();
            return;
        }

        this.blockList.clear();
        this.setBlocks();

        this.blocksToClear.addAll(this.previousBlockList);
        this.blocksToClear.removeAll(this.blockList);

        for (Block block : this.blocksToClear) {
            block.getState().update();
            block.getRelative(BlockFace.UP).getState().update();
        }

        this.blocksToClear.clear();

        this.countDown--;
    }

    public void setBlocks() {
        Location loc = this.player.getLocation();

        int cx = loc.getBlockX();
        int cy = loc.getBlockY() - 1;
        int cz = loc.getBlockZ();

        for (int x = cx - RADIUS; x <= cx + RADIUS; x++) {
            for (int z = cz - RADIUS; z <= cz + RADIUS; z++) {
                if ((cx - x) * (cx - x) + (cz - z) * (cz - z) <= RADIUS * RADIUS) {
                    Block block = loc.getWorld().getBlockAt(x, cy, z);
                    if (block.getType() != Material.AIR && block.getType().isSolid()) {
                        this.blockList.add(block);
                    }

                    Block blockAbove = loc.getWorld().getBlockAt(x, cy + 1, z);
                    if (blockAbove.getType() != Material.AIR && blockAbove.getType().isSolid()) {
                        this.blockList.add(blockAbove);
                    }

                    Block blockBellow = loc.getWorld().getBlockAt(x, cy - 1, z);
                    if (blockBellow.getType() != Material.AIR && blockBellow.getType().isSolid()) {
                        this.blockList.add(blockBellow);
                    }
                }
            }
        }

        for (Player nearbyPlayer : PVPUtils.getNearbyPlayers(this.player, 30)) {
            this.blockList.forEach(block -> {
                nearbyPlayer.sendBlockChange(block.getLocation(), Math.random() < 0.5 ? Material.LEAVES: Material.SNOW_BLOCK, (byte) 0);
            });
        }

        for (Player nearbyPlayer : PVPUtils.getNearbyPlayersExceptPlayer(this.player, RADIUS)) {
            if (FactionUtils.cantPvPFaction(this.player, nearbyPlayer)) continue;
            if (!WorldGuardUtils.isPvPEnabled(this.player.getLocation())) continue;
            if (!WorldGuardUtils.isPvPEnabled(nearbyPlayer.getLocation())) continue;
            //nearbyPlayer.damage(this.damage);
            nearbyPlayer.damage(0);
            nearbyPlayer.sendMessage(CC.translate("&b&l*&f&l*&b&l* &3&lFROZEN TROPICS &7[&c-4HP&7] &b&l*&f&l*&b&l*"));
            if (nearbyPlayer.getHealth() < 4) continue;
            AstroDamageHandler.getInstance().getDamageHandler().dealDamage(nearbyPlayer, 2, "FrozenTropics");
        }
    }
}