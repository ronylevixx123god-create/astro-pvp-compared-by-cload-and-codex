package org.demonicpvp.demonictrials.trial.room.impl;

import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreeper;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.demonicpvp.demonictrials.DemonicTrials;
import org.demonicpvp.demonictrials.manager.TrialManager;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.room.TrialRoom;
import org.demonicpvp.demonictrials.util.WorldEditHelper;

import java.util.*;

import static org.demonicpvp.demonictrials.listener.MobListener.ENTITY_PROPERTIES_KEY;

public class ThunderstruckRoom extends TrialRoom {
    private final static String THUNDERSTRUCK_CREEPER_KEY = "thunderstruckCreeper";
    private final static String THUNDERSTRUCK_GHAST_KEY = "thunderstruckGhast";
    private final static String THUNDERSTRUCK_CRYSTAL_KEY = "thunderstruckCrystal";
    private final static String NEXT_FIRE_TICK_HIT_KEY = "thunderstruckNextFireTickHit";

    private Ghast boss;
    private int bossHealth;

    public ThunderstruckRoom(TrialSession trial) {
        super(trial);
    }

    @Override
    public String getName() {
        return "Thunderstruck";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.LIGHT_PURPLE;
    }

    @Override
    public Material getMaterial() {
        return Material.SULPHUR;
    }

    @Override
    public List<String> getDescription() {
        return Lists.newArrayList(
                "Kill the boss before",
                "the time runs out!"
        );
    }

    @Override
    public Location getSpawnLocation() {
        return new Location(this.trial.getWorld(), -1506.5, 83, -1432.5, 0f, 0f);
    }

    @Override
    public Set<Location> getExitPortalLocations() {
        return Sets.newHashSet(new Location(this.trial.getWorld(), -1507, 77, -1433, 0f, 0f));
    }

    @Override
    public void updateScoreboard(Player player, List<String> lines) {
        lines.add("&f");
        lines.add(this.trial.getCurrentTier().getColor() + "&lBoss HP");
        lines.add("&7" + this.bossHealth + " / 1500");
    }

    @Override
    public void reset() {
        super.reset();

        WorldEditHelper.pasteSchematic("trials_thunderstruck", new Location(this.trial.getWorld(), -1500, 50, -1500));

        this.bossHealth = 1500;

        List<Location> glassList = Lists.newArrayList(
                new Location(this.trial.getWorld(), -1507, 82, -1456),
                new Location(this.trial.getWorld(), -1504, 83, -1457),
                new Location(this.trial.getWorld(), -1505, 84, -1459),
                new Location(this.trial.getWorld(), -1507, 85, -1461),
                new Location(this.trial.getWorld(), -1507, 85, -1461),
                new Location(this.trial.getWorld(), -1507, 85, -1466),
                new Location(this.trial.getWorld(), -1504, 86, -1468),
                new Location(this.trial.getWorld(), -1507, 87, -1470),
                new Location(this.trial.getWorld(), -1507, 80, -1408),
                new Location(this.trial.getWorld(), -1510, 81, -1408),
                new Location(this.trial.getWorld(), -1512, 82, -1408),
                new Location(this.trial.getWorld(), -1512, 83, -1406),
                new Location(this.trial.getWorld(), -1509, 84, -1407),
                new Location(this.trial.getWorld(), -1507, 85, -1405),
                new Location(this.trial.getWorld(), -1507, 85, -1400),
                new Location(this.trial.getWorld(), -1510, 86, -1398),
                new Location(this.trial.getWorld(), -1507, 87, -1396)
        );

        for (Location location : glassList) {
            location.getBlock().setType(Material.GLASS);
        }


        Bukkit.getScheduler().runTaskLater(DemonicTrials.getInstance(), () -> {
            for (int x = -96; x <= -94; x++) {
                for (int z = -91; z <= -89; z++) {
                    Chunk chunk = this.trial.getWorld().getChunkAt(x, z);

                    if (!chunk.isLoaded()) {
                        chunk.load();
                    }

                    for (int chunkX = 0; chunkX < 16; chunkX++) {
                        for (int chunkZ = 0; chunkZ < 16; chunkZ++) {
                            for (int chunkY = 0; chunkY < 255; chunkY++) {
                                Block block = chunk.getBlock(chunkX, chunkY, chunkZ);

                                if (block.getType() == Material.MOB_SPAWNER) {
                                    block.setType(Material.AIR);
                                    block.setType(Material.MOB_SPAWNER);
                                    //silkUtil.setSpawnerEntityID(block, "Creeper");
                                    ((CreatureSpawner) block.getState()).setSpawnedType(EntityType.CREEPER);

                                }
                            }
                        }
                    }
                }
            }
        }, 20L * 3L);
    }

