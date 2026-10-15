package me.kingsavaqe.astrotournaments.commands;

import me.kingsavaqe.astrotournaments.AstroTournamentsPlugin;
import me.kingsavaqe.astrotournaments.inventory.TournamentGUI;
import me.kingsavaqe.astrotournaments.inventory.settingsGUI.PlayerSettingsGUI;
import me.kingsavaqe.astrotournaments.managers.TournamentManager;
import me.kingsavaqe.astrotournaments.tasks.TournamentStartTask;
import me.kingsavaqe.astrotournaments.utils.Utils;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

@Command("tournament")
@Alias({"tm", "astrotournament", "tournaments"})
public class TournamentCommand extends CommandBase {

    private final AstroTournamentsPlugin astroTournamentsPlugin;
    private final TournamentManager tournamentManager;
    public TournamentCommand(AstroTournamentsPlugin astroTournamentsPlugin)
    {
        this.astroTournamentsPlugin = astroTournamentsPlugin;
        this.tournamentManager = astroTournamentsPlugin.getTournamentManager();
    }

    @Default
    public void defaultCommand(CommandSender commandSender) {
        Player player = (Player)commandSender;
        TournamentGUI tournamentInventory = new TournamentGUI(astroTournamentsPlugin, player);
        tournamentInventory.getUI().open(player);
        player.playSound(player.getLocation(), Sound.CHEST_OPEN, 2.0F, 2.0F);
    }

    @SubCommand("help")
    public void helpCommand(CommandSender commandSender) {
        if(commandSender.hasPermission("tournament.*")) {
            sendAdminHelp(commandSender);
            return;
        }
        sendUserHelp(commandSender);
    }

    @SubCommand("join")
    public void joinCommand(CommandSender commandSender) {
        Player player = (Player)commandSender;
        if(!astroTournamentsPlugin.getTournamentManager().isActive()) {
            player.sendMessage(Utils.fixColor("&cThere is no tournament currently active"));
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
            return;
        }
        if(!astroTournamentsPlugin.getTournamentManager().isJoinable()) {
            player.sendMessage(Utils.fixColor("&cYou can no longer join this tournament"));
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
            return;
        }

        if(tournamentManager.getTournamentParticipants().contains(tournamentManager.getTournamentPlayer(player.getUniqueId()))) {
            player.sendMessage(Utils.fixColor("&cYou are already in the tournament"));
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
            return;
        }
        if(!Utils.isEmptyInventory(player)) {
            player.sendMessage(Utils.fixColor("&cYou must have an empty inventory to join the tournament"));
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
            return;
        }


        tournamentManager.getTournamentParticipants().add(tournamentManager.getTournamentPlayer(player.getUniqueId()));
        tournamentManager.getTournamentPlayer(player.getUniqueId()).setInTournament(true);
        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 2.0F, 2.0F);
        player.teleport(astroTournamentsPlugin.getTournamentManager().getWaitingRoomLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
        player.sendMessage(Utils.fixColor("&aYou have successfully joined the tournament"));
    }

    @SubCommand("leave")
    public void leaveCommand(CommandSender commandSender) {
        Player player = (Player) commandSender;
        if(!astroTournamentsPlugin.getTournamentManager().isActive()) {
            player.sendMessage(Utils.fixColor("&cThere is no tournament currently active"));
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
            return;
        }
        if(!astroTournamentsPlugin.getTournamentManager().getTournamentPlayer(player.getUniqueId()).isInTournament()) {
            player.sendMessage(Utils.fixColor("&cYou are not in a tournament"));
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
            return;
        }
        Utils.emptyInventory(player);
        AstroTournamentsPlugin.instance.getTournamentManager().getTournamentPlayer(player.getUniqueId()).leaveTournament();
        player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 2.0F, 2.0F);
        player.teleport(Bukkit.getWorld("world").getSpawnLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
        player.sendMessage(Utils.fixColor("&aYou have successfully left the tournament"));
    }

