package net.crankedpvp.staff.managers;

import com.google.common.collect.Maps;
import lombok.Getter;
import net.astropvp.core.AstroCore;
import net.crankedpvp.staff.struct.StaffMode;
import org.bukkit.entity.Player;

import java.util.Map;

public class StaffManager {

    private final AstroCore plugin;
    public StaffManager(AstroCore plugin) {
        this.plugin = plugin;
        this.playersInStaffMode = Maps.newConcurrentMap();
    }

    @Getter
    private final Map<Player, StaffMode> playersInStaffMode;

    public StaffMode getStaffModeFromPlayer(Player player) {
        return this.playersInStaffMode.get(player);
    }

    public boolean isPlayerInStaffMode(Player player) {
        return playersInStaffMode.containsKey(player);
    }

    public void removePlayerFromStaffMode(Player player) {
        this.playersInStaffMode.remove(player);
    }

    public void putPlayerInStaffMode(Player player, StaffMode staffMode) {
        this.playersInStaffMode.put(player, staffMode);
    }
}
