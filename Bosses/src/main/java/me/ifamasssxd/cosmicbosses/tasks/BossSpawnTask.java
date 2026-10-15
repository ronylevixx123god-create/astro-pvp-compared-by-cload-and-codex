package me.ifamasssxd.cosmicbosses.tasks;

import java.util.Random;

import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.ifamasssxd.cosmicbosses.*;
import me.ifamasssxd.cosmicbosses.struct.*;
import me.ifamasssxd.cosmicbosses.struct.enums.*;
import org.bukkit.plugin.*;
import org.bukkit.*;
import me.ifamasssxd.cosmicbosses.utils.*;
import org.bukkit.entity.*;
import com.google.common.collect.*;
import java.util.*;
public class BossSpawnTask implements Runnable
{
    Random random;

    public BossSpawnTask() {
        this.random = new Random();
    }

    @Override
    public void run() {
        final boolean enabled = CosmicBosses.getInstance().isNaturalSpawnEnabled();
        if (!enabled) {
            return;
        }
        if (Arrays.asList(Bukkit.getOnlinePlayers()).size() == 0 || Boss.bosses.size() >= 10) {
            return;
        }
        BossType type = null;
        final int chance = this.random.nextInt(100);
        if (chance < 70) {
            type = BossType.KING_SLIME;
        }
        else if (chance < 85) {
            type = BossType.SKELETON_HORSE;
        }
        else if (chance < 94) {
            type = BossType.PLAGUE_BLOATER;
        }
        final Location l = BossUtils.getRandomRegionLocation("spawnwarzone");
        if (l == null) {
            Bukkit.getLogger().info("[CosmicBosses] Null location returned by getRandomRegionLocation, skipping BossSpawnTask.");
            return;
        }
        final Location highest = l.clone();
        highest.setY(BossUtils.getHighestBlockYAt(highest, false) + 2);
        final boolean isSuiteableLocation = this.isSuitableLocation(highest);
        Bukkit.getLogger().info("[CosmicBosses] getRandomRegionLocation(): " + l + ", goodLocation: " + isSuiteableLocation);
        if (!isSuiteableLocation) {
            Bukkit.getScheduler().runTask(CosmicBosses.get(), this);
            return;
        }
        assert type != null;
        final Boss boss = type.createInstance(highest);
        assert boss != null;
        boss.setNaturallySpawned(true);
        boss.onBossSpawn();
    }

    private boolean isSuitableLocation(final Location loc) {
        return WorldGuardUtils.isPvPEnabled(loc) && WorldGuardUtils.getRegionsIn(loc).contains("spawnwarzone") && !WorldGuardUtils.getRegionsIn(loc).contains("spawn_region");
    }

    private Player getRandomWarzonePlayer() {
        final List<Player> players = Lists.newArrayList(Bukkit.getServer().getOnlinePlayers());
        Collections.shuffle(players);
        for (final Player retr : players) {
            if (!retr.hasMetadata("NPC") && retr.getLocation().getY() < 256.0 && BossUtils.canAffectPlayer(retr, false) && !WorldGuardUtils.isPvPDisabled(retr.getLocation()) && !WorldGuardUtils.getRegionName(retr.getLocation()).startsWith("spawn_") && !WorldGuardUtils.getRegionName(retr.getLocation()).contains("warp")) {
                return retr;
            }
        }
        return null;
    }
}
