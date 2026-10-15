package org.demonicpvp.demonictrials.trial.room.impl;

import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.rit.sucy.EnchantmentAPI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.demonicpvp.demonictrials.DemonicTrials;
import org.demonicpvp.demonictrials.manager.TrialManager;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.TrialTier;
import org.demonicpvp.demonictrials.trial.room.TrialRoom;
import org.demonicpvp.demonictrials.util.WorldEditHelper;

import java.util.*;
import java.util.stream.Collectors;

public class HotscotchRoom extends TrialRoom {
    private Map<Player, Integer> playerLives;
    private Location endLeverLocation;

    private List<Location> barriers;
    private Map<Location, List<Location>> levers;

    public HotscotchRoom(TrialSession trial) {
        super(trial);
    }

    @Override
    public String getName() {
        return "Hotscotch";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.GOLD;
    }

    @Override
    public Material getMaterial() {
        return Material.BLAZE_POWDER;
    }

    @Override
    public List<String> getDescription() {
        return Lists.newArrayList(
                "Navigate to the end of the parkour",
                "before the time runs out!"
        );
    }

    @Override
    public Location getSpawnLocation() {
        return new Location(this.trial.getWorld(), -1954.5, 71, -1987.5, 0, 0);
    }

    @Override
    public Set<Location> getExitPortalLocations() {
        return Sets.newHashSet(new Location(this.trial.getWorld(), -1955, 82, -1878));
    }

    @Override
    public void updateScoreboard(Player player, List<String> lines) {
        lines.add("&f");
        lines.add(this.trial.getCurrentTier().getColor() + "&lLives");
        lines.add("&7" + this.playerLives.get(player) + " / 5");
    }

    @Override
    public TrialTier getRollUntil() {
        return TrialTier.MASTER;
    }

    @Override
    public void start() {
        super.start();

        this.sendToolTipAndMessage();

        ItemStack leggings = new ItemBuilder(Material.DIAMOND_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).enchant(Enchantment.DURABILITY, 3).build();
        EnchantmentAPI.getEnchantment("Obsidianshield").addToItem(leggings, 1);

        ItemStack regBoots = new ItemBuilder(Material.DIAMOND_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).enchant(Enchantment.DURABILITY, 3).build();

        ItemStack springsBoots = new ItemBuilder(Material.DIAMOND_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).enchant(Enchantment.DURABILITY, 3).build();
        EnchantmentAPI.getEnchantment("Springs").addToItem(springsBoots, 1);

        ItemStack bow = new ItemBuilder(Material.BOW).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.ARROW_INFINITE, 1).build();
        EnchantmentAPI.getEnchantment("Teleportation").addToItem(bow, 5);

        this.trial.getPlayers().forEach(p -> {
            PlayerInventory inventory = p.getInventory();
            inventory.setArmorContents(new ItemStack[] { this.trial.getItemWithTrialLore(regBoots.clone()), this.trial.getItemWithTrialLore(leggings.clone()) });

            inventory.setItem(0, this.trial.getItemWithTrialLore(bow.clone()));
            inventory.setItem(2, this.trial.getItemWithTrialLore(springsBoots.clone()));
            inventory.setItem(3, this.trial.getItemWithTrialLore(new ItemStack(Material.DIAMOND_PICKAXE)));
            inventory.setItem(27, this.trial.getItemWithTrialLore(new ItemStack(Material.ARROW)));
        });

