package devtest;

import java.lang.reflect.*;
import java.util.Collections;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/** Disposable patch registration/alias fixture. No grants, billing or economy calls. */
public class ArkkitAliasProbe extends JavaPlugin {
    private void check(boolean ok, String message) {
        if (!ok) throw new AssertionError(message);
        getLogger().info("PROBE PASS " + message);
    }
    @Override public void onEnable() {
        Bukkit.getScheduler().runTaskLater(this, () -> {
            Object patch = null;
            boolean passed = false;
            try {
                Plugin ark = Bukkit.getPluginManager().getPlugin("Arkkit");
                check(ark != null && ark.isEnabled(), "actual Arkkit enabled");
                ClassLoader loader = ark.getClass().getClassLoader();
                Class<?> commands = Class.forName("org.arkhamnetwork.Arkkit.libs.AbstractCommand", true, loader);
                Field cached = commands.getField("cmap");
                CommandMap previous = (CommandMap) cached.get(null);
                check(previous == null || previous.getCommand("showcaseexpander") == null,
                        "showcase patch not already registered in fixture");
                cached.set(null, null); // Exercise real CraftServer reflection, not the cached path.
                Class<?> patchType = Class.forName("org.arkhamnetwork.Arkkit.patches.ShowcaseExpander", true, loader);
                patch = patchType.getConstructor(Plugin.class).newInstance(ark);
                patchType.getMethod("enable").invoke(patch);
                CommandMap map = (CommandMap) cached.get(null);
                check(map != null, "uncached command map reflection succeeds");
                Command command = map.getCommand("showcaseexpander");
                check(command != null && command == map.getCommand("sce") && command == map.getCommand("arkkit:showcaseexpander"),
                        "showcase normal alias and namespace resolve together");
                check(Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "showcaseexpander")
                                && Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sce"),
                        "showcase normal and alias dispatch usage without grants");
                Plugin core = Bukkit.getPluginManager().getPlugin("AstroCore");
                check(core != null, "Core class loader available for alias fixture");
                getLogger().info("PROBE SCOPE Core enabled=" + core.isEnabled() + "; alias methods invoked directly, not live player dispatch");
                Object aliases = Class.forName("net.astropvp.core.listeners.CommandAliases", true, core.getClass().getClassLoader()).newInstance();
                Method lootbox = aliases.getClass().getMethod("onPlayerPreProcessCommand", PlayerCommandPreprocessEvent.class);
                Method roots = aliases.getClass().getMethod("onPlayerCommandPreprocess", PlayerCommandPreprocessEvent.class);
                // Message-only fixture; the three-argument legacy event constructor
                // accepts a null player. These handlers must not access a player.
                Player player = null;
                PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(player, "/LoOtBoX give Alice CaseSensitiveID", Collections.emptySet());
                lootbox.invoke(aliases, event);
                check(event.getMessage().equals("/mc give Alice CaseSensitiveID"), "lootbox arguments preserved");
                event = new PlayerCommandPreprocessEvent(player, "/lootboxfilter", Collections.emptySet());
                lootbox.invoke(aliases, event);
                check(event.getMessage().equals("/lootboxfilter"), "longer lootbox command left intact");
                event = new PlayerCommandPreprocessEvent(player, "/tpyes Alice", Collections.emptySet());
                roots.invoke(aliases, event);
                check(event.getMessage().equals("/tpaccept Alice"), "teleport alias target preserved");
                event = new PlayerCommandPreprocessEvent(player, "/lootbox give Alice", Collections.emptySet());
                event.setCancelled(true); lootbox.invoke(aliases, event);
                check(event.isCancelled() && event.getMessage().equals("/lootbox give Alice"), "cancelled alias left intact");
                passed = true;
            } catch (Throwable failure) {
                getLogger().severe("PROBE FAIL " + failure); failure.printStackTrace();
            } finally {
                try {
                    if (patch != null) {
                        patch.getClass().getMethod("unregisterCommands").invoke(patch);
                        patch.getClass().getMethod("unregisterListeners").invoke(patch);
                        check(true, "fixture patch registrations cleaned up");
                    }
                } catch (Throwable failure) { passed = false; getLogger().severe("PROBE FAIL cleanup " + failure); }
                if (passed) getLogger().info("PROBE RESULT arkkit-alias passed");
                Bukkit.shutdown();
            }
        }, 20L);
    }
}
