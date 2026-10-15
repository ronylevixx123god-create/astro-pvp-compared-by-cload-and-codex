package me.kingsavaqe.astrotournaments.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.kingsavaqe.astrotournaments.AstroTournamentsPlugin;
import me.kingsavaqe.astrotournaments.objects.TournamentPlayer;
import me.kingsavaqe.astrotournaments.utils.Utils;
import me.recolance.auction.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        TournamentPlayer player = AstroTournamentsPlugin.instance.getTournamentManager().getTournamentPlayer(event.getPlayer().getUniqueId());
        if (player.isInTournament()) {
            Utils.emptyInventory(player.getPlayer());
            player.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
            player.getPlayer().sendMessage(CC.translate("&c&l(!)&c An error occured when leaving the tournament, your inventory has been cleared and you have been teleported to /spawn."));
            player.setInTournament(false);
        }
    }
}
