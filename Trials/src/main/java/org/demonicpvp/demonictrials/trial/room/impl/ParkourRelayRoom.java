package org.demonicpvp.demonictrials.trial.room.impl;

import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Button;

import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.TrialTier;
import org.demonicpvp.demonictrials.trial.room.TrialRoom;
import org.demonicpvp.demonictrials.util.WorldEditHelper;
import org.github.paperspigot.Title;

import java.util.*;

public class ParkourRelayRoom extends TrialRoom {
    private Player runner;
    private int currentPoints;
    private int pointsNeeded;

    private List<Location> activationBlocks;
    private List<Location> tpBlocks;
    private Location currentActivationBlock;

    public ParkourRelayRoom(TrialSession trial) {
        super(trial);
    }

    @Override
    public String getName() {
        return "Parkour Relay";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.GOLD;
    }

    @Override
    public Material getMaterial() {
        return Material.GOLD_INGOT;
    }

    @Override
    public List<String> getDescription() {
        return Lists.newArrayList(
                "Reach the point goal by finding the",
                "activation blocks before the time runs out!"
        );
    }

    @Override
    public Location getSpawnLocation() {
        return new Location(this.trial.getWorld(), 756.5, 140, 754.5);
    }

    @Override
    public Set<Location> getExitPortalLocations() {
        return Sets.newHashSet(new Location(this.trial.getWorld(), 756, 130, 754));
    }

    @Override
    public TrialTier getRollUntil() {
        return TrialTier.MASTER;
    }

    @Override
    public void updateScoreboard(Player player, List<String> lines) {
        lines.add("&f");
        lines.add(this.trial.getCurrentTier().getColor() + "&lRunner");
        lines.add("&7" + this.runner.getName());
        lines.add("&1&2");
        lines.add(this.trial.getCurrentTier().getColor() + "&lPoints");
        lines.add("&7" + this.currentPoints + " / " + this.pointsNeeded);
    }