    @Override
    public void start() {
        super.start();

        this.sendToolTipAndMessage();

        this.spawnCreeper();

        this.boss = (Ghast)this.trial.getWorld().spawnEntity(this.getSpawnLocation().clone().add(0, 10, 0), EntityType.GHAST);
        this.boss.setCustomName(CommonsUtil.color(this.getColor() + ChatColor.BOLD.toString() + "Thunderstruck"));
        this.boss.setCustomNameVisible(false);
        this.boss.setMetadata(THUNDERSTRUCK_GHAST_KEY, new FixedMetadataValue(DemonicTrials.getInstance(), true));
        this.boss.setMaxHealth(1500);
        this.boss.setHealth(1500);

        for (Player p : this.trial.getPlayers()) {
            PlayerInventory inventory = p.getInventory();

            inventory.setHelmet(this.trial.getItemWithTrialLore(new ItemBuilder(Material.DIAMOND_HELMET).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).enchant(Enchantment.DURABILITY, 3).build()));
            inventory.setChestplate(this.trial.getItemWithTrialLore(new ItemBuilder(Material.DIAMOND_CHESTPLATE).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).enchant(Enchantment.DURABILITY, 3).build()));
            inventory.setLeggings(this.trial.getItemWithTrialLore(new ItemBuilder(Material.DIAMOND_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).enchant(Enchantment.DURABILITY, 3).build()));
            inventory.setBoots(this.trial.getItemWithTrialLore(new ItemBuilder(Material.DIAMOND_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).enchant(Enchantment.DURABILITY, 3).build()));

