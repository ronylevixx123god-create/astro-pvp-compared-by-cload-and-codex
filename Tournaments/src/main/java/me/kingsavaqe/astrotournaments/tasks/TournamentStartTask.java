package me.kingsavaqe.astrotournaments.tasks;

import me.kingsavaqe.astrotournaments.AstroTournamentsPlugin;
import me.kingsavaqe.astrotournaments.utils.Clickable;
import me.kingsavaqe.astrotournaments.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TournamentStartTask extends BukkitRunnable {

    private final AstroTournamentsPlugin astroTournamentsPlugin;
    private int seconds;

    public TournamentStartTask(AstroTournamentsPlugin astroTournamentsPlugin) {
        this.astroTournamentsPlugin = astroTournamentsPlugin;
        if(!astroTournamentsPlugin.getTournamentManager().isActive()) {
            return;
        }
        this.seconds = 121;
        this.runTaskTimer(astroTournamentsPlugin, 20L * 2, 20L);
    }

    @Override
    public void run() {
        this.seconds--;
        if(this.seconds == 0) {

            astroTournamentsPlugin.getTournamentManager().setupQueue();

            astroTournamentsPlugin.getTournamentManager().setJoinable(false);

            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(Utils.fixColor("&6&lAstro Tournaments &f(1v1 Tournament)"));
            Bukkit.broadcastMessage(Utils.fixColor("&7&oThe tournament event is now starting..."));
            Bukkit.broadcastMessage(Utils.fixColor("&7&oMay the best Astronaut win :D"));
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(Utils.fixColor("&6Tournament entry has now &c&nCLOSED"));
            Bukkit.broadcastMessage("");

            this.astroTournamentsPlugin.getTournamentManager().putNextPlayersInArenas();

            return;
        }

        if(this.seconds == 120 || this.seconds == 100 || this.seconds == 80 || this.seconds == 60 || this.seconds == 45 || this.seconds == 30 || this.seconds == 15 || this.seconds == 10 || (this.seconds <= 5 && this.seconds > 0)) {

            for(Player player : Bukkit.getOnlinePlayers()) {
                sendJSONMessage(player);
            }

        }
    }


    private String getFormattedTime(int seconds) {
        int minutes = seconds/60;
        seconds %= 60;
        StringBuilder builder = new StringBuilder();
        if(minutes > 0) {
            builder.append(minutes).append("m, ");
        }
        if(seconds > 0 || seconds == 0) {
            builder.append(seconds).append("s, ");
        }
        return builder.substring(0, builder.toString().lastIndexOf(","));
    }

    private void sendJSONMessage(Player player) {
        player.sendMessage("");
        player.sendMessage(Utils.fixColor("&6&lAstro Tournaments &f(1v1 Tournament)"));
        player.sendMessage(Utils.fixColor("&7&oTournaments are special events where players"));
        player.sendMessage(Utils.fixColor("&7&ofight eachother until only one player remains"));
        player.sendMessage("");
        Clickable clickable = new Clickable("&7Click &nhere&r &7to join the tournament", "&7&oClick to join tournament", "/tournament join");
        clickable.sendToPlayer(player);
        player.sendMessage("");
        player.sendMessage(Utils.fixColor("&6Tournament entry will close in &f" + getFormattedTime(this.seconds)));
        player.sendMessage("");
    }
}
