package net.crankedpvp.arcade.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.CooldownUtils;
import net.astropvp.armorsets.AstroArmorSetsAPI;
import net.crankedpvp.arcade.ArcadeAPI;
import net.crankedpvp.arcade.ArcadePlugin;
import net.crankedpvp.arcade.cooldowns.GracePeriod;
import net.crankedpvp.arcade.language.Language;
import net.crankedpvp.arcade.struct.ArcadeManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.List;

public class ArcadeLogicListener implements Listener {
    private ArcadeManager arcadeManager;
    private ArcadePlugin plugin;

    public ArcadeLogicListener(ArcadePlugin plugin) {
        this.plugin = plugin;
        this.arcadeManager = plugin.getArcadeManager();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onArrowDamage(EntityDamageByEntityEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            if (event.getEntity() instanceof Player) {
                Player killed = (Player) event.getEntity();
                if (!ArcadeAPI.inArcadeWorld(killed.getUniqueId())) return;
                if (CooldownUtils.isCooldownActive(killed, "arcadeGracePeriod")) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player killer = (Player) event.getDamager();
            Player killed = (Player) event.getEntity();
            if (!ArcadeAPI.inArcadeWorld(killer.getUniqueId())) return;
            if (!ArcadeAPI.inArcadeWorld(killed.getUniqueId())) return;
            if (!ArcadeAPI.inArcadeMatch(killer.getUniqueId()) && !ArcadeAPI.inArcadeMatch(killed.getUniqueId())) {

                if (CooldownUtils.isCooldownActive(killed, "arcadeGracePeriod")) {
                    killer.sendMessage(CC.translate(Language.GRACE_PERIOD_OTHER.getMessage()).replace("{victim}", killed.getName()).replace("{time}", CooldownUtils.getFormattedCooldown(killed, "arcadeGracePeriod")));
                    event.setCancelled(true);
                    return;
                }

                if (CooldownUtils.isCooldownActive(killer, "arcadeGracePeriod")) {
                    killer.sendMessage(CC.translate(Language.GRACE_PERIOD.getMessage()).replace("{time}", CooldownUtils.getFormattedCooldown(killer, "arcadeGracePeriod")));
                    event.setCancelled(true);
                    return;
                }
                arcadeManager.startMatch(killer.getUniqueId(), killed.getUniqueId());
                return;
            }

            if (!ArcadeAPI.canPvP(killer.getUniqueId(), killed.getUniqueId())) {
                killer.sendMessage(CC.translate(Language.CANT_PVP.getMessage().replace("{victim}", killed.getName())));
                event.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (ArcadeAPI.inArcadeMatch(event.getPlayer().getUniqueId())) {
            arcadeManager.endMatch(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        if (ArcadeAPI.inArcadeMatch(event.getPlayer().getUniqueId())) {
            arcadeManager.endMatch(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (ArcadeAPI.inArcadeMatch(event.getEntity().getUniqueId())) {
            arcadeManager.endMatch(event.getEntity().getUniqueId());
            killer.sendMessage(CC.translate(Language.MATCH_KILLED.getMessage().replace("{victim}", event.getEntity().getName())));
            Bukkit.broadcastMessage(Language.MATCH_KILLED_BROADCAST.getMessage().replace("{killer}", killer.getName()).replace("{killed}", event.getEntity().getName()));
            CooldownUtils.registerCooldown(killer, new GracePeriod("arcadeGracePeriod", 30));

        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
       if (!event.getTo().getWorld().getName().equals(ArcadeAPI.WORLD_NAME) && ArcadeAPI.inArcadeMatch(event.getPlayer().getUniqueId())) {
           arcadeManager.endMatch(event.getPlayer().getUniqueId());
       }
    }

    @EventHandler
    public void onTeleportArcadeDisabled(PlayerTeleportEvent event) {
        if (event.getTo().getWorld().getName().equals(ArcadeAPI.WORLD_NAME) && !arcadeManager.isEnabled() && !event.getPlayer().isOp() && event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            event.getPlayer().sendMessage(CC.translate("&c&l(!)&c You cannot teleport to the arcade world while it is disabled!"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onJoinInDisabledArcade(PlayerJoinEvent event) {
        if (!arcadeManager.isEnabled() && event.getPlayer().getLocation().getWorld().getName().equals(ArcadeAPI.WORLD_NAME)) {
            event.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
        }
    }

    @EventHandler
    public void teleportTo(PlayerTeleportEvent event) {
        if (event.getTo().getWorld().getName().equals(ArcadeAPI.WORLD_NAME) && event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL && !event.getPlayer().hasPermission("cranked.admin")) {
            event.getPlayer().setFlying(false);
            event.getPlayer().setAllowFlight(false);
        }
    }

    @EventHandler
    public void teleportToNoSet(PlayerTeleportEvent event) {
        if (event.getTo().getWorld().getName().equals(ArcadeAPI.WORLD_NAME) && event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL && !event.getPlayer().hasPermission("cranked.admin")) {

            if (AstroArmorSetsAPI.getBonusCrystalsEquipped(event.getPlayer()).isEmpty() && !event.getPlayer().hasPermission("astro.staff")) {
                event.setCancelled(true);
                Language.TELEPORT_NO_SET.send(event.getPlayer());
            }
        }
    }

    @EventHandler
    public void onFly(PlayerToggleFlightEvent event) {
        if (!event.getPlayer().getWorld().getName().equals(ArcadeAPI.WORLD_NAME)) return;
        if (event.getPlayer().hasPermission("astro.admin")) return;
        event.setCancelled(true);
    }


    @EventHandler (priority = EventPriority.LOWEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (!event.getPlayer().getWorld().getName().equals(ArcadeAPI.WORLD_NAME)) return;
        Player player = event.getPlayer();
        if (player.hasPermission("astro.admin")) {
            return;
        }
        String command = event.getMessage().replace("/", "");
        List<String> whitelistedCommands = Language.WHITELISTED_COMMANDS.getValuesAsList();

        if (whitelistedCommands.stream().anyMatch(command::startsWith)) return;

        event.setCancelled(true);
        Language.BLACKLISTED_COMMAND.send(player);
    }

}
