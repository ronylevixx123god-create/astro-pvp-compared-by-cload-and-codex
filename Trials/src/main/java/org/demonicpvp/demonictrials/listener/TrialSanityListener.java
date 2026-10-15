package org.demonicpvp.demonictrials.listener;

import com.cosmicpvp.cosmicutils.utils.CommonsUtil;

import org.arkhamnetwork.Arkkit.patches.chat_filter.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.demonicpvp.demonictrials.DemonicTrials;
import org.demonicpvp.demonictrials.manager.TrialManager;
import org.demonicpvp.demonictrials.trial.TrialSession;

import java.util.Arrays;
import java.util.Optional;

import static org.demonicpvp.demonictrials.item.TrialPortalCustomItem.TRIAL_PORTAL_BLOCK_KEY;

public class TrialSanityListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void on(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        String[] allowedCommands = { "trial", "toggle", "toggles" };

        if (Arrays.stream(allowedCommands).noneMatch(e -> event.getMessage().startsWith("/" + e)) && TrialManager.getTrial(player).isPresent() && !player.hasPermission("demonictrials.commandbypass")) {
            CommonsUtil.sendMessage(player, "&c&l(!) &cYou cannot use that command in a Trials session!");
            CommonsUtil.sendMessage(player, "&7Use &f/trials leave&7 to leave the trial.");

            event.setCancelled(true);
            event.setMessage("/trialsblocked " + event.getMessage());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(PlayerTeleportEvent event) {
        if (!event.getFrom().getWorld().equals(event.getTo().getWorld()) && event.getTo().getWorld().getName().startsWith("trials_")) {
            Player player = event.getPlayer();

            if (player.isOp() || (player.hasPermission("essentials.tpo") || player.hasPermission("essentials.*"))) {
                return;
            }

            if (event.getFrom().getWorld().getName().equals(event.getTo().getWorld().getName())) {
                event.setCancelled(true);
                return;
            }

            if (!event.getFrom().getBlock().hasMetadata(TRIAL_PORTAL_BLOCK_KEY) && !event.getFrom().clone().add(0.0D, 1.0D, 0.0D).getBlock().hasMetadata(TRIAL_PORTAL_BLOCK_KEY)) {
                event.setCancelled(true);
                CommonsUtil.sendMessage(player, "&c&l(!) &cYou can only teleport into trials through trial portals.");
            }
        }
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        TrialManager.getTrial(event.getPlayer()).ifPresent(trial -> trial.removePlayer(event.getPlayer(), TrialSession.TrialPlayerRemoveReason.LOGGED_OUT));
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Optional<TrialSession> trialOptional = TrialManager.getTrial(player);

        if (trialOptional.isPresent()) {
            trialOptional.get().removePlayer(player, TrialSession.TrialPlayerRemoveReason.DIED);
        } else if (player.getWorld().getName().startsWith("trials_")) {
            player.getInventory().clear();
            player.getInventory().setArmorContents(new ItemStack[player.getInventory().getArmorContents().length]);

            Location spawnLocation = Bukkit.getWorlds().get(0).getSpawnLocation();

            player.setMetadata("bypassCombatTeleport", new FixedMetadataValue(DemonicTrials.getInstance(), spawnLocation));
            player.teleport(spawnLocation, PlayerTeleportEvent.TeleportCause.UNKNOWN);
        }
    }

    @EventHandler
    public void on(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player && TrialManager.getTrial((Player)event.getEntity()).isPresent()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void on(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player entity = (Player) event.getEntity();

            TrialManager.getTrial(damager).ifPresent(trial -> {
                if (trial.getPlayers().contains(entity)) {
                    CommonsUtil.sendMessage(damager, "&c&l(!) &cYou cannot damage your teammates!");
                    event.setDamage(0);
                    event.setCancelled(true);
                }
            });
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (player.getHealth() - event.getFinalDamage() > 0) return;
        TrialManager.getTrial(player).ifPresent(trial -> {
            event.setCancelled(true);
            player.setHealth(20);

            CommonsUtil.sendMessage(player, "&6&l[Trials] &cYou have died and been eliminated from the trial!");
            trial.removePlayer(player, TrialSession.TrialPlayerRemoveReason.DIED);
        });
    }

    @EventHandler(priority = EventPriority.LOW)
    public void on(PlayerDeathEvent event) {
        Player player = event.getEntity();

        TrialManager.getTrial(player).ifPresent(trial -> {
            CommonsUtil.sendMessage(player, "&6&l[Trials] &cYou have died and been eliminated from the trial!");
            trial.removePlayer(player, TrialSession.TrialPlayerRemoveReason.DIED_REAL);
        });
    }

    @EventHandler
    public void on(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

/*        if (DemonicTournaments.getInstance().getSavedInventoriesManager().getSavedInventories().containsKey(player.getUniqueId())) {
            DemonicTournaments.getInstance().getSavedInventoriesManager().restoreInventory(player);
        }*/
    }

    @EventHandler
    public void on(BlockBreakEvent event) {
        Player player = event.getPlayer();

        TrialManager.getTrial(player).ifPresent(trial -> {
            if (!player.hasPermission("demonictrials.breakblocks")) {
                event.setCancelled(true);
            }
        });
    }

    @EventHandler
    public void on(PlayerDropItemEvent event) {
        TrialManager.getTrial(event.getPlayer()).ifPresent(trial -> {
            if (trial.getCurrentRoom() == null || !trial.getCurrentRoom().canDropItems()) {
                CommonsUtil.sendMessage(event.getPlayer(), "&c&l(!) &cYou cannot drop items in this room!");
                event.setCancelled(true);
            }
        });
    }

    @EventHandler
    public void on(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            Player player = (Player)event.getEntity();

            TrialManager.getTrial(player).ifPresent(trial -> {
                event.setCancelled(true);
            });
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        TrialManager.getTrial(player).ifPresent(trial -> {
            //if (CosmicToggles.get().getTogglesManager().isToggleEnabled(player, CosmicToggles.get().getTogglesManager().getRegisteredToggle("trialchat"))) {
                for (Player p : trial.getPlayers()) {
                    if (player.hasPermission("arkkit.chatfilterbypass")) {
                        p.sendMessage(CommonsUtil.color("&6&l[&e&lTrials&6&l] &e") + player.getName() + CommonsUtil.color(": &6") + ChatColor.stripColor(event.getMessage()));
                    } else {
                        p.sendMessage(CommonsUtil.color("&6&l[&e&lTrials&6&l] &e") + player.getName() + CommonsUtil.color(": &6") + ChatUtils.getFilteredMessage(ChatColor.stripColor(event.getMessage()), p, true));
                    }
                }

                DemonicTrials.getInstance().getLogger().info("[TrialsChat] " + player.getName() + ": " + event.getMessage());

                event.setCancelled(true);
            //}
        });
    }





    private void cancelIfInTrial(Player player, Cancellable cancellable) {
        if (TrialManager.getTrial(player).isPresent()) cancellable.setCancelled(true);
    }
}
