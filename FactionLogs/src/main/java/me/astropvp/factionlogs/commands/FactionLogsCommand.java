package me.astropvp.factionlogs.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.perms.Role;
import me.astropvp.factionlogs.gui.FactionLogsGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class FactionLogsCommand implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().equalsIgnoreCase("/f logs") || event.getMessage().equalsIgnoreCase("/f log")) {
            event.setMessage("/null");
            event.setCancelled(true);
            FPlayer fPlayer = FPlayers.getInstance().getByPlayer(event.getPlayer());
            if (!fPlayer.hasFaction()) {
                fPlayer.sendMessage(CC.translate("&6&lFactions &8⎜ &cError&8: &eYou need a faction to type this command!"));
                return;
            }

            if (!fPlayer.getRole().isAtLeast(Role.MODERATOR)) {
                fPlayer.sendMessage(CC.translate("&6&lFactions &8⎜ &cError&8: &eYou need to be atleast a mod to type this command!"));
                return;
            }
            FactionLogsGUI factionLogsGUI = new FactionLogsGUI(event.getPlayer(), fPlayer.getFaction());
            factionLogsGUI.getFLogGui().open(event.getPlayer());
            fPlayer.sendMessage(CC.translate("&6&lFactions &8⎜ &eOpening the faction logs gui."));

        }
    }
}
