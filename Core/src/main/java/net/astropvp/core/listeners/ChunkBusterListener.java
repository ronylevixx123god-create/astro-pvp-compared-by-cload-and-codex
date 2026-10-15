package net.astropvp.core.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.perms.Role;

import me.astropvp.astrospawners.AstroSpawners;
import me.astropvp.astrospawners.configs.DeleteSpawner;
import me.astropvp.astrospawners.data.SpawnerData;
import me.astropvp.astrospawners.data.SpawnerManager;
import me.astropvp.factionlogs.struct.impl.ChunkBust;
import me.astropvp.factionlogs.struct.impl.SpawnerBreak;
import me.astropvp.factionlogs.utils.FactionsLogsAPI;
import net.astropvp.core.utils.BlocksUtil;
import net.astropvp.core.utils.ChunkBusterUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Dropper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class ChunkBusterListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (item.isSimilar(ChunkBusterUtils.getItem())) {
            if (event.getPlayer().getWorld().getEnvironment() != World.Environment.NORMAL) {
                event.getPlayer().sendMessage(ChatColor.RED + "You can only use chunk busters in the overworld.");
                event.setCancelled(true);
                return;
            }
            if ((FactionUtils.getFaction(event.getPlayer()) == null) || (FactionUtils.getFaction(event.getPlayer()).getId().equals("0"))) {
                event.getPlayer().sendMessage(ChatColor.RED + "You can only use chunk busters in your territory.");
                event.setCancelled(true);
                return;
            }

            if (FactionUtils.getFPlayer(event.getPlayer()).getRole() == Role.NORMAL || FactionUtils.getFPlayer(event.getPlayer()).getRole() == Role.RECRUIT || FactionUtils.getFPlayer(event.getPlayer()).getRole() == Role.MODERATOR || FactionUtils.getFPlayer(event.getPlayer()).getRole() == Role.ALT) {
                event.getPlayer().sendMessage(CC.translate("Your faction role is not high enough to place this!"));
                event.getPlayer().sendMessage(CC.translate("&c&l* &cRequired Role: &4Co-Leader or Higher."));
                event.setCancelled(true);
                return;
            }
            if (!FactionUtils.getFactionAt(event.getBlock().getLocation()).getId().equals(FactionUtils.getFaction(event.getPlayer()).getId())) {
                event.getPlayer().sendMessage(ChatColor.RED + "You can only use chunk busters in your territory.");
                event.setCancelled(true);
                return;
            }

            int X = event.getBlock().getChunk().getX() * 16;
            int Z = event.getBlock().getChunk().getZ() * 16;
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 0; y < 256; y++) {
                        Block block = event.getBlock().getWorld().getBlockAt(X + x, y, Z + z);
                        if ((block.getType() != Material.BEDROCK) && (block.getType() != Material.AIR) && (block.getType() != Material.JUKEBOX) && (block.getType() != Material.NOTE_BLOCK) &&   (block.getType() != Material.MOB_SPAWNER)  && (block.getType() != Material.BEACON)) {
                            if (block.getType() == Material.CHEST || block.getType() == Material.DISPENSER || block.getType() == Material.DROPPER) {
                                if (block.getType() == Material.CHEST) {
                                    Chest c = (Chest) block.getState();
                                    c.getBlockInventory().clear();
                                }

                                if (block.getType() == Material.DISPENSER) {
                                    Dispenser d = (Dispenser) block.getState();
                                    d.getInventory().clear();
                                }

                                if (block.getType() == Material.DROPPER) {
                                    Dropper d = (Dropper) block.getState();
                                    d.getInventory().clear();
                                }

                            }
                            BlocksUtil.setAir(block, false);
                        }
                    }
                }
            }
            event.getPlayer().sendMessage(CC.translate("&a&l(!)&a Finished chunkbusting the whole chunk."));
            FPlayer fPlayer = FPlayers.getInstance().getByPlayer(event.getPlayer());
            if (!fPlayer.getFaction().isWilderness()) {
                FactionsLogsAPI.log(fPlayer.getFaction().getId(), new ChunkBust(event.getBlock().getLocation(), fPlayer.getPlayer().getUniqueId()));
            }
        }
    }
}
