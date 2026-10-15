package org.demonicpvp.demonictrials.trial.room.impl;

import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.demonicpvp.demonictrials.manager.TrialManager;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.room.TrialRoom;
import org.demonicpvp.demonictrials.util.Utils;
import org.demonicpvp.demonictrials.util.WorldEditHelper;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class MysteryCubeRoom extends TrialRoom {
    private List<MysteryCube> finalCubes;
    private List<MysteryCube> usedCubes;
    private List<MysteryCube> unusedCubes;

    private MysteryCube firstUsedCube;
    private MysteryCube secondUsedCube;

    private MysteryCube completionCube;

    private Location firstLever;
    private Location secondLever;

    public MysteryCubeRoom(TrialSession trial) {
        super(trial);
    }

    @Override
    public String getName() {
        return "Mystery Cube";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.DARK_RED;
    }

    @Override
    public Material getMaterial() {
        return Material.LEVER;
    }

    @Override
    public List<String> getDescription() {
        return Lists.newArrayList(
                "Navigate the cubes and reach the end",
                "before the time runs out!"
        );
    }

    @Override
    public Location getSpawnLocation() {
        return new Location(this.trial.getWorld(), 2040.5, 213, 2088.5, -90, 0);
    }

    @Override
    public Set<Location> getExitPortalLocations() {
        return Sets.newHashSet(new Location(this.trial.getWorld(), 2083, 248, 2088));
    }

    @Override
    public void start() {
        super.start();

        this.sendToolTipAndMessage();

        this.trial.getPlayers().forEach(p -> {
            p.getInventory().setArmorContents(this.trial.getHazmatArmor());
        });

        Location startGlass = new Location(this.trial.getWorld(), 2044, 213, 2088);

        for (int y = 0; y <= 2; y++) {
            startGlass.clone().add(0, y, 0).getBlock().setType(Material.AIR);
        }
    }

    @Override
    public void reset() {
        super.reset();
        WorldEditHelper.pasteSchematic("trials_mystery_cube", new Location(this.trial.getWorld(), 2000, 100, 2000));

        this.usedCubes = Lists.newArrayList();
        this.unusedCubes = Lists.newArrayList();
        this.finalCubes = Lists.newArrayList();

        this.firstLever = new Location(this.trial.getWorld(), 2053, 213, 2086);
        this.secondLever = new Location(this.trial.getWorld(), 2053, 213, 2090);

        this.parseCubes();
    }

    private void parseCubes() {
        Location startingPlace = new Location(this.trial.getWorld(), 2051, 213, 2088);
        Location startParsing = new Location(this.trial.getWorld(), 2051, 213, 2056, -90, 0);

        for (int x = 0; x < 5; x++) {
            for (int z = 0; z < 5; z++) {
                for (int y = 0; y < 5; y++) {
                    Location teleportLocation = startParsing.clone().add(x * 16, y * 7, z * 16);

                    if (!teleportLocation.equals(startingPlace)) {
                        this.unusedCubes.add(new MysteryCube(null, teleportLocation));
                    }
                }
            }
        }

        this.firstUsedCube = this.unusedCubes.get(new Random().nextInt(this.unusedCubes.size()));
        this.secondUsedCube = this.unusedCubes.get(new Random().nextInt(this.unusedCubes.size()));

        this.usedCubes.add(this.firstUsedCube);
        this.usedCubes.add(this.secondUsedCube);

        this.unusedCubes.remove(this.firstUsedCube);
        this.unusedCubes.remove(this.secondUsedCube);

        while (!this.unusedCubes.isEmpty()) {
            MysteryCube usedCube = this.usedCubes.get(new Random().nextInt(this.usedCubes.size()));
            MysteryCube unusedCube = this.unusedCubes.get(new Random().nextInt(this.unusedCubes.size()));

            usedCube.addChild(unusedCube);
            this.usedCubes.add(unusedCube);

            if (usedCube.getChildren().size() >= 2) {
                this.finalCubes.add(usedCube);
                this.usedCubes.remove(usedCube);
            }

            this.unusedCubes.remove(unusedCube);
        }

        this.finalCubes.addAll(this.usedCubes);
        this.completionCube = this.finalCubes.get(new Random().nextInt(this.finalCubes.size()));
    }

    @EventHandler
    public void on(PlayerInteractEvent event) {
        if (!this.hasStarted || this.hasEnded) return;

        Player player = event.getPlayer();

        TrialManager.getTrial(player).ifPresent(trial -> {
            if (trial.getCurrentRoom() != this) {
                return;
            }

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Block clickedBlock = event.getClickedBlock();

                if (clickedBlock.getType() == Material.LEVER) {
                    String endLever = Utils.locationToString(new Location(this.trial.getWorld(), 2085, 248, 2088));
                    String location = Utils.locationToString(clickedBlock.getLocation());

                    if (location.equals(Utils.locationToString(this.firstLever))) {
                        this.trial.teleportPlayer(player, this.firstUsedCube.getTeleportLocation());
                    } else if (location.equals(Utils.locationToString(this.secondLever))) {
                        this.trial.teleportPlayer(player, this.secondUsedCube.getTeleportLocation());
                    } else if (location.equals(endLever)) {
                        this.trial.onRoomComplete();
                    } else {
                        for (MysteryCube finalCube : this.finalCubes) {
                            MysteryCube teleportCube = null;

                            if (location.equals(Utils.locationToString(finalCube.getLeverOne()))) {
                                if (finalCube.getChildren().isEmpty()) {
                                    CommonsUtil.sendMessage(player, "&6&l[Trials] &cThis is a dead end!");
                                    break;
                                } else {
                                    this.trial.teleportPlayer(player, (teleportCube = finalCube.getChildren().get(0)).getTeleportLocation());
                                    CommonsUtil.sendMessage(player, "&6&l[Trials] &eYou used the left lever!");
                                }
                            } else if (location.equals(Utils.locationToString(finalCube.getLeverTwo()))) {
                                if (finalCube.getChildren().size() < 2) {
                                    CommonsUtil.sendMessage(player, "&6&l[Trials] &cThis is a dead end!");
                                    break;
                                } else {
                                    this.trial.teleportPlayer(player, (teleportCube = finalCube.getChildren().get(1)).getTeleportLocation());
                                    CommonsUtil.sendMessage(player, "&6&l[Trials] &eYou used the right lever!");
                                }
                            } else if (location.equals(Utils.locationToString(finalCube.getBackLever()))) {
                                if (finalCube.getParentCube() != null) {
                                    this.trial.teleportPlayer(player, (teleportCube = finalCube.getParentCube()).getTeleportLocation());
                                    CommonsUtil.sendMessage(player, "&6&l[Trials] &eYou used the back lever!");
                                } else {
                                    this.trial.teleportPlayer(player, new Location(this.trial.getWorld(), 2051, 213, 2088, -90, 0));
                                    CommonsUtil.sendMessage(player, "&6&l[Trials] &eYou have returned to the start!");
                                }
                            }

                            if (teleportCube != null) {
                                if (teleportCube.equals(this.completionCube)) {
                                    this.trial.teleportPlayer(player, new Location(this.trial.getWorld(), 2083, 248, 2088));
                                    this.trial.sendMessage("&a&l(!) &a" + player.getName() + " has found the completion cube!");
                                }

                                break;
                            }
                        }
                    }
                }
            }
        });
    }

    private static class MysteryCube {
        private MysteryCube parentCube;

        private final Location teleportLocation;
        private final Location leverOne;
        private final Location leverTwo;
        private final List<MysteryCube> children;
        private final Location backLever;

        public MysteryCube(MysteryCube parentCube, Location teleportLocation) {
            this.parentCube = parentCube;
            this.teleportLocation = teleportLocation.clone().add(0.5, 0, 0.5);
            this.children = Lists.newArrayList();
            this.backLever = teleportLocation.clone().add(2, 0, 0);
            this.leverOne = this.backLever.clone().add(0, 0, -2);
            this.leverTwo = this.backLever.clone().add(0, 0, 2);

            this.backLever.setYaw(0);
            this.backLever.setPitch(0);

            this.leverOne.setYaw(0);
            this.leverOne.setPitch(0);

            this.leverTwo.setYaw(0);
            this.leverTwo.setPitch(0);
        }

        public void addChild(MysteryCube child) {
            child.setParentCube(this);
            this.children.add(child);
        }

        public void setParentCube(MysteryCube parentCube) {
            this.parentCube = parentCube;
        }

        public MysteryCube getParentCube() {
            return this.parentCube;
        }

        public List<MysteryCube> getChildren() {
            return this.children;
        }

        public Location getTeleportLocation() {
            return this.teleportLocation;
        }

        public Location getLeverOne() {
            return this.leverOne;
        }

        public Location getLeverTwo() {
            return this.leverTwo;
        }

        public Location getBackLever() {
            return this.backLever;
        }
    }
}
