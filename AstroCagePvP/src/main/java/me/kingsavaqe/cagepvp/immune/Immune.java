package me.kingsavaqe.cagepvp.immune;

import me.kingsavaqe.cagepvp.AstroCagePvP;
import me.kingsavaqe.cagepvp.utils.TitleManager;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Immune {
   private final AstroCagePvP instance;
   private final Player player;
   private boolean cancelled = false;

   public Immune(AstroCagePvP instance, Player player) {
      this.instance = instance;
      this.player = player;
   }

   public Player getPlayer() {
      return this.player;
   }

   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void start() {
      final ImmuneManager immuneManager = this.instance.getImmuneManager();
      final FileConfiguration configLoad = this.instance.getConfig();
      this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Immune.Countdown.Started.Message")));
      (new BukkitRunnable() {
         int countdown = 20;

         public void run() {
            if (!Immune.this.cancelled && Immune.this.player.isOnline() && this.countdown >= 0) {
               if (this.countdown == 15 || this.countdown == 10 || this.countdown > 0 && this.countdown <= 5) {
                  Immune.this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Immune.Countdown.Ending.Message").replace("%countdown", String.valueOf(this.countdown))));
                  TitleManager.sendTitle(Immune.this.player, ChatColor.translateAlternateColorCodes('&', configLoad.getString("Immune.Countdown.Ending.Title.Title").replace("%countdown", String.valueOf(this.countdown))), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Immune.Countdown.Ending.Title.SubTitle").replace("%countdown", String.valueOf(this.countdown))), 0, 30, 0);
                  Immune.this.player.playSound(Immune.this.player.getLocation(), Sound.NOTE_STICKS, 1.0F, 1.0F);
               } else if (this.countdown == 0) {
                  Immune.this.cancelled = true;
                  this.cancel();
                  immuneManager.removeImmune(Immune.this.player);
                  Immune.this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Immune.Countdown.Ended.Message").replace("%countdown", String.valueOf(this.countdown))));
                  TitleManager.sendTitle(Immune.this.player, ChatColor.translateAlternateColorCodes('&', configLoad.getString("Immune.Countdown.Ended.Title.Title").replace("%countdown", String.valueOf(this.countdown))), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Immune.Countdown.Ended.Title.SubTitle").replace("%countdown", String.valueOf(this.countdown))), 0, 30, 10);
                  Immune.this.player.playSound(Immune.this.player.getLocation(), Sound.FIZZ, 1.0F, 1.0F);
               }

               --this.countdown;
            } else {
               immuneManager.removeImmune(Immune.this.player);
               this.cancel();
            }
         }
      }).runTaskTimerAsynchronously(this.instance, 0L, 20L);
   }
}
