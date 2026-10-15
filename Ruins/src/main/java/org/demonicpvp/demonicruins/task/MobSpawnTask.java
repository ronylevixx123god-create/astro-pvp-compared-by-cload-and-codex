package org.demonicpvp.demonicruins.task;

import com.google.common.collect.Lists;
import net.astropvp.core.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.demonicpvp.demonicruins.mob.MobUtil;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.demonicpvp.demonicruins.listener.MobListener.RUINS_WORLD_NAME;
import static org.demonicpvp.demonicruins.mob.MobUtil.*;

public class MobSpawnTask extends BukkitRunnable {
    private final static int MAX_AREA_MOBS_THRESHOLD = 4;

    private int ticks = 0;
    private double minutes = 0;

    @Override
    public void run() {
        World ruinsWorld = Bukkit.getWorld(RUINS_WORLD_NAME);

        if (this.ticks % 5 == 0) {
            ruinsWorld.getEntities().stream().filter(MobUtil::isRuinMob).filter(m -> MobUtil.getRuinsMobType(m).equals(GUARDIAN_RUINS_MOB_VALUE)).forEach(m -> {
                Location spawnLocation = LocationUtils.fromStringLocation(m.getMetadata(SPAWN_LOCATION_KEY).get(0).asString());

                if (m.getLocation().distance(spawnLocation) > 40) {
                    // send back to spawn location
                    m.teleport(spawnLocation);
                }
            });
        }

        if (this.ticks >= 20 * 15) {
            List<Location> spawnLocations = Lists.newArrayList(
                    ruinsWorld.getBlockAt(-18, 23, 80).getLocation(),
                    ruinsWorld.getBlockAt(150, 24, 207).getLocation(),
                    ruinsWorld.getBlockAt(292, 24, 98).getLocation(),
                    ruinsWorld.getBlockAt(310, 25, 267).getLocation(),
                    ruinsWorld.getBlockAt(-8, 27, 307).getLocation(),
                    ruinsWorld.getBlockAt(191, 22, 311).getLocation(),
                    ruinsWorld.getBlockAt(386, 24, 177).getLocation(),
                    ruinsWorld.getBlockAt(386, 24, 177).getLocation(),
                    ruinsWorld.getBlockAt(320, 25, 45).getLocation(),
                    ruinsWorld.getBlockAt(63, 23, -138).getLocation(),
                    ruinsWorld.getBlockAt(59, 72, 32).getLocation(),
                    ruinsWorld.getBlockAt(109, 23, 208).getLocation(),
                    ruinsWorld.getBlockAt(-101, 23, 157).getLocation(),
                    ruinsWorld.getBlockAt(-48, 24, 304).getLocation(),
                    ruinsWorld.getBlockAt(-104, 22, 158).getLocation(),
                    ruinsWorld.getBlockAt(-81, 26, 59).getLocation(),
                    ruinsWorld.getBlockAt(-31, 26, 19).getLocation(),
                    ruinsWorld.getBlockAt(-102, 26, -149).getLocation(),
                    ruinsWorld.getBlockAt(117, 25, -84).getLocation(),
                    ruinsWorld.getBlockAt(135, 26, -163).getLocation()
            );

            for (Location spawnLocation : spawnLocations) {
                // don't *always* spawn
                if (Math.random() < 0.85) {
                    int nearbyRuinMobs = spawnLocation.getWorld().getNearbyEntities(spawnLocation, 25, 25, 25).stream().filter(MobUtil::isRuinMob).collect(Collectors.toSet()).size();

                    if (nearbyRuinMobs < MAX_AREA_MOBS_THRESHOLD) {
                        int randomSubtraction = new Random().nextInt(3);
                        int toSpawn = Math.max(1, (MAX_AREA_MOBS_THRESHOLD - nearbyRuinMobs) - randomSubtraction);

                        for (int i = 0; i < toSpawn; i++) {
                            Location offsetLocation = spawnLocation.clone().add(new Random().nextInt(6), 0, new Random().nextInt(6));
                            if (offsetLocation.getBlock().getType() == Material.AIR) MobUtil.spawnGuardian(offsetLocation, Math.random() < 0.75);
                        }
                    }
                }
            }

            this.minutes += 0.25D;
            this.ticks = 0;
        }

        if (this.minutes >= 5) {
            List<Location> eliteGuardianSpawnLocations = Lists.newArrayList(
                    ruinsWorld.getBlockAt(58, 27, 180).getLocation(),
                    ruinsWorld.getBlockAt(203, 23, 20).getLocation(),
                    ruinsWorld.getBlockAt(58, 23, -123).getLocation(),
                    ruinsWorld.getBlockAt(311, 25, 81).getLocation(),
                    ruinsWorld.getBlockAt(353, 26, 205).getLocation(),
                    ruinsWorld.getBlockAt(353, 26, 205).getLocation(),
                    ruinsWorld.getBlockAt(-8, 27, 307).getLocation()
            );

            // pick a random location
            Collections.shuffle(eliteGuardianSpawnLocations);
            Location chosenLocation = eliteGuardianSpawnLocations.get(0);

            // if there's no elite guardian on the map, spawn one
            /*
            for (Entity entity : ruinsWorld.getEntities()) {
                if (MobUtil.isRuinMob(entity) && MobUtil.getRuinsMobType(entity).equals(ELITE_GUARDIAN_RUINS_MOB_VALUE)) {
                    System.out.println(entity.getLocation());
                }
            }*/

            if (ruinsWorld.getEntities().stream().noneMatch(e -> MobUtil.isRuinMob(e) && MobUtil.getRuinsMobType(e).equals(ELITE_GUARDIAN_RUINS_MOB_VALUE))) {
                Bukkit.getLogger().info("Spawned Elite Guardian at " + chosenLocation.getBlockX() + ", " + chosenLocation.getBlockY() + ", " + chosenLocation.getBlockZ());
                MobUtil.spawnEliteGuardian(chosenLocation, Math.random() < 0.10);
            }

            this.minutes = 0;
        }

        this.ticks++;
    }
}
