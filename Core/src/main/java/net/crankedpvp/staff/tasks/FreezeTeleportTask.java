package net.crankedpvp.staff.tasks;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.core.AstroCore;
import net.crankedpvp.staff.utils.TitleUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

public class FreezeTeleportTask extends BukkitRunnable {

    private final AstroCore plugin;
    public FreezeTeleportTask(AstroCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for(Map.Entry<UUID, Location> playerLocationEntry : this.plugin.getFreezeManager().getFrozenPlayers().entrySet()) {
            OfflinePlayer oplayer = Bukkit.getOfflinePlayer(playerLocationEntry.getKey());
            if(!oplayer.isOnline()) continue;
            Player player = (Player)oplayer;
            Location location = playerLocationEntry.getValue();
            if(!player.getLocation().equals(location)) {
                player.sendMessage(CC.translate("&b&lFreeze &8➥ &7Do not logout while you are frozen"));
                TitleUtils.sendTitle(player, CC.translate("&c&lFROZEN"), CC.translate("&7DO NOT LOG OUT"), 1, 1, 1);
                player.playSound(player.getLocation(), Sound.NOTE_BASS_DRUM, 2.0f, 2.0f);
            }
            player.teleport(location, PlayerTeleportEvent.TeleportCause.UNKNOWN);
        }
    }

}
