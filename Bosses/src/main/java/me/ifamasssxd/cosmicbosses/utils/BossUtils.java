package me.ifamasssxd.cosmicbosses.utils;

import com.cosmicpvp.cosmicutils.compat.ParamBuilder;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.bosses.yijki.WitherBoss;
import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonYijki;
import me.ifamasssxd.cosmicbosses.dungeonboss.YijkiRunnerEntity;
import me.ifamasssxd.cosmicbosses.entities.CustomEntity;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import me.ifamasssxd.cosmicbosses.struct.enums.ChunkCoords;
import net.minecraft.server.v1_8_R3.*;
import org.arkhamnetwork.Arkkit.commands.CommandArkReboot;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.*;

public class BossUtils {
    private static Essentials ess;
    static boolean factions;
    private static HashMap<ChunkCoords, Boolean> warzone;
    private static Random rand;

    public BossUtils() {
        BossUtils.factions = Bukkit.getPluginManager().isPluginEnabled("Factions");
    }

    public static boolean isInGodMode(final Player player) {
        if (player.getGameMode() == GameMode.CREATIVE) {
            return true;
        }
        if (BossUtils.ess != null) {
            final User user = BossUtils.ess.getUser(player);
            return user.isVanished() || user.isGodModeEnabled();
        }
        return false;
    }

    public static int getHighestBlockYAt(final Location location, final boolean needSolid) {
        for (int i = 255; i > 0; --i) {
            final Block block = location.getWorld().getBlockAt(location.getBlockX(), i, location.getBlockZ());
            final Material type = block.getType();
            if ((type != Material.AIR && !needSolid) || (needSolid && type.isBlock() && type.isSolid() && !type.isTransparent())) {
                return i;
            }
        }
        return location.getWorld().getHighestBlockYAt(location.getBlockX(), location.getBlockZ());
    }

    public static int getHighestBlockYAt(final World world, final int x, final int z, final boolean needSolid) {
        for (int i = 255; i > 0; --i) {
            final Block block = world.getBlockAt(x, i, z);
            final Material type = block.getType();
            if ((type != Material.AIR && !needSolid) || (needSolid && type.isBlock() && type.isSolid() && !type.isTransparent())) {
                return i;
            }
        }
        return world.getHighestBlockYAt(x, z);
    }

    public static boolean canAffectPlayer(final Player pl, final boolean checkBattlefield) {
        return canAffectPlayer(pl, checkBattlefield, false);
    }

    public static boolean canAffectPlayer(final Player pl, final boolean checkBattlefield, final boolean checkEnd) {
        if (BossUtils.ess == null) {
            BossUtils.ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        }
        if (pl.hasMetadata("NPC")) {
            return false;
        }

        if (pl.hasMetadata("frozenSkrrt")) {
            return false;
        }
        if (BossUtils.ess != null && (BossUtils.ess.getUser(pl).isVanished() || BossUtils.ess.getUser(pl).isGodModeEnabled())) {
            return false;
        }
        if (com.cosmicpvp.cosmicutils.utils.WorldGuardUtils.isPvPEnabled(pl.getLocation())) {
            if (checkEnd && pl.getLocation().getWorld().getEnvironment() == World.Environment.THE_END) {
                return true;
            }
            final String regionName = com.cosmicpvp.cosmicutils.utils.WorldGuardUtils.getRegionName(pl.getLocation());
            if (regionName.contains("warzone") || (checkBattlefield && regionName.contains("battlefield"))) {
                if (BossUtils.factions) {
                    final FLocation ploc = new FLocation(pl.getLocation());
                    return FactionUtils.getFactionAt(ploc).isWarZone();
                }
                return true;
            }
        }
        return false;
    }

    public static Entity spawnDungeonBoss(final Location location, final int phase) {
        final DungeonYijki boss = (DungeonYijki) BossType.DUNGEON_YIJKI.createInstance(location);
        assert boss != null;
        boss.onBossSpawn();
        try {
            boss.setBossPhase(DungeonYijki.BossPhase.values()[phase - 1]);
        } catch (Exception e) {
            Bukkit.getLogger().info("Invalid Dungeon Yijki Phase: " + phase + "! " + DungeonYijki.BossPhase.values().length);
        }
        return (Entity) boss.getEntity();
    }

