package net.astropvp.core.utils;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.core.AstroCore;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class WhitelistUtil {

    public static File configFile;
    public static YamlConfiguration config;

    // action passed needs to be "add" or "remove"
    public static void whitelist(String action) {

        configFile = new File(AstroCore.getInstance().getDataFolder(), "whitelist.yml");
        if (!configFile.exists()) {
            return;
        }

        config = YamlConfiguration.loadConfiguration(configFile);

        // WHITELIST:
        //  - "BOWP"
        //  - "Kipes"
        //  - "tempay"
        //  - "horseass"

        List<String> names = config.getStringList("WHITELIST");
        for (String name : names) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "whitelist " + action + " " + name);
            Bukkit.broadcastMessage(CC.Gray + "(Whitelist) "+ CC.Yellow + "Automatically " + action + "ed " + name + ". ");
        }
    }
}
