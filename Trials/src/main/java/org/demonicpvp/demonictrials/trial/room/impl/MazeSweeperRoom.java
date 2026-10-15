package org.demonicpvp.demonictrials.trial.room.impl;

import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.demonicpvp.demonictrials.DemonicTrials;
import org.demonicpvp.demonictrials.manager.TrialManager;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.TrialTier;
import org.demonicpvp.demonictrials.trial.room.TrialRoom;
import org.demonicpvp.demonictrials.util.WorldEditHelper;
import org.javatuples.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class MazeSweeperRoom extends TrialRoom {
    private Location leverOne;
    private Location leverTwo;

    private List<List<Location>> trapBlockPaths;
    private List<Pair<Integer, Boolean>> trapBlockIndices;

    public MazeSweeperRoom(TrialSession trial) {
        super(trial);
    }

    @Override
    public String getName() {
        return "Maze Sweeper";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.DARK_PURPLE;
    }

    @Override
    public Material getMaterial() {
        return Material.REDSTONE_BLOCK;
    }

    @Override
    public List<String> getDescription() {
        return Lists.newArrayList(
                "Get to the end of the maze",
                "before the time runs out!"
        );
    }

    @Override
    public Location getSpawnLocation() {
        return new Location(this.trial.getWorld(), -120.5, 142, 419.5, -90, 0);
    }

    @Override
    public Set<Location> getExitPortalLocations() {
        return Sets.newHashSet(
                new Location(this.trial.getWorld(), -123, 135, 419),
                new Location(this.trial.getWorld(), -152, 135, 419)
        );
    }

    @Override
    public TrialTier getRollUntil() {
        return TrialTier.MASTER;
    }

    @Override
    public void teleportPlayersToSpawn() {
        Location spawnOne = this.getSpawnLocation();
        Location spawnTwo = new Location(this.trial.getWorld(), -153.5, 142, 419.5, 90, 0);

        List<Player> players = new ArrayList<>(this.trial.getPlayers());
        Collections.shuffle(players);

        boolean otherSpawn = false;

        for (Player player : players) {
            this.trial.teleportPlayer(player, otherSpawn ? spawnTwo : spawnOne);
            otherSpawn = !otherSpawn;
        }
    }

    @Override
    public void reset() {
        super.reset();
        WorldEditHelper.pasteSchematic("trials_maze_sweeper", new Location(this.trial.getWorld(), -200, 63, 355));

        // remove levers because they are not used
        Location leverLocation = new Location(this.trial.getWorld(), -176, 142, 368);
        Location secondLeverLocation = new Location(this.trial.getWorld(), -107, 142, 371);

        for (int i = 0; i <= 5; i++) {
            leverLocation.clone().add(0, i, 0).getBlock().setType(Material.AIR);
            secondLeverLocation.clone().add(0, i, 0).getBlock().setType(Material.AIR);
        }

        this.trapBlockPaths = Lists.newLinkedList();
        this.trapBlockIndices = Lists.newLinkedList();

        this.parseTrapBlocks(new Location(this.trial.getWorld(), -194, 141, 359), new Location(this.trial.getWorld(), -81, 141, 424)); // 1st layer
        this.parseTrapBlocks(new Location(this.trial.getWorld(), -194, 134, 359), new Location(this.trial.getWorld(), -81, 134, 424)); // 2nd layer

        for (List<Location> trapBlockPath : this.trapBlockPaths) {
            for (Location location : trapBlockPath) {
                location.getBlock().setType(Material.STAINED_GLASS);
            }
        }
    }

    @Override
    public void start() {
        super.start();

        this.sendToolTipAndMessage();

        Location startingGlass = new Location(this.trial.getWorld(), -118, 142, 419);
        Location startingGlassTwo = new Location(this.trial.getWorld(), -157, 142, 419);

        for (int y = 0; y <= 2; y++) {
            startingGlass.clone().add(0, y, 0).getBlock().setType(Material.AIR);
            startingGlassTwo.clone().add(0, y, 0).getBlock().setType(Material.AIR);
        }

        this.leverOne = new Location(this.trial.getWorld(), -121, 135, 419);
        this.leverTwo = new Location(this.trial.getWorld(), -154, 135, 419);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (MazeSweeperRoom.this.trial.getPlayers().isEmpty() || MazeSweeperRoom.this.hasEnded) {
                    this.cancel();
                    return;
                }

                for (int i = 0; i < MazeSweeperRoom.this.trapBlockPaths.size(); i++) {
                    List<Location> path = MazeSweeperRoom.this.trapBlockPaths.get(i);

                    Pair<Integer, Boolean> currentIdx = MazeSweeperRoom.this.trapBlockIndices.get(i);
                    Integer idx = currentIdx.getValue0();
                    Boolean shouldReverse = currentIdx.getValue1();

                    int nextIdx;
                    boolean newReverseValue = shouldReverse;

                    if (shouldReverse) {
                        nextIdx = idx - 1;

                        if (nextIdx < 0) {
                            nextIdx = idx + 1;
                            newReverseValue = false;
                        }
                    } else {
                        nextIdx = idx + 1;

                        if (nextIdx > path.size() - 1) {
                            nextIdx = idx - 1;
                            newReverseValue = true;
                        }
                    }

                    path.get(idx).getBlock().setType(Material.STAINED_GLASS);

                    Location newTrapBlockLocation = path.get(nextIdx);
                    newTrapBlockLocation.getBlock().setType(Material.REDSTONE_BLOCK);

                    for (Player player : MazeSweeperRoom.this.trial.getPlayers()) {
                        if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation().equals(newTrapBlockLocation)) {
                            MazeSweeperRoom.this.onTrapBlock(player);
                        }
                    }

                    MazeSweeperRoom.this.trapBlockIndices.set(i, Pair.with(nextIdx, newReverseValue));
                }
            }
        }.runTaskTimer(DemonicTrials.getInstance(), 0L, 15L);
    }

    private void onTrapBlock(Player player) {
        Location spawnOne = new Location(this.trial.getWorld(), -120.5, 142, 419.5, -90, 0);
        Location spawnTwo = new Location(this.trial.getWorld(), -153.5, 142, 419.5, 90, 0);

        CommonsUtil.sendMessage(player, "&c&l(!) &cYou hit a trap block and have been teleported back to the start!");

        this.trial.teleportPlayer(player, Math.random() < 0.5 ? spawnOne : spawnTwo);
    }

    private void parseTrapBlocks(Location startCorner, Location endCorner) {
        int deltaX = Math.abs(endCorner.getBlockX() - startCorner.getBlockX());
        int deltaZ = Math.abs(endCorner.getBlockZ() - startCorner.getBlockZ());

        for (int x = 0; x <= deltaX; x++) {
            for (int z = 0; z <= deltaZ; z++) {
                Location offsetLocation = startCorner.clone().add(x, 0, z);

                if (offsetLocation.getBlock().getType() == Material.WOOL && offsetLocation.getBlock().getData() == 15 && this.trapBlockPaths.stream().noneMatch(e -> e.stream().anyMatch(e2 -> e2.equals(offsetLocation)))) {
                    this.addNewTrapPath(offsetLocation);
                } else if (offsetLocation.getBlock().getType() == Material.WOOL) {
                    offsetLocation.getBlock().setType(Material.STAINED_GLASS);

                    // 50% chance to block it
                    /*if (Math.random() < 0.5) {
                        offsetLocation.clone().add(0, 1, 0).getBlock().setTypeIdAndData(Material.STAINED_CLAY.getId(), (byte)9, false);

                        for (int y = 2; y <= 6; y++) {
                            offsetLocation.clone().add(0, y, 0).getBlock().setTypeIdAndData(Material.STAINED_GLASS.getId(), (byte)14, false);
                        }
                    }*/
                }
            }
        }
    }

    private void addNewTrapPath(Location location) {
        List<Location> currentPath = Lists.newLinkedList();
        Queue<Location> queue = new ConcurrentLinkedQueue<>();

        currentPath.add(location);
        queue.add(location);

        while (!queue.isEmpty()) {
            Location poll = queue.poll();

            Vector[] offsets = {
                    new Vector(1, 0, 0),
                    new Vector(-1, 0, 0),
                    new Vector(0, 0, 1),
                    new Vector(0, 0, -1)
            };

            for (Vector offset : offsets) {
                Location add = poll.clone().add(offset);

                if (add.getBlock().getType() == Material.WOOL && add.getBlock().getData() == 15 && !currentPath.contains(add)) {
                    currentPath.add(add);
                    queue.add(add);
                    break;
                }
            }
        }

        if (currentPath.size() <= 3) {
            return;
        }

        this.trapBlockPaths.add(currentPath);
        this.trapBlockIndices.add(Pair.with(0, false));
    }

    @EventHandler(ignoreCancelled = true)
    public void on(PlayerInteractEvent event) {
        if (!this.hasStarted) return;

        Player player = event.getPlayer();

        TrialManager.getTrial(player).ifPresent(trial -> {
            if (trial.getCurrentRoom() != this) {
                return;
            }

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if ((event.getClickedBlock().getLocation().equals(this.leverOne) || event.getClickedBlock().getLocation().equals(this.leverTwo)) && !this.hasEnded) {
                    for (Player trialPlayer : this.trial.getPlayers()) {
                        this.trial.teleportPlayer(trialPlayer, this.leverOne.clone().add(0.0, 1.0, 0.0));
                    }

                    this.trial.onRoomComplete();
                }
            }
        });
    }

    @EventHandler
    public void on(PlayerMoveEvent event) {
        if (!this.hasStarted) return;

        Player player = event.getPlayer();

        TrialManager.getTrial(player).ifPresent(trial -> {
            if (trial.getCurrentRoom() != this) {
                return;
            }

            if (event.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.REDSTONE_BLOCK) {
                this.onTrapBlock(player);
            }
        });
    }
}
