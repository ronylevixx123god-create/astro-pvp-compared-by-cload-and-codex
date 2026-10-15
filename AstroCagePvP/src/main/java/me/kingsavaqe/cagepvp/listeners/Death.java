package me.kingsavaqe.cagepvp.listeners;

import java.util.Iterator;

import me.kingsavaqe.cagepvp.AstroCagePvP;
import me.kingsavaqe.cagepvp.combattag.CombatTagManager;
import me.kingsavaqe.cagepvp.immune.Immune;
import me.kingsavaqe.cagepvp.immune.ImmuneManager;
import me.kingsavaqe.cagepvp.utils.TitleManager;
import me.kingsavaqe.cagepvp.wildpvp.WildPvP;
import me.kingsavaqe.cagepvp.wildpvp.WildPvPManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Death implements Listener {
   private final AstroCagePvP instance;

   public Death(AstroCagePvP instance) {
      this.instance = instance;
   }

   @EventHandler
   public void onPlayerDeath(PlayerDeathEvent event) {
      Player player = event.getEntity();
      CombatTagManager combatTagManager = this.instance.getCombatTagManager();
      WildPvPManager wildPvPManager = this.instance.getWildPvPManager();
      ImmuneManager immuneManager = this.instance.getImmuneManager();
      FileConfiguration configLoad = this.instance.getConfig();
      if (wildPvPManager.hasWildPvP(player)) {
         WildPvP wildPvP = wildPvPManager.getWildPvP(player);
         if (wildPvP.hasJoined()) {
            wildPvP.setEnded(true);
            Player creator;
            Immune immune;
            Iterator var10;
            Player all;
            if (wildPvP.isJoined(player)) {
               creator = wildPvP.getCreator();
               wildPvP.hideBorder(player);
               wildPvP.hideBorder(creator);
               player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Death.Lost.Message").replace("%player", creator.getName())));
               player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0F, 5.0F);
               if (configLoad.getBoolean("WildPvP.Match.Title")) {
                  TitleManager.sendTitle(player, ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Death.Lost.Title.Title")), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Death.Lost.Title.SubTitle")), 0, 30, 10);
               }

               creator.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Death.Won.Message").replace("%player", player.getName())));
               creator.playSound(creator.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0F, 5.0F);
               if (configLoad.getBoolean("WildPvP.Match.Title")) {
                  TitleManager.sendTitle(creator, ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Death.Won.Title.Title")), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Death.Won.Title.SubTitle")), 0, 30, 10);
               }

               immune = immuneManager.createImmune(creator);
               immune.start();
               var10 = Bukkit.getOnlinePlayers().iterator();

               while(var10.hasNext()) {
                  all = (Player)var10.next();
                  if (!all.getUniqueId().equals(player.getUniqueId()) && !all.getUniqueId().equals(creator.getUniqueId())) {
                     all.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Death.Broadcast.Message").replace("%winner", creator.getName()).replace("%loser", player.getName())));
                  }
               }

               if (combatTagManager.isTagged(creator)) {
                  combatTagManager.removeTag(creator);
               }
            } else {
               creator = wildPvP.getJoined();
               wildPvP.hideBorder(player);
               wildPvP.hideBorder(creator);
               player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Death.Lost.Message").replace("%player", creator.getName())));
               player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0F, 5.0F);
               if (configLoad.getBoolean("WildPvP.Match.Title")) {
                  TitleManager.sendTitle(player, ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Death.Lost.Title.Title")), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Death.Lost.Title.SubTitle")), 0, 30, 10);
               }

               creator.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Death.Won.Message").replace("%player", player.getName())));
               creator.playSound(creator.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 5.0F);
               if (configLoad.getBoolean("WildPvP.Match.Title")) {
                  TitleManager.sendTitle(creator, ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Quit.Won.Title.Title")), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Quit.Won.Title.SubTitle")), 0, 30, 10);
               }

               immune = immuneManager.createImmune(creator);
               immune.start();
               var10 = Bukkit.getOnlinePlayers().iterator();

               while(var10.hasNext()) {
                  all = (Player)var10.next();
                  if (!all.getUniqueId().equals(player.getUniqueId()) && !all.getUniqueId().equals(creator.getUniqueId())) {
                     all.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Death.Broadcast.Message").replace("%winner", creator.getName()).replace("%loser", player.getName())));
                  }
               }

               if (combatTagManager.isTagged(creator)) {
                  combatTagManager.removeTag(creator);
               }
            }
         } else {
            player.sendMessage(configLoad.getString("Listeners.Death.Alone.Message"));
            player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 1.0F, 1.0F);
         }

         wildPvPManager.removeWildPvP(wildPvP);
      }
   }
}