    public static void teleportPlayer(final Player p, final Location location) {
        //p.setMetadata("bypassCombatTeleport", (MetadataValue)new FixedMetadataValue((Plugin) Enchantments.getInstance(), (Object)location));
        p.setMetadata("bossTeleport", new FixedMetadataValue(CosmicBosses.getInstance(), location));
        //p.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) CosmicBosses.get(), () -> p.teleport(location, PlayerTeleportEvent.TeleportCause.UNKNOWN), 1L);
    }

    public static Entity spawnDungeonBoss(final Location location, final int phase, final BossType type) {
        final DungeonYijki boss = (DungeonYijki) type.createInstance(location);
        boss.onBossSpawn();
        try {
            boss.setBossPhase(DungeonYijki.BossPhase.values()[phase - 1]);
        } catch (Exception e) {
            Bukkit.getLogger().info("Invalid Dungeon Yijki Phase: " + phase + "! " + DungeonYijki.BossPhase.values().length);
        }
        return (Entity) boss.getEntity();
    }

    public static Entity createDungeonGhost(final Location spawn, final Location target, final int aliveDuration) {
        final CraftWorld craft = (CraftWorld) spawn.getWorld();
        final YijkiRunnerEntity entity = new YijkiRunnerEntity((net.minecraft.server.v1_8_R3.World) craft.getHandle(), spawn, target, aliveDuration);
        entity.setPosition(spawn.getX(), spawn.getY(), spawn.getZ());
        craft.getHandle().addEntity((net.minecraft.server.v1_8_R3.Entity) entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (Entity) entity.getBukkitEntity();
    }

    public static void spawnBoss(final String[] args) {
        final String bossName = args[0];
        final World world = Bukkit.getWorld(args[1]);
        final Location loc = new Location(world, (double) Integer.parseInt(args[2]), (double) Integer.parseInt(args[3]), (double) Integer.parseInt(args[4]));
        if (!loc.getChunk().isLoaded()) {
            loc.getChunk().load();
        }
        final Location spawn = new Location(world, (double) Integer.parseInt(args[5]), (double) Integer.parseInt(args[6]), (double) Integer.parseInt(args[7]));
        if (!spawn.getChunk().isLoaded()) {
            spawn.getChunk().load();
        }
        final double health = Double.parseDouble(args[8]);
        final BossType type = BossType.getBossType(bossName);
        if (type == null) {
            return;
        }
        final Boss newBoss = type.createInstance(spawn);
        newBoss.onBossSpawn();
        newBoss.getEntity().teleport(loc);
        newBoss.getEntity().setHealth(health);
    }

    public static boolean isWarzone(final Location loc) {
        if (loc.getWorld().getName().toLowerCase().startsWith("dungeon")) {
            return true;
        }
        final ChunkCoords coords = new ChunkCoords(loc.getChunk().getX(), loc.getChunk().getZ());
        if (BossUtils.warzone.containsKey(coords)) {
            return BossUtils.warzone.get(coords);
        }
        final String regionName = com.cosmicpvp.cosmicutils.utils.WorldGuardUtils.getRegionName(loc);
        if (regionName.contains("warzone")) {
            if (BossUtils.factions) {
                final FLocation ploc = new FLocation(loc);
                if (!FactionUtils.getFactionAt(ploc).getId().equals("-2")) {
                    BossUtils.warzone.put(coords, false);
                    return false;
                }
            }
            BossUtils.warzone.put(coords, true);
            return true;
        }
        return false;
    }

    public static net.minecraft.server.v1_8_R3.Entity createCustomEntity(final CustomEntity type, final Boss boss, final Location location) {
        final CraftWorld world = (CraftWorld) location.getWorld();
        try {
            final net.minecraft.server.v1_8_R3.Entity entity = (net.minecraft.server.v1_8_R3.Entity) type.getCustomClass().getDeclaredConstructor(net.minecraft.server.v1_8_R3.World.class).newInstance(world.getHandle());
            entity.setPosition(location.getX(), location.getY(), location.getZ());
            world.getHandle().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
            if (entity instanceof WitherBoss) {
                ((WitherBoss) entity).setBoss(boss);
            }
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Entity createCustomEntity(final CustomEntity type, final Location location) {
        final CraftWorld world = (CraftWorld) location.getWorld();
        try {
            final net.minecraft.server.v1_8_R3.Entity entity = (net.minecraft.server.v1_8_R3.Entity) type.getCustomClass().getDeclaredConstructor(net.minecraft.server.v1_8_R3.World.class).newInstance(world.getHandle());
            entity.setPosition(location.getX(), location.getY(), location.getZ());
            world.getHandle().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
            return (Entity) entity.getBukkitEntity();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static PathfinderGoalSelector getSelector(final EntityInsentient ent, final String select) {
        final Object goalSelector = ReflectionUtils.getObject(EntityInsentient.class, ent, select);
        return (PathfinderGoalSelector) goalSelector;
    }

    public static void addSelector(final EntityInsentient insent, final int index, final PathfinderGoal goal) {
        final Object goalSelector = ReflectionUtils.getObject(EntityInsentient.class, insent, "goalSelector");
        if (goalSelector != null) {
            com.cosmicpvp.cosmicutils.utils.ReflectionUtils.callReflectionMethod(goalSelector, "a", new ParamBuilder((Class) Integer.TYPE, (Object) index).add((Class) PathfinderGoal.class, (Object) goal));
        }
    }

    public static void addTargetSelector(final EntityInsentient insent, final int index, final PathfinderGoal goal) {
        final Object goalSelector = ReflectionUtils.getObject(EntityInsentient.class, insent, "targetSelector");
        if (goalSelector != null) {
            com.cosmicpvp.cosmicutils.utils.ReflectionUtils.callReflectionMethod(goalSelector, "a", new ParamBuilder((Class) Integer.TYPE, (Object) index).add((Class) PathfinderGoal.class, (Object) goal));
        }
    }

    public static void clearGoalSelectors(final EntityInsentient ent) {
        final Object goalSelector = ReflectionUtils.getObject(EntityInsentient.class, ent, "goalSelector");
        final List<?> c = (List<?>) ReflectionUtils.getObject(PathfinderGoalSelector.class, goalSelector, "c");
        c.clear();
        final List<?> b = (List<?>) ReflectionUtils.getObject(PathfinderGoalSelector.class, goalSelector, "b");
        b.clear();
        final Object targetSelector = ReflectionUtils.getObject(EntityInsentient.class, ent, "targetSelector");
        final List<?> targC = (List<?>) ReflectionUtils.getObject(PathfinderGoalSelector.class, targetSelector, "c");
        targC.clear();
        final List<?> targB = (List<?>) ReflectionUtils.getObject(PathfinderGoalSelector.class, targetSelector, "b");
        targB.clear();
    }

    public static void dropWhitelistedItem(final Player p, final ItemStack is) {
        final Item item = p.getWorld().dropItem(p.getLocation(), is);
        item.setMetadata("whitelisted_item", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) p.getName()));
        item.setMetadata("do_not_clear", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
    }

    public static Location getRandomLocationNearby(final Location l, final int maxDist, final int yBoost) {
        return getRandomLocationNearby(l, maxDist, 2, yBoost);
    }

    public static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }

    public static Location getRandomRegionLocation(final String regionName) {
        final RegionManager regionMan = getWorldGuard().getGlobalRegionManager().get(Bukkit.getWorld("world"));

        if (regionMan == null) {
            return null;
        }

        final ProtectedRegion region = regionMan.getRegion(regionName);
        if (region == null) {
            return null;
        }

        final Random r = new Random();
        final int minX = region.getMinimumPoint().getBlockX();
        final int maxX = region.getMaximumPoint().getBlockX();
        final int minZ = region.getMinimumPoint().getBlockZ();
        final int maxZ = region.getMaximumPoint().getBlockZ();
        final int x = minX + r.nextInt(maxX + 1 - minX);
        final int z = minZ + r.nextInt(maxZ + 1 - minZ);
        final int y = Bukkit.getWorld("world").getHighestBlockYAt(x, z);
        return new Location(Bukkit.getWorld("world"), (double) x, (double) y, (double) z);
    }

    public static Location getRandomLocationNearby(final Location l, final int maxDist, final int minDistance, final int yBoost) {
        final Location retrLoc = l.clone();
        retrLoc.add((double) ((BossUtils.rand.nextBoolean() ? 1 : -1) * (BossUtils.rand.nextInt(maxDist - minDistance) + minDistance)), 0.0, (double) ((BossUtils.rand.nextBoolean() ? 1 : -1) * (BossUtils.rand.nextInt(maxDist - minDistance) + minDistance)));
        retrLoc.add(0.5, (double) yBoost, 0.5);
        return retrLoc;
    }


    public static Location getRandomLocationNearby(final Location l, final int maxDist, final int minDistance, final int yBoost, final boolean highestY) {
        final Location retrLoc = l.clone();
        retrLoc.add((double) ((BossUtils.rand.nextBoolean() ? 1 : -1) * (BossUtils.rand.nextInt(maxDist - minDistance) + minDistance)), 0.0, (double) ((BossUtils.rand.nextBoolean() ? 1 : -1) * (BossUtils.rand.nextInt(maxDist - minDistance) + minDistance)));
        retrLoc.add(0.5, (double) yBoost, 0.5);
        if (highestY && retrLoc.getBlock().getType() == Material.AIR) {
            while (retrLoc.getBlock().getType() == Material.AIR && retrLoc.getY() > 0.0) {
                retrLoc.setY(retrLoc.getY() - 1.0);
            }
        }
        return retrLoc;
    }

    public static Vec3D oldJ(EntityLiving ent, float f) {
        if (f == 1.0F) {
            float f7 = MathHelper.cos(-ent.yaw * 0.017453292F - 3.1415927F);
            float f8 = MathHelper.sin(-ent.yaw * 0.017453292F - 3.1415927F);
            float f9 = -MathHelper.cos(-ent.pitch * 0.017453292F);
            float f10 = MathHelper.sin(-ent.pitch * 0.017453292F);
            return new Vec3D((f8 * f9), f10, (f7 * f9));
        }
        float f1 = ent.lastPitch + (ent.pitch - ent.lastPitch) * f;
        float f2 = ent.lastYaw + (ent.yaw - ent.lastYaw) * f;
        float f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        return new Vec3D((f4 * f5), f6, (f3 * f5));
    }

    public static EntityHuman findNearbyVulnerablePlayer(net.minecraft.server.v1_8_R3.World world, net.minecraft.server.v1_8_R3.Entity entity, double d3) {
        double d4 = -1.0D;
        EntityHuman entityhuman = null;
        for (int i = 0; i < world.players.size(); i++) {
            EntityHuman entityhuman1 = world.players.get(i);
            if (entityhuman1 != null && !entityhuman1.dead)
                if (!entityhuman1.abilities.isInvulnerable && entityhuman1.isAlive()) {
                    double d5 = entityhuman1.e(entity.locX, entity.locY, entity.locZ);
                    double d6 = d3;
                    if ((d3 < 0.0D || d5 < d6 * d6) && (d4 == -1.0D || d5 < d4)) {
                        d4 = d5;
                        entityhuman = entityhuman1;
                    }
                }
        }
        return entityhuman;
    }

    public static void pushAwayPlayer(final Entity entity, final Player p, final double speed) {
        final Vector unitVector = p.getLocation().toVector().subtract(entity.getLocation().toVector());
        if (unitVector.length() != 0.0) {
            unitVector.normalize();
        }
        p.setVelocity(unitVector.multiply(speed));
    }

    public static void pushAwayPlayer(final Player p, final Location toPushFrom, final double speed) {
        final Vector unitVector = p.getLocation().toVector().subtract(toPushFrom.toVector());
        if (unitVector.length() != 0.0) {
            unitVector.normalize();
        }
        p.setVelocity(unitVector.multiply(speed));
    }

    public static List<Boss> getAllBossesFromType(final Class<? extends Boss> clazz) {
        final List<Boss> bosses = new ArrayList<Boss>();
        for (final Boss boss : Boss.bosses.values()) {
            if (boss != null && boss.getClass().isAssignableFrom(clazz)) {
                bosses.add(boss);
            }
        }
        return bosses;
    }

    public static Boss getBossFromMinion(final Entity e) {
        if (e.getMetadata("minion").size() > 0) {
            final UUID uuid = (UUID) e.getMetadata("minion").get(0).value();
            if (uuid != null) {
                final Boss boss = Boss.bosses.get(uuid);
                if (boss != null) {
                    return boss;
                }
            }
        }
        final Iterator<Boss> iterator = Boss.bosses.values().iterator();
        while (iterator.hasNext()) {
            final Boss boss = iterator.next();
            if (boss.hasMinions() && boss.getMinions().contains(e)) {
                return boss;
            }
        }
        return null;
    }

    public static boolean dealPercentMaxHealthDamage(final Player p, final double percentage, final int maxDamage, final boolean check) {
        return dealPercentMaxHealthDamage(p, percentage, maxDamage, check, false);
    }

    public static boolean dealPercentMaxHealthDamage(final Player p, final double percentage, final int maxDamage, final boolean check, final boolean checkEnd) {
        if (p.isDead() || p.getHealth() <= 0.0 || (check && !canAffectPlayer(p, true, checkEnd)) || p.getGameMode() == GameMode.CREATIVE) {
            return false;
        }
        double damage = p.getMaxHealth() * (percentage * 0.01);
        if (damage > maxDamage) {
            damage = maxDamage;
        }
        if (p.getHealth() - damage <= 0.0) {
            p.setHealth(0.0);
        } else {
            p.setHealth(p.getHealth() - damage);
        }
        return true;
    }

    public static boolean dealPercentMaxHealthDamage(final Player p, final double percentage, final int maxDamage) {
        return dealPercentMaxHealthDamage(p, percentage, maxDamage, true);
    }

    public static boolean dealPercentHealthDamage(final Player p, final double percentage, final int maxDamage) {
        if (p.isDead() || !canAffectPlayer(p, true)) {
            return false;
        }
        double damage = p.getHealth() * (percentage * 0.01);
        if (damage > maxDamage) {
            damage = maxDamage;
        }
        if (p.getHealth() - damage <= 0.0) {
            p.setHealth(0.0);
        } else {
            p.setHealth(p.getHealth() - damage);
        }
        return true;
    }

    public static boolean isRebooting() {
        return (Bukkit.getPluginManager().isPluginEnabled("Arkkit") && CommandArkReboot.restart_task_exists) || (Bukkit.getPluginManager().isPluginEnabled("NetworkManager"));
    }

    public static void dealPercentHealthDamage(final Player p, final double percentage, final int maxDamage, final int minDamage) {
        double damage = p.getHealth() * (percentage * 0.01);

        if (!canAffectPlayer(p, true)) {
            return;
        }
        if (damage > maxDamage) {
            damage = maxDamage;
        }
        if (damage < minDamage) {
            damage = minDamage;
        }
        if (p.getHealth() - damage <= 0.0) {
            p.setHealth(0.0);
        } else {
            p.setHealth(p.getHealth() - damage);
        }
    }

    static {
        BossUtils.factions = true;
        BossUtils.warzone = new HashMap<ChunkCoords, Boolean>();
        BossUtils.rand = new Random();
    }
}
