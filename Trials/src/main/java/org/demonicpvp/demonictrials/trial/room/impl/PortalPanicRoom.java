package org.demonicpvp.demonictrials.trial.room.impl;

import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.ParticleEffect;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Lever;
import org.bukkit.metadata.FixedMetadataValue;

import org.demonicpvp.demonictrials.DemonicTrials;
import org.demonicpvp.demonictrials.manager.TrialManager;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.room.TrialRoom;
import org.demonicpvp.demonictrials.util.WorldEditHelper;
import org.github.paperspigot.Title;

import java.util.*;
import java.util.stream.Collectors;

public class PortalPanicRoom extends TrialRoom {
    private static final String PORTAL_PANIC_PORTAL_KEY = "portalPanicPortal";

    private Map<Byte, List<Location>> emeraldLocations;
    private Map<Location, Integer> emeraldHealth;

    private PortalPanicColor currentColor;
    private List<PortalPanicColor> shownColors;
    private Map<PortalPanicColor, Location> openPortals;

    public PortalPanicRoom(TrialSession trial) {
        super(trial);
    }

    @Override
    public String getName() {
        return "Portal Panic";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.GREEN;
    }

    @Override
    public Material getMaterial() {
        return Material.EMERALD;
    }

    @Override
    public List<String> getDescription() {
        return Lists.newArrayList(
                "Harvest fuel and navigate portals to escape",
                "before the time runs out!"
        );
    }

    @Override
    public Location getSpawnLocation() {
        return new Location(this.trial.getWorld(), 1434.5, 126, 1569.5);
    }

    @Override
    public Set<Location> getExitPortalLocations() {
        return Sets.newHashSet(new Location(this.trial.getWorld(), 1434, 120, 1569));
    }

    @Override
    public void reset() {
        super.reset();
        WorldEditHelper.pasteSchematic("trials_portal_panic", new Location(this.trial.getWorld(), 1500, 100, 1500));

        this.emeraldLocations = Maps.newHashMap();
        this.emeraldHealth = Maps.newHashMap();

        Location cornerOne = new Location(this.trial.getWorld(), 1383, 149, 1518);
        Location cornerTwo = new Location(this.trial.getWorld(), 1485, 149, 1620);

        int lowestY = 128;
        int highestY = 143;

        for (int x = cornerOne.getBlockX(); x <= cornerTwo.getBlockX(); x++) {
            for (int z = cornerOne.getBlockZ(); z <= cornerTwo.getBlockZ(); z++) {
                for (int y = lowestY; y <= highestY; y++) {
                    Block block = this.trial.getWorld().getBlockAt(x, y, z);

                    if (block.getType() == Material.WOOL) {
                        byte key = block.getData();

                        if (!this.emeraldLocations.containsKey(key)) {
                            this.emeraldLocations.put(key, Lists.newArrayList());
                        }

                        this.emeraldLocations.get(key).add(block.getLocation());

                        block.setType(Material.STONE);
                    }
                }
            }
        }

        int players = this.trial.getPlayers().size();
        int emeraldAmount = 8 + new Random().nextInt(5);

        for (int i = 0; i < emeraldAmount; i++) {
            this.spawnRandomEmerald();
        }

        this.clearPortals();

        this.currentColor = PortalPanicColor.values()[new Random().nextInt(PortalPanicColor.values().length)];
        this.shownColors = Lists.newArrayList(this.currentColor);
        this.openPortals = Maps.newHashMap();

        this.spawnPortal(this.currentColor);
    }

