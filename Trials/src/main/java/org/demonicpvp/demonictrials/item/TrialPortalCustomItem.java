package org.demonicpvp.demonictrials.item;

import com.cosmicpvp.cosmicutils.utils.*;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.commands.WarpNotFoundException;
import com.gmail.filoghost.holograms.api.Hologram;
import com.gmail.filoghost.holograms.api.HolographicDisplaysAPI;
import com.google.common.collect.Maps;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.perms.Relation;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import me.ifamasssxd.cosmicskycustoms.struct.PlayerInteractable;
import net.ess3.api.InvalidWorldException;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.arkhamnetwork.Arkkit.patches.combatlog.CombatLog;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import org.demonicpvp.demonictrials.DemonicTrials;
import org.demonicpvp.demonictrials.manager.TrialManager;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.TrialState;
import org.demonicpvp.demonictrials.trial.trinket.ITrialTrinket;
import org.demonicpvp.demonictrials.util.Utils;
import org.javatuples.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.demonicpvp.demonictrials.command.AdminTrialsCommand.TRIAL_PORTAL_PLACEMENT_ENABLED;

public class TrialPortalCustomItem extends CosmicItem implements PlayerInteractable, Listener {
    public final static String TRIAL_PORTAL_BLOCK_KEY = "trialPortalBlock";
    private final static String LAST_TELEPORTED_KEY = "trialLastTeleported";
    private final static Map<String, Long> PORTAL_COOLDOWNS = Maps.newHashMap();

    private final Essentials essentials;

    public TrialPortalCustomItem() {
        super("trial_portal");
        this.essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
    }

    @Override
    public ItemStack createItem(Player player, Object... objects) {
        return this.applyItemData(
                CommonsUtil.createItem(
                        Material.EYE_OF_ENDER,
                        "&6&lTrial Portal",
                        "&7Right-Click to create a trial",
                        "&7portal to a Trial.",
                        "",
                        "&7Use &e/trials&7 for more information."
                )
        ).build();
    }

