package net.astropvp.core.listeners;

import me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest;
import me.ifamasssxd.cosmicmysterychest.struct.MysteryChest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class FirstJoin implements Listener {
    private MysteryChest chest;

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        //this.chest = CosmicMysteryChest.get().getChestManager().getMysteryChestMap().get("StarterBundle");

        if (!player.hasPlayedBefore()) {
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            //Bukkit.dispatchCommand(console, "mc give " + event.getPlayer().getName() + " StarterBundle 1");
            //event.getPlayer().getInventory().addItem(this.chest.createClaimableItem(true));
            event.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
        }
    }


}
