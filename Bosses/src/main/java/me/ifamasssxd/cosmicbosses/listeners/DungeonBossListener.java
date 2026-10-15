package me.ifamasssxd.cosmicbosses.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import java.util.Random;

public class DungeonBossListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPortal(final PlayerPortalEvent event) {
        final Block portalBlock = event.getPlayer().getLocation().getBlock();
        if (portalBlock.hasMetadata("temporalRift") || portalBlock.hasMetadata("armorMagnetPortal")) {
            event.setCancelled(true);
            event.useTravelAgent(false);
            if (event.getPlayer().isDead() || BossUtils.isInGodMode(event.getPlayer())) {
                return;
            }
            if (!event.getPlayer().getGameMode().equals((Object) GameMode.CREATIVE)) {
                event.getPlayer().setHealth(0.0);
            }
            event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.WITHER_SPAWN, 3.0f, 1.4f);
        }
    }

    @EventHandler
    public void onCreeperExplode(EntityExplodeEvent event) {
        if (event.getEntity().getType() == EntityType.CREEPER && event.getEntity().getCustomName() != null && event.getEntity().getCustomName().equals(CC.translate("&6&lCreepy Healer"))) {
            Boss.bosses.values().stream().filter(b -> b.getLocation().getWorld() == event.getEntity().getLocation().getWorld() && b.getBossType() == BossType.CORRUPT_COSMONAUT).findAny().ifPresent(boss -> {
                if (boss.getMinions().contains(event.getEntity())) {
                    boss.getMinions().remove(event.getEntity());

                    if (boss.getEntity().getLocation().distance(event.getEntity().getLocation()) <= 6.0) {
                        boss.onHeal(20 + new Random().nextInt(250));
                    }
                }
            });
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.getWorld().getName().equalsIgnoreCase("dungeon_planet_nul")) {
            if (player.getLocation().getBlock().getType() == Material.LAVA || player.getLocation().getBlock().getType() == Material.STATIONARY_LAVA) {
                if (player.getGameMode() != GameMode.CREATIVE && player.getHealth() != 0.0) {
                    player.setHealth(0.0);
                    player.sendMessage(CC.translate("&c&l(!) &cYou fell into the scorching lava and were incinerated instantly."));
                }
            }
        }
    }

/*    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION && event.getDamager().getType() == EntityType.CREEPER) {
            Bukkit.broadcastMessage("1");
            if (event.getDamager().getCustomName().equals(CC.translate("&6&lCreepy Healer")) && event.getEntity().getType() == EntityType.SKELETON) {
                Bukkit.broadcastMessage("2");
                Boss.bosses.values().stream().filter(b -> b.getLocation().getWorld() == event.getEntity().getLocation().getWorld() && b.getBossType() == BossType.CORRUPT_COSMONAUT).findAny().ifPresent(boss -> {
                    Bukkit.broadcastMessage("healing");
                    boss.onHeal(20 + new Random().nextInt(100));
                });
            }
        }
    }*/
}
