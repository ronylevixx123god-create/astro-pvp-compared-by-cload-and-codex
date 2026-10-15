package net.astropvp.adminitems.managers;

import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

@Getter
public class AdminItemAbilityManager {
    private Map<UUID, Long> trapBlockered;
    private Map<UUID, BukkitRunnable> timeTravellers;
    public AdminItemAbilityManager() {
        this.trapBlockered = new HashMap<>();
        this.timeTravellers = new HashMap<>();
    }



    public boolean isTrapBlockered(UUID uuid) {
        return trapBlockered.containsKey(uuid);
    }

    public void trapBlock(UUID uuid) {
        trapBlockered.put(uuid, System.currentTimeMillis());

    }

}
