package devtest;

import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

/** Empty fixtures for plugin initialization, never a substitute for Astro's missing maps. */
public class IntegrationWorlds extends JavaPlugin {
    @Override public void onEnable() {
        for (String name : new String[]{"world_ruins", "world_arcade", "tournament_world", "dungeon_yijki_a", "dungeon_yijki_b"}) {
            World world = Bukkit.createWorld(new WorldCreator(name).type(WorldType.FLAT).generateStructures(false));
            world.setSpawnFlags(false, false);
            world.setGameRuleValue("doMobSpawning", "false");
        }
        World world = Bukkit.getWorld("world_arcade");
        WorldGuardPlugin guard = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
        guard.getRegionManager(world).addRegion(new ProtectedCuboidRegion("arcade_blocks", new BlockVector(-2, 1, -2), new BlockVector(2, 5, 2)));
        getLogger().info("TEST FIXTURE empty worlds and arcade region initialized; original map behavior untested");
    }
}
