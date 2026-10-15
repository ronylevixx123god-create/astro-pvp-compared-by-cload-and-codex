package me.kingsavaqe.cagepvp.factions;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.perms.Relation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class FactionsManager {
   private boolean factionsPlugin;

   public FactionsManager() {
      this.registerFactions();
   }

   public String getFactionName(Player player) {
      return FPlayers.getInstance().getByPlayer(player).getFaction().getTag();
   }

   public boolean isFactionMember(Player player1, Player player2) {
      FPlayers fPlayers = FPlayers.getInstance();
      return fPlayers.getByPlayer(player1).getFaction().getRelationTo(fPlayers.getByPlayer(player2).getFaction()) == Relation.MEMBER;
   }

   public boolean isFactionAlly(Player player1, Player player2) {
      FPlayers fPlayers = FPlayers.getInstance();
      return fPlayers.getByPlayer(player1).getFaction().getRelationTo(fPlayers.getByPlayer(player2).getFaction()) == Relation.ALLY;
   }

   public boolean isWilderness(Location location) {
      Faction factionAtLocation = Board.getInstance().getFactionAt(new FLocation(location));
      return factionAtLocation == null || factionAtLocation.isWilderness();
   }

   public void registerFactions() {
      PluginManager pluginManager = Bukkit.getServer().getPluginManager();
      if (pluginManager.getPlugin("Factions") != null) {
         this.factionsPlugin = pluginManager.getPlugin("Factions") != null;
      }

   }

   public boolean hasFactions() {
      if (!this.factionsPlugin) {
         this.registerFactions();
      }

      return this.factionsPlugin;
   }
}