            inventory.setItem(0, this.trial.getItemWithTrialLore(new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, 5).enchant(Enchantment.DURABILITY, 3).build()));
            inventory.setItem(1, this.trial.getItemWithTrialLore(new ItemBuilder(Material.FLINT_AND_STEEL).enchant(Enchantment.DURABILITY, 10).build()));
            inventory.setItem(2, this.trial.getItemWithTrialLore(new ItemBuilder(Material.GOLDEN_APPLE).amount(20).build()));
            inventory.setItem(3, this.trial.getItemWithTrialLore(new ItemBuilder(Material.FISHING_ROD).enchant(Enchantment.DURABILITY, 10).build()));
        }

        new BukkitRunnable() {
            private int secondsPassed = 0;
            private int withoutCrystalSpawn = 0;

            @Override
            public void run() {
                if (ThunderstruckRoom.this.trial.getPlayers().isEmpty() || ThunderstruckRoom.this.hasEnded) {
                    this.cancel();
                    return;
                }

                if (this.secondsPassed >= 20) {
                    if (ThunderstruckRoom.this.trial.getWorld().getEntities().stream().filter(e -> e.hasMetadata(THUNDERSTRUCK_CREEPER_KEY)).count() < 5) {
                        ThunderstruckRoom.this.spawnCreeper();
                        this.secondsPassed = 0;
                    }

                    this.withoutCrystalSpawn++;

                    if (this.withoutCrystalSpawn >= 4 && ThunderstruckRoom.this.trial.getWorld().getEntities().stream().filter(e -> e.hasMetadata(THUNDERSTRUCK_CRYSTAL_KEY)).count() < 5) {
                        ThunderstruckRoom.this.spawnCrystal();
                        this.withoutCrystalSpawn = 0;
                    }
                }

                this.secondsPassed++;
            }
        }.runTaskTimer(DemonicTrials.getInstance(), 20L, 20L);

        this.spawnCrystal();
    }

    @EventHandler
    public void on(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            TrialManager.getTrial(player).ifPresent(trial -> {
                if (trial.getCurrentRoom() != this) {
                    return;
                }

                if (event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.LAVA) {
                    event.setCancelled(true);

                    if (this.hasStarted && !this.hasEnded && (!player.hasMetadata(NEXT_FIRE_TICK_HIT_KEY) || player.getMetadata(NEXT_FIRE_TICK_HIT_KEY).get(0).asLong() <= System.currentTimeMillis())) {
                        player.damage(0.0);

                        double newHealth = Math.max(0, player.getHealth() - 1);

                        if (newHealth > 0) {
                            player.setHealth(newHealth);
                            player.setMetadata(NEXT_FIRE_TICK_HIT_KEY, new FixedMetadataValue(DemonicTrials.getInstance(), System.currentTimeMillis() + 500L));
                        } else {
                            CommonsUtil.sendMessage(player, "&6&l[Trials] &cYou have died and been eliminated from the trial!");
                            this.trial.removePlayer(player, TrialSession.TrialPlayerRemoveReason.DIED);
                        }
                    }
                }
            });
        } else if (event.getEntity() instanceof Ghast) {
            if (event.getEntity().hasMetadata(THUNDERSTRUCK_GHAST_KEY)) {
                event.setCancelled(true);
            }
        } else if (event.getEntity().hasMetadata(THUNDERSTRUCK_CREEPER_KEY) && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(SpawnerSpawnEvent event) {
        if (event.getSpawner().getLocation().getWorld().equals(this.trial.getWorld())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void on(EntityCombustEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();

            TrialManager.getTrial(player).ifPresent(trial -> {
                if (trial.getCurrentRoom() != this) {
                    return;
                }

                event.setDuration((int)(event.getDuration() * 1.5));
                event.setCancelled(true);
            });
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(EntityExplodeEvent event) {
        if (!this.hasStarted || this.hasEnded) return;
        if (!event.getEntity().getWorld().getName().equals(this.trial.getWorld().getName())) return;

        boolean isCrystal = event.getEntity().hasMetadata(THUNDERSTRUCK_CRYSTAL_KEY);
        boolean isCreeper = event.getEntity().hasMetadata(THUNDERSTRUCK_CREEPER_KEY);

        if (isCrystal) {
            this.trial.getWorld().getEntities().stream().filter(e -> e.hasMetadata(THUNDERSTRUCK_CREEPER_KEY)).findAny().ifPresent(creeper -> {
                ((Creeper)creeper).setPowered(true);
            });

            event.blockList().clear();

            event.setYield(0.0F);
            event.setCancelled(true);
        } else if (isCreeper) {
            Iterator<Block> iterator = event.blockList().iterator();
            List<Block> spawners = Lists.newArrayList();

            while (iterator.hasNext()) {
                Block block = iterator.next();

                if (block.getType() == Material.MOB_SPAWNER) {
                    spawners.add(block);
                    BlockState state = block.getState();
                    state.setType(Material.AIR);
                    state.update(true, false);
                }
            }

            int size = spawners.size();

            //if (((Creeper)event.getEntity()).isPowered()) {
            //    this.bossHealth -= size * (25 + new Random().nextInt(6));
            //} else {
                this.bossHealth -= size * (15 + new Random().nextInt(6));
            //}

            if (this.bossHealth > 0) {
                this.boss.setHealth(this.bossHealth);
            }

            if (this.bossHealth <= 0) {
                this.bossHealth = 0;
                this.boss.remove();

                Location start = new Location(this.trial.getWorld(), -1509, 82, -1435);

                for (int x = 0; x < 5; x++) {
                    for (int z = 0; z < 5; z++) {
                        Block block = start.clone().add(x, 0, z).getBlock();

                        if (block.getType() == Material.STAINED_GLASS) {
                            block.setType(Material.AIR);
                        }
                    }
                }

                this.trial.onRoomComplete();
            }

            event.setYield(0.0F);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void on(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            TrialManager.getTrial((Player) event.getDamager()).ifPresent(trial -> {
                if (trial.getCurrentRoom() != this) {
                    return;
                }

                if (event.getEntity().hasMetadata(THUNDERSTRUCK_CREEPER_KEY)) {
                    //((CraftEntity) event.getEntity()).getHandle().setAI(true);
                    ((CraftEntity) event.getEntity()).getHandle().fromMobSpawner = false;
                } else if (event.getEntity().hasMetadata(THUNDERSTRUCK_GHAST_KEY)) {
                    CommonsUtil.sendMessage(event.getDamager(), "&c&l(!) &cYou cannot damage the boss directly!");
                    event.setCancelled(true);
                }
            });
        }

        if (event.getEntity() instanceof Player && event.getDamager() instanceof Projectile) {
            TrialManager.getTrial((Player)event.getEntity()).ifPresent(trial -> {
                if (trial.getCurrentRoom() != this) {
                    return;
                }

                if (!(event.getDamager() instanceof Fireball)) {
                    event.setCancelled(true);
                }
            });
        }

        if (event.getEntity().hasMetadata(THUNDERSTRUCK_GHAST_KEY)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(EntityDeathEvent event) {
        if (event.getEntity().hasMetadata(THUNDERSTRUCK_CREEPER_KEY)) {
            event.getDrops().clear();
            event.setDroppedExp(0);
        }
    }

    @EventHandler
    public void on(PlayerInteractEvent event) {
        if (!this.hasStarted || this.hasEnded) return;

        Player player = event.getPlayer();

        TrialManager.getTrial(player).ifPresent(trial -> {
            Location buttonOne = new Location(this.trial.getWorld(), -1507, 90, -1388);
            Location buttonTwo = new Location(this.trial.getWorld(), -1507, 90, -1478);

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Block clickedBlock = event.getClickedBlock();

                if (clickedBlock.getLocation().equals(buttonOne) || clickedBlock.getLocation().equals(buttonTwo)) {
                    this.trial.teleportPlayer(player, this.getSpawnLocation());
                    this.spawnCrystal();
                }
            }
        });

    }

    private void spawnCrystal() {
        Location crystalLocation = this.getRandomBoardLocation();
        EnderCrystal enderCrystal = (EnderCrystal) this.trial.getWorld().spawnEntity(crystalLocation, EntityType.ENDER_CRYSTAL);
        enderCrystal.setMetadata(THUNDERSTRUCK_CRYSTAL_KEY, new FixedMetadataValue(DemonicTrials.getInstance(), true));
    }

    private void spawnCreeper() {
        Location spawnLocation = this.getRandomBoardLocation();

        Creeper creeper = (Creeper)this.trial.getWorld().spawnEntity(spawnLocation, EntityType.CREEPER);
        creeper.setMetadata(THUNDERSTRUCK_CREEPER_KEY, new FixedMetadataValue(DemonicTrials.getInstance(), true));
        //((CraftCreeper)creeper).getHandle().setAI(false);
        ((CraftCreeper)creeper).getHandle().fromMobSpawner = true;

        //String entityProperties = new Gson().toJson(new SSetEntityProperties.EntityProperties(1.5, false, false, false, "", 0, 0, false, ""));
        //creeper.setMetadata(ENTITY_PROPERTIES_KEY, new FixedMetadataValue(DemonicTrials.getInstance(), entityProperties));
    }

    private Location getRandomBoardLocation() {
        Location start = new Location(this.trial.getWorld(), -1522, 83, -1448);
        Location end = new Location(this.trial.getWorld(), -1492, 83, -1418);

        int deltaX = end.getBlockX() - start.getBlockX();
        int deltaZ = end.getBlockZ() - start.getBlockZ();

        List<Location> possible = Lists.newArrayList();

        for (int x = 0; x < deltaX; x++) {
            for (int z = 0; z < deltaZ; z++) {
                possible.add(start.clone().add(x, 0, z));
            }
        }

        Collections.shuffle(possible);

        return possible.get(0);
    }
}
