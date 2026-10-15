package me.kingsavaqe.cagepvp.listeners;

import java.util.Iterator;

import me.kingsavaqe.cagepvp.AstroCagePvP;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {
   private final AstroCagePvP instance;

   public Join(AstroCagePvP instance) {
      this.instance = instance;
   }

   @EventHandler
   public void onPlayerJoin(PlayerJoinEvent event) {
      Player player = event.getPlayer();
      this.instance.getPlayerDataManager().createPlayerData(player);
      if (this.instance.getWildPvPManager().hasWildPvP(player.getLocation().getChunk())) {
         FileConfiguration configLoad = this.instance.getConfig();

         for(String command : configLoad.getStringList("WildPvP.Join.Commands")) {
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command.replace("%player", player.getName()));
         }

         player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Join.Teleported.Message")));
         player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
      }
   }
}
