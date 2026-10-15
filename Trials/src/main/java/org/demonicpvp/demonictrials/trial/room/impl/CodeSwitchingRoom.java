package org.demonicpvp.demonictrials.trial.room.impl;

import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.demonicpvp.demonictrials.DemonicTrials;
import org.demonicpvp.demonictrials.manager.TrialManager;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.room.TrialRoom;
import org.demonicpvp.demonictrials.util.WorldEditHelper;

import java.util.*;
import java.util.stream.Collectors;

public class CodeSwitchingRoom extends TrialRoom {
    private Map<CodeColor, List<CodeColor>> colorCombinations;
    private List<CodeColor> floorColors;

    private Map<Player, Integer> playerLives;

    private Map<Player, Integer> playerToRoomMap;
    private Map<Integer, Location> roomToLocationMap;

    public CodeSwitchingRoom(TrialSession trial) {
        super(trial);
    }

    @Override
    public String getName() {
        return "Code Switching";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.AQUA;
    }

    @Override
    public Material getMaterial() {
        return Material.REDSTONE;
    }

    @Override
    public List<String> getDescription() {
        return Lists.newArrayList(
                "Navigate the hallway using the correct doors",
                "before the time runs out!"
        );
    }

    @Override
    public Location getSpawnLocation() {
        return new Location(this.trial.getWorld(), -29.5, 227.0, 79.5, 0.f, 0.f);
    }

    @Override
    public Set<Location> getExitPortalLocations() {
        return Sets.newHashSet(new Location(this.trial.getWorld(), -30, 227, 229, 0.f, 0.f));
    }

    @Override
    public void updateScoreboard(Player player, List<String> lines) {
        lines.add("&f");
        lines.add(this.trial.getCurrentTier().getColor() + "&lLives");
        lines.add("&7" + this.playerLives.get(player) + " / 3");
    }

