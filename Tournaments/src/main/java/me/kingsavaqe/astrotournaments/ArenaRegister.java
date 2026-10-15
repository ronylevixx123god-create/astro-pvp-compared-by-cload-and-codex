package me.kingsavaqe.astrotournaments;

import com.google.common.collect.Maps;
import lombok.Getter;
import me.kingsavaqe.astrotournaments.objects.Arena;
import me.kingsavaqe.astrotournaments.utils.Pair;
import me.kingsavaqe.astrotournaments.utils.Utils;
import org.bukkit.Location;
import java.util.Map;

public class ArenaRegister {

    @Getter
    public Map<String, Arena> arenas;
    private final AstroTournamentsPlugin astroTournamentsPlugin;

    public ArenaRegister(AstroTournamentsPlugin astroTournamentsPlugin) {
        this.astroTournamentsPlugin = astroTournamentsPlugin;
        this.arenas = Maps.newConcurrentMap();
    }

    public void registerArenas() {
        for(String section : this.astroTournamentsPlugin.getConfig().getConfigurationSection("Tournament.Arenas").getKeys(false)) {
            Location playerLocationOne = Utils.getLocationFromString(this.astroTournamentsPlugin.getConfig().getString("Tournament.Arenas." + section + ".PlayerOneSpawnPoint"));
            Location playerLocationTwo = Utils.getLocationFromString(this.astroTournamentsPlugin.getConfig().getString("Tournament.Arenas." + section + ".PlayerTwoSpawnPoint"));

            try {
                Arena arena = new Arena(section, new Pair<>(playerLocationOne, playerLocationTwo), new Pair<>(null, null), 0, false);
                register(arena);
            } catch (NullPointerException exception) {
                exception.printStackTrace();
            }
        }
    }

    public void register(Arena arena) {
        try {
            this.arenas.put(arena.getId(), arena);
            this.astroTournamentsPlugin.getLogger().info("Successfully registered arena: " + arena.getId());
        } catch (Exception var3) {
            System.err.println("&8(&c!&8) Failed to register arena, " + arena.getId());
        }
    }
}
