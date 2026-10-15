package net.astropvp.customblocks.tasks;

import com.cosmicpvp.cosmicutils.utils.LocationUtils;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import net.astropvp.customblocks.CustomBlocks;
import net.astropvp.customblocks.struct.CustomBlock;
import net.astropvp.customblocks.struct.CustomBlockType;
import net.astropvp.customblocks.struct.abilities.ChunkAble;
import net.astropvp.customblocks.utils.ChunkUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChunkCustomBlocksTask extends BukkitRunnable {
    public static Map<Player, List<CustomBlockType>> playerChunkCustomBlocks = new ConcurrentHashMap<>();
    @Override
    public void run() {
        playerChunkCustomBlocks.clear();
        for (String key : CustomBlocks.getInstance().getCustomBlockManager().getCacheKeys()) {
            Location loc = LocationUtils.getLocationFromString(key, false);
            CustomBlock data = CustomBlocks.getInstance().getCustomBlockManager().getCustomBlockData(loc);

            for (Player player : PlayerUtil.getOnlinePlayers()) {
                if (ChunkUtil.isLocationAInSameChunkAsLocationB(loc, player.getLocation()) && data instanceof ChunkAble) {
                    List<CustomBlockType> blocks;
                    if (playerChunkCustomBlocks.containsKey(player)) {
                        blocks = playerChunkCustomBlocks.get(player);
                    } else {
                        blocks = new ArrayList<>();
                    }
                    blocks.add(data.getType());
                    playerChunkCustomBlocks.put(player, blocks);
                }
            }


        }
    }
}