        for (Location barrier : this.barriers) {
            for (int y = barrier.getBlockY(); y <= 110; y++) {
                Location block = new Location(this.trial.getWorld(), barrier.getBlockX(), y, barrier.getBlockZ());

                if (block.getBlock().getType() == Material.BARRIER) {
                    block.getBlock().setType(Material.AIR);
                }
            }
        }
    }

    @Override
    public void reset() {
        super.reset();
        WorldEditHelper.pasteSchematic("trials_hotscotch", new Location(this.trial.getWorld(), -2000, 50, -2000));

        this.playerLives = Maps.newHashMap();
        this.trial.getPlayers().forEach(p -> this.playerLives.put(p, 5));

        this.endLeverLocation = new Location(this.trial.getWorld(), -1955, 83, -1875);

        this.levers = Maps.newHashMap();

        Location leverOneLocation = new Location(this.trial.getWorld(), -1929, 77, -1914);
        Location startAt = new Location(this.trial.getWorld(), -1940, 90, -1941);
        List<Location> leverOneList = Lists.newArrayList();

        for (int z = 0; z <= 11; z++) {
            leverOneList.add(startAt.clone().add(0, 0, -z));
        }

        this.levers.put(leverOneLocation, leverOneList);

        Location leverTwoLocation = new Location(this.trial.getWorld(), -1973, 82, -1967);
        startAt = new Location(this.trial.getWorld(), -1974, 88, -1933);
        List<Location> leverTwoList = Lists.newArrayList();

        for (int x = 0; x <= 11; x++) {
            leverTwoList.add(startAt.clone().add(-x, 0, 0));
        }

        this.levers.put(leverTwoLocation, leverTwoList);

        Location leverThreeLocation = new Location(this.trial.getWorld(), -1983, 83, -1907);
        startAt = new Location(this.trial.getWorld(), -1950, 91, -1897);
        List<Location> leverThreeList = Lists.newArrayList();

        for (int x = 0; x <= 11; x++) {
            leverThreeList.add(startAt.clone().add(-x, 0, 0));
        }

        this.levers.put(leverThreeLocation, leverThreeList);

        // so scuffed lmao, I just cba remaking the schem
        this.barriers = Lists.newArrayList(
                new Location(this.trial.getWorld(), -1949, 71, -1990),
                new Location(this.trial.getWorld(), -1949, 71, -1989),
                new Location(this.trial.getWorld(), -1949, 71, -1988),
                new Location(this.trial.getWorld(), -1950, 71, -1987),
                new Location(this.trial.getWorld(), -1950, 71, -1986),
                new Location(this.trial.getWorld(), -1951, 71, -1985),
                new Location(this.trial.getWorld(), -1952, 71, -1985),
                new Location(this.trial.getWorld(), -1953, 71, -1984),
                new Location(this.trial.getWorld(), -1954, 71, -1984),
                new Location(this.trial.getWorld(), -1955, 71, -1984),
                new Location(this.trial.getWorld(), -1956, 71, -1984),
                new Location(this.trial.getWorld(), -1957, 71, -1984),
                new Location(this.trial.getWorld(), -1958, 71, -1985),
                new Location(this.trial.getWorld(), -1959, 71, -1985),
                new Location(this.trial.getWorld(), -1960, 71, -1986),
                new Location(this.trial.getWorld(), -1960, 71, -1987),
                new Location(this.trial.getWorld(), -1961, 71, -1988),
                new Location(this.trial.getWorld(), -1961, 71, -1989),
                new Location(this.trial.getWorld(), -1961, 71, -1990)
        );

        for (Location barrier : this.barriers) {
            for (int y = barrier.getBlockY(); y <= 110; y++) {
                Location block = new Location(this.trial.getWorld(), barrier.getBlockX(), y, barrier.getBlockZ());

                if (block.getBlock().getType() == Material.AIR) {
                    block.getBlock().setType(Material.BARRIER);
                }
            }
        }
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
                    if (this.endLeverLocation.equals(clickedBlock.getLocation())) {
                        this.trial.onRoomComplete();
                    } else if (this.levers.containsKey(clickedBlock.getLocation())) {
                        List<Location> locations = this.levers.get(clickedBlock.getLocation());

                        for (Location location : locations) {
                            if (location.getBlock().getType() != Material.AIR) {
                                location.getBlock().setType(Material.AIR);
                            }

                            for (int y = 0; y < 25; y++) {
                                Location checkAround = location.clone().add(0, -y, 0);

                                org.bukkit.util.Vector[] offsets = {
                                        new org.bukkit.util.Vector(1, 0, 0),
                                        new org.bukkit.util.Vector(-1, 0, 0),
                                        new org.bukkit.util.Vector(0, 0, 1),
                                        new org.bukkit.util.Vector(0, 0, -1)
                                };

                                for (Vector offset : offsets) {
                                    Location around = checkAround.clone().add(offset);

                                    if (around.getBlock().getType() == Material.LAVA || around.getBlock().getType() == Material.STATIONARY_LAVA) {
                                        around.getBlock().setType(Material.AIR);
                                    }
                                }
                            }
                        }

                        this.levers.remove(clickedBlock.getLocation());
                    }
                }
            }
        });
    }

    @EventHandler
    private void on(PlayerMoveEvent event) {
        if (!this.hasStarted || this.hasEnded) return;

        Player player = event.getPlayer();

        TrialManager.getTrial(player).ifPresent(trial -> {
            if (trial.getCurrentRoom() != this) return;

            Block block = player.getLocation().getBlock();

            if (block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA) {
                int playerLives = this.playerLives.get(player) - 1;

                if (playerLives <= 0) {
                    this.trial.removePlayer(player, TrialSession.TrialPlayerRemoveReason.DIED);
                    CommonsUtil.sendMessage(player, "&6&l[Trials] &cYou have been eliminated since you ran out of lives.");
                } else {
                    this.playerLives.put(player, playerLives);
                    CommonsUtil.sendMessage(player, "&6&l[Trials] &cYou fell in the lava!");
                    CommonsUtil.sendMessage(player, "&7You have been teleported back to the start and have " + playerLives + " lives remaining.");
                    this.trial.teleportPlayer(player, this.getSpawnLocation());

                    if (player.getFireTicks() > 0) {
                        player.setFireTicks(0);
                    }
                }
            }
        });
    }
}
