package devtest;

import me.kingsavaqe.astroenvoy.EnvoyPlugin;
import me.kingsavaqe.astroenvoy.struct.EnvoyManager;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import java.util.*;

/** Destructive fixture confined to the runner's disposable world, never a live server. */
public class EnvoySafetyProbe extends JavaPlugin {
    private void check(boolean ok, String message) {
        if (!ok) throw new AssertionError(message);
        getLogger().info("PROBE PASS " + message);
    }
    @Override public void onEnable() {
        Bukkit.getScheduler().runTaskLater(this, () -> {
            EnvoyManager manager = EnvoyPlugin.getInstance().getEnvoyManager();
            List<Location> saved = new ArrayList<>(manager.getLocations());
            World world = Bukkit.getWorlds().get(0);
            List<Location> fixtures = new ArrayList<>();
            ArmorStand decoration = null;
            Location untouched = new Location(world, 128, 100, 128);
            boolean deferred = false;
            try {
                manager.setLocations(new ArrayList<>());
                check(!manager.tryStartEnvoyEvent() && !manager.isActive(), "empty setup stays inactive");
                for (int i = 0; i < 7; i++) {
                    Location l = new Location(world, 100 + i * 2, 100, 100);
                    l.getBlock().setType(Material.AIR);
                    fixtures.add(l);
                }
                untouched.getBlock().setType(Material.CHEST);
                List<Location> configured = new ArrayList<>(fixtures);
                configured.add(untouched);
                manager.setLocations(configured);
                decoration = (ArmorStand)world.spawnEntity(fixtures.get(0).clone().add(0.5, 0.1, 0.5), EntityType.ARMOR_STAND);
                decoration.setGravity(false);
                decoration.setCustomName("fixture decoration");
                Set<UUID> beforeStands = armorStandIds(world);
                check(manager.tryStartEnvoyEvent(), "exactly seven empty locations start");
                check(manager.getRemainingCrates() == 7 && manager.getCurrentChests().size() == 7,
                        "seven actual event crates tracked");
                for (Location l : fixtures) check(l.getBlock().getType() == Material.CHEST, "fixture chest spawned " + l.getBlockX());
                check(!manager.getCurrentChests().contains(untouched), "preexisting chest excluded");
                Set<UUID> eventStands = armorStandIds(world);
                eventStands.removeAll(beforeStands);
                check(eventStands.size() == 7, "seven event holograms spawned");
                fixtures.get(0).getBlock().setType(Material.STONE);
                manager.endEnvoyEvent();
                check(fixtures.get(0).getBlock().getType() == Material.STONE, "replacement block preserved");
                for (Location l : fixtures.subList(1, fixtures.size())) check(l.getBlock().getType() == Material.AIR, "event chest removed " + l.getBlockX());
                check(!decoration.isDead(), "preexisting nearby armor stand preserved");
                for (Entity entity : world.getEntities()) {
                    if (eventStands.contains(entity.getUniqueId()))
                        check(entity.isDead(), "event hologram marked removed");
                }
                manager.removeNearbyHologram(fixtures.get(0));
                check(!decoration.isDead(), "unowned hologram cleanup preserves decoration");
                manager.removeCrateAt(untouched);
                check(untouched.getBlock().getType() == Material.CHEST, "preexisting chest preserved");
                check(manager.getRemainingCrates() == 0 && manager.getCurrentChests().isEmpty() && !manager.isActive(), "event state cleared");
                manager.endEnvoyEvent();
                check(manager.getRemainingCrates() == 0, "repeated stop safe");
                final ArmorStand preserved = decoration;
                // Bukkit marks entities dead immediately; the world index clears on the next tick.
                Bukkit.getScheduler().runTaskLater(this, () -> {
                    try {
                        check(Collections.disjoint(eventStands, armorStandIds(world)), "all event holograms absent after world cleanup tick");
                        check(!preserved.isDead(), "decoration survives world cleanup tick");
                        getLogger().info("PROBE RESULT envoy-safety passed");
                    } catch (Throwable failure) {
                        getLogger().severe("PROBE FAIL " + failure);
                        failure.printStackTrace();
                    } finally {finish(manager, saved, fixtures, untouched, preserved);}
                }, 2L);
                deferred = true;
            } catch (Throwable failure) {
                getLogger().severe("PROBE FAIL " + failure);
                failure.printStackTrace();
            } finally {
                if (!deferred) finish(manager, saved, fixtures, untouched, decoration);
            }
        }, 20L);
    }

    private void finish(EnvoyManager manager, List<Location> saved, List<Location> fixtures,
                        Location untouched, ArmorStand decoration) {
        try {
            if (manager.isActive()) manager.endEnvoyEvent();
            manager.setLocations(saved);
            if (decoration != null) decoration.remove();
            for (Location location : fixtures) location.getBlock().setType(Material.AIR);
            untouched.getBlock().setType(Material.AIR);
        } finally {Bukkit.shutdown();}
    }

    private static Set<UUID> armorStandIds(World world) {
        Set<UUID> ids = new HashSet<>();
        for (Entity entity : world.getEntities())
            if (entity instanceof ArmorStand) ids.add(entity.getUniqueId());
        return ids;
    }

}
