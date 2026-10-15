package net.astropvp.astroevents.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.astropvp.astroevents.utils.configuration.ConfigManager;

@AllArgsConstructor
@Getter
public enum MiniGameType {

    PARKOUR(ConfigManager.ConfigFile.PARKOUR),
    SUMO(ConfigManager.ConfigFile.SUMO),
    SPLEEF(ConfigManager.ConfigFile.SPLEEF)
    ;

    private final ConfigManager.ConfigFile configFile;
}