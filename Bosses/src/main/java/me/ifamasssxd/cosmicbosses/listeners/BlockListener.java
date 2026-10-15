package me.ifamasssxd.cosmicbosses.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class BlockListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerPlaceInterdimensional(final BlockPlaceEvent event) {
        if (event.getItemInHand() != null && event.getItemInHand().getType() == Material.BEACON) {
            final String s = new NBTWrapper(event.getItemInHand()).getString("cosmicType");
            if (s != null && s.equals("dimensionalCore")) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot place this item.");
            }
        }
    }

    @EventHandler
    public void onWaterMove(final BlockFromToEvent e) {
        for (final Boss boss : Boss.bosses.values()) {
            if (boss.getModifier() != null && boss.getModifier().containsLocation(e.getBlock().getLocation())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = false)
    public void onChunkUnload(final ChunkUnloadEvent event) {
        CosmicBosses.getInstance().getBlockChangeManager().getStoredBlocks().keySet().stream().filter(loc -> event.getChunk().getX() == loc.getChunk().getX() && event.getChunk().getZ() == loc.getChunk().getZ() && loc.getChunk().getWorld().getName().equals(event.getChunk().getWorld().getName())).forEach(loc -> CosmicBosses.getInstance().getBlockChangeManager().restoreBlock(loc));
    }
}