    @Override
    public void start() {
        super.start();

        this.sendToolTipAndMessage();

        for (Player p : this.trial.getPlayers()) {
            p.getInventory().setArmorContents(this.trial.getHazmatArmor());
            p.getInventory().addItem(this.trial.getItemWithTrialLore(new ItemBuilder(Material.DIAMOND_PICKAXE).enchant(Enchantment.DIG_SPEED, 2).enchant(Enchantment.DURABILITY, 3).build()));
        }

        this.trial.getPlayers().stream().findAny().ifPresent(p -> {
            p.getInventory().addItem(this.getFuelOre());
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(BlockBreakEvent event) {
        if (!this.hasStarted || this.hasEnded) return;

        Player player = event.getPlayer();
        Block block = event.getBlock();

        TrialManager.getTrial(player).ifPresent(trial -> {
            if (trial.getCurrentRoom() != this) {
                return;
            }

            if (block.getType() == Material.EMERALD_ORE) {
                if (!this.emeraldHealth.containsKey(block.getLocation())) {
                    this.emeraldHealth.put(block.getLocation(), 5);
                }

                for (Player p : this.trial.getPlayers()) {
                    //CosmicPacketHandler.sendPacketToPlayer(p, new SHitSplatPacket(0, CommonsUtil.color("&c-1 HP"), "HEALTH", block.getX(), block.getY(), block.getZ(), 0));
                }

                int newHealth = this.emeraldHealth.get(block.getLocation()) - 1;

                if (newHealth <= 0) {
                    this.emeraldHealth.remove(block.getLocation());
                    player.getInventory().addItem(this.getFuelOre());
                    block.setType(Material.STONE);

                    this.spawnRandomEmerald();
                } else {
                    this.emeraldHealth.put(block.getLocation(), newHealth);
                }

                event.setCancelled(true);
            }
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void on(PlayerInteractEvent event) {
        if (!this.hasStarted || this.hasEnded) return;

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();

            TrialManager.getTrial(player).ifPresent(trial -> {
                if (trial.getCurrentRoom() != this) {
                    return;
                }

                if (event.getClickedBlock().getType() == Material.LEVER) {
                    byte glassData = -1;
                    Location start = event.getClickedBlock().getLocation().clone().add(0, 2, 0);

                    if (start.add(2, 0, 0).getBlock().getType() == Material.STAINED_GLASS) {
                        glassData = start.getBlock().getData();
                    } else if (start.add(-4, 0, 0).getBlock().getType() == Material.STAINED_GLASS) {
                        glassData = start.getBlock().getData();
                    } else if (start.add(2, 0, 2).getBlock().getType() == Material.STAINED_GLASS) {
                        glassData = start.getBlock().getData();
                    } else if (start.add(0, 0, -4).getBlock().getType() == Material.STAINED_GLASS) {
                        glassData = start.getBlock().getData();
                    }

                    byte finalGlassData = glassData;

                    Arrays.stream(PortalPanicColor.values()).filter(c -> c.getGlassData() == finalGlassData).findFirst().ifPresent(color -> {
                        if (this.openPortals.containsKey(color)) {
                            CommonsUtil.sendMessage(player, "&c&l(!) &cThe " + color.getColor() + color.getName() + "&c Portal is already open!");
                        } else {
                            Block chestBlock = event.getClickedBlock().getLocation().clone().add(0, -1, 0).getBlock();
                            Chest chest = (Chest) chestBlock.getState();
                            boolean hasEmerald = false;

                            for (int i = 0; i < chest.getInventory().getSize(); i++) {
                                ItemStack item = chest.getInventory().getItem(i);

                                if (item != null && item.getType() == Material.EMERALD && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals(this.getFuelOre().getItemMeta().getDisplayName())) {
                                    if (item.getAmount() > 1) {
                                        item.setAmount(item.getAmount() - 1);
                                    } else {
                                        chest.getInventory().setItem(i, null);
                                    }

                                    hasEmerald = true;
                                    break;
                                }
                            }

                            if (!hasEmerald) {
                                CommonsUtil.sendMessage(player, "&c&l(!) &cA Fuel Ore must be in the chest to power the portal!");
                            } else {
                                CommonsUtil.sendMessage(player, "&a&l(!) &aYou opened the " + color.getColor() + color.getName() + " &aPortal!");
                                this.openPortal(start.clone().add(0, -3, 0), color);
                            }
                        }

                        event.setCancelled(true);
                    });
                }
            });
        }
    }

    private ItemStack getFuelOre() {
        return this.trial.getItemWithTrialLore(CommonsUtil.createItem(Material.EMERALD, "&aFuel Ore"));
    }

    private void spawnRandomEmerald() {
        List<Byte> keys = new ArrayList<>(this.emeraldLocations.keySet());
        Byte key = keys.get(new Random().nextInt(keys.size()));

        List<Block> possibleLocations = this.emeraldLocations.get(key).stream().map(Location::getBlock).filter(e -> e.getType() == Material.STONE).collect(Collectors.toList());
        possibleLocations.get(new Random().nextInt(possibleLocations.size())).setType(Material.EMERALD_ORE);
    }

    private void openPortal(Location location, PortalPanicColor color) {
        this.openPortals.put(color, location);

        Block portalBlockA = location.getBlock();
        Block portalBlockB = location.clone().add(0, 1, 0).getBlock();

        portalBlockA.setTypeIdAndData(90, (byte) 0, false);
        portalBlockB.setTypeIdAndData(90, (byte) 0, false);

        portalBlockA.setMetadata(PORTAL_PANIC_PORTAL_KEY, new FixedMetadataValue(DemonicTrials.getInstance(), color.ordinal()));
        portalBlockB.setMetadata(PORTAL_PANIC_PORTAL_KEY, new FixedMetadataValue(DemonicTrials.getInstance(), color.ordinal()));

        ParticleEffect.playEffect(ParticleEffect.WITCH_MAGIC, portalBlockA.getLocation(), 0.45f, 50);
        ParticleEffect.playEffect(ParticleEffect.PORTAL, portalBlockA.getLocation(), 0.6f, 50);
        ParticleEffect.playEffect(ParticleEffect.PORTAL, portalBlockB.getLocation(), 0.6f, 50);
    }

    @EventHandler
    public void on(PlayerPortalEvent event) {
        Player player = event.getPlayer();

        TrialManager.getTrial(player).ifPresent(trial -> {
            if (trial.getCurrentRoom() != this) {
                return;
            }

            Block block = player.getLocation().getBlock();

            if (block.hasMetadata(PORTAL_PANIC_PORTAL_KEY)) {
                PortalPanicColor[] colors = PortalPanicColor.values();
                PortalPanicColor color = colors[block.getMetadata(PORTAL_PANIC_PORTAL_KEY).get(0).asInt()];

                if (color != this.currentColor) {
                    CommonsUtil.sendMessage(player, "&c&l(!) &cYou entered the &c&lINCORRECT&c Portal!");
                    CommonsUtil.sendMessage(player, "&7Closing portal...");

                    block.removeMetadata(PORTAL_PANIC_PORTAL_KEY, DemonicTrials.getInstance());
                    block.setType(Material.AIR);

                    block.getLocation().clone().add(0, 1, 0).getBlock().removeMetadata(PORTAL_PANIC_PORTAL_KEY, DemonicTrials.getInstance());
                    block.getLocation().clone().add(0, 1, 0).getBlock().setType(Material.AIR);

                    this.openPortals.remove(color);

                    this.trial.teleportPlayer(player, this.getSpawnLocation());
                } else {
                    CommonsUtil.sendMessage(player, "&a&l(!) &aYou entered the &a&lCORRECT&a Portal!");

                    for (Player p : this.trial.getPlayers()) {
                        this.trial.teleportPlayer(p, this.getSpawnLocation());
                        p.sendTitle(new Title(CommonsUtil.color("&a&lCorrect Portal"), CommonsUtil.color("&7You were teleported back to the center!"), 0, 40, 10));
                    }

                    this.openPortals.forEach((k, v) -> {
                        v.getBlock().removeMetadata(PORTAL_PANIC_PORTAL_KEY, DemonicTrials.getInstance());
                        v.getBlock().setType(Material.AIR);

                        v.clone().add(0, 1, 0).getBlock().removeMetadata(PORTAL_PANIC_PORTAL_KEY, DemonicTrials.getInstance());
                        v.clone().add(0, 1, 0).getBlock().setType(Material.AIR);
                    });

                    this.openPortals.clear();

                    List<PortalPanicColor> possibleColorsList = Arrays.stream(colors).collect(Collectors.toList());
                    possibleColorsList.removeAll(this.shownColors);
                    Collections.shuffle(possibleColorsList);

                    if (possibleColorsList.isEmpty()) {
                        Location start = new Location(this.trial.getWorld(), 1432, 125, 1567);

                        for (int x = 0; x <= 4; x++) {
                            for (int z = 0; z <= 4; z++) {
                                Block loopBlock = start.clone().add(x, 0, z).getBlock();

                                if (loopBlock.getType() != Material.STAINED_CLAY) {
                                    loopBlock.setType(Material.AIR);
                                }
                            }
                        }

                        this.trial.onRoomComplete();
                        event.setCancelled(true);
                        return;
                    }

                    for (int i = 0; i < 2; i++) {
                        if (i > possibleColorsList.size() - 1) break;

                        PortalPanicColor portalPanicColor = possibleColorsList.get(i);
                        this.shownColors.add(portalPanicColor);
                        this.spawnPortal(portalPanicColor);
                    }

                    this.currentColor = this.shownColors.get(new Random().nextInt(this.shownColors.size()));
                }

                event.setCancelled(true);
            }
        });
    }

    // I think the best way to do this is just to hardcode, it's not many blocks
    private void spawnPortal(PortalPanicColor color) {
        switch (color) {
            case BLACK:
                this.spawnChestAndLever(BlockFace.SOUTH, BlockFace  .WEST, new Location(this.trial.getWorld(), 1429, 126, 1561));
                this.spawnQuartzPillar(new Location(this.trial.getWorld(), 1430, 126, 1561));
                this.spawnMiddlePart(PortalPanicColor.BLACK, new Location(this.trial.getWorld(), 1431, 126, 1561));
                this.spawnQuartzPillar(new Location(this.trial.getWorld(), 1432, 126, 1561));
                break;
            case RED:
                this.spawnChestAndLever(BlockFace.SOUTH, BlockFace.EAST, new Location(this.trial.getWorld(), 1439, 126, 1561));
                this.spawnQuartzPillar(new Location(this.trial.getWorld(), 1438, 126, 1561));
                this.spawnMiddlePart(PortalPanicColor.RED, new Location(this.trial.getWorld(), 1437, 126, 1561));
                this.spawnQuartzPillar(new Location(this.trial.getWorld(), 1436, 126, 1561));
                break;
            case MAGENTA:
                this.spawnChestAndLever(BlockFace.WEST, BlockFace.NORTH, new Location(this.trial.getWorld(), 1442, 126, 1564));
                this.spawnQuartzPillar(new Location(this.trial.getWorld(), 1442, 126, 1565));
                this.spawnMiddlePart(PortalPanicColor.MAGENTA, new Location(this.trial.getWorld(), 1442, 126, 1566));
                this.spawnQuartzPillar(new Location(this.trial.getWorld(), 1442, 126, 1567));
                break;
            case LIME:
                this.spawnChestAndLever(BlockFace.WEST, BlockFace.SOUTH, new Location(this.trial.getWorld(), 1442, 126, 1574));
                this.spawnQuartzPillar(new Location(this.trial.getWorld(), 1442, 126, 1573));
                this.spawnMiddlePart(PortalPanicColor.LIME, new Location(this.trial.getWorld(), 1442, 126, 1572));
                this.spawnQuartzPillar(new Location(this.trial.getWorld(), 1442, 126, 1571));
                break;
            case CYAN:
                this.spawnChestAndLever(BlockFace.NORTH, BlockFace.EAST, new Location(this.trial.getWorld(), 1439, 126, 1577));
                this.spawnQuartzPillar(new Location(this.trial.getWorld(), 1438, 126, 1577));
                this.spawnMiddlePart(PortalPanicColor.CYAN, new Location(this.trial.getWorld(), 1437, 126, 1577));
                this.spawnQuartzPillar(new Location(this.trial.getWorld(), 1436, 126, 1577));
                break;
            case PINK:
                this.spawnChestAndLever(BlockFace.NORTH, BlockFace.WEST, new Location(this.trial.getWorld(), 1429, 126, 1577));
                this.spawnQuartzPillar(new Location(this.trial.getWorld(), 1430, 126, 1577));
                this.spawnMiddlePart(PortalPanicColor.PINK, new Location(this.trial.getWorld(), 1431, 126, 1577));
                this.spawnQuartzPillar(new Location(this.trial.getWorld(), 1432, 126, 1577));
                break;
            case BLUE:
                this.spawnChestAndLever(BlockFace.EAST, BlockFace.SOUTH, new Location(this.trial.getWorld(), 1426, 126, 1574));
                this.spawnQuartzPillar(new Location(this.trial.getWorld(), 1426, 126, 1573));
                this.spawnMiddlePart(PortalPanicColor.BLUE, new Location(this.trial.getWorld(), 1426, 126, 1572));
                this.spawnQuartzPillar(new Location(this.trial.getWorld(), 1426, 126, 1571));
                break;
            case PURPLE:
                this.spawnChestAndLever(BlockFace.EAST, BlockFace.NORTH, new Location(this.trial.getWorld(), 1426, 126, 1564));
                this.spawnQuartzPillar(new Location(this.trial.getWorld(), 1426, 126, 1565));
                this.spawnMiddlePart(PortalPanicColor.PURPLE, new Location(this.trial.getWorld(), 1426, 126, 1566));
                this.spawnQuartzPillar(new Location(this.trial.getWorld(), 1426, 126, 1567));
                break;
        }
    }

    private void spawnQuartzPillar(Location location) {
        for (int y = 0; y <= 2; y++) {
            location.clone().add(0, y, 0).getBlock().setType(Material.QUARTZ_BLOCK);
        }

        location.clone().add(0, 3, 0).getBlock().setType(Material.COBBLE_WALL);
    }

    private void spawnMiddlePart(PortalPanicColor color, Location location) {
        location.clone().add(0, 2, 0).getBlock().setType(Material.SEA_LANTERN);
        location.clone().add(0, 3, 0).getBlock().setTypeIdAndData(Material.STAINED_GLASS.getId(), (byte)color.getGlassData(), false);
    }

    private void clearPortals() {
        Location cornerOne = new Location(this.trial.getWorld(), 1426, 126, 1561);
        Location cornerTwo = new Location(this.trial.getWorld(), 1442, 129, 1577);

        for (int x = cornerOne.getBlockX(); x <= cornerTwo.getBlockX(); x++) {
            for (int z = cornerOne.getBlockZ(); z <= cornerTwo.getBlockZ(); z++) {
                for (int y = cornerOne.getBlockY(); y <= cornerTwo.getBlockY(); y++) {
                    Block block = new Location(this.trial.getWorld(), x, y, z).getBlock();

                    if (block.getType() != Material.AIR) {
                        block.setType(Material.AIR);

                        if (block.hasMetadata(PORTAL_PANIC_PORTAL_KEY)) {
                            block.removeMetadata(PORTAL_PANIC_PORTAL_KEY, DemonicTrials.getInstance());
                        }
                    }
                }
            }
        }
    }

    private void spawnChestAndLever(BlockFace chestFacing, BlockFace leverFacing, Location location) {
        Block chest = location.getBlock();
        chest.setType(Material.CHEST);
        BlockState state = chest.getState();
        state.setData(new org.bukkit.material.Chest(chestFacing));
        state.update();

        Block lever = location.clone().add(0, 1, 0).getBlock();
        lever.setType(Material.LEVER);
        state = lever.getState();
        Lever data = (Lever) state.getData();
        data.setFacingDirection(leverFacing);
        state.setData(data);
        state.update();
    }

    private enum PortalPanicColor {
        BLACK("Black", ChatColor.BLACK, 15),
        RED("Red", ChatColor.RED, 14),
        MAGENTA("Magenta", ChatColor.LIGHT_PURPLE, 2),
        LIME("Lime", ChatColor.GREEN, 5),
        CYAN("Cyan", ChatColor.DARK_AQUA, 9),
        PINK("Pink", ChatColor.LIGHT_PURPLE, 6),
        BLUE("Blue", ChatColor.BLUE, 11),
        PURPLE("Purple", ChatColor.DARK_PURPLE, 10);

        private final String name;
        private final ChatColor color;
        private final int glassData;

        PortalPanicColor(String name, ChatColor color, int glassData) {
            this.name = name;
            this.color = color;
            this.glassData = glassData;
        }

        public String getName() {
            return this.name;
        }

        public ChatColor getColor() {
            return this.color;
        }

        public int getGlassData() {
            return this.glassData;
        }
    }
}
