package net.crankedpvp.staff.managers;

import lombok.Getter;
import net.astropvp.core.AstroCore;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FreezeManager {

    private AstroCore plugin;
    public FreezeManager(AstroCore plugin) {
        this.plugin = plugin;
    }

    @Getter
    private Map<UUID, Location> frozenPlayers = new HashMap<>();
}
