package me.kingsavaqe.cagepvp.playerdata;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerDataManager {
   private Map<UUID, PlayerData> playerDataStorage = new HashMap();

   public PlayerDataManager() {

      for(Player all : Bukkit.getOnlinePlayers()) {
         this.createPlayerData(all);
      }

   }

   public void createPlayerData(Player player) {
      this.playerDataStorage.put(player.getUniqueId(), new PlayerData());
   }

   public PlayerData getPlayerData(UUID uuid) {
      return (PlayerData)this.playerDataStorage.get(uuid);
   }

   public PlayerData getPlayerData(Player player) {
      return (PlayerData)this.playerDataStorage.get(player.getUniqueId());
   }

   public void removePlayerData(UUID uuid) {
      this.playerDataStorage.remove(uuid);
   }

   public void removePlayerData(Player player) {
      this.playerDataStorage.remove(player.getUniqueId());
   }

   public boolean hasPlayerData(UUID uuid) {
      return this.playerDataStorage.containsKey(uuid);
   }

   public boolean hasPlayerData(Player player) {
      return this.playerDataStorage.containsKey(player.getUniqueId());
   }
}
