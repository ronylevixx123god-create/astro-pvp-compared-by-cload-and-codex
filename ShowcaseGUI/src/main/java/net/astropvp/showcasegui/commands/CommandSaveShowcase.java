package net.astropvp.showcasegui.commands;
import java.util.UUID;

import net.astropvp.showcasegui.ShowcaseAPI;
import net.astropvp.showcasegui.ShowcaseGUI;
import net.astropvp.showcasegui.utils.MojangUUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
public class CommandSaveShowcase implements CommandExecutor {
    public boolean onCommand(final CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            return true;
        } else {
            if (args.length == 1) {
                final String name = args[0];
                if (Bukkit.getPlayer(name) != null) {
                    sender.sendMessage(ChatColor.RED + name + " is currently online and their Showcase will be saved once they are offline.");
                    return true;
                }

                (new BukkitRunnable() {
                    public void run() {
                        try {
                            UUID uuid = MojangUUIDFetcher.getUUIDOf(name);
                            if (uuid == null) {
                                return;
                            }

                            if (!ShowcaseAPI.hasShowcase(uuid)) {
                                sender.sendMessage(ChatColor.RED + "That player does not have a Showcase!");
                                return;
                            }

                            sender.sendMessage("Saving " + name + "'s offline showcase..");
                            ShowcaseAPI.saveShowcase(uuid, false, true);
                        } catch (Exception var2) {
                            var2.printStackTrace();
                        }

                    }
                }).runTaskAsynchronously(ShowcaseGUI.get());
            }

            return false;
        }
    }
}
