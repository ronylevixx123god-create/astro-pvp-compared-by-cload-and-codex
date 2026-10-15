package me.astropvp.hotfixplugin.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AntiDoorHit implements Listener {
    private final Map<UUID, Long> doorHits;

    public AntiDoorHit() {
        this.doorHits = new HashMap<>();
    }

    @EventHandler
    public void openDoor(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) return;
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock().getType().name().endsWith("_DOOR")) {
            doorHits.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            if (!canHit((Player) e.getDamager())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (event.getItem() != null && event.getItem().getType() == Material.MONSTER_EGG) {
                if (event.getItem().getDurability() == 50) {
                    if (!canHit(event.getPlayer())) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getCause() == BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL) {
            Block ignitedBlock = event.getBlock();
            Player player = event.getPlayer();

            if (player != null && ignitedBlock.getType() == Material.MONSTER_EGG) {
                if (isClosedDoorBetween(player.getLocation().getBlock(), ignitedBlock)) {
                    event.setCancelled(true);
                    //player.sendMessage("You cannot ignite the creeper egg because there is a closed door in the way.");
                }
            }
        }
    }

    private boolean isClosedDoorBetween(Block start, Block end) {
        int x1 = start.getX();
        int y1 = start.getY();
        int z1 = start.getZ();
        int x2 = end.getX();
        int y2 = end.getY();
        int z2 = end.getZ();

        int dx = Integer.compare(x2, x1);
        int dy = Integer.compare(y2, y1);
        int dz = Integer.compare(z2, z1);

        int x = x1, y = y1, z = z1;

        while (x != x2 || y != y2 || z != z2) {
            Block block = start.getWorld().getBlockAt(x, y, z);

            if (block.getType() == Material.WOODEN_DOOR || block.getType() == Material.IRON_DOOR_BLOCK) {
                org.bukkit.material.Door door = (org.bukkit.material.Door) block.getState().getData();
                if (!door.isOpen()) {
                    return true;
                }
            }

            // Move towards end block
            if (x != x2) x += dx;
            if (y != y2) y += dy;
            if (z != z2) z += dz;
        }

        return false;
    }

    public boolean canHit(Player player) {
        if (doorHits.containsKey(player.getUniqueId())) {
            long lastOpened = doorHits.get(player.getUniqueId());
            int timeBetweenDoorHit = 850;
            return System.currentTimeMillis() >= lastOpened + timeBetweenDoorHit;
        }
        return true;
    }
}
