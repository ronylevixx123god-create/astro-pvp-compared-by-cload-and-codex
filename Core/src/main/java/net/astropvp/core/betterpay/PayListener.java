package net.astropvp.core.betterpay;

import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import net.astropvp.core.betterpay.commands.CommandPay;
import net.astropvp.core.AstroCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class PayListener implements Listener {
    @EventHandler(priority= EventPriority.LOW, ignoreCancelled=true)
    public void onPlayerCommandpre(PlayerCommandPreprocessEvent e) {
        if (!Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
            Bukkit.getPluginManager().disablePlugin((Plugin)this);
            Bukkit.getLogger().severe("Disabling BetterPay due to missing Essentials...");
            return;
        }
        if (!(e.getMessage().contains(" ") || e.getMessage().equalsIgnoreCase("/pay") || e.getMessage().equalsIgnoreCase("/epay"))) {
            return;
        }
        String[] args = e.getMessage().split(" ");
        if (args[0].equalsIgnoreCase("/pay") || args[0].equalsIgnoreCase("/epay")) {
            ArrayList<String> argList = new ArrayList<String>();
            int index = 0;
            for (String arg : args) {
                if (index++ == 0) continue;
                argList.add(arg);
            }
            e.setCancelled(true);
            e.setMessage("/null");
            CommandPay cmd = new CommandPay();
            try {
                cmd.run(Bukkit.getServer(), e.getPlayer(), AstroCore.getInstance().getEss().getUser(e.getPlayer()), "/pay", argList.toArray(new String[argList.size()]));
            }
            catch (NotEnoughArgumentsException e1) {
                e.getPlayer().sendMessage((Object) ChatColor.RED + "Invalid arguments: /pay <player> <amount>");
            }
            catch (Exception ex) {
                AstroCore.getInstance().showError(e.getPlayer(), ex, "pay");
            }
        }
    }
}
