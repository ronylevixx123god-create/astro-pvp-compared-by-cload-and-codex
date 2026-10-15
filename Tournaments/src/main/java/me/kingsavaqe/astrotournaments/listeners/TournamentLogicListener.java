package me.kingsavaqe.astrotournaments.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.ifamasssxd.cosmicduel.DuelAPI;
import me.ifamasssxd.cosmicduel.struct.Arena;
import me.ifamasssxd.cosmicduel.struct.enums.Setting;
import me.ifamasssxd.cosmicduel.struct.newduel.NewDuel;
import me.kingsavaqe.astrotournaments.AstroTournamentsPlugin;
import me.kingsavaqe.astrotournaments.managers.TournamentManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TournamentLogicListener implements Listener {
    public List<String> allowedCommands = Arrays.asList("/msg", "/r", "/message", "/reply", "/tournament", "/tm", "/tournaments", "/f c f", "/f c p", "/f c t", "/f c m", "/bless", "/apply");
    private TournamentManager tournamentManager = AstroTournamentsPlugin.instance.getTournamentManager();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (tournamentManager.getTournamentPlayer(e.getPlayer().getUniqueId()).isInTournament() && !isAllowedCommand(e.getMessage())) {
            e.getPlayer().sendMessage(CC.translate("&c&l(!)&c You cannot use this command in a tournament."));
            e.getPlayer().sendMessage(CC.translate("&7You can leave the tournament with /tournament leave."));
            e.setCancelled(true);
            e.setMessage("/null");
        }

    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        if (tournamentManager.getTournamentPlayer(e.getPlayer().getUniqueId()).isInTournament()) {
            e.getPlayer().sendMessage(CC.translate("&c&l(!)&c You cannot drop items in a tournament."));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (tournamentManager.getTournamentPlayer(e.getPlayer().getUniqueId()).isInTournament()) {
            e.getPlayer().sendMessage(CC.translate("&c&l(!)&c You cannot break blocks while in a tournament."));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (tournamentManager.getTournamentPlayer(e.getPlayer().getUniqueId()).isInTournament()) {
            e.getPlayer().sendMessage(CC.translate("&c&l(!)&c You cannot place blocks while in a tournament."));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFly(PlayerToggleFlightEvent e) {
        if (tournamentManager.getTournamentPlayer(e.getPlayer().getUniqueId()).isInTournament()) {
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (e.getInventory().getType() != InventoryType.PLAYER && tournamentManager.getTournamentPlayer(e.getPlayer().getUniqueId()).isInTournament() && !CC.strip(e.getInventory().getTitle()).equals("Astro Tournaments Menu")) {
            e.getPlayer().sendMessage(CC.translate("&c&l(!)&c You cannot open inventories while in a tournament."));
            e.setCancelled(true);
            e.getPlayer().closeInventory();
        }
    }

    public boolean isAllowedCommand(String message) {
        for (String allowedCMD : allowedCommands) {
            if (message.toLowerCase().startsWith(allowedCMD)) {
                return true;
            }
        }
        return false;
    }


}
