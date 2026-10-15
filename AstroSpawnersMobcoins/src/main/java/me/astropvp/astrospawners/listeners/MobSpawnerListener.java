package me.astropvp.astrospawners.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import me.astropvp.astrospawners.AstroSpawners;
import me.astropvp.astrospawners.data.SpawnerData;
import me.astropvp.astrospawners.data.SpawnerManager;
import me.astropvp.astrospawners.items.ItemSpawner;
import me.astropvp.factionlogs.struct.impl.SpawnerPlace;
import me.astropvp.factionlogs.utils.FactionsLogsAPI;
import me.venir.astrocollectors.collector.Collector;
import me.venir.astrocollectors.collector.CollectorManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import me.astropvp.astrospawners.utils.EntityUtils;

public class MobSpawnerListener implements Listener {

    //This is when you place a new spawner
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getType() != Material.MOB_SPAWNER) {
            return;
        }

        boolean allowSpawners = AstroSpawners.getInstance().getConfig().getBoolean("allow-spawner-placement");

        if (!allowSpawners) {
            event.getPlayer().sendMessage(CC.translate("&c&l(!)&c Spawner placement has been disabled!"));
            event.getPlayer().sendMessage(CC.translate("&7You can no longer place FTOP value."));
            event.setCancelled(true);
            return;
        }

        ItemStack item = event.getPlayer().getItemInHand();
        if (item.getAmount() > 1) {
            event.getPlayer().sendMessage(CC.translate("&c&l(!)&c You cannot place stacked spawners!"));
            event.getPlayer().sendMessage(CC.translate("&7Combine them by drag and dropping them together or type /stackspawners"));
            event.setCancelled(true);
            return;
        }


        EntityType type = ItemSpawner.getEntityTypeFromItem(item);

        if (FactionUtils.isWilderness(event.getBlock().getLocation())) {
            event.getPlayer().sendMessage(CC.translate("&c&l(!)&c You are not allowed to place spawners in wilderness!"));
            event.getPlayer().sendMessage(CC.translate("&7Type &f/f claim&7 to claim this land and place spawners."));
            event.setCancelled(true);
            return;
        }

        if (!SpawnerManager.canPlaceStackedSpawnerInThisChunk(event.getBlock().getChunk(), type)) {
            event.getPlayer().sendMessage(CC.translate("&c&l(!)&c This chunk already contains this spawner type."));
            event.getPlayer().sendMessage(CC.translate("&cPlease stack it on the existing spawner."));
            event.setCancelled(true);
            return;
        }

        if (type == null) {
            event.setCancelled(true);
            return;
        }

        if (AstroSpawners.coolDownSpawnerPlace.contains(event.getPlayer())) {
            event.getPlayer().sendMessage(CC.translate("&c&l(!)&c Please be patient, you are still on spawner place cooldown."));
            event.setCancelled(true);
            return;
        }


        int amount = ItemSpawner.getSpawnerAmount(item);
        if (amount <= 0) {
            event.setCancelled(true);
            return;
        }
        int math = Math.min(amount, ItemSpawner.getSpawnerLimit());
        int spawnersLeft = amount - math;
        event.getPlayer().sendMessage(CC.translate("&a&l+ &a&n" + amount + "&a " + EntityUtils.getEntityName(type) + " Spawner(s) &7[" + amount + " Total]"));


        AstroSpawners.coolDownSpawnerPlace.add(event.getPlayer());
        Bukkit.getScheduler().runTaskLaterAsynchronously(AstroSpawners.getInstance(), () -> {
            AstroSpawners.coolDownSpawnerPlace.remove(event.getPlayer());
        }, 20L);
        Location location = event.getBlock().getLocation();

        SpawnerData spawnerData = new SpawnerData(type, math, location);
        AstroSpawners.getInstance().getSpawnerManager().addToCache(spawnerData);

        event.getPlayer().setItemInHand(spawnersLeft <= 0 ? null : ItemSpawner.getItem(type, spawnersLeft));
        Bukkit.getScheduler().runTaskLater(AstroSpawners.getInstance(), (Runnable) new BukkitRunnable() {
            @Override
            public void run() {
                location.getBlock().setType(Material.MOB_SPAWNER);
                ((CreatureSpawner) location.getBlock().getState()).setSpawnedType(type);
                if (CollectorManager.DELEGATE.isCollectorContained(event.getBlock().getLocation())) {
                    Collector collector = CollectorManager.DELEGATE.getCollector(event.getBlock().getLocation());
                    collector.clearSpawners();
                    collector.scanSpawners(collector.getLocation());
                }
            }

        }, 1);
        event.setCancelled(true);

        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(event.getPlayer());
        if (!fPlayer.getFaction().isWilderness()) {
            FactionsLogsAPI.log(fPlayer.getFaction().getId(), new SpawnerPlace(amount, location, type, fPlayer.getPlayer().getUniqueId()));
        }
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.NOTE_PLING, 10f, 10f);

    }


    //This is when you place a spawner on a different spawner
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onSpawnerPlaceOnSpawner(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.MOB_SPAWNER) {
            if (event.getBlockAgainst().getType() == Material.MOB_SPAWNER) {
                EntityType type = ((CreatureSpawner) event.getBlockAgainst().getState()).getSpawnedType();
                if (ItemSpawner.getEntityTypeFromItem(event.getPlayer().getItemInHand()) == type) {
                    int amount = ItemSpawner.getSpawnerAmount(event.getPlayer().getItemInHand());
                    if (amount <= 0) {
                        event.setCancelled(true);
                        return;
                    }

                    if (AstroSpawners.coolDownSpawnerPlace.contains(event.getPlayer())) {
                        event.getPlayer().sendMessage(CC.translate("&c&l(!)&c Please be patient, you are still on spawner place cooldown."));
                        event.setCancelled(true);
                        return;
                    }


                    //CHANGED
                    event.setCancelled(true);
                    Player player = event.getPlayer();
                    AstroSpawners.coolDownSpawnerPlace.add(event.getPlayer());
                    Bukkit.getScheduler().runTaskLaterAsynchronously(AstroSpawners.getInstance(), () -> {
                        AstroSpawners.coolDownSpawnerPlace.remove(event.getPlayer());
                    }, 20L);

                    SpawnerData found = AstroSpawners.getInstance().getSpawnerManager().getSpawnerData(event.getBlockAgainst().getLocation());
                    if (found != null) {
                        if (found.getSpawnerAmount() >= ItemSpawner.getSpawnerLimit()) {
                            player.sendMessage(CC.RedB + "(!) " + CC.Red + "You can not have more than "
                                    + ItemSpawner.getSpawnerLimit() + " spawners stacked together!");
                            event.setCancelled(true);
                            return;
                        }

                        boolean allowSpawners = AstroSpawners.getInstance().getConfig().getBoolean("allow-spawner-placement");


                        if (!allowSpawners) {
                            event.getPlayer().sendMessage(CC.translate("&c&l(!)&c Spawner placement has been disabled!"));
                            event.getPlayer().sendMessage(CC.translate("&7You can no longer place FTOP value."));
                            event.setCancelled(true);
                            return;
                        }

                        if (event.getItemInHand().getAmount() > 1) {
                            event.getPlayer().sendMessage(CC.translate("&c&l(!)&c You cannot place stacked spawners!"));
                            event.getPlayer().sendMessage(CC.translate("&7Combine them by drag and dropping them together"));
                            event.setCancelled(true);
                            return;
                        }

                        int math = found.getSpawnerAmount() + amount >= ItemSpawner.getSpawnerLimit()
                                ? ItemSpawner.getSpawnerLimit() - found.getSpawnerAmount()
                                : amount;
                        int spawnersLeft = amount - math;
                        found.setSpawnerAmount(found.getSpawnerAmount() + math);
                        player.sendMessage(CC.translate("&a&l+ &a&n" + amount + "&a " + EntityUtils.getEntityName(type) + " Spawner(s) &7[" + found.getSpawnerAmount() + " Total]"));
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 10f, 10f);
                        if (CollectorManager.DELEGATE.isCollectorContained(event.getBlock().getLocation())) {
                            Collector collector = CollectorManager.DELEGATE.getCollector(event.getBlock().getLocation());
                            collector.clearSpawners();
                            collector.scanSpawners(collector.getLocation());
                        }
                        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(event.getPlayer());
                        if (!fPlayer.getFaction().isWilderness()) {
                            FactionsLogsAPI.log(fPlayer.getFaction().getId(), new SpawnerPlace(amount, event.getBlockAgainst().getLocation(), type, fPlayer.getPlayer().getUniqueId()));
                        }
                        player.setItemInHand(
                                spawnersLeft <= 0 ? null : ItemSpawner.getItem(type, spawnersLeft));
                    } else {
                        player.sendMessage(CC.translate("&c&l(!)&c This is not a valid stacked spawner. Contact an administrator if you think this is an issue."));
                        event.setCancelled(true);
                        return;
                    }
                    player.updateInventory();

                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSpawnerInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) return;
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getItem() == null) {
            Block block = event.getClickedBlock();
            if (block.getType() == Material.MOB_SPAWNER) {
                event.setCancelled(true);
                EntityType type = ((CreatureSpawner) block.getState()).getSpawnedType();

                SpawnerData found = AstroSpawners.getInstance().getSpawnerManager().getSpawnerData(event.getClickedBlock().getLocation());
                Player player = event.getPlayer();
                player.sendMessage("");
                player.sendMessage(CC.GreenB + "Spawner Info");
                player.sendMessage(CC.GreenB + " * " + CC.White + "Type: " +
                        CC.Green + EntityUtils.getEntityName(type));
                player.sendMessage(CC.GreenB + " * " + CC.White + "Amount: " + CC.Green
                        + (found != null ? found.getSpawnerAmount() : 1) + "x");
                player.sendMessage("");
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 10f, 10f);
            }

        }
    }
}
