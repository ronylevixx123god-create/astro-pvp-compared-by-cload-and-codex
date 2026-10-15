package me.ifamasssxd.cosmicbosses.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import me.ifamasssxd.cosmicbosses.utils.BossEggUtils;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class EggListener implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void onInteractPLayer(final PlayerInteractEvent e) {
        if (e.hasItem()) {
            final Player p = e.getPlayer();
            final ItemStack is = p.getItemInHand();
            final BossType type = BossEggUtils.getBossType(is);
            if (type != null) {
                final boolean alreadyCancelled = e.isCancelled();
                e.setCancelled(true);
                e.setUseInteractedBlock(Event.Result.DENY);
                e.setUseItemInHand(Event.Result.DENY);
                if (!e.hasBlock() || alreadyCancelled) {
                    return;
                }
                final Block block = e.getClickedBlock();
                final Block above = block.getRelative(BlockFace.UP);
                if (!CosmicBosses.getInstance().isEggEnabled()) {
                    p.sendMessage(ChatColor.RED + "Bosses are currently disabled!");
                    return;
                }
                final int highestY = BossUtils.getHighestBlockYAt(block.getLocation(), false);
                if (block.getY() < highestY - 1 && !block.getWorld().getName().equals("world_private") && (!p.isOp() || !p.isSneaking())) {
                    p.sendMessage(ChatColor.RED + "You must spawn this boss at the highest Y coordinate.");
                    return;
                }

                if (!WorldGuardUtils.getRegionsIn(block.getLocation()).contains("spawnwarzone")) {
                    p.sendMessage(CC.translate("&c&l(!)&c You must spawn this boss in the /spawn warzone!"));
                    return;
                }

                if ((p.isOp() && p.isSneaking()) || (above.isEmpty() && above.getRelative(BlockFace.UP).isEmpty() && WorldGuardUtils.isPvPEnabled(block.getLocation()) && BossUtils.isWarzone(block.getLocation()))) {
                    if (Boss.bosses.values().stream().filter(Boss::isBossSpawned).count() > 10L) {
                        p.sendMessage(CC.RedB + "(!) " + CC.Red + "There are currently too many active bosses to summon this creature!");
                        return;
                    }
                    final Location spawnLoc = block.getLocation().add(0.5, 1.0, 0.5);
                    if (is.getAmount() > 1) {
                        is.setAmount(is.getAmount() - 1);
                    } else {
                        p.setItemInHand((ItemStack) null);
                    }
                    p.updateInventory();
                    final Boss boss = type.createInstance(spawnLoc);
                    boss.onBossSpawn();
                    if (boss.getEntity() != null) {
                        boss.getEntity().setMetadata("playerSpawnedBoss", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "You cannot spawn this boss here.");
                }
            }
        }
    }
}
