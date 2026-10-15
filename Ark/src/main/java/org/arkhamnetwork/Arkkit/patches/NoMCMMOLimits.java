package org.arkhamnetwork.Arkkit.patches;

import com.gmail.nossr50.mcMMO;
import java.util.ArrayList;
import java.util.Arrays;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class NoMCMMOLimits
        extends ArkPlugin {
    public NoMCMMOLimits(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        if (Bukkit.getPluginManager().isPluginEnabled("mcMMO")) {
            mcMMO mcmmo = (mcMMO)Bukkit.getPluginManager().getPlugin("mcMMO");
            String maxSecondsConfigPath = "Abilities.Max_Seconds.";
            boolean update = false;
            if (mcmmo.getConfig().getInt("Abilities.Max_Seconds.Giga_Drill_Breaker") != 32) {
                Bukkit.getLogger().info("[Arkkit (NoMCMMOLimits)] Setting Max_Seconds values and reloading mcMMO config.yml");
                mcmmo.getConfig().set("Abilities.Max_Seconds.Giga_Drill_Breaker", (Object)32);
                mcmmo.getConfig().set("Abilities.Max_Seconds.Green_Terra", (Object)32);
                mcmmo.getConfig().set("Abilities.Max_Seconds.Serrated_Strikes", (Object)27);
                mcmmo.getConfig().set("Abilities.Max_Seconds.Skull_Splitter", (Object)27);
                mcmmo.getConfig().set("Abilities.Max_Seconds.Super_Breaker", (Object)32);
                mcmmo.getConfig().set("Abilities.Max_Seconds.Tree_Feller", (Object)32);
                update = true;
            }
            ArrayList<String> configSkills = new ArrayList<String>(Arrays.asList("Acrobatics", "Alchemy", "Archery", "Axes", "Excavation", "Fishing", "Herbalism", "Mining", "Repair", "Salvage", "Smelting", "Swords", "Taming", "Unarmed", "Woodcutting"));
            for (String skill : configSkills) {
                if (mcmmo.getConfig().getInt("Skills." + skill + ".Level_Cap") == 0) continue;
                Bukkit.getLogger().info("[Arkkit (NoMCMMOLimits)] Set Skills." + skill + ".Level_Cap => 0");
                mcmmo.getConfig().set("Skills." + skill + ".Level_Cap", (Object)0);
                update = true;
            }
            if (update) {
                Bukkit.getLogger().info("[Arkkit (NoMCMMOLimits)] Saving + Reloading mcMMO config.yml!");
                mcmmo.saveConfig();
                mcmmo.reloadConfig();
            }
        }
    }

    @Override
    public void disable() {
    }
}
