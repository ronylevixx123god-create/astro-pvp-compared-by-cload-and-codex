package me.kingsavaqe.cagepvp.tasks;

import me.kingsavaqe.cagepvp.AstroCagePvP;
import me.kingsavaqe.cagepvp.menus.Matches;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.scheduler.BukkitRunnable;

public class MatchesTask extends BukkitRunnable {
   private final AstroCagePvP instance;

   public MatchesTask(AstroCagePvP instance) {
      this.instance = instance;
   }

   public void run() {
      FileConfiguration configLoad = this.instance.getConfig();

      for (Player all : Bukkit.getOnlinePlayers()) {
         InventoryView inv = all.getOpenInventory();
         if (inv != null && inv.getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menus.Matches.Title")))) {
            Matches.getInstance().open(all);
         }
      }

   }
}
