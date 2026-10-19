package devtest;

import me.kingsavaqe.astroenvoy.EnvoyPlugin;
import me.kingsavaqe.astroenvoy.struct.EnvoyManager;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;
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
            Location untouched = new Location(world, 128, 100, 128);
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
                check(manager.tryStartEnvoyEvent(), "exactly seven empty locations start");
                check(manager.getRemainingCrates() == 7 && manager.getCurrentChests().size() == 7,
                        "seven actual event crates tracked");
                for (Location l : fixtures) check(l.getBlock().getType() == Material.CHEST, "fixture chest spawned " + l.getBlockX());
                check(!manager.getCurrentChests().contains(untouched), "preexisting chest excluded");
                manager.endEnvoyEvent();
                for (Location l : fixtures) check(l.getBlock().getType() == Material.AIR, "event chest removed " + l.getBlockX());
                check(untouched.getBlock().getType() == Material.CHEST, "preexisting chest preserved");
                check(manager.getRemainingCrates() == 0 && manager.getCurrentChests().isEmpty() && !manager.isActive(), "event state cleared");
                manager.endEnvoyEvent();
                check(manager.getRemainingCrates() == 0, "repeated stop safe");
                getLogger().info("PROBE RESULT envoy-safety passed");
            } catch (Throwable failure) {
                getLogger().severe("PROBE FAIL " + failure);
                failure.printStackTrace();
            } finally {
                if (manager.isActive()) manager.endEnvoyEvent();
                manager.setLocations(saved);
                for (Location l : fixtures) l.getBlock().setType(Material.AIR);
                untouched.getBlock().setType(Material.AIR);
                Bukkit.shutdown();
            }
        }, 20L);
    }
}
