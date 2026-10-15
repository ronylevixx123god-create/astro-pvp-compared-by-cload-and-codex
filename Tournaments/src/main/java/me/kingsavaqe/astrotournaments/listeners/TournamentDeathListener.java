package me.kingsavaqe.astrotournaments.listeners;

import me.kingsavaqe.astrotournaments.AstroTournamentsPlugin;
import me.kingsavaqe.astrotournaments.managers.TournamentManager;
import me.kingsavaqe.astrotournaments.objects.TournamentMatch;
import me.kingsavaqe.astrotournaments.objects.TournamentPlayer;
import me.kingsavaqe.astrotournaments.utils.Utils;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffectType;

public class TournamentDeathListener implements Listener {

    private final AstroTournamentsPlugin astroTournamentsPlugin;
    private final TournamentManager tournamentManager;

    public TournamentDeathListener(AstroTournamentsPlugin astroTournamentsPlugin) {
        this.astroTournamentsPlugin = astroTournamentsPlugin;
        this.tournamentManager = astroTournamentsPlugin.getTournamentManager();
    }

    @EventHandler
    public void tournamentDeathListener(PlayerDeathEvent event) {
        if(event.getEntity() == null || event.getEntity().getKiller() == null) return;
        TournamentPlayer victim = tournamentManager.getTournamentPlayer(event.getEntity().getUniqueId());
        TournamentPlayer attacker;

        if(astroTournamentsPlugin.getTournamentManager().getTournamentMatchList().size() < 1) return;
        for (int i = 0; i < astroTournamentsPlugin.getTournamentManager().getTournamentMatchList().size(); i++) {
            TournamentMatch tournamentMatch = astroTournamentsPlugin.getTournamentManager().getTournamentMatchList().get(i);
            if(tournamentMatch.getPlayerOne() == victim || tournamentMatch.getPlayerTwo() == victim) {


                event.getDrops().clear();

                if(tournamentMatch.getPlayerOne() == victim) {
                    attacker = tournamentMatch.getPlayerTwo();
                } else {
                    victim = tournamentMatch.getPlayerTwo();
                    attacker = tournamentMatch.getPlayerOne();
                }

                for (int j = 0; j < astroTournamentsPlugin.getTournamentManager().getTournamentParticipants().size(); j++) {
                    TournamentPlayer player = astroTournamentsPlugin.getTournamentManager().getTournamentParticipants().get(i);
                    player.getPlayer().sendMessage(Utils.fixColor("&6Tournaments &8| &7" + attacker.getPlayer().getName() + " &ehas beaten the player &7" + victim.getPlayer().getName() + " &ein a tournament match"));

                }


                tournamentMatch.getArena().reset();
                astroTournamentsPlugin.getTournamentManager().getTournamentMatchList().remove(tournamentMatch);

                if(astroTournamentsPlugin.getTournamentManager().getPlayerQueue().contains(tournamentManager.getTournamentPlayer(victim.getUuid()))) {
                    astroTournamentsPlugin.getTournamentManager().getPlayerQueue().removeFromQueue(victim);
                }
                astroTournamentsPlugin.getTournamentManager().getTournamentParticipants().remove(victim);
                victim.setInTournament(false);
                victim.getPlayer().sendMessage(Utils.fixColor("&cYou have been eliminated from the tournament"));

                attacker.getPlayer().teleport(astroTournamentsPlugin.getTournamentManager().getWaitingRoomLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);

                if(astroTournamentsPlugin.getTournamentManager().getPlayerQueue().contains(attacker)) {
                    astroTournamentsPlugin.getTournamentManager().getPlayerQueue().removeFromQueue(attacker);
                }
                astroTournamentsPlugin.getTournamentManager().getPlayerQueue().enqueue(attacker);

                astroTournamentsPlugin.getServer().getScheduler().runTaskLater(astroTournamentsPlugin, () -> {
                    astroTournamentsPlugin.getTournamentManager().putNextPlayersInArenas();
                }, 20L);

                attacker.increaseKills();
                victim.increaseDeaths();
                victim.increaseLosses();

                Utils.emptyInventory(victim.getPlayer());
                Utils.emptyInventory(attacker.getPlayer());
                GeneralUtil.healPlayer(victim.getPlayer(), victim.getPlayer().getMaxHealth());
                GeneralUtil.healPlayer(attacker.getPlayer(), attacker.getPlayer().getMaxHealth());
                attacker.getPlayer().setFireTicks(0);
                victim.getPlayer().setFireTicks(0);


            }
        }
    }




}
