package org.arkhamnetwork.Arkkit.patches.trading;

import com.cosmicpvp.cosmicutils.utils.CC;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.commands.CommandArkReboot;
import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class BetterTradeCommand extends AbstractCommand {
    private BetterTrade betterTradeInstance;

    public BetterTradeCommand(BetterTrade instance, String command, String usage, String description, List<String> aliases) {
        super(command, usage, description, aliases);
        this.betterTradeInstance = instance;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        } else {
            Player pl = (Player)sender;
            if (args.length != 1) {
                pl.sendMessage((String)(ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "Trade Commands"));
                pl.sendMessage((String)(ChatColor.GOLD.toString() + "/trade <player>"));
                pl.sendMessage((String)(ChatColor.GRAY + "Sends a trade request to the specified <player>."));
                return true;
            } else if (Arkkit.get().shutting_down || CommandArkReboot.restart_task_exists || Bukkit.getPluginManager().getPlugin("NetworkManager") != null && Arkkit.get().isRebooting()) {
                pl.sendMessage((String)(CC.RedB + "(!) " + CC.Red + "You cannot use this command while the server is rebooting!"));
                return true;
            } else {
                PlayerTrade trade = this.betterTradeInstance.getCurrentTrade(pl);
                if (trade != null) {
                    pl.sendMessage((String)(CC.RedB + "(!) " + CC.Red + "You already had an active trade!"));
                    trade.cancelTrade(pl);
                    Bukkit.getLogger().info("[CosmicTrade] " + pl.getName() + " had an active trade but ran /trade again? " + trade.toString());
                    return true;
                } else {
                    String tradie = args[0];
                    if (tradie.equalsIgnoreCase(pl.getName())) {
                        pl.sendMessage((String)(ChatColor.RED + "'If a man loves the labour of his trade, apart from any question of success or fame, the gods have called him.'"));
                        return true;
                    } else {
                        Player p_tradie = Bukkit.getPlayer(tradie);
                        if (p_tradie == null) {
                            pl.sendMessage((String)(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!)" + ChatColor.RED + " " + ChatColor.RED + "You aren't close enough to " + tradie + " to send a /trade."));
                            return true;
                        } else if (p_tradie.getName().equalsIgnoreCase(pl.getName())) {
                            pl.sendMessage((String)(ChatColor.RED + "'If a man loves the labour of his trade, apart from any question of success or fame, the gods have called him.'"));
                            return true;
                        } else if (!p_tradie.getWorld().getName().equalsIgnoreCase(pl.getWorld().getName())) {
                            pl.sendMessage((String)(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!)" + ChatColor.RED + " " + ChatColor.RED + "You aren't close enough to " + tradie + " to send a /trade."));
                            return true;
                        } else if (p_tradie.getLocation().distanceSquared(pl.getLocation()) > Math.pow(10.0D, 2.0D)) {
                            pl.sendMessage((String)(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!)" + ChatColor.RED + " " + ChatColor.RED + "You aren't close enough to " + tradie + " to send a /trade."));
                            pl.sendMessage((String)(ChatColor.GRAY + "Move within 10 blocks of them and try again."));
                            return true;
                        } else {
                            if (pl.hasMetadata("trade_request") || p_tradie.hasMetadata("trade_request")) {
                                if (pl.hasMetadata("trade_request")) {
                                    String stored_tradie = ((MetadataValue)pl.getMetadata("trade_request").get(0)).asString();
                                    if (stored_tradie.equalsIgnoreCase(tradie)) {
                                        if (p_tradie.hasMetadata("trade_request") && ((MetadataValue)p_tradie.getMetadata("trade_request").get(0)).asString().equalsIgnoreCase(pl.getName())) {
                                            pl.removeMetadata("trade_request", Arkkit.get());
                                            p_tradie.removeMetadata("trade_request", Arkkit.get());
                                            this.betterTradeInstance.startPlayerTrade(pl, p_tradie);
                                            return true;
                                        }

                                        pl.sendMessage((String)(ChatColor.RED + p_tradie.getName() + "'s trade request has timed out."));
                                        pl.removeMetadata("trade_request", Arkkit.get());
                                        return true;
                                    }
                                }

                                if (p_tradie.hasMetadata("trade_request")) {
                                    if (((MetadataValue)p_tradie.getMetadata("trade_request").get(0)).asString().equalsIgnoreCase(pl.getName())) {
                                        pl.removeMetadata("trade_request", Arkkit.get());
                                        p_tradie.removeMetadata("trade_request", Arkkit.get());
                                        this.betterTradeInstance.startPlayerTrade(pl, p_tradie);
                                    }

                                    return true;
                                }
                            }

                            pl.setMetadata("trade_request", new FixedMetadataValue(Arkkit.get(), p_tradie.getName()));
                            pl.sendMessage((String)(ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "(!)" + ChatColor.GOLD + " You have sent a trade request to " + p_tradie.getName() + "!"));
                            p_tradie.sendMessage((String)(ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "TRADE " + ChatColor.YELLOW + pl.getName() + ChatColor.GOLD + " wants to trade with you. " + ChatColor.YELLOW + "/trade " + pl.getName()));
                            return true;
                        }
                    }
                }
            }
        }
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> results = new ArrayList();
        if (args.length != 1) {
            return null;
        } else {
            String s = args[0];
            Iterator var7 = Bukkit.getOnlinePlayers().iterator();

            while(true) {
                Player pl;
                do {
                    if (!var7.hasNext()) {
                        return results.size() > 0 ? results : null;
                    }

                    pl = (Player)var7.next();
                } while(sender instanceof Player && !((Player)sender).canSee(pl));

                if (pl.getName().toLowerCase().startsWith(s.toLowerCase())) {
                    results.add(pl.getName());
                }
            }
        }
    }
}