    @SubCommand("info")
    public void infoCommand(CommandSender commandSender) {
        Player player = (Player) commandSender;
        if(!astroTournamentsPlugin.getTournamentManager().isActive()) {
            player.sendMessage(Utils.fixColor("&cThere is no tournament currently active"));
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
            return;
        }
        player.sendMessage(Utils.fixColor("Players Remaining: " + astroTournamentsPlugin.getTournamentManager().getTournamentParticipants().size()));
    }

    @SubCommand("start")
    @Permission("tournaments.*")
    @NoPermission("&cAn internal error occurred when executing this command.")
    public void startCommand(CommandSender commandSender) {
        Player player = (Player) commandSender;
        if(astroTournamentsPlugin.getTournamentManager().isActive()) {
            player.sendMessage(Utils.fixColor("&cThere is already an active tournament event"));
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
            return;
        }
        player.sendMessage(Utils.fixColor("&aYou have successfully started a tournament"));
        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 2.0F, 2.0F);
        astroTournamentsPlugin.getTournamentManager().setActive(true);
        astroTournamentsPlugin.getTournamentManager().setJoinable(true);
        new TournamentStartTask(astroTournamentsPlugin);
    }

    /*@SubCommand("end")
    @Permission("tournaments.*")
    @NoPermission("&cAn internal error occurred when executing this command.")
    public void endCommand(CommandSender commandSender) {
        Player player = (Player) commandSender;
        if(!astroTournamentsPlugin.getTournamentManager().isActive()) {
            player.sendMessage(Utils.fixColor("&cThere is not an active tournament event"));
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
            return;
        }
        astroTournamentsPlugin.getTournamentManager().getTournamentParticipants().forEach(player1 -> {
            player.teleport(Bukkit.getWorld("world").getSpawnLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
        });
        astroTournamentsPlugin.getTournamentManager().end();
        player.sendMessage(Utils.fixColor("&aYou have successfully ended a tournament"));
        player.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 2.0F, 2.0F);

    }*/

    private void sendUserHelp(CommandSender commandSender) {
        commandSender.sendMessage(Utils.fixColor("&a-------[ &2Tournaments &a]-------"));
        commandSender.sendMessage(Utils.fixColor("&a[] &8- &7Optional argument"));
        commandSender.sendMessage(Utils.fixColor("&2<> &8- &7Required argument"));
        commandSender.sendMessage("");
        commandSender.sendMessage(Utils.fixColor(" &2* &a/tournament join"));
        commandSender.sendMessage(Utils.fixColor(" &2* &a/tournament leave"));
        commandSender.sendMessage(Utils.fixColor(" &2* &a/tournament info"));
        commandSender.sendMessage(Utils.fixColor("&a-------[ &2Tournaments  &a]-------"));
    }

    private void sendAdminHelp(CommandSender commandSender) {
        commandSender.sendMessage(Utils.fixColor("&a-------[ &2Tournaments &a]-------"));
        commandSender.sendMessage(Utils.fixColor("&a[] &8- &7Optional argument"));
        commandSender.sendMessage(Utils.fixColor("&2<> &8- &7Required argument"));
        commandSender.sendMessage("");
        commandSender.sendMessage(Utils.fixColor(" &2* &a/tournament join"));
        commandSender.sendMessage(Utils.fixColor(" &2* &a/tournament leave"));
        commandSender.sendMessage(Utils.fixColor(" &2* &a/tournament info"));
        commandSender.sendMessage("");
        commandSender.sendMessage(Utils.fixColor(" &2* &a/tournament start <time>"));
        commandSender.sendMessage(Utils.fixColor(" &2* &a/tournament end"));
        commandSender.sendMessage(Utils.fixColor(" &2* &a/tournament reload"));
        commandSender.sendMessage(Utils.fixColor("&a-------[ &2Tournaments  &a]-------"));
    }

}
