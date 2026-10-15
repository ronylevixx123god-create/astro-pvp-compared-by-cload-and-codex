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
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Quit implements Listener {
   private final AstroCagePvP instance;

   public Quit(AstroCagePvP instance) {
      this.instance = instance;
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onPlayerQuit(PlayerQuitEvent event) {
      Player player = event.getPlayer();
      this.instance.getPlayerDataManager().removePlayerData(player);
      CombatTagManager combatTagManager = this.instance.getCombatTagManager();
      WildPvPManager wildPvPManager = this.instance.getWildPvPManager();
      ImmuneManager immuneManager = this.instance.getImmuneManager();
      FileConfiguration configLoad = this.instance.getConfig();
      if (immuneManager.hasImmune(player)) {
         Immune immune = immuneManager.getImmune(player);
         immune.setCancelled(true);
         immuneManager.removeImmune(immune);
      }

      if (wildPvPManager.hasWildPvP(player)) {
         WildPvP wildPvP = wildPvPManager.getWildPvP(player);
         wildPvP.setEnded(true);
         if (wildPvP.hasJoined()) {
            Player joined;
            if (!combatTagManager.isTagged(player)) {
               if (wildPvP.isJoined(player)) {
                  joined = wildPvP.getCreator();
                  wildPvP.teleportToJoinLocation(player);
                  wildPvP.hideBorder(joined);
                  joined.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Quit.Left.Message").replace("%player", player.getName())));
                  joined.playSound(joined.getLocation(), Sound.IRONGOLEM_HIT, 1.0F, 1.0F);

               } else {
                  joined = wildPvP.getJoined();
                  wildPvP.teleportToJoinLocation(joined);
                  wildPvP.hideBorder(joined);
                  joined.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Quit.Left.Message").replace("%player", player.getName())));
                  joined.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Quit.Teleported.Message").replace("%player", player.getName())));
                  joined.playSound(joined.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);

               }
               if (configLoad.getBoolean("WildPvP.Match.Title")) {
                  TitleManager.sendTitle(joined, ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Quit.Left.Title.Title")), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Quit.Left.Title.SubTitle")), 0, 30, 10);
               }
               if (combatTagManager.isTagged(joined)) {
                  combatTagManager.removeTag(joined);
               }
            } else {
               Immune immune;
               Iterator var10;
               Player all;
               if (wildPvP.isJoined(player)) {
                  joined = wildPvP.getCreator();

               } else {
                  joined = wildPvP.getJoined();

               }
               wildPvP.hideBorder(joined);
               joined.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Quit.Won.Message").replace("%player", player.getName())));
               joined.playSound(joined.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0F, 5.0F);
               if (configLoad.getBoolean("WildPvP.Match.Title")) {
                  TitleManager.sendTitle(joined, ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Quit.Won.Title.Title")), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Quit.Won.Title.SubTitle")), 0, 30, 10);
               }
               combatTagManager.removeTag(joined);
               immune = immuneManager.createImmune(joined);
               immune.start();
               var10 = Bukkit.getOnlinePlayers().iterator();
               while(var10.hasNext()) {
                  all = (Player)var10.next();
                  if (!all.getUniqueId().equals(joined.getUniqueId())) {
                     all.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Quit.Broadcast.Message").replace("%winner", joined.getName()).replace("%loser", player.getName())));
                  }
               }
               if (combatTagManager.isTagged(joined)) {
                  combatTagManager.removeTag(joined);
               }
            }
         }

         wildPvPManager.removeWildPvP(wildPvP);
      }

   }
}
