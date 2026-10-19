package devtest;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import java.lang.reflect.Method;
import java.util.UUID;

/** Isolated test: AstroDiscordSync NOT installed. Calls AstroCore's badge helper for a
 *  server-side player (no client) and logs the result; then stops the server. */
public class BadgeProbe extends JavaPlugin {
    @Override public void onEnable() { Bukkit.getScheduler().runTaskLater(this, this::run, 20L); }
    private void log(String s) { getLogger().info("PROBE " + s); }
    private void run() {
        try {
            log("AstroDiscordSync installed=" + (Bukkit.getPluginManager().getPlugin("AstroDiscordSync") != null));
            Plugin core = Bukkit.getPluginManager().getPlugin("AstroCore");
            MinecraftServer ms = ((CraftServer) Bukkit.getServer()).getServer();
            WorldServer ws = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
            EntityPlayer ep = new EntityPlayer(ms, ws, new GameProfile(UUID.randomUUID(), "ChatTester"), new PlayerInteractManager(ws));
            Player p = ep.getBukkitEntity();
            Class<?> c = Class.forName("net.astropvp.core.listeners.DiscordSyncBadges", true, core.getClass().getClassLoader());
            Method m = c.getDeclaredMethod("getNitroLeader", Player.class);
            m.setAccessible(true);
            log("RESULT badges='" + m.invoke(null, p) + "' (no exception)");
        } catch (Throwable t) {
            log("FAIL " + t + (t.getCause() != null ? " caused by " + t.getCause() : ""));
        }
        Bukkit.shutdown();
    }
}
