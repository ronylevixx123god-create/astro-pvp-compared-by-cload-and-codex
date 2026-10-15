package net.crankedpvp.arcade.tasks;

import net.crankedpvp.arcade.utils.ChunkUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArcadeBorderPlayers extends BukkitRunnable {
    public static List<UUID> borderPlayers = new ArrayList<>();
    public static List<Location> borderBlocks = ChunkUtils.getRegionBlocks("arcade_blocks");
    @Override
    public void run() {
        if (borderPlayers.isEmpty()) return;
        for (UUID uuid : borderPlayers) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline()) {
                borderPlayers.remove(uuid);
                continue;
            }
            for (Location location : borderBlocks) {
                player.sendBlockChange(location, Material.STAINED_GLASS, (byte) 14);
            }
        }
    }
}
