package me.astropvp.hotfixplugin.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.LuckPermUtils;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import me.astropvp.hotfixplugin.HotFixPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.List;

public class AntiPhase implements Listener {

    private List<Player> grace;

    public AntiPhase() {
        this.grace = new ArrayList<>();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        this.grace.add(event.getPlayer());
        Bukkit.getScheduler().runTaskLater(HotFixPlugin.getInstance(), () -> this.grace.remove(event.getPlayer()), 5L);
    }

    @EventHandler
    public void onTeleportAntiPhase(PlayerTeleportEvent event) {
        if(event.getCause() != PlayerTeleportEvent.TeleportCause.UNKNOWN) return;

        if(this.grace.contains(event.getPlayer())) return;

        Location from = event.getFrom();
        Location to = event.getTo();

        if(from.getWorld() != to.getWorld()) return;

        double xChange = Math.abs(from.getX() - to.getX());
        double zChange = Math.abs(from.getZ() - to.getZ());
        double yChange = Math.abs(from.getY() - to.getY());

        if(yChange < 2) return;
        if(xChange > 2 || zChange > 2) return;

        Faction fromFaction = Board.getInstance().getFactionAt(new FLocation(from));
        Faction toFaction = Board.getInstance().getFactionAt(new FLocation(to));

        Faction yourFaction = FPlayers.getInstance().getByPlayer(event.getPlayer()).getFaction();

        if(toFaction.isWarZone() || toFaction.isWilderness() || toFaction.isSafeZone()) return;
        if(yourFaction == toFaction) return;
        if(fromFaction != toFaction) return;

        sendToStaff(event.getPlayer(), toFaction.getTag());
        event.getPlayer().setHealth(0);
        event.setCancelled(true);

    }

    private void sendToStaff(Player player, String facTag) {
        Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("antiphase.view")).forEach(
                p -> p.sendMessage(CC.translate("&d&lAnti Phase &8| &fThe player &c" + player.getName() + " &ftried to phase into &c" + facTag + " 's &fbase and has been /killed."))
        );
    }
}