    @Override
    public void reset() {
        super.reset();

        WorldEditHelper.pasteSchematic("trials_parkour_relay", new Location(this.trial.getWorld(), 700, 100, 700));

        this.currentPoints = 0;
        this.pointsNeeded = Math.min(Math.max(3, this.trial.getPlayers().size()), 8);

        Location cornerOne = new Location(this.trial.getWorld(), 747, 126, 708);
        Location cornerTwo = new Location(this.trial.getWorld(), 765, 146, 799);

        int deltaX = cornerTwo.getBlockX() - cornerOne.getBlockX();
        int deltaZ = cornerTwo.getBlockZ() - cornerOne.getBlockZ();
        int deltaY = cornerTwo.getBlockY() - cornerOne.getBlockY();

        this.activationBlocks = Lists.newArrayList();
        this.tpBlocks = Lists.newArrayList(
                new Location(this.trial.getWorld(), 756, 137, 794),
                new Location(this.trial.getWorld(), 756, 137, 714)
        );

        // secret parkour path thing
        Lists.newArrayList(
                new Location(this.trial.getWorld(), 750, 129, 758),
                new Location(this.trial.getWorld(), 750, 130, 761),
                new Location(this.trial.getWorld(), 747, 130, 761),
                new Location(this.trial.getWorld(), 747, 131, 758),
                new Location(this.trial.getWorld(), 747, 132, 755),
                new Location(this.trial.getWorld(), 750, 133, 755),
                new Location(this.trial.getWorld(), 749, 134, 757)
        ).forEach(l -> l.getBlock().setType(Material.BARRIER));

        Block button = new Location(this.trial.getWorld(), 751, 140, 754).getBlock();
        button.setType(Material.STONE_BUTTON);
        BlockState state = button.getState();
        Button blockData = (Button) state.getData();
        blockData.setFacingDirection(BlockFace.EAST);
        blockData.setData((byte)2);
        state.update();

        for (Location tpBlock : this.tpBlocks) {
            tpBlock.getBlock().setType(Material.EMERALD_BLOCK);
        }

        for (int x = 0; x <= deltaX; x++) {
            for (int z = 0; z <= deltaZ; z++) {
                for (int y = 0; y <= deltaY; y++) {
                    Location loopLocation = cornerOne.clone().add(x, y, z);

                    // skip the spawn platform
                    if (loopLocation.getBlockZ() >= 748 && loopLocation.getBlockZ() <= 760) continue;

                    Material type = loopLocation.getBlock().getType();

                    if (type == Material.WOOL/* || type == Material.ACACIA_FENCE || type == Material.CARPET*/) {
                        Material replaceMaterial = Material.AIR;
                        byte replaceData = 0;

                        byte data = loopLocation.getBlock().getData();

                        if (type == Material.WOOL) {
                            switch (data) {
                                case 14:
                                    replaceMaterial = Material.STAINED_CLAY;
                                    replaceData = 14;
                                    break;
                                case 8:
                                    replaceMaterial = Material.STEP;
                                    break;
                                case 7:
                                    replaceMaterial = Material.COBBLE_WALL;
                                    break;
                            }
                        }

                        if (replaceMaterial != Material.AIR) {
                            loopLocation.getBlock().setTypeIdAndData(replaceMaterial.getId(), replaceData, false);
                        }

                        if (loopLocation.getBlock().getRelative(BlockFace.UP).getType() == Material.AIR) {
                            this.activationBlocks.add(loopLocation);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void start() {
        super.start();

        this.sendToolTipAndMessage();

        this.pickRandomRunner(true);

        new Location(this.trial.getWorld(), 756, 139, 754).getBlock().setType(Material.AIR);
    }

    public void pickRandomRunner(boolean steppedOnBlock) {
        List<Player> players = new ArrayList<>(this.trial.getPlayers());
        Player oldRunner = this.runner;

        if (steppedOnBlock) {
            Collections.shuffle(this.activationBlocks);
            this.currentActivationBlock = this.activationBlocks.get(0);

            for (Player p : this.trial.getPlayers()) {
/*                CosmicPacketHandler.sendPacketToPlayer(p, new SPingAlliesPacket(
                        "Target Block",
                        this.currentActivationBlock.getBlockX(),
                        this.currentActivationBlock.getBlockY(),
                        this.currentActivationBlock.getBlockZ(),
                        0,
                        false
                ));*/

                this.trial.sendMessage("&6&l[Trials] &eThe next target block is at &6" + this.currentActivationBlock.getBlockX() + "x " + this.currentActivationBlock.getBlockY() + "y " + this.currentActivationBlock.getBlockZ() + "z");
            }
        }

        if (oldRunner != null) {
            if (this.trial.getPlayers().size() == 1 || this.trial.getPlayers().isEmpty()) {
                return;
            }

            players.remove(oldRunner);
        }

        this.runner = players.get(new Random().nextInt(players.size()));

        this.runner.sendTitle(new Title(CommonsUtil.color("&6&lRUNNER"), CommonsUtil.color("&7You have been selected as the runner!"), 0, 30, 5));

        for (Player p : this.trial.getPlayers()) {
            if (oldRunner != null) {
                /*CosmicPacketHandler.sendPacketToPlayer(p,
                        new SPlayerOutlineColor(
                                oldRunner.getEntityId(),
                                CosmicPacketHandler.rgbaToInt(
                                        0,
                                        0,
                                        0,
                                        0
                                )
                        )
                );*/
            }

            /*CosmicPacketHandler.sendPacketToPlayer(p,
                    new SPlayerOutlineColor(
                            this.runner.getEntityId(),
                            CosmicPacketHandler.rgbaToInt(
                                    255,
                                    0,
                                    0,
                                    255
                            )
                    )
            );*/

            p.getInventory().setItem(0, this.trial.getItemWithTrialLore(new ItemStack(Material.DIAMOND_PICKAXE)));

            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
        }

        this.trial.sendMessage("");
        this.trial.sendMessage(CommonsUtil.getCenteredMessage("&6&lParkour Relay"));
        this.trial.sendMessage(CommonsUtil.getCenteredMessage("&6&l" + this.runner.getName() + "&f is the new runner!"));
        this.trial.sendMessage(CommonsUtil.getCenteredMessage("&7They must stand on an activation block to earn a point."));
        this.trial.sendMessage("");
    }

    @EventHandler(ignoreCancelled = true)
    public void on(PlayerInteractEvent event) {
        if (!this.hasStarted || this.hasEnded) return;

        Location buttonLocation = new Location(this.trial.getWorld(), 751, 140, 754);

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getLocation().equals(buttonLocation)) {
            Location spawn = new Location(this.trial.getWorld(), 756.5, 130, 754.5);
            this.pickRandomRunner(false);
            this.trial.teleportPlayer(event.getPlayer(), spawn);
        }
    }

    @EventHandler
    public void on(PlayerMoveEvent event) {
        if (!this.hasStarted) return;

        Player player = event.getPlayer();

        if (event.getTo().getWorld().equals(this.trial.getWorld())) {
            Location spawn = new Location(this.trial.getWorld(), 756.5, 130, 754.5);
            boolean isRunner = player.getUniqueId().equals(this.runner.getUniqueId());

            // fell off the map
            if (event.getTo().getY() < 126 && !this.trial.getPlayersEnteredNextRoomPortal().contains(player)) {
                CommonsUtil.sendMessage(player, "&c&l(!) &cYou have fallen and been teleported back to the start!");
                this.trial.teleportPlayer(player, spawn);

                if (!this.hasEnded && isRunner) {
                    /*if (this.currentPoints > 0) {
                        this.currentPoints--;
                        this.trial.sendMessage("&c&l(!) &cA point has been taken for " + player.getName() + " falling!");
                    }*/

                    this.pickRandomRunner(false);
                }

                return;
            }

            if (isRunner && !this.hasEnded) {
                Location blockLocation = event.getTo().getBlock().getRelative(BlockFace.DOWN).getLocation();

                if (this.tpBlocks.stream().anyMatch(blockLocation::equals)) {
                    this.trial.teleportPlayer(player, spawn);
                } else if (blockLocation.equals(this.currentActivationBlock)) {
                    this.trial.sendMessage("&a&l(!) &a" + player.getName() + " has found the activation block! (+1 Point)");

                    for (Player p : this.trial.getPlayers()) {
                        //CosmicPacketHandler.sendPacketToPlayer(p, new SHitSplatPacket(0, CommonsUtil.color("&a+1 Point"), "HEALTH", blockLocation.getX(), blockLocation.getY(), blockLocation.getZ(), 0));
                    }

                    this.trial.teleportPlayer(player, spawn);
                    this.currentPoints++;

                    if (this.currentPoints >= this.pointsNeeded) {
                        this.trial.onRoomComplete();

                       /* for (Player p : this.trial.getPlayers()) {
                            CosmicPacketHandler.sendPacketToPlayer(p,
                                    new SPlayerOutlineColor(
                                            this.runner.getEntityId(),
                                            CosmicPacketHandler.rgbaToInt(0, 0, 0, 0)
                                    )
                            );
                        }*/
                    } else {
                        this.pickRandomRunner(true);
                    }
                }
            }
        }
    }

    @Override
    public void onPlayerRemove(Player player) {
        if (this.hasStarted && !this.hasEnded && this.runner.getUniqueId().equals(player.getUniqueId())) {
            this.pickRandomRunner(false);
        }
    }
}
