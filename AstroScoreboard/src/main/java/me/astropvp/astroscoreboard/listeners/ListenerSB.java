package me.astropvp.astroscoreboard.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.event.*;
import me.astropvp.astroscoreboard.AstroScoreboardAPI;
import me.astropvp.astroscoreboard.AstroScoreboardPlugin;
import me.astropvp.astroscoreboard.board.Board;
import me.astropvp.astroscoreboard.manager.PlayerDisplayManager;
import me.astropvp.proxypach.ProxyPatch;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Criterias;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.util.Collection;

public class ListenerSB implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.getScoreboard().getObjective("showhealth") == null) {
            player.getScoreboard().registerNewObjective("showhealth", Criterias.HEALTH);
        }
        Objective h = player.getScoreboard().getObjective("showhealth");

        h.setDisplaySlot(DisplaySlot.BELOW_NAME);
        h.setDisplayName(ChatColor.RED + "❤");


        //Bukkit.getScheduler().runTaskLaterAsynchronously(AstroScoreboardPlugin.getInstance(), () -> refreshTagsAsyncForServer(event.getPlayer()), 40L);
    }


    /*@EventHandler
    public void onFactionJoin(FPlayerJoinEvent event) {
        refreshTagsAsyncForServer(event.getfPlayer().getPlayer());
    }

    @EventHandler
    public void onFactionLeave(FPlayerLeaveEvent event) {
        refreshTagsAsyncForServer(event.getfPlayer().getPlayer());
    }

    @EventHandler
    public void onFactionCreate(FactionCreateEvent event) {
        refreshTagsAsyncForServer(event.getFPlayer().getPlayer());
    }

    @EventHandler
    public void onFactionDisband(FactionDisbandEvent event) {
        refreshTagsAsyncForServer(event.getPlayer());
    }

    @EventHandler
    public void onFactionRelationChange(FactionRelationEvent event) {
        refreshTagsAsyncForFaction(event.getFaction(), event.getTargetFaction());
        refreshTagsAsyncForFaction(event.getTargetFaction(), event.getFaction());
    }

    @EventHandler
    public void onFactionRename(FactionRenameEvent event) {
        refreshTagsAsyncForServer(event.getPlayer());
    }


    public void refreshTagsAsyncForFaction(Faction faction1, Faction faction2) {
        Collection<? extends Player> faction1Players = faction1.getOnlinePlayers();
        Collection<? extends Player> factions2Players = faction2.getOnlinePlayers();

        Bukkit.getScheduler().runTaskAsynchronously(AstroScoreboardPlugin.getInstance(), () -> {
            for (Player player : faction1Players) {
                for (Player other : factions2Players) {
                    try {
                        //PlayerDisplayManager.setUserPrefix(player, other, AstroScoreboardAPI.getDefaultPrefix(player, other));
                        PlayerDisplayManager.setUserPrefix(player, other, AstroScoreboardAPI.getDefaultPrfix(player));
                        PlayerDisplayManager.setUserSuffix(player, other, " " + AstroScoreboardAPI.getDefaultSuffix(player, other));

                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
    }

    public void refreshTagsAsyncForServer(Player playerJoining) {
        Collection<? extends Player> allPlayers = Bukkit.getOnlinePlayers();
        String prefix = AstroScoreboardAPI.getDefaultPrfix(playerJoining);
        Bukkit.getScheduler().runTaskAsynchronously(AstroScoreboardPlugin.getInstance(), () -> {
            for (Player player : allPlayers) {
                //PlayerDisplayManager.setUserPrefix(player, playerJoining, AstroScoreboardAPI.getDefaultPrefix(player, playerJoining));
                //PlayerDisplayManager.setUserPrefix(playerJoining, player, AstroScoreboardAPI.getDefaultPrefix(playerJoining, player));
                PlayerDisplayManager.setUserPrefix(player, playerJoining, AstroScoreboardAPI.getDefaultPrfix(player));
                PlayerDisplayManager.setUserPrefix(playerJoining, player, prefix);


                PlayerDisplayManager.setUserSuffix(player, playerJoining, " " + AstroScoreboardAPI.getDefaultSuffix(player, playerJoining));
                PlayerDisplayManager.setUserSuffix(playerJoining, player, " " + AstroScoreboardAPI.getDefaultSuffix(playerJoining, player));

            }
        });
    }*/
}
