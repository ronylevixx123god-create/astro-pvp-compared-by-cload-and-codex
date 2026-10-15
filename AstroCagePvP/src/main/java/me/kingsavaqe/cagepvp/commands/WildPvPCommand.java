package me.kingsavaqe.cagepvp.commands;

import me.kingsavaqe.cagepvp.AstroCagePvP;
import me.kingsavaqe.cagepvp.combattag.CombatTagManager;
import me.kingsavaqe.cagepvp.menus.Matches;
import me.kingsavaqe.cagepvp.utils.TitleManager;
import me.kingsavaqe.cagepvp.wildpvp.WildPvP;
import me.kingsavaqe.cagepvp.wildpvp.WildPvPManager;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class WildPvPCommand implements CommandExecutor {
   private final AstroCagePvP instance;

   public WildPvPCommand(AstroCagePvP instance) {
      this.instance = instance;
   }

   public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
      if (!(sender instanceof Player)) {
         sender.sendMessage(this.instance.getDescription().getName() + " | Error: You must be a player to perform that command.");
      } else {
         Player player = (Player)sender;
         CombatTagManager combatTagManager = this.instance.getCombatTagManager();
         WildPvPManager wildPvPManager = this.instance.getWildPvPManager();
         FileConfiguration configLoad = this.instance.getConfig();
         if (args.length != 0 && args[0].equalsIgnoreCase("leave")) {
            if (!wildPvPManager.hasWildPvP(player)) {
               player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Commands.WildPvP.Leave.Match.Message")));
               player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
            } else {
               if (combatTagManager.isTagged(player)) {
                  player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Commands.WildPvP.Leave.CombatTag.Message")));
                  player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
               } else {
                  WildPvP wildPvP = wildPvPManager.getWildPvP(player);
                  wildPvP.setEnded(true);
                  if (configLoad.getBoolean("WildPvP.Match.Title")) {
                     TitleManager.sendTitle(player, ChatColor.translateAlternateColorCodes('&', configLoad.getString("Commands.WildPvP.Leave.Left.Title.Title")), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Commands.WildPvP.Leave.Left.Title.SubTitle")), 0, 30, 10);
                  }

                  if (wildPvP.hasJoined()) {
                     Player creator;
                     if (wildPvP.isJoined(player)) {
                        creator = wildPvP.getCreator();


                        wildPvP.teleportToJoinLocation(player);

                        wildPvP.hideBorder(player);
                        wildPvP.hideBorder(creator);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Commands.WildPvP.Leave.Left.Player.Participant.Message").replace("%player", creator.getName())));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Commands.WildPvP.Leave.Teleported.Message")));
                        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                        creator.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Commands.WildPvP.Leave.Left.Participant.Message").replace("%player", player.getName())));
                        creator.playSound(creator.getLocation(), Sound.IRONGOLEM_HIT, 1.0F, 1.0F);
                     } else {
                        creator = wildPvP.getJoined();
                        wildPvP.teleportToJoinLocation(creator);
                        wildPvP.hideBorder(player);
                        wildPvP.hideBorder(creator);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Commands.WildPvP.Leave.Left.Player.Participant.Message").replace("%player", creator.getName())));
                        player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 1.0F, 1.0F);
                        creator.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Commands.WildPvP.Leave.Left.Participant.Message").replace("%player", player.getName())));

                        creator.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Commands.WildPvP.Leave.Teleported.Message")));

                        creator.playSound(creator.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                     }
                     if (configLoad.getBoolean("WildPvP.Match.Title")) {
                        TitleManager.sendTitle(creator, ChatColor.translateAlternateColorCodes('&', configLoad.getString("Commands.WildPvP.Leave.Left.Title.Title")), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Commands.WildPvP.Leave.Left.Title.SubTitle")), 0, 30, 10);
                     }
                  } else {
                     wildPvP.hideBorder(player);
                     player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Commands.WildPvP.Leave.Left.Player.Alone.Message")));
                     player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 1.0F, 1.0F);
                     if (configLoad.getBoolean("WildPvP.Match.Title")) {
                        TitleManager.sendTitle(player, ChatColor.translateAlternateColorCodes('&', configLoad.getString("Commands.WildPvP.Leave.Left.Title.Title")), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Commands.WildPvP.Leave.Left.Title.SubTitle")), 0, 30, 10);
                     }
                  }

                  if (combatTagManager.isTagged(wildPvP.getCreator())) {
                     combatTagManager.removeTag(wildPvP.getCreator());
                  }

                  if (wildPvP.hasJoined() && combatTagManager.isTagged(wildPvP.getJoined())) {
                     combatTagManager.removeTag(wildPvP.getJoined());
                  }

                  wildPvPManager.removeWildPvP(wildPvP);
               }
            }
         } else {
            this.instance.getPlayerDataManager().getPlayerData(player).setPage(1);
            Matches.getInstance().open(player);
            player.playSound(player.getLocation(), Sound.CHEST_OPEN, 1.0F, 1.0F);
         }
      }
       return true;
   }
}
