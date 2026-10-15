package org.demonicpvp.demonictrials.trial.room.impl;

import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.demonicpvp.demonictrials.DemonicTrials;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.room.TrialRoom;
import org.demonicpvp.demonictrials.util.WorldEditHelper;
import org.github.paperspigot.Title;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class TargetPracticeRoom extends TrialRoom {
    private final static String TARGET_PRACTICE_MOB_KEY = "targetPracticeMob";

    private int pointsNeeded;
    private int currentPoints;

    public TargetPracticeRoom(TrialSession trial) {
        super(trial);
    }

    @Override
    public String getName() {
        return "Target Practice";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.LIGHT_PURPLE;
    }

    @Override
    public Material getMaterial() {
        return Material.ARMOR_STAND;
    }

    @Override
    public List<String> getDescription() {
        return Lists.newArrayList(
                "Reach the point goal by shooting the mobs before",
                "time runs out!"
        );
    }

    @Override
    public Location getSpawnLocation() {
        return new Location(this.trial.getWorld(), -221.5, 151, -193.5, 0f, 0f);
    }

    @Override
    public Set<Location> getExitPortalLocations() {
        return Sets.newHashSet(new Location(this.trial.getWorld(), -222, 151, -194, 0f, 0f));
    }

    @Override
    public void updateScoreboard(Player player, List<String> lines) {
        lines.add("&f");
        lines.add(this.trial.getCurrentTier().getColor() + "&lPoints");
        lines.add("&7" + this.currentPoints + " / " + this.pointsNeeded);
    }

    @Override
    public void reset() {
        super.reset();

        WorldEditHelper.pasteSchematic("trials_target_practice", new Location(this.trial.getWorld(), -280, 143, -249));

        for (Entity entity : this.trial.getWorld().getEntities()) {
            if (entity.hasMetadata(TARGET_PRACTICE_MOB_KEY)) {
                entity.remove();
            }
        }
    }

    @Override
    public void start() {
        super.start();

        this.sendToolTipAndMessage();

        int totalPlayerAdd = 0;

        for (Player p : this.trial.getPlayers()) {
            ItemStack bow = new ItemStack(Material.BOW);
            bow.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
            ItemMeta meta = bow.getItemMeta();
            meta.setDisplayName(CommonsUtil.color("&c&lCupid's Bow"));
            bow.setItemMeta(meta);

            p.getInventory().setItem(0, this.trial.getItemWithTrialLore(this.trial.applySneakSkin(bow, "CUPID_BOW")));
            p.getInventory().setItem(1, this.trial.getItemWithTrialLore(new ItemBuilder(Material.GOLDEN_APPLE).amount(5).build()));
            p.getInventory().setItem(2, this.trial.getItemWithTrialLore(new ItemBuilder(Material.POTION).data((short)16421).amount(4).build()));
            p.getInventory().setItem(27, this.trial.getItemWithTrialLore(new ItemBuilder(Material.ARROW).name(CommonsUtil.color("&c&lCupid's Arrow")).build()));

            p.getInventory().setArmorContents(this.trial.getHazmatArmor());

            totalPlayerAdd += 5 + new Random().nextInt(10);
        }

        this.currentPoints = 0;
        this.pointsNeeded = Math.min(115, 80 + totalPlayerAdd);

        List<Location> spawnLocations = Lists.newArrayList(
                new Location(this.trial.getWorld(), -224, 151, -168),
                new Location(this.trial.getWorld(), -222, 151, -168),
                new Location(this.trial.getWorld(), -220, 151, -168),
                new Location(this.trial.getWorld(), -235, 151, 170),
                new Location(this.trial.getWorld(), -240, 151, -175),
                new Location(this.trial.getWorld(), -252, 151, -174),
                new Location(this.trial.getWorld(), -257, 151, -183),
                new Location(this.trial.getWorld(), -248, 151, -194),
                new Location(this.trial.getWorld(), -254, 151, -205),
                new Location(this.trial.getWorld(), -249, 151, -200),
                new Location(this.trial.getWorld(), -246, 151, -221),
                new Location(this.trial.getWorld(), -235, 151, -213),
                new Location(this.trial.getWorld(), -230, 151, -226),
                new Location(this.trial.getWorld(), -230, 151, -226),
                new Location(this.trial.getWorld(), -221, 151, -222),
                new Location(this.trial.getWorld(), -207, 151, -217),
                new Location(this.trial.getWorld(), -206, 151, -206),
                new Location(this.trial.getWorld(), -196, 151, -212),
                new Location(this.trial.getWorld(), -195, 151, -199),
                new Location(this.trial.getWorld(), -196, 151, -193),
                new Location(this.trial.getWorld(), -192, 151, -180),
                new Location(this.trial.getWorld(), -200, 151, -169),
                new Location(this.trial.getWorld(), -204, 151, -182),
                new Location(this.trial.getWorld(), -211, 151, -175),
                new Location(this.trial.getWorld(), -241, 151, -161),
                new Location(this.trial.getWorld(), -224, 151, -153),
                new Location(this.trial.getWorld(), -225, 151, -232),
                new Location(this.trial.getWorld(), -218, 151, -229),
                new Location(this.trial.getWorld(), -183, 151, -199),
                new Location(this.trial.getWorld(), -204, 151, -159),
                new Location(this.trial.getWorld(), -261, 151, -198)
        );

        new BukkitRunnable() {
            private int lastSpawned = 0;

            @Override
            public void run() {
                if (TargetPracticeRoom.this.trial.getPlayers().isEmpty() || TargetPracticeRoom.this.hasEnded) {
                    this.cancel();
                    return;
                }

                for (Entity entity : TargetPracticeRoom.this.trial.getWorld().getEntities()) {
                    if (entity.hasMetadata(TARGET_PRACTICE_MOB_KEY)) {
                        long spawnedAt = entity.getMetadata(TARGET_PRACTICE_MOB_KEY).get(0).asLong();

                        if (System.currentTimeMillis() - spawnedAt > 7500) {
                            entity.remove();
                        }
                    }
                }

                if (this.lastSpawned >= 6 || (this.lastSpawned > 3 && Math.random() < 0.5)) {
                    int amount = new Random().nextInt(4) + 1;

                    Collections.shuffle(spawnLocations);

                    for (int i = 0; i < amount; i++) {
                        Location location = spawnLocations.get(i);
                        TargetPracticeRoom.this.spawnMob(location);
                    }

                    this.lastSpawned = 0;
                }

                this.lastSpawned++;
            }
        }.runTaskTimer(DemonicTrials.getInstance(), 20L, 20L);
    }

    private void spawnMob(Location location) {
        int addX = new Random().nextInt(3) + 1;
        int addZ = new Random().nextInt(3) + 1;
        Location addLocation = location.clone().add(addX, 0, addZ);

        if (addLocation.getBlock().getType() == Material.AIR && addLocation.getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
            location = addLocation;
        }

        Zombie zombie = (Zombie)this.trial.getWorld().spawnEntity(location, EntityType.ZOMBIE);
        zombie.setVillager(false);
        zombie.setBaby(false);
        if (zombie.getVehicle() != null) zombie.getVehicle().remove();
        zombie.getEquipment().clear();
        zombie.setMetadata(TARGET_PRACTICE_MOB_KEY, new FixedMetadataValue(DemonicTrials.getInstance(), System.currentTimeMillis()));

        //((CraftEntity)zombie).getHandle().setAI(false);
        ((CraftEntity)zombie).getHandle().fromMobSpawner = true;
    }

    @EventHandler
    public void on(EntityDamageByEntityEvent event) {
        if (!this.hasStarted) return;

        if (event.getDamager() instanceof Arrow && event.getEntity().getWorld().getName().equals(this.trial.getWorld().getName())) {
            ProjectileSource shooter = ((Arrow) event.getDamager()).getShooter();

            if (!(shooter instanceof Player)) {
                return;
            }

            if (event.getEntity().hasMetadata(TARGET_PRACTICE_MOB_KEY) && !this.hasEnded) {
                this.currentPoints += 2;

                if (event.getDamager().getLocation().getY() - event.getEntity().getLocation().getY() > 1.35D) {
                    ((Player) shooter).sendTitle(new Title("", CommonsUtil.color("&c&lHEADSHOT (&72x Points&c&l)"), 0, 20, 5));
                    ((Player) shooter).playSound(((Player) shooter).getLocation(), Sound.ORB_PICKUP, 1.0F, 2.0F);
                    this.currentPoints += 2;
                } else {
                    ((Player) shooter).playSound(((Player) shooter).getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                }

                event.getEntity().remove();
                event.setCancelled(true);

                if (this.currentPoints >= this.pointsNeeded) {
                    this.trial.onRoomComplete();
                    this.trial.getWorld().getEntities().stream().filter(e -> e.hasMetadata(TARGET_PRACTICE_MOB_KEY)).forEach(Entity::remove);
                }
            } else if (event.getEntity() instanceof Player) {
                event.setCancelled(true);
            }
        }
    }
}
