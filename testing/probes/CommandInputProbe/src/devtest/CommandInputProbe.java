package devtest;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/** Disposable fixture only. No giftcard API, economy or player mutation is invoked. */
public class CommandInputProbe extends JavaPlugin {
    private void check(boolean ok, String message) {
        if (!ok) throw new AssertionError(message);
        getLogger().info("PROBE PASS " + message);
    }
    @Override public void onEnable() {
        Bukkit.getScheduler().runTaskLater(this, () -> {
            try {
                Plugin giftcards = Bukkit.getPluginManager().getPlugin("AstroGiftCardPlugin");
                check(giftcards != null && giftcards.isEnabled(), "giftcard plugin enabled for command dispatch");
                PluginCommand command = Bukkit.getPluginCommand("creategiftcard");
                check(command != null && command.getPlugin() == giftcards && Bukkit.getPluginCommand("creategc") == command,
                        "giftcard alias shares registered command");
                check(!command.execute(Bukkit.getConsoleSender(), "creategc", new String[]{"NoVendorCallFixture", "1"}),
                        "giftcard console command rejected without exception");
                Plugin core = Bukkit.getPluginManager().getPlugin("AstroCore");
                check(core != null, "Core class loader available");
                getLogger().info("PROBE SCOPE Core enabled=" + core.isEnabled() + "; invoking console handler only, no gameplay manager");
                CommandExecutor coinflip = (CommandExecutor) Class.forName(
                        "net.astropvp.core.cosmiccoinflip.commands.CommandCoinFlip", true, core.getClass().getClassLoader()).newInstance();
                check(coinflip.onCommand(Bukkit.getConsoleSender(), null, "cf", new String[0]),
                        "coinflip console handler safe");
                getLogger().info("PROBE RESULT command-input passed");
            } catch (Throwable failure) {
                getLogger().severe("PROBE FAIL " + failure);
                failure.printStackTrace();
            } finally { Bukkit.shutdown(); }
        }, 20L);
    }
}
