package org.demonicpvp.demonictrials.trial.room.impl;

import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Button;
import org.bukkit.material.Lever;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.demonicpvp.demonictrials.DemonicTrials;
import org.demonicpvp.demonictrials.manager.TrialManager;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.room.TrialRoom;
import org.demonicpvp.demonictrials.util.WorldEditHelper;
import org.github.paperspigot.Title;
import org.javatuples.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class SimonRoom extends TrialRoom {
    private Location mainLever;
    private List<Location> underLeverWhiteBlocks;
    private Map<SimonColor, SimonColorInputLocations> colorToInputLocations;

    private SimonState state;
    private int progress;

    private List<Pair<SimonColor, SimonColorMode>> currentOrder;
    private int currentOrderIdx;

    private boolean onInputCooldown;

    public SimonRoom(TrialSession trial) {
        super(trial);
    }

    @Override
    public String getName() {
        return "Simon";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.GREEN;
    }

    @Override
    public Material getMaterial() {
        return Material.LEVER;
    }

    @Override
    public List<String> getDescription() {
        return Lists.newArrayList(
                "Earn enough points to progress",
                "before the time runs out!"
        );
    }

    @Override
    public Location getSpawnLocation() {
        return new Location(this.trial.getWorld(), -188.5, 120, -560.5, 0, 0);
    }

    @Override
    public Set<Location> getExitPortalLocations() {
        return Sets.newHashSet(new Location(this.trial.getWorld(), -189, 120, -561));
    }

    @Override
    public void reset() {
        super.reset();
        WorldEditHelper.pasteSchematic("trials_simon", new Location(this.trial.getWorld(), -230, 109, -584));
    }

    @Override
    public void updateScoreboard(Player player, List<String> lines) {
        lines.add("&5&6");
        lines.add(this.trial.getCurrentTier().getColor() + "&lState");
        lines.add("&7" + this.state.name().toUpperCase());
        lines.add("&6&5");
        lines.add(this.trial.getCurrentTier().getColor() + "&lProgress");
        lines.add("&7" + (this.progress + 1) + " / 5");
    }

    @Override
    public void start() {
        super.start();

        World world = this.trial.getWorld();

        this.sendToolTipAndMessage();

        this.mainLever = new Location(world, -189, 121, -556);

        Queue<Location> queue = new ConcurrentLinkedQueue<>();
        queue.add(this.mainLever.clone().add(5, -2, 0));

        this.underLeverWhiteBlocks = Lists.newArrayList();

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

                if (add.getBlock().getType() == Material.QUARTZ_BLOCK && !this.underLeverWhiteBlocks.contains(add)) {
                    this.underLeverWhiteBlocks.add(add);
                    queue.add(add);
                }
            }
        }

        this.state = SimonState.LEVER;
        this.progress = 0;
        this.onInputCooldown = false;

        this.colorToInputLocations = new HashMap<SimonColor, SimonColorInputLocations>() {{
            this.put(
                    SimonColor.RED,
                    new SimonColorInputLocations(
                        new Location(world, -174, 120, -565),
                        new Location(world, -173, 120, -563),
                        new Location(world, -172, 120, -561)
                    )
            );

            this.put(
                    SimonColor.ORANGE,
                    new SimonColorInputLocations(
                            new Location(world, -172, 120, -552),
                            new Location(world, -173, 120, -550),
                            new Location(world, -174, 120, -548)
                    )
            );

            this.put(
                    SimonColor.YELLOW,
                    new SimonColorInputLocations(
                            new Location(world, -180, 120, -541),
                            new Location(world, -182, 120, -540),
                            new Location(world, -184, 120, -539)
                    )
            );

            this.put(
                    SimonColor.LIME,
                    new SimonColorInputLocations(
                            new Location(world, -193, 120, -539),
                            new Location(world, -195, 120, -540),
                            new Location(world, -197, 120, -541)
                    )
            );

            this.put(
                    SimonColor.GREEN,
                    new SimonColorInputLocations(
                            new Location(world, -204, 120, -547),
                            new Location(world, -205, 120, -549),
                            new Location(world, -206, 120, -551)
                    )
            );

            this.put(
                    SimonColor.AQUA,
                    new SimonColorInputLocations(
                            new Location(world, -206, 120, -560),
                            new Location(world, -205, 120, -562),
                            new Location(world, -204, 120, -564)
                    )
            );

            this.put(
                    SimonColor.PURPLE,
                    new SimonColorInputLocations(
                            new Location(world, -198, 120, -571),
                            new Location(world, -196, 120, -572),
                            new Location(world, -194, 120, -573)
                    )
            );

            this.put(
                    SimonColor.MAGENTA,
                    new SimonColorInputLocations(
                            new Location(world, -185, 120, -573),
                            new Location(world, -183, 120, -572),
                            new Location(world, -181, 120, -571)
                    )
            );
        }};
    }

    private void setWedge(SimonColor color, SimonColorMode mode) {
        Material material;

        switch (mode) {
            case GLASS:
                material = Material.STAINED_GLASS;
                break;
            case CLAY:
                material = Material.STAINED_CLAY;
                break;
            default:
                material = Material.WOOL;
        }

        for (Location woolBlock : this.colorToInputLocations.get(color).getWoolBlocks()) {
            woolBlock.getBlock().setTypeIdAndData(material.getId(), (byte)color.getData(), false);
        }
    }

    private void resetWedge(SimonColor color) {
        for (Location woolBlock : this.colorToInputLocations.get(color).getWoolBlocks()) {
            woolBlock.getBlock().setType(Material.WOOL, false);
        }
    }

    private void setUnderLeverWhiteBlocks(Material material, int data) {
        for (Location underLeverWhiteBlock : this.underLeverWhiteBlocks) {
            underLeverWhiteBlock.getBlock().setTypeIdAndData(material.getId(), (byte)data, false);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void on(PlayerInteractEvent event) {
        if (!this.hasStarted || this.hasEnded) return;

        Player player = event.getPlayer();

        TrialManager.getTrial(player).ifPresent(trial -> {
            if (trial.getCurrentRoom() != this) {
                return;
            }

            Block clickedBlock = event.getClickedBlock();

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && clickedBlock.getLocation().equals(this.mainLever)) {
                if (this.state == SimonState.LEVER) {
                    event.setCancelled(true);

                    this.currentOrder = Lists.newLinkedList();
                    this.currentOrderIdx = 0;

                    List<SimonColor> possibleColors = Arrays.stream(SimonColor.values()).collect(Collectors.toList());
                    Collections.shuffle(possibleColors);

                    for (int i = 0; i < this.progress + 1; i++) {
                        this.currentOrder.add(Pair.with(possibleColors.get(0), SimonColorMode.values()[new Random().nextInt(SimonColorMode.values().length)]));
                        possibleColors.remove(0);
                    }

                    this.state = SimonState.SHOW;

                    new BukkitRunnable() {
                        int progressIdx = 0;
                        int ticksPassed = 0;

                        @Override
                        public void run() {
                            Pair<SimonColor, SimonColorMode> toShow = SimonRoom.this.currentOrder.get(this.progressIdx);

                            Block block = SimonRoom.this.mainLever.getBlock();

                            if (this.ticksPassed == 0) {
                                SimonRoom.this.setWedge(toShow.getValue0(), toShow.getValue1());

                                BlockState state;

                                switch (toShow.getValue1()) {
                                    case WOOL:
                                        block.setType(Material.STONE_BUTTON, false);
                                        state = block.getState();
                                        Button data1 = (Button) state.getData();
                                        data1.setFacingDirection(BlockFace.UP);
                                        data1.setData((byte) 6);
                                        state.update();

                                        SimonRoom.this.setUnderLeverWhiteBlocks(Material.WOOL, toShow.getValue0().getData());
                                        break;
                                    case CLAY:
                                        boolean notAlreadyLever = block.getType() != Material.LEVER;

                                        if (notAlreadyLever) {
                                            block.setType(Material.LEVER, false);
                                        }

                                        state = block.getState();
                                        Lever data = (Lever) state.getData();
                                        data.setPowered(!data.isPowered());

                                        if (notAlreadyLever) {
                                            data.setFacingDirection(BlockFace.SOUTH);
                                            data.setData((byte) 6);
                                        }

                                        state.setData(data);
                                        state.update();

                                        SimonRoom.this.setUnderLeverWhiteBlocks(Material.STAINED_CLAY, toShow.getValue0().getData());
                                        break;
                                    case GLASS:
                                        block.setType(Material.STONE_PLATE, false);
                                        SimonRoom.this.setUnderLeverWhiteBlocks(Material.STAINED_GLASS, toShow.getValue0().getData());
                                        break;
                                }
                            } else if (this.ticksPassed == 15) {
                                SimonRoom.this.resetWedge(toShow.getValue0());
                                SimonRoom.this.setUnderLeverWhiteBlocks(Material.QUARTZ_BLOCK, 0);

                                boolean notAlreadyLever = block.getType() != Material.LEVER;

                                if (notAlreadyLever) {
                                    block.setType(Material.LEVER, false);
                                }

                                BlockState state = block.getState();
                                Lever data = (Lever) state.getData();
                                data.setPowered(!data.isPowered());

                                if (notAlreadyLever) {
                                    data.setFacingDirection(BlockFace.SOUTH);
                                    data.setData((byte) 6);
                                }

                                state.setData(data);
                                state.update();

                                this.progressIdx++;

                                if (this.progressIdx > SimonRoom.this.currentOrder.size() - 1) {
                                    SimonRoom.this.state = SimonState.INPUT;
                                    this.cancel();
                                    return;
                                }
                            } else if (this.ticksPassed == 25) {
                                this.ticksPassed = -1;
                            }

                            this.ticksPassed++;
                        }
                    }.runTaskTimer(DemonicTrials.getInstance(), 0L, 1L);
                } else {
                    CommonsUtil.sendMessage(player, "&c&l(!) &cYou cannot pull the center lever during the " + this.state.name().toLowerCase() + " phase!");
                    CommonsUtil.sendMessage(player, "&7Press the interactables to trigger the color wedges!");
                    event.setCancelled(true);
                }
            } else if (this.state == SimonState.INPUT) {
                if (this.onInputCooldown && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.PHYSICAL)) {
                    CommonsUtil.sendMessage(player, "&c&l(!) &cPlease wait!");
                    event.setCancelled(true);
                    return;
                }

                boolean showIncorrect = false;
                boolean showCorrect = false;

                for (Map.Entry<SimonColor, SimonColorInputLocations> entry : this.colorToInputLocations.entrySet()) {
                    SimonColor color = entry.getKey();
                    SimonColorInputLocations inputLocations = entry.getValue();

                    Pair<SimonColor, SimonColorMode> correctCombination = this.currentOrder.get(this.currentOrderIdx);

                    if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        if (clickedBlock.getType() == Material.LEVER && clickedBlock.getLocation().equals(inputLocations.getLever())) {
                            if (correctCombination.getValue0() != color || correctCombination.getValue1() != SimonColorMode.CLAY) {
                                showIncorrect = true;
                            } else {
                                showCorrect = true;
                            }
                        } else if (clickedBlock.getType() == Material.STONE_BUTTON && clickedBlock.getLocation().equals(inputLocations.getButton())) {
                            if (correctCombination.getValue0() != color || correctCombination.getValue1() != SimonColorMode.WOOL) {
                                showIncorrect = true;
                            } else {
                                showCorrect = true;
                            }
                        }
                    } else if (event.getAction() == Action.PHYSICAL) {
                        if (clickedBlock.getType() == Material.STONE_PLATE && clickedBlock.getLocation().equals(inputLocations.getPressurePlate())) {
                            if (correctCombination.getValue0() != color || correctCombination.getValue1() != SimonColorMode.GLASS) {
                                showIncorrect = true;
                            } else {
                                showCorrect = true;
                            }
                        }
                    }

                    if (showIncorrect) {
                        this.trial.getPlayers().forEach(p -> {
                            p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 1.5F);
                            p.sendTitle(new Title(CommonsUtil.color("&c&lINCORRECT"), CommonsUtil.color(color.getChatColor() + "&l" + color.getName() + " Wedge"), 0, 15, 5));
                        });

                        this.currentOrderIdx = 0;
                        this.progress = 0;
                        this.state = SimonState.LEVER;
                        event.setCancelled(true);
                        break;
                    } else if (showCorrect) {
                        this.onInputCooldown = true;
                        SimonRoom.this.setUnderLeverWhiteBlocks(correctCombination.getValue1().getMaterial(), color.getData());
                        this.setWedge(color, correctCombination.getValue1());

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                SimonRoom.this.resetWedge(color);
                                SimonRoom.this.setUnderLeverWhiteBlocks(Material.QUARTZ_BLOCK, 0);
                                SimonRoom.this.onInputCooldown = false;
                            }
                        }.runTaskLater(DemonicTrials.getInstance(), 15L);

                        this.trial.getPlayers().forEach(p -> {
                            p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);
                            p.sendTitle(new Title(CommonsUtil.color("&a&lCORRECT"), CommonsUtil.color(color.getChatColor() + "&l" + color.getName() + " Wedge"), 0, 15, 5));
                        });

                        this.currentOrderIdx++;
                        break;
                    }
                }

                if (this.currentOrderIdx > this.currentOrder.size() - 1) {
                    if (this.progress == 4) {
                        this.trial.onRoomComplete();
                    } else {
                        this.progress++;
                        this.state = SimonState.LEVER;
                    }
                }
            }
        });
    }

    private enum SimonColor {
        RED("Red", ChatColor.RED, 14),
        ORANGE("Orange", ChatColor.GOLD, 1),
        YELLOW("Yellow", ChatColor.YELLOW, 4),
        LIME("Lime", ChatColor.GREEN, 5),
        GREEN("Green", ChatColor.DARK_GREEN, 13),
        AQUA("Aqua", ChatColor.AQUA, 3),
        PURPLE("Purple", ChatColor.DARK_PURPLE, 10),
        MAGENTA("Magenta", ChatColor.LIGHT_PURPLE, 2);

        private final String name;
        private final ChatColor chatColor;
        private final int data;

        SimonColor(String name, ChatColor chatColor, int data) {
            this.name = name;
            this.chatColor = chatColor;
            this.data = data;
        }

        public String getName() {
            return this.name;
        }

        public ChatColor getChatColor() {
            return this.chatColor;
        }

        public int getData() {
            return this.data;
        }
    }

    private static class SimonColorInputLocations {
        private final Location button;
        private final Location pressurePlate;
        private final Location lever;
        private final List<Location> woolBlocks;

        public SimonColorInputLocations(Location button, Location pressurePlate, Location lever) {
            this.button = button;
            this.pressurePlate = pressurePlate;
            this.lever = lever;
            this.woolBlocks = Lists.newArrayList();

            // pre-compute the wool blocks
            this.traverse(this.pressurePlate.clone().add(0, -1, 0));
        }

        private void traverse(Location location) {
            // check around
            this.check(location.clone().add(1, 0, 0));
            this.check(location.clone().add(-1, 0, 0));
            this.check(location.clone().add(0, 0, 1));
            this.check(location.clone().add(0, 0, -1));
        }

        private void check(Location location) {
            if (location.getBlock().getType() == Material.WOOL && location.getBlock().getData() == 0 && !this.woolBlocks.contains(location)) {
                this.woolBlocks.add(location);
                this.traverse(location);
            }
        }

        public Location getButton() {
            return this.button;
        }

        public Location getPressurePlate() {
            return this.pressurePlate;
        }

        public Location getLever() {
            return this.lever;
        }

        public List<Location> getWoolBlocks() {
            return this.woolBlocks;
        }
    }

    private enum SimonColorMode {
        WOOL(Material.WOOL),
        GLASS(Material.STAINED_GLASS),
        CLAY(Material.STAINED_CLAY);

        private Material material;

        SimonColorMode(Material material) {
            this.material = material;
        }

        public Material getMaterial() {
            return this.material;
        }
    }

    private enum SimonState {
        LEVER,
        INPUT,
        SHOW
    }
}
