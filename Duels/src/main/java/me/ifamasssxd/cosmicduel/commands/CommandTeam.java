package me.ifamasssxd.cosmicduel.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.UUID;
import me.ifamasssxd.cosmicduel.CosmicDuel;
import me.ifamasssxd.cosmicduel.inventory.teamgui.MainTeamGUI;
import me.ifamasssxd.cosmicduel.struct.TeamData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTeam implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player))
            return false;
        Player player = (Player)sender;
        if (args.length == 0) {
            MainTeamGUI mainTeamGUI = new MainTeamGUI(player);
            mainTeamGUI.getExpShopUI().open(player);

            player.sendMessage("");
            player.sendMessage(CC.translate("&a&lAstro Team Duels"));
            player.sendMessage(CC.translate("&f&l* &a/team create &f- &7create a team"));
            player.sendMessage(CC.translate("&f&l* &a/team invite <player> &f- &7invite a player to your team"));
            player.sendMessage(CC.translate("&f&l* &a/team deinvite <player> &f- &7de-invite a player from your team"));
            player.sendMessage(CC.translate("&f&l* &a/team join <player> &f- &7join a players team"));
            player.sendMessage(CC.translate("&f&l* &a/team kick <player> &f- &7kick a player from your team"));
            player.sendMessage(CC.translate("&f&l* &a/team leave &f- &7leave a team"));
            player.sendMessage(CC.translate("&f&l* &a/team disband &f- &7disband a team"));
            player.sendMessage(CC.translate("&f&l* &a/team list &f- &7see who is apart of your team"));
            player.sendMessage(CC.translate("&f&l* &a/team who <player> &f- &7check player's team"));
            player.sendMessage("");
            return true;
        }
        if (args.length >= 2 && args[0].equalsIgnoreCase("invite")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null || !target.isOnline()) {
                player.sendMessage(CC.translate("&c&l(!) &cThat player is not online!"));
                return false;
            }
            if (target == player) {
                player.sendMessage(CC.translate("&c&l(!) &cYou can not invite yourself to your team!"));
                return false;
            }
            TeamData data = CosmicDuel.get().getTeamDataManager().tryFindTeam(player.getUniqueId());
            if (data != null && !data.getLeaderUUID().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
                player.sendMessage(CC.translate("&c&l(!) &cYou are not the leader of your team! You can not invite players!"));
                return false;
            }
            TeamData targetTeamData = CosmicDuel.get().getTeamDataManager().tryFindTeam(target.getUniqueId());
            if (targetTeamData != null) {
                if (targetTeamData == data) {
                    player.sendMessage(CC.translate("&c&l(!) &c" + target.getName() + " is already on your team!"));
                    return false;
                }
                player.sendMessage(CC.translate("&c&l(!) &cYou can not invite &f" + target.getName() + " &cto your team, as they are already part of a team!"));
                return false;
            }
            if (CosmicDuel.get().getTeamDataManager().alreadyHasTeamInvite(target)) {
                TeamData getTeamThatSentInvite = CosmicDuel.get().getTeamDataManager().getTeamInvite(target);
                player.sendMessage(CC.translate("&c&l(!) &f" + target.getName() + "&c already has a team invite from a team!"));
                return false;
            }
            if (data == null) {
                data = new TeamData(player.getUniqueId());
                CosmicDuel.get().getTeamDataManager().addTeam(data);
            }

            if (data.getTeamates().size() == 1) {
                player.sendMessage(CC.translate("&c&l(!)&c The max team size is 2!"));
                return false;
            }
            player.sendMessage(CC.translate("&a&l(!) &aYou have sent a team invite to &f" + target.getName() + "&a!"));
            CosmicDuel.get().getTeamDataManager().sendTeamInvite(data, target);
            return false;
        }
        if (args.length >= 2 && args[0].equalsIgnoreCase("deinvite")) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            if (target == null) {
                player.sendMessage(CC.translate("&c&l(!) &cThat player is not online!"));
                return false;
            }
            if (target == player) {
                player.sendMessage(CC.translate("&c&l(!) &cYou can not de-invite yourself!"));
                return false;
            }
            TeamData data = CosmicDuel.get().getTeamDataManager().tryFindTeam(player.getUniqueId());
            if (data == null) {
                player.sendMessage(CC.translate("&c&l(!) &CYou do not have a team! You can not de-invite players!"));
                return false;
            }
            if (data != null && !data.getLeaderUUID().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
                player.sendMessage(CC.translate("&c&l(!) &cYou are not the leader of your team! You can not de-invite players!"));
                return false;
            }
            if (CosmicDuel.get().getTeamDataManager().alreadyHasTeamInvite((Player)target)) {
                TeamData getTeamThatSentInvite = CosmicDuel.get().getTeamDataManager().getTeamInvite((Player)target);
                if (getTeamThatSentInvite != null && getTeamThatSentInvite != data) {
                    player.sendMessage(CC.translate("&c&l(!) &c" + target.getName() + " does not have an active invite associated with your team!"));
                    return false;
                }
            } else {
                player.sendMessage(CC.translate("&c&l(!) &cYou cannot deinvite a player that is not invited to your team!"));
                return false;
            }
            player.sendMessage(CC.translate("&c&l(!) &cYou have de-invited &f" + target.getName() + "&c!"));
            CosmicDuel.get().getTeamDataManager().removeInvite(data, (Player)target);
            return false;
        }
        if (args.length >= 2 && args[0].equalsIgnoreCase("join")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null || !target.isOnline()) {
                player.sendMessage(CC.translate("&c&l(!) &cThat player is not online!"));
                return false;
            }
            if (target == player) {
                player.sendMessage(CC.translate("&c&l(!) &cYou can not join your own team!"));
                return false;
            }
            TeamData targetTeam = CosmicDuel.get().getTeamDataManager().tryFindTeam(target.getUniqueId());
            if (targetTeam == null) {
                player.sendMessage(CC.translate("&cThat player is not apart of a team!"));
                return false;
            }
            if (!CosmicDuel.get().getTeamDataManager().hasInviteToTeam(player, targetTeam)) {
                player.sendMessage(CC.translate("&c&l(!) &f" + target.getName() + "'s &cteam has not invited you!"));
                player.sendMessage(CC.translate("&7Ask for an invite from the team leader &n" + Bukkit.getPlayer(targetTeam.getLeaderUUID()).getName() + "&7."));
                return false;
            }
            if (CosmicDuel.get().getTeamDataManager().hitTeamInivteLimit(targetTeam)) {
                player.sendMessage(CC.translate("&c&l(!) &cThat team already has a maximum of 5 team members."));
                return false;
            }
            TeamData playerTeam = CosmicDuel.get().getTeamDataManager().tryFindTeam(player.getUniqueId());
            if (playerTeam == null) {
                if (targetTeam.getTeamates() != null)
                    targetTeam.getTeamates().stream().forEach(uuid -> {
                        Player teammate = Bukkit.getPlayer(uuid);
                        if (teammate.isOnline()) {
                            teammate.sendMessage(CC.translate("&a&l(!) &f" + player.getName() + "&a has joined your team!"));
                            teammate.playSound(teammate.getLocation(), Sound.ORB_PICKUP, 10.0F, 10.0F);
                        }
                    });
                if (CosmicDuel.get().getTeamDataManager().hasInviteToTeam(player, targetTeam))
                    targetTeam.removeFromInvites(player.getUniqueId());
                targetTeam.addToTeam(player.getUniqueId());
                Bukkit.getPlayer(targetTeam.getLeaderUUID()).sendMessage(CC.translate("&a&l(!) &f" + player.getName() + " &ahas joined your team!"));
                player.sendMessage(CC.translate("&a&l(!) &aYou have joined &f" + Bukkit.getPlayer(targetTeam.getLeaderUUID()).getName() + "'s &ateam!"));
            }
            return false;
        }
        if (args.length >= 2 && args[0].equalsIgnoreCase("kick")) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            if (target == null) {
                player.sendMessage(CC.translate("&c&l(!) &cThat player is not online!"));
                return false;
            }
            TeamData data = CosmicDuel.get().getTeamDataManager().tryFindTeam(player.getUniqueId());
            if (data == null) {
                player.sendMessage(CC.translate("&c&l(!) &cYou do not have a team!"));
                return false;
            }
            if (target == player) {
                player.sendMessage(CC.translate("&c&l(!) &cYou can not kick yourself from your own team!"));
                return false;
            }
            if (!data.getLeaderUUID().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
                player.sendMessage(CC.translate("&c&l(!) &cYou are not the leader of this team!"));
                return false;
            }
            if (!data.getTeamates().contains(target.getUniqueId())) {
                player.sendMessage(CC.translate("&c&l(!) &cThat player is not on your team!"));
                return false;
            }
            data.removeFromTeam(target.getUniqueId());
            if (target.isOnline())
                ((Player)target).sendMessage(CC.translate("&c&l(!) &cYou were kicked from " + player.getName() + "'s team!"));
            player.sendMessage(CC.translate("&c&l(!) &cYou kicked " + target.getName() + " from your team!"));
            return false;
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("leave")) {
            TeamData data = CosmicDuel.get().getTeamDataManager().tryFindTeam(player.getUniqueId());
            if (data == null) {
                player.sendMessage(CC.translate("&c&l(!) &cYou do not have a team!"));
                return false;
            }
            if (data.getLeaderUUID() == player.getUniqueId()) {
                if (data.getTeamates() != null)
                    data.getTeamates().stream().forEach(uuid -> {
                        Player teammate = Bukkit.getPlayer(uuid);
                        if (teammate != null) {
                            teammate.sendMessage(CC.translate("&c&l(!) &c" + player.getName() + " has disbanded your team!"));
                            teammate.playSound(teammate.getLocation(), Sound.ORB_PICKUP, 10.0F, 10.0F);
                        }
                    });
                player.sendMessage(CC.translate("&c&l(!) &cYou have disbanded your team!"));
                CosmicDuel.get().getTeamDataManager().removeTeam(data);
                return false;
            }
            data.removeFromTeam(player.getUniqueId());
            player.sendMessage(CC.translate("&c&l(!) &cYou have left the team!"));
            Bukkit.getPlayer(data.getLeaderUUID()).sendMessage(CC.translate("&c&l(!) &f" + player.getName() + "&c has left your team!"));
            data.getTeamates().forEach(uuid -> {
                Player teammate = Bukkit.getPlayer(uuid);
                if (teammate.isOnline()) {
                    teammate.sendMessage(CC.translate("&c&l(!) &c" + player.getName() + " has left your team!"));
                    teammate.playSound(teammate.getLocation(), Sound.ORB_PICKUP, 10.0F, 10.0F);
                }
            });
            return false;
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("disband")) {
            TeamData data = CosmicDuel.get().getTeamDataManager().tryFindTeam(player.getUniqueId());
            if (data == null) {
                player.sendMessage(CC.translate("&c&l(!) &cYou do not have a team!"));
                return false;
            }
            if (!data.getLeaderUUID().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
                player.sendMessage(CC.translate("&c&l(!) &cYou are not the leader of this team!"));
                return false;
            }
            if (data.getTeamates() != null)
                data.getTeamates().stream().forEach(uuid -> {
                    Player teammate = Bukkit.getPlayer(uuid);
                    if (teammate != null) {
                        teammate.sendMessage(CC.translate("&c&l(!) &c" + player.getName() + " has disbanded your team!"));
                        teammate.playSound(teammate.getLocation(), Sound.ORB_PICKUP, 10.0F, 10.0F);
                    }
                });
            player.sendMessage(CC.translate("&c&l(!) &cYou have disbanded your team!"));
            CosmicDuel.get().getTeamDataManager().removeTeam(data);
            return false;
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("list")) {
            TeamData playerTeam = CosmicDuel.get().getTeamDataManager().tryFindTeam(player.getUniqueId());
            if (playerTeam == null) {
                player.sendMessage(CC.translate("&cYou are not apart of a team!"));
                return false;
            }
            CosmicDuel.get().getTeamDataManager().showTeamMessage(player, playerTeam);
        }
        if (args.length >= 2 && args[0].equalsIgnoreCase("who")) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            if (target == null || !target.isOnline()) {
                player.sendMessage(CC.translate("&c&l(!) &cPlayer not found!"));
                return false;
            }
            TeamData targetTeam = CosmicDuel.get().getTeamDataManager().tryFindTeam(target.getUniqueId());
            if (targetTeam == null) {
                player.sendMessage(CC.translate("&c&l(!) &cThat player does not have a team!"));
                return false;
            }
            CosmicDuel.get().getTeamDataManager().showTeamMessage(player, targetTeam);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("who")) {
            TeamData data = CosmicDuel.get().getTeamDataManager().tryFindTeam(((Player)sender).getUniqueId());
            if (data == null) {
                player.sendMessage(CC.translate("&c&l(!) &cYou do not have a team!"));
                return false;
            }
            CosmicDuel.get().getTeamDataManager().showTeamMessage(player, data);
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("create")) {
            if (CosmicDuel.get().getTeamDataManager().tryFindTeam(player.getUniqueId()) == null) {
                TeamData data = new TeamData(player.getUniqueId());
                CosmicDuel.get().getTeamDataManager().addTeam(data);
                player.sendMessage(CC.translate("&a&l(!) &aYou have created a team!"));
                return false;
            }
            player.sendMessage(CC.translate("&c&l(!) &cYou already have a team created!"));
            return false;
        }
        if (args.length == 1 && player.isOp() && args[0].equalsIgnoreCase("disbandall")) {
            CosmicDuel.get().getTeamDataManager().getTeams().clear();
            player.sendMessage(CC.translate("&cRemoved all teams"));
            return false;
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("leader")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null || !target.isOnline()) {
                sender.sendMessage(CC.translate("&c&l(!) &cPlayer not found!"));
                return true;
            }
            TeamData senderData = CosmicDuel.get().getTeamDataManager().tryFindTeam(((Player)sender).getUniqueId());
            if (senderData == null) {
                sender.sendMessage(CC.translate("&c&l(!) &cYou cannot do this because you don't have a team!"));
                return true;
            }
            if (!senderData.isLeader(((Player)sender).getUniqueId())) {
                sender.sendMessage(CC.translate("&c&l(!) &cYou cannot do this command because you are not the leader of this team!"));
                return true;
            }
            if (!senderData.getTeamates().contains(target.getUniqueId())) {
                sender.sendMessage(CC.translate("&c&l(!) &cYou can only give your teamates the leadership role!"));
                return true;
            }
            List<UUID> teammates = Lists.newArrayList();
            senderData.getTeamates().stream().forEach(teammate -> teammates.add(teammate));
            teammates.remove(target.getUniqueId());
            teammates.add(((Player)sender).getUniqueId());
            CosmicDuel.get().getTeamDataManager().removeTeam(senderData);
            TeamData newTeam = new TeamData(target.getUniqueId());
            teammates.stream().forEach(teammate -> newTeam.addToTeam(teammate));
            CosmicDuel.get().getTeamDataManager().addTeam(newTeam);
            sender.sendMessage(CC.translate("&c&l(!) &cYou have transferred your /team leadership role to &f" + target.getName() + "&c!"));
            target.sendMessage(CC.translate("&a&l(!) &aYou are now the leader of &f" + sender.getName() + "'s&a /team."));
        }
        return false;
    }
}