    @Override
    public void start() {
        super.start();

        this.sendToolTipAndMessage();

        this.playerToRoomMap = Maps.newHashMap();

        this.trial.getPlayers().forEach(p -> {
            this.playerToRoomMap.put(p, 1);

            p.getInventory().setArmorContents(this.trial.getHazmatArmor());
        });

        Location roomTwoLocation = new Location(this.trial.getWorld(), -29.5, 227, 127.5, 0.f, 0.f);

        this.roomToLocationMap = Maps.newHashMap();
        this.roomToLocationMap.put(2, roomTwoLocation);
        this.roomToLocationMap.put(3, roomTwoLocation.clone().add(0, 0, 21));
        this.roomToLocationMap.put(4, roomTwoLocation.clone().add(0, 0, 21 * 2));
        this.roomToLocationMap.put(5, roomTwoLocation.clone().add(0, 0, 21 * 3));
        this.roomToLocationMap.put(6, roomTwoLocation.clone().add(0, 0, 21 * 4));

        this.breakRedGlass();
        this.resetColors();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (CodeSwitchingRoom.this.trial.getPlayers().isEmpty() || CodeSwitchingRoom.this.hasEnded) {
                    this.cancel();
                    return;
                }

                CodeSwitchingRoom.this.resetColorCombinations();
            }
        }.runTaskTimer(DemonicTrials.getInstance(), 0L, 20L * 60L);
    }

    @Override
    public void reset() {
        super.reset();
        WorldEditHelper.pasteSchematic("trials_code_switching_2", new Location(this.trial.getWorld(), -43, 216, 67));

        this.trial.getWorld().getBlockAt(-30, 227, 229).setType(Material.AIR);
        this.trial.getWorld().getBlockAt(-30, 228, 229).setType(Material.AIR);

        this.playerLives = Maps.newHashMap();
        this.trial.getPlayers().forEach(p -> this.playerLives.put(p, 3));
    }

    private void resetColorCombinations() {
        this.colorCombinations = Maps.newLinkedHashMap();

        Location startLocation = new Location(this.trial.getWorld(), -26, 226, 82);

        for (int i = 0; i < CodeColor.values().length; i++) {
            CodeColor color = CodeColor.values()[i];
            List<CodeColor> list = Lists.newArrayList();

            for (int j = 0; j < 6; j++) {
                if (Math.random() < 0.33) {
                    list.add(CodeColor.RED);
                } else if (Math.random() < 0.66) {
                    list.add(CodeColor.BLUE);
                } else {
                    list.add(CodeColor.LIME);
                }
            }

            this.colorCombinations.put(color, list);

            Location colorStartLocation = startLocation.clone().add(-i, 0, 0);

            for (int j = 0; j < 6; j++) {
                colorStartLocation.clone().add(0, 0, j + 1).getBlock().setTypeIdAndData(Material.STAINED_GLASS.getId(), (byte)this.colorCombinations.get(color).get(j).getGlassData(), false);
            }
        }

        this.resetFloorColors();
    }

    private void resetFloorColors() {
        this.floorColors = Lists.newLinkedList();

        List<CodeColor> possibleColors = Arrays.stream(CodeColor.values()).collect(Collectors.toList());
        Location startLocation = new Location(this.trial.getWorld(), -37, 226, 99);

        for (int i = 0; i < 6; i++) {
            Collections.shuffle(possibleColors);
            this.floorColors.add(possibleColors.get(0));

            for (int x = 0; x < 15; x++) {
                for (int z = 0; z < 15; z++) {
                    Block block = startLocation.clone().add(x, 0, z).getBlock();

                    if (block.getType() != Material.STAINED_CLAY) {
                        block.setTypeIdAndData(Material.WOOL.getId(), (byte)possibleColors.get(0).getWoolData(), false);
                    }
                }
            }

            startLocation.add(0, 0, 21);
        }
    }

    private void breakRedGlass() {
        List<Location> locations = Lists.newArrayList(
                new Location(this.trial.getWorld(), -24, 227, 95),
                new Location(this.trial.getWorld(), -29, 227, 95),
                new Location(this.trial.getWorld(), -34, 227, 95)
        );

        locations.forEach(startingLocation -> {
            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 5; y++) {
                    startingLocation.clone().add(-x, y, 0).getBlock().setType(Material.AIR);
                }
            }

            this.trial.getPlayers().forEach(p -> p.playSound(startingLocation, Sound.GLASS, 1.0F, 1.0F));
        });
    }

    private void resetColors() {
        Location startLocation = new Location(this.trial.getWorld(), -26, 226, 82);

        for (int i = 0; i < CodeColor.values().length; i++) {
            CodeColor codeColor = CodeColor.values()[i];

            startLocation.clone().add(-i, 0, 0).getBlock().setTypeIdAndData(Material.WOOL.getId(), (byte)codeColor.getWoolData(), false);
        }
    }

    @EventHandler
    private void on(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        TrialManager.getTrial(player).ifPresent(trial -> {
            if (trial.getCurrentRoom() != this) return;

            Block block = player.getLocation().getBlock();
            int blockX = block.getLocation().getBlockX();

            if (block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER) {
                CodeColor waterPortalColor = this.getWaterPortalColor(player, blockX);

                int playerRoom = this.playerToRoomMap.get(player);
                int playerLives = this.playerLives.get(player);

                boolean isCorrectColor = this.colorCombinations.get(this.floorColors.get(playerRoom - 1)).get(playerRoom - 1) == waterPortalColor;

                if (isCorrectColor) {
                    if (playerRoom == 6) {
                        this.trial.teleportPlayer(player, this.getExitPortalLocations().iterator().next());
                        CommonsUtil.sendMessage(player, "&a&l(!) &aYou entered the &a&lCORRECT &aPortal! &7(Room " + playerRoom + " -> Final Room)");
                    } else {
                        this.playerToRoomMap.put(player, playerRoom + 1);
                        this.trial.teleportPlayer(player, this.roomToLocationMap.get(playerRoom + 1));
                        CommonsUtil.sendMessage(player, "&a&l(!) &aYou entered the &a&lCORRECT &aPortal! &7(Room " + playerRoom + " -> Room " + (playerRoom + 1) + ")");
                    }
                } else {
                    if (playerLives != 1) {
                        this.playerToRoomMap.put(player, 1);
                        this.playerLives.put(player, playerLives - 1);

                        this.trial.teleportPlayer(player, this.getSpawnLocation());

                        this.resetColorCombinations();

                        CommonsUtil.sendMessage(player, "&c&l(!) &cYou entered the &c&lINCORRECT&c Portal! &7(Room " + playerRoom + " -> Room " + (playerRoom + 1) + ")");
                        CommonsUtil.sendMessage(player, "&7You have been teleported back to the start and now have " + (playerLives - 1) + " lives remaining.");
                    } else {
                        this.trial.removePlayer(player, TrialSession.TrialPlayerRemoveReason.DIED);
                        CommonsUtil.sendMessage(player, "&6&l[Trials] &cYou have been eliminated since you ran out of lives.");
                    }
                }
            }
        });
    }

    @EventHandler(ignoreCancelled = true)
    private void on(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        TrialManager.getTrial(player).ifPresent(trial -> {
            if (trial.getCurrentRoom() != this) return;

            Block leverBlock = this.trial.getWorld().getBlockAt(-30, 227, 231);

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK
                    && event.getClickedBlock().getLocation().equals(leverBlock.getLocation())
                    && this.playerToRoomMap.get(player) == 6
                    && !this.hasEnded) {
                this.trial.onRoomComplete();
            }
        });
    }

    private CodeColor getWaterPortalColor(Player player, int blockX) {
        Integer playerRoom = this.playerToRoomMap.get(player);

        if (playerRoom != 5) {
            if (blockX >= -27 && blockX <= -23) return CodeColor.LIME;
            if (blockX >= -32 && blockX <= -28) return CodeColor.RED;
            if (blockX >= -37 && blockX <= -33) return CodeColor.BLUE;
        } else {
            if (blockX >= -27 && blockX <= -23) return CodeColor.BLUE;
            if (blockX >= -32 && blockX <= -28) return CodeColor.LIME;
            if (blockX >= -37 && blockX <= -33) return CodeColor.RED;
        }

        return CodeColor.ORANGE;
    }

    private enum CodeColor {
        RED(14),
        ORANGE(1),
        YELLOW(4),
        LIME(5),
        LIGHT_BLUE(3),
        CYAN(9),
        BLUE(11),
        PURPLE(10),
        PINK(6);

        CodeColor(int data) {
            this.glassData = data;
            this.woolData = data;
        }

        private final int glassData;
        private final int woolData;

        public ItemStack getGlass() {
            return new ItemStack(Material.GLASS, 1, (short)this.glassData);
        }

        public ItemStack getWool() {
            return new ItemStack(Material.WOOL, 1, (short)this.woolData);
        }

        public int getGlassData() {
            return this.glassData;
        }

        public int getWoolData() {
            return this.woolData;
        }
    }
}
