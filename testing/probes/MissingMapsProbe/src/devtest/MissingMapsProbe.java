package devtest;

import org.bukkit.*;
import org.bukkit.entity.Player;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.WorldServer;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.NetworkManager;
import net.minecraft.server.v1_8_R3.EnumProtocolDirection;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.plugin.java.JavaPlugin;
import java.lang.reflect.*;
import java.util.*;

/** Runs only in the disposable integration runner: deliberately unloads its empty map fixture. */
public class MissingMapsProbe extends JavaPlugin {
    private void check(boolean ok, String message) {
        if (!ok) throw new AssertionError(message);
        getLogger().info("PROBE PASS " + message);
    }
    @Override public void onEnable() {
        Bukkit.getScheduler().runTaskLater(this, () -> {
            EntityPlayer fixture = null;
            try {
                check(Bukkit.getPluginManager().isPluginEnabled("AstroHypeBoxes"), "HypeBoxes enables with actual utilities");
                check(Bukkit.getPluginManager().isPluginEnabled("AstroPvPTop"), "PvPTop enables with actual utilities");
                check(Bukkit.getPluginManager().getPlugin("ProxyPatch") == null, "optional ProxyPatch absent");
                check(Bukkit.getWorld("world_outpost_pluto") == null && Bukkit.getWorld("world_outpost_mars") == null,
                        "outpost maps absent in fixture");
                new me.astropvp.outpost.task.NoFlyTask().run();
                check(true, "outpost flight task tolerates absent maps");
                check(Bukkit.unloadWorld("tournament_world", false), "empty tournament fixture unloaded");
                new me.kingsavaqe.astrotournaments.tasks.NoFlightTask().run();
                check(true, "tournament flight task tolerates absent map");
                Bukkit.createWorld(new WorldCreator("tournament_world").type(WorldType.FLAT).generateStructures(false));
                new me.kingsavaqe.astrotournaments.tasks.NoFlightTask().run();
                check(true, "tournament flight task runs after map reload");
                List<Packet> messages = new ArrayList<>();
                MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
                WorldServer world = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
                fixture = new EntityPlayer(server, world, new GameProfile(UUID.fromString("bbbbbbbb-0000-0000-0000-000000000099"), "EmptyTargetProbe"), new PlayerInteractManager(world));
                // Deliberately not added to world/player lists: there must be zero eligible targets.
                fixture.playerConnection = new PlayerConnection(server, new NetworkManager(EnumProtocolDirection.CLIENTBOUND), fixture) {
                    @Override public void sendPacket(Packet packet) {if (packet instanceof PacketPlayOutChat) messages.add(packet);}
                };
                Player staff = fixture.getBukkitEntity();
                // Core startup is independently blocked; exercise only this method,
                // using its own class loader rather than pretending Core enabled.
                org.bukkit.plugin.Plugin core = Bukkit.getPluginManager().getPlugin("AstroCore");
                Class<?> type = Class.forName("net.crankedpvp.staff.listeners.RandomTeleportListener", true, core.getClass().getClassLoader());
                Object listener = type.getConstructors()[0].newInstance(new Object[]{null});
                Method teleport = listener.getClass().getDeclaredMethod("teleportToRandomPlayer", Player.class);
                teleport.setAccessible(true);
                teleport.invoke(listener, staff);
                check(messages.size() == 1, "empty random teleport reports no target");
                getLogger().info("PROBE RESULT missing-maps passed");
            } catch (Throwable failure) {
                getLogger().severe("PROBE FAIL " + failure);
                failure.printStackTrace();
            } finally {Bukkit.shutdown();}
        }, 20L);
    }
}