    @Override
    public void onPlayerInteractable(Player player, ItemStack item, NBTWrapper wrapper, PlayerInteractEvent event) {
        event.setCancelled(true);

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (!player.getWorld().getName().equals("world")) {
            CommonsUtil.sendMessage(player, "&c&l(!) &cTrial portals can only be spawned in the overworld.");
            return;
        }

        Block portalBlockA = event.getClickedBlock().getLocation().add(0.0, 1.0, 0.0).getBlock();
        Block portalBlockB = event.getClickedBlock().getLocation().add(0.0, 2.0, 0.0).getBlock();

        if (portalBlockA.getType() != Material.AIR || portalBlockB.getType() != Material.AIR) {
            CommonsUtil.sendMessage(player, "&c&l(!) &cTrial portals need a 1x2 area to be spawned.");
            return;
        }

        if (WorldGuardUtils.isPvPEnabled(portalBlockA.getLocation())) {
            Faction pFaction = FactionUtils.getFaction(player);

            if (!pFaction.isNormal() || !pFaction.getRelationTo(FactionUtils.getFactionAt(portalBlockA.getLocation())).isAtLeast(Relation.ALLY)) {
                    CommonsUtil.sendMessage(player, "&c&l(!) &cTrial portals may only be placed in Warzone or your faction/ally territory.");
                return;
            }
        }

        for (String warpName : this.essentials.getWarps().getList()) {
            try {
                Location warp = this.essentials.getWarps().getWarp(warpName);

                if (warp != null && warp.getWorld() != null && warp.getWorld().getName().equals(portalBlockA.getWorld().getName())) {
                    if (warp.distance(portalBlockA.getLocation()) < 4) {
                        CommonsUtil.sendMessage(player, "&c&l(!) &cYou cannot place this Trial portal here!");
                        return;
                    }
                }
            } catch (WarpNotFoundException | InvalidWorldException e) {
                e.printStackTrace();
            }
        }

        if (portalBlockA.getWorld().getSpawnLocation().distance(portalBlockA.getLocation()) < 4) {
            CommonsUtil.sendMessage(player, "&c&l(!) &cYou cannot place this Trial portal here!");
            return;
        }

        if (!TRIAL_PORTAL_PLACEMENT_ENABLED) {
            CommonsUtil.sendMessage(player, "&c&l(!) &cTrials are currently disabled! Please try again later.");
            return;
        }

        List<World> worlds = TrialManager.getAvailableWorlds();

        if (worlds.isEmpty()) {
            CommonsUtil.sendMessage(player, "&c&l(!) &cThere are too many active trials! Please try again later.");
            return;
        }

        if (item.getAmount() > 1) {
            player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
        } else {
            player.setItemInHand(null);
        }

        //self centered faggot nigger
//        boolean isQuantiom = player.getName().equals("quantiom");
        final int portalSecondsLeft = 30;

        //if (!isQuantiom) {
        //    CommonsUtil.sendMessage(player, "&c&l(!) &cTrials are still in development! Please try again later.");
        //    return;
        //}

        if (PORTAL_COOLDOWNS.containsKey(player.getUniqueId().toString())) {
            Long time = PORTAL_COOLDOWNS.get(player.getUniqueId().toString());

            if (time > System.currentTimeMillis()) {
                CommonsUtil.sendMessage(
                        player,
                        "&c&l(!) &cYou must wait " + Utils.formatTimeAbbreviated((time - System.currentTimeMillis()) / 1000L) + " before placing another trial portal."
                );
                return;
            }
        }

        List<Pair<ITrialTrinket, Integer>> portalTrinkets = TrialTrinketCustomItem.getTrinkets(item);

        TrialSession trial = new TrialSession(worlds.get(0), portalBlockA.getLocation(), portalTrinkets, portalSecondsLeft, MinecraftServer.currentTick, player);
        trial.resetRooms();

        TrialManager.addTrial(trial);

        PORTAL_COOLDOWNS.put(player.getUniqueId().toString(), System.currentTimeMillis() + TimeUnit.MINUTES.toSeconds(10L));

        portalBlockA.setMetadata(TRIAL_PORTAL_BLOCK_KEY, new FixedMetadataValue(DemonicTrials.getInstance(), trial.getId()));
        portalBlockB.setMetadata(TRIAL_PORTAL_BLOCK_KEY, new FixedMetadataValue(DemonicTrials.getInstance(), trial.getId()));

        portalBlockA.setTypeIdAndData(90, (byte) 0, false);
        portalBlockB.setTypeIdAndData(90, (byte) 0, false);

/*        ParticleEffect.playEffect(ParticleEffect.WITCH_MAGIC, portalBlockA.getLocation(), 0.45f, 50);
        ParticleEffect.playEffect(ParticleEffect.PORTAL, portalBlockA.getLocation(), 0.6f, 50);
        ParticleEffect.playEffect(ParticleEffect.PORTAL, portalBlockB.getLocation(), 0.6f, 50);*/

        final Hologram holoA = HolographicDisplaysAPI.createHologram(DemonicTrials.getInstance(), portalBlockB.getLocation().add(0.5, 1.5, 0.5), CommonsUtil.color("&a&lEntrance"));
        final Hologram holoB = HolographicDisplaysAPI.createHologram(DemonicTrials.getInstance(), portalBlockB.getLocation().add(0.5, 1.3, 0.5), CommonsUtil.color("&6&lTrial"));

        this.getNearbyPlayers(portalBlockA.getLocation())
                .forEach(p -> {
                    String distance = "";

                    if (p.getWorld().getName().equals("world")) {
                        distance = " &7(" + (int) trial.getPortalLocation().distance(p.getLocation()) + "m away)";
                    }

                    p.sendMessage("");
                    p.sendMessage(CommonsUtil.getCenteredMessage(CommonsUtil.color("&7&l&m----&e&l&m{ &6&l " + player.getName() + "'s /trial &e&l&m }&7&m----")));
                    p.sendMessage(CommonsUtil.getCenteredMessage(CommonsUtil.color("&6&lLocation: &e" + trial.getPortalLocation().getBlockX() + "x " + trial.getPortalLocation()
                            .getBlockY() + "y " + trial.getPortalLocation().getBlockZ() + "z " + distance)));

                    if (!portalTrinkets.isEmpty()) {
                        p.sendMessage(CommonsUtil.getCenteredMessage(CommonsUtil.color("&6&lTrinkets: " + portalTrinkets.stream().map(t -> t.getValue0().getFormattedName(t.getValue1())).collect(Collectors.joining("&f, ")))));
                    }

                    p.sendMessage(CommonsUtil.getCenteredMessage(CommonsUtil.color("&a&lOpen for " + portalSecondsLeft + "!")));
                    p.sendMessage("");
                });

        new BukkitRunnable() {
            private int secondsLeft = portalSecondsLeft;

            @Override
            public void run() {
                if (this.secondsLeft == 15 || this.secondsLeft == 10 || (this.secondsLeft <= 5 && this.secondsLeft >= 1)) {
                    TrialPortalCustomItem.this.getNearbyPlayers(portalBlockA.getLocation()).forEach(p -> {
                        String distance = "";

                        if (p.getWorld().getName().equals("world")) {
                            distance = " &7(" + (int) trial.getPortalLocation().distance(p.getLocation()) + "m away)";
                        }

                        p.sendMessage("");
                        p.sendMessage(CommonsUtil.getCenteredMessage(CommonsUtil.color("&7&l&m----&e&l&m{ &6&l " + player.getName() + "'s /trial &e&l&m }&7&m----")));
                        p.sendMessage(CommonsUtil.getCenteredMessage(CommonsUtil.color("&6&lLocation: &e" + trial.getPortalLocation().getBlockX() + "x " + trial.getPortalLocation()
                                .getBlockY() + "y " + trial.getPortalLocation().getBlockZ() + "z " + distance)));

                        p.sendMessage(CommonsUtil.getCenteredMessage(CommonsUtil.color("&a&lOpen for " + this.secondsLeft + "s!")));
                        p.sendMessage("");
                    });
                } else if (this.secondsLeft == 0) {
                    TrialPortalCustomItem.this.getNearbyPlayers(portalBlockA.getLocation()).forEach(p -> {
                        p.sendMessage("");
                        p.sendMessage(CommonsUtil.getCenteredMessage(CommonsUtil.color("&7&l&m----&e&l&m{ &6&l " + player.getName() + "'s /trial &e&l&m }&7&m----")));
                        p.sendMessage(CommonsUtil.getCenteredMessage(CommonsUtil.color("&a&lStarted!")));
                        p.sendMessage("");
                    });

                    holoA.delete();
                    holoB.delete();

                    if (!portalBlockA.getChunk().isLoaded()) {
                        portalBlockA.getChunk().load();
                    }

                    portalBlockA.removeMetadata(TRIAL_PORTAL_BLOCK_KEY, DemonicTrials.getInstance());
                    portalBlockB.removeMetadata(TRIAL_PORTAL_BLOCK_KEY, DemonicTrials.getInstance());

                    portalBlockA.setType(Material.AIR);
                    portalBlockB.setType(Material.AIR);

                    portalBlockA.getWorld().playSound(portalBlockA.getLocation(), Sound.AMBIENCE_THUNDER, 2.0f, 0.35f);
/*                    ParticleEffect.playEffect(ParticleEffect.WITCH_MAGIC, portalBlockA.getLocation(), 0.45f, 50);
                    ParticleEffect.playEffect(ParticleEffect.PORTAL, portalBlockA.getLocation(), 0.6f, 50);
                    ParticleEffect.playEffect(ParticleEffect.PORTAL, portalBlockB.getLocation(), 0.6f, 50);*/

                    if (trial.getPlayers().isEmpty()) {
                        TrialManager.removeTrial(trial);
                    } else {
                        trial.start();
                    }

                    this.cancel();
                }

                this.secondsLeft--;
            }
        }.runTaskTimer(DemonicTrials.getInstance(), 0L, 20L);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void on(BlockPhysicsEvent event) {
        if (event.getBlock().hasMetadata(TRIAL_PORTAL_BLOCK_KEY) || event.getBlock().hasMetadata("trialExitPortalBlock")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void on(BlockDamageEvent event) {
        if (event.getBlock().hasMetadata(TRIAL_PORTAL_BLOCK_KEY) || event.getBlock().hasMetadata("trialExitPortalBlock")) {
            WorldUtil.sendWorldEffect(event.getBlock(), 90, 2001);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void on(PlayerPortalEvent event) {
        event.setCancelled(true);
        this.handlePortalEvent(event);
    }


    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(PlayerTeleportEvent event) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            event.getPlayer().setMetadata(LAST_TELEPORTED_KEY, new FixedMetadataValue(DemonicTrials.getInstance(), System.currentTimeMillis()));
        }
    }

    private void handlePortalEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Block metadataBlock = player.getLocation().add(0.0, 1.0, 0.0).getBlock();

        if (metadataBlock.hasMetadata(TRIAL_PORTAL_BLOCK_KEY)) {
            int id = metadataBlock.getMetadata(TRIAL_PORTAL_BLOCK_KEY).get(0).asInt();

            TrialManager.getTrial(id).ifPresent(trial -> {
                if (trial.getState() == TrialState.LOADING) {
/*                    if (!DemonicClientComms.isOnCosmicClient(player)) {
                        CommonsUtil.sendMessage(player, "");
                        CommonsUtil.sendMessage(
                                player,
                                "&c&l(!) &cYou must be on DemonicClient to enter this trial. For more information, please use /discord and view the #cc-features channel."
                        );
                        CommonsUtil.sendMessage(player, "");
                        return;
                    }*/


                    if (CombatLog.inCombat(player)) {
                        CommonsUtil.sendMessage(player, "&c&l(!) &cYou must be out of combat to enter a trial.");
                        return;
                    }

                    if (player.hasMetadata(LAST_TELEPORTED_KEY) && System.currentTimeMillis() - player.getMetadata(LAST_TELEPORTED_KEY).get(0).asLong() < 2500) {
                        CommonsUtil.sendMessage(player, "&c&l(!) &cPlease wait a moment after teleporting before entering a trial.");
                        return;
                    }

                    if (trial.isFull()) {
                        CommonsUtil.sendMessage(player, "&c&l(!) &cThis trial is full!");
                        return;
                    }

                    trial.addPlayer(player);
                }
            });
        }
    }

    private List<Player> getNearbyPlayers(Location portalBlock) {
        return portalBlock.getWorld().getNearbyEntities(portalBlock, 64.0, 64.0, 64.0).stream()
                .filter(e -> e.getType() == EntityType.PLAYER)
                .map(e -> (Player) e)
                .collect(Collectors.toList());
    }
}
