package net.astropvp.showcasegui.commands;

import net.astropvp.showcasegui.ShowcaseAPI;
import net.astropvp.showcasegui.ShowcaseGUI;
import net.astropvp.showcasegui.struct.Showcase;
import net.astropvp.showcasegui.utils.MojangUUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class CommandShowcase implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        } else {
            Player p = (Player) sender;
            if (args.length != 1 && args.length != 0) {
                p.sendMessage((String) (ChatColor.GRAY + "Usage: " + ChatColor.RED + "/showcase <player>"));
                return true;
            } else {
                String name = args.length == 0 ? p.getName() : args[0];
                Player pl = args.length == 1 ? Bukkit.getPlayer(name) : p;
                boolean isHidden = pl != null && !p.canSee(pl);
                if (pl != null && !isHidden) {
                    Showcase showcase;
                    if (ShowcaseAPI.hasShowcase(pl)) {
                        showcase = ShowcaseAPI.getShowcase(pl);
                        showcase.getFirstPage().openInventory(p, false);
                        p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 1.0F, 1.4F);
                        return false;
                    } else if (pl.equals(p)) {
                        showcase = ShowcaseAPI.createShowcase(p.getUniqueId(), p.getName(), (String) null, ShowcaseAPI.getMaxShowcaseSize(p));
                        showcase.getFirstPage().openInventory(p, false);
                        p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 1.0F, 1.4F);
                        return true;
                    } else {
                        p.sendMessage((String) (ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + pl.getName() + " does not have a Showcase!"));
                        return true;
                    }
                } else if (p.isOp()) {
                    this.loadOfflineShowcase(p, name);
                    return true;
                } else {
                    p.sendMessage((String) (ChatColor.RED + "That is not a valid online player!"));
                    return true;
                }
            }
        }
    }

    public void loadOfflineShowcase(final Player p, final String name) {
        (new BukkitRunnable() {
            public void run() {
                try {
                    p.sendMessage((String) (ChatColor.RED + "Loading " + name + " offline showcase..."));
                    UUID uuid = MojangUUIDFetcher.getUUIDOf(name);
                    if (uuid == null) {
                        return;
                    }

                    p.sendMessage((String) (ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "Remember to /saveshowcase " + name + " once you are finished to push your changes to the cloud."));
                    if (ShowcaseAPI.hasShowcase(uuid)) {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(ShowcaseGUI.get(), (Runnable) (() -> {
                            ShowcaseAPI.getShowcase(uuid).getFirstPage().openInventory(p, false);
                        }));
                    } else {
                        ShowcaseAPI.loadShowcase(uuid, name, false);
                        if (!ShowcaseAPI.hasShowcase(uuid)) {
                            p.sendMessage((String) (ChatColor.RED + "No showcase found for " + name));
                            return;
                        }

                        Bukkit.getScheduler().scheduleSyncDelayedTask(ShowcaseGUI.get(), (Runnable) (() -> {
                            ShowcaseAPI.getShowcase(uuid).getFirstPage().openInventory(p, false);
                        }));
                    }
                } catch (Exception var2) {
                    var2.printStackTrace();
                }

            }
        }).runTaskAsynchronously(ShowcaseGUI.get());
    }
}
