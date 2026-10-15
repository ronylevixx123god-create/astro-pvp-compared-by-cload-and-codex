package me.astropvp.astrospawners.xpboosters;

import com.cosmicpvp.cosmicutils.utils.CC;
import lombok.Getter;
import me.astropvp.astrospawners.AstroSpawners;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class BoosterManager {
    private Map<UUID, Double> activeXPBoosters;
    private Map<UUID, Double> activeDropBoosters;



    public BoosterManager() {
        this.activeXPBoosters = new HashMap<>();
        this.activeDropBoosters = new HashMap<>();

    }

    public void addBooster(Player player, double amount, int timeInMinutes, boolean isXpBooster) {
        final UUID uuid = player.getUniqueId();
        if (isXpBooster) {
            activeXPBoosters.put(uuid, amount);
            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroSpawners.getInstance(), () -> {
                activeXPBoosters.remove(uuid);
                if (player.isOnline()) {
                    player.sendMessage(CC.translate("&b&l(XP Booster) &bYour xp booster has expired!"));
                }
            }, (long) timeInMinutes *20*60);
        } else {
            activeDropBoosters.put(uuid, amount);
            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroSpawners.getInstance(), () -> {
                activeDropBoosters.remove(uuid);
                if (player.isOnline()) {
                    player.sendMessage(CC.translate("&6&l(Drop Booster) &6Your drop booster has expired!"));
                }
            }, (long) timeInMinutes *20*60);
        }
    }
}
