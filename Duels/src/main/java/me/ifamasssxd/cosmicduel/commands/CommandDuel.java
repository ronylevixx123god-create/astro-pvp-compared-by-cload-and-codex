/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.VanishUtils
 *  com.gmail.nossr50.runnables.skills.BleedTimerTask
 *  com.google.common.collect.Lists
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Sound
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package me.ifamasssxd.cosmicduel.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.VanishUtils;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.ifamasssxd.cosmicduel.CosmicDuel;
import me.ifamasssxd.cosmicduel.DuelAPI;
import me.ifamasssxd.cosmicduel.struct.DuelInvite;
import me.ifamasssxd.cosmicduel.struct.DuelPlayer;
import me.ifamasssxd.cosmicduel.struct.TeamData;
import me.ifamasssxd.cosmicduel.struct.enums.CancelReason;
import me.ifamasssxd.cosmicduel.struct.enums.DuelCreateState;
import me.ifamasssxd.cosmicduel.struct.enums.DuelState;
import me.ifamasssxd.cosmicduel.struct.enums.EndReason;
import me.ifamasssxd.cosmicduel.struct.enums.Reason;
import me.ifamasssxd.cosmicduel.struct.newduel.ClassicDuel;
import me.ifamasssxd.cosmicduel.struct.newduel.NewDuel;
import me.ifamasssxd.cosmicduel.struct.newduel.TeamDuel;
import me.ifamasssxd.cosmicduel.utils.PlayerUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class CommandDuel implements CommandExecutor {
    String red = ChatColor.RED + ChatColor.BOLD.toString() + "(!) ";

    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (args.length == 1 && sender.isOp() && args[0].equalsIgnoreCase("togglequeue")) {
            CosmicDuel.queueEnabled = !CosmicDuel.queueEnabled;
            CosmicDuel.get().getConfig().set("queueEnabled", Boolean.valueOf(CosmicDuel.queueEnabled));
            CosmicDuel.get().saveConfig();
            sender.sendMessage(ChatColor.RED + "Queue Enabled: " + CosmicDuel.queueEnabled);
            return true;
        }
        if (!(sender instanceof Player))
            return true;
        Player p = (Player) sender;
        if (CosmicDuel.get().isShuttingDown()) {
            p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You cannot use duel  while the server is rebooting.");
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                sendHelp(p);
                return true;
            }
            if (args[0].equalsIgnoreCase("leave")) {
                if (!p.hasMetadata("in_ranked_queue_menu") || p.getMetadata("in_ranked_queue_menu").size() <= 0) {
                    if (DuelAPI.hasDuel(p)) {
                        NewDuel duel2 = DuelAPI.getDuel(p);
                        if (DuelAPI.isDuelQueued(duel2)) {
                            duel2.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "Duel was in queue and it has been cancelled by " + p.getName() + ".", true);
                            DuelAPI.cancelDuel(duel2, CancelReason.LEFT_DURING_QUEUE);
                            return true;
                        }
                        for (DuelInvite invite : DuelAPI.getAllDuelInvites(p, DuelAPI.InviteType.INVITER)) {
                            DuelAPI.invite_timer.remove(invite);
                            System.out.println("Declining invite for " + p.getName());
                        }
                        p.sendMessage(this.red + ChatColor.RED + "You left the duel.");
                        final Player oppositePlayer = duel2.getOppositePlayer(p);
                        final String pName = p.getName();
                        (new BukkitRunnable() {
                            public void run() {
                                if (Bukkit.getPlayer(oppositePlayer.getName()).hasPotionEffect(PotionEffectType.SPEED)) {
                                    Bukkit.getPlayer(oppositePlayer.getName()).removePotionEffect(PotionEffectType.SPEED);
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "apply " + oppositePlayer.getName());
                                }
                                if (Bukkit.getPlayer(pName).hasPotionEffect(PotionEffectType.SPEED)) {
                                    Bukkit.getPlayer(pName).removePotionEffect(PotionEffectType.SPEED);
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "apply " + pName);
                                }
                            }
                        }).runTaskLater(CosmicDuel.get(), 10L);
                        duel2.removePlayer(p, Reason.LEFT_DUEL, false);
                        if (duel2.getPlayers().size() <= 0 && duel2.getDuelState() == DuelState.IN_QUEUE && duel2.getCreateState() == DuelCreateState.PLAYER_SELECT)
                            DuelAPI.duels.remove(duel2);
                    } else if (p.hasMetadata("spectator")) {
                        NewDuel spectating = DuelAPI.getDuelSpectating(p);
                        if (spectating != null) {
                            spectating.removeSpectator(p);
                        } else {
                            p.sendMessage(this.red + ChatColor.RED + "You are not spectating a duel.. resetting player data.");
                            p.removeMetadata("spectator", CosmicDuel.get());
                            PlayerUtils.setCollides(p, true);
                            if (!VanishUtils.isVanished(p))
                                for (Player pl3 : Bukkit.getOnlinePlayers())
                                    pl3.showPlayer(p);
                        }
                    } else {
                        p.sendMessage(this.red + ChatColor.RED + "You are not in a duel.");
                    }
                    return true;
                }

                p.removeMetadata("in_ranked_queue_menu", CosmicDuel.get());
                p.sendMessage(ChatColor.RED + "Your duel queue menu data has been cleared!");
                return true;
            }
            if (args[0].equalsIgnoreCase("toggle")) {
                p.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "(!) " + ChatColor.YELLOW + "Duels have been toggled " + (CosmicDuel.get().getDuelToggles().togglePlayer(p) ? (ChatColor.RED + ChatColor.BOLD.toString() + "OFF") : (ChatColor.GREEN + ChatColor.BOLD.toString() + "ON")));
                return true;
            }
            if (args[0].equalsIgnoreCase("spectate")) {
                p.performCommand("spectate");
                return true;
            }
            Player pl5 = Bukkit.getPlayer(args[0]);
            if (pl5 == null || !pl5.isOnline()) {
                p.sendMessage(ChatColor.RED + "The player '" + args[0] + "' is not online!");
                return false;
            }
            if (pl5.equals(p)) {
                p.sendMessage(ChatColor.RED + "You cannot duel yourself.");
                return true;
            }
            if (!DuelAPI.checkStatus(p, pl5))
                return true;
            TeamData team = CosmicDuel.get().getTeamDataManager().tryFindTeam(p.getUniqueId());
            TeamData oTeam = CosmicDuel.get().getTeamDataManager().tryFindTeam(pl5.getUniqueId());
            if (team != null && Bukkit.getPlayer(team.getLeaderUUID()) != p) {
                p.sendMessage(CC.translate("&c&l(!) &cOnly team leaders can run this command!"));
                return true;
            }
            if (team != null && oTeam != null && team == oTeam) {
                p.sendMessage(CC.translate("&c&l(!) &cYou cannot duel members of your own team!"));
                return true;
            }
            if (team != null && oTeam == null) {
                p.sendMessage(CC.translate("&c&l(!) &cYou cannot duel &f" + pl5.getName() + " &cbecause they are not apart of a team."));
                p.sendMessage(CC.translate("&7Please leave your team to duel them or tell them to create a team!"));
                return true;
            }
            if (team != null && Bukkit.getPlayer(oTeam.getLeaderUUID()) != pl5) {
                p.sendMessage(CC.translate("&c&l(!) &cThat player is not the leader of that team, please duel the leader, please duel &f" + Bukkit.getPlayer(oTeam.getLeaderUUID()).getName() + "&c."));
                return true;
            }
            if (team != null && team.getTotalSize() != oTeam.getTotalSize()) {
                p.sendMessage(CC.translate("&c&l(!) &cMake sure you have the same amount of players on both teams."));
                return true;
            }
            if (team != null && (team.getTotalSize() < 2 || oTeam.getTotalSize() < 2)) {
                p.sendMessage(CC.translate("&c&l(!) &cBoth teams must have atleast 1 teammate to duel."));
                return true;
            }

            if (team != null) {
                for (Player player : getBothTeamPlayers(team, oTeam)) {
                    if (player == null || !player.isOnline()) {
                        sender.sendMessage(CC.translate("&cOne of the members of the opponent team isn't online."));
                        return false;
                    }
                }
                int team1OnlineCount = 0;
                for (UUID uuid : team.getTeamates()) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null && player.isOnline())
                        team1OnlineCount++;
                }
                int team2OnlineCount = 0;
                for (UUID uuid : oTeam.getTeamates()) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null && player.isOnline())
                        team2OnlineCount++;
                }
                if (team1OnlineCount != team2OnlineCount) {
                    p.sendMessage(CC.translate("&c&l(!) &cMake sure the same amount of players on both team are online."));
                    return true;
                }
            }
            if (team != null && oTeam != null && team != oTeam) {
                try {
                    TeamDuel teamDuel = new TeamDuel(p, Lists.newArrayList(args[0]));
                    DuelAPI.duels.add(teamDuel);
                    teamDuel.setCreateState(DuelCreateState.SETTINGS);
                    teamDuel.getSettings().getSettings().openInventory(p);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    ClassicDuel classicDuel = new ClassicDuel(p, Lists.newArrayList(args[0]));
                    DuelAPI.duels.add(classicDuel);
                    classicDuel.setCreateState(DuelCreateState.SETTINGS);
                    classicDuel.getSettings().getSettings().openInventory(p);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            CosmicDuel.logMessage("Creating duel for " + p.getName() + " and inviting " + pl5.getName());
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("decline") || args[0].equalsIgnoreCase("settings") || args[0].equalsIgnoreCase("top"))) {
            if (args[0].equalsIgnoreCase("accept")) {
                if (!DuelAPI.hasPendingInvite(p)) {
                    p.sendMessage(this.red + ChatColor.RED + "You do not have a pending invite.");
                    return true;
                }
                if (DuelAPI.isRebooting()) {
                    p.sendMessage(this.red + ChatColor.RED + "You cannot duel while the server is rebooting!");
                    return true;
                }
                String p_name = args[1];
                DuelAPI.acceptInvite(p, p_name, true);
                return true;
            }
            if (args[0].equalsIgnoreCase("decline")) {
                if (!DuelAPI.hasPendingInvite(p)) {
                    p.sendMessage(this.red + ChatColor.RED + "You do not have a pending invite.");
                    return true;
                }
                String p_name = args[1];
                DuelAPI.declineDuel(p, p_name, true);
            } else if (args[0].equalsIgnoreCase("settings")) {
                if (!DuelAPI.hasPendingInvite(p)) {
                    p.sendMessage(this.red + ChatColor.RED + "You do not have a pending invite.");
                    return true;
                }
                String p_name = args[1];
                DuelAPI.sendSettings(p, p_name);
            }
        } else if (args.length == 0) {
            p.sendMessage(CC.translate("&c&l(!) &cUsage: /duel <player>"));
        } else {
            sendHelp(p);
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("end") && sender.isOp()) {
                NewDuel d = DuelAPI.getDuel(p);
                if (d == null) {
                    p.sendMessage(CC.translate("&c&l(!) &cYou cannot do this because you are not in a duel!"));
                    return true;
                }
                d.onDuelEnd(EndReason.NO_WINNER, null);
                p.sendMessage(CC.translate("&c&l(!) &cYou have cancelled your duel!"));
                return true;
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("end")) {
            Player target = Bukkit.getPlayer(args[1]);
            NewDuel d = DuelAPI.getDuel(target);
            if (d == null) {
                p.sendMessage(CC.translate("&c&l(!) &cYou cannot do this because they are not in a duel!"));
                return true;
            }
            d.onDuelEnd(EndReason.NO_WINNER, null);
            p.sendMessage(CC.translate("&c&l(!) &cYou have cancelled their duel!"));
            return true;
        }
        return false;
    }

    private List<Player> getBothTeamPlayers(TeamData a, TeamData b) {
        List<Player> players = Lists.newArrayList();
        players.add(Bukkit.getPlayer(a.getLeaderUUID()));
        a.getTeamates().forEach(teammate -> players.add(Bukkit.getPlayer(teammate)));
        b.getTeamates().forEach(teammate -> players.add(Bukkit.getPlayer(teammate)));
        return players;
    }

    private void sendHelp(Player p) {
        p.sendMessage("");
        p.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "Duel Commands");
        p.sendMessage(ChatColor.YELLOW + "/duel <name>");
        p.sendMessage(ChatColor.GRAY + "Invite a player to a settings specific duel.");
        p.sendMessage(ChatColor.YELLOW + "/duel toggle");
        p.sendMessage(ChatColor.GRAY + "Toggle duel invites from other players.");
        p.sendMessage(ChatColor.YELLOW + "/duel spectate");
        p.sendMessage(ChatColor.GRAY + "Access the duel spectate menu.");
        p.sendMessage("");
    }
}
