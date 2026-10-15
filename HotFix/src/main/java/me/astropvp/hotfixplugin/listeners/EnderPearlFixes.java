package me.astropvp.hotfixplugin.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.ifamasssxd.cosmicduel.DuelAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class EnderPearlFixes implements Listener {
    /*private final List<Material> hollowMaterials;


    public EnderPearlFixes() {
        this.hollowMaterials = new ArrayList<>();
        hollowMaterials.add(Material.STEP);
        hollowMaterials.add(Material.FENCE);
    }


    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL && event.getTo().getY() < 1.0) {
            event.setCancelled(true);
        }


    }


    @EventHandler(priority = EventPriority.LOW)
    public void onEnderpearlTeleport(final PlayerTeleportEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            final Location to = e.getTo().clone();
            final Location from = e.getFrom().clone();
            final Player player = e.getPlayer();
            final Block toBlock = to.clone().add(0.0, 1.0, 0.0).getBlock();
            final Block toAbove = toBlock.getLocation().add(0.0, 1.0, 0.0).getBlock();
            if (to.getY() > from.getY()) {
                System.out.println("[AntiClip] toBlock=" + toBlock.getType() + " | toAbove=" + toAbove.getType());
                if (toBlock.getType().isSolid() && toAbove.getType().isSolid()) {
                    System.out.println("[AntiClip] Allowing pearl since both blocks are solid");
                    return;
                }
                if (toBlock.getType().isSolid() && !toAbove.getType().isSolid()) {
                    Location newLoc;
                    if (!player.isFlying() && !player.getAllowFlight()) {
                        System.out.println("[AntiClip] (newPatch v1) Adjusted Pearl Height (flight=false)");
                        newLoc = to.clone().add(0.0, -0.9, 0.0);

                    } else {
                        System.out.println("[AntiClip] (newPatch v1) Adjusted Pearl Height (flight=true)");
                        newLoc = to.clone().add(0.0, -1.5, 0.0);
                        if (newLoc.getBlock().getType() != Material.AIR) {
                            newLoc = to.clone().add(0.0, -1.0, 0.0);
                        }
                    }
                    System.out.println(toBlock.getType().name());
                    System.out.println(toAbove.getType().name());
                    e.setTo(newLoc);
                    return;
                }
                if (to.getBlock().getType() == Material.AIR && toBlock.getType().isSolid()) {
                    e.setTo(to.add(0.0, -0.5, 0.0));
                    System.out.println("[AntiClip] (newPatch v2) Adjusted Pearl Height -0.5");
                }
            } else {

                System.out.println(toBlock.getType().name());
                System.out.println(toAbove.getType().name());
                if (toBlock.getType() == Material.AIR && toAbove.getType() == Material.AIR) {
                    to.setY(to.getY()+1);
                    e.setTo(to);
                    return;
                }
                if (to.clone().add(0, -0.3, 0).getBlock().getType() == Material.AIR) {
                    System.out.println("setting +1 y loc");

                    //to.setY(to.getY()+1);
                    //e.setTo(to);
                    //e.setCancelled(true);
                    return;
                } else  if (toAbove.getType() != Material.AIR) {
                    System.out.println("[AntiClip] (Fixed BlockGlitch)");
                    to.setY(to.getY()+1);
                    e.setTo(to);

                }
                if (to.getY() > from.getY()) {
                    final Location newLoc = to.clone();
                    Bukkit.getLogger().info("to_upwards = " + newLoc + ", m:" + newLoc.getBlock().getType().name());
                    newLoc.setY(Math.ceil(newLoc.getY()));
                    if (this.isSolidBlock(newLoc) && !this.isSolidBlock(newLoc.clone().add(0.0, 1.0, 0.0))) {
                        Bukkit.getLogger().info("[AntiClip] Preventing (upwards) V-Clip teleport by " + e.getPlayer().getName() + " to " + e.getTo() + " (to_upwards:" + newLoc + ")");
                        e.setCancelled(true);
                        return;
                    }
                    if (this.isSolidBlock(newLoc.clone().add(0.0, 1.0, 0.0)) && !this.isSolidBlock(newLoc)) {
                        Bukkit.getLogger().info("[AntiClip] Preventing (upwards_v2) V-Clip teleport by " + e.getPlayer().getName() + " to " + e.getTo() + " (to_upwards:" + newLoc + ")");
                        e.setCancelled(true);
                        return;
                    }
                }
                if (this.isSolidBlock(to.add(0.0, 1.0, 0.0)) || this.isSolidBlock(from.add(0.0, 1.0, 0.0))) {
                    if (to.getY() >= 256.0 && to.getBlock().getType() == Material.BEDROCK && (!this.isSolidBlock(from) || from.getBlock().getType() == Material.BEDROCK)) {
                        return;
                    }
                    if (to.getY() > 3.0 && player.getItemInHand().getType() == Material.ENDER_PEARL && player.getLocation().distance(to) < 5.0) {
                        return;
                    }
                    Bukkit.getLogger().info("[AntiClip] Preventing V-Clip teleport by " + e.getPlayer().getName() + " to " + e.getTo());
                    e.setCancelled(true);
                }
            }
        }
    }


    private static final Set<Material> PASSABLE_MATERIALS = EnumSet.of(Material.ENCHANTMENT_TABLE, new Material[]{Material.STONE_SLAB2, Material.STEP, Material.WOOD_STEP, Material.FENCE_GATE, Material.ACACIA_FENCE_GATE, Material.BIRCH_FENCE_GATE, Material.DARK_OAK_FENCE_GATE, Material.JUNGLE_FENCE_GATE, Material.SPRUCE_FENCE_GATE, Material.DAYLIGHT_DETECTOR, Material.DAYLIGHT_DETECTOR_INVERTED, Material.STONE_PLATE, Material.GOLD_PLATE, Material.WOOD_PLATE, Material.IRON_PLATE, Material.ENDER_CHEST, Material.CHEST, Material.TRAPPED_CHEST});

    @EventHandler
    public void on(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof EnderPearl)) {
            return;
        }
        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }

        Player player = (Player) ((Object) event.getEntity().getShooter());
        Block hitBlock = this.getBlockHitting(event.getEntity().getLocation(), event.getEntity().getVelocity());
        if (hitBlock == null) {
            return;
        }
        if (PASSABLE_MATERIALS.contains((Object) hitBlock.getType())) {
            Material upperMaterial;
            Material type;
            if (DuelAPI.hasDuel(player)) {
                return;
            }
            if (player.getWorld().getName().equalsIgnoreCase("dungeon_planet_nul") || player.getWorld().getName().equalsIgnoreCase("dungeon_destroyed_outpost") || player.getWorld().getName().equalsIgnoreCase("dungeon_yijki_a")) {
                return;
            }
            Material upOneBlock = hitBlock.getLocation().add(0.0, 1.0, 0.0).getBlock().getType();
            if (upOneBlock == Material.AIR) {
                Location location = hitBlock.getLocation().clone().add(0.0, 0.5, 0.0);
                location.setYaw(player.getLocation().getYaw());
                location.setPitch(player.getLocation().getPitch());
                player.teleport(location, PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
                System.out.println("Deb 1");
                return;
            }
            Location blockBehindLocation = this.getLocationBehind(event.getEntity().getLocation(), event.getEntity().getVelocity(), 2.1);
            blockBehindLocation.setYaw(player.getLocation().getYaw());
            blockBehindLocation.setPitch(player.getLocation().getPitch());
            Block blockBehind = blockBehindLocation.getBlock();
            Material material = type = blockBehind == null ? null : blockBehind.getType();
            if (!(blockBehind != null && type != Material.AIR && !this.isPermeable(type) || (upperMaterial = blockBehind.getLocation().add(0.0, 1.0, 0.0).getBlock().getType()) != null && upperMaterial != Material.AIR)) {
                player.teleport(blockBehindLocation, PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
                System.out.println("Deb 2");

            }
        }
    }

    private Location getLocationBehind(Location location, Vector vector, double multiply) {
        Location behind = location.clone().add(vector.clone().setY(0).normalize().multiply(multiply));
        if (behind.getBlock() == location.getBlock()) {
            return this.getLocationBehind(location, vector, multiply + 0.1);
        }
        if (behind.getBlock().getType() == Material.AIR && this.isPermeable(behind.getBlock().getRelative(BlockFace.DOWN).getType())) {
            behind.subtract(0.0, 1.0, 0.0);
        }
        return behind;
    }

    private Block getBlockHitting(Location location, Vector vector) {
        BlockIterator iterator = new BlockIterator(location.getWorld(), location.toVector(), vector.normalize(), 0.0, 2);
        Block hitBlock = null;
        while (iterator.hasNext() && ((hitBlock = iterator.next()).getType() == Material.AIR || this.isPermeable(hitBlock.getType()))) {
        }
        return hitBlock;
    }

    private boolean isPermeable(Material material) {
        switch (material) {
            case AIR:
            case LAVA:
            case WATER:
            case STATIONARY_LAVA:
            case STATIONARY_WATER: {
                return true;
            }
        }
        return false;
    }


    private boolean isSolidBlock(final Location l) {
        return l.getBlock().getType() != Material.AIR && !this.hollowMaterials.contains(l.getBlock().getType());
    }*/
}
