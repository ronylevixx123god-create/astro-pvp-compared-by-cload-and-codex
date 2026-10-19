package com.conaxgames.customskulls.data;

import com.conaxgames.customskulls.CoreSkulls;
import com.conaxgames.customskulls.ability.AbilityData;
import com.conaxgames.customskulls.blocks.impl.MobGrinder;
import com.conaxgames.customskulls.enums.AbilityType;
import me.astropvp.cratekeys.enums.CrateType;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Map;

public class SaveAllGrinderCustomBlocks {
    public static void saveAllGrinderCustomBlocks() {
        CoreSkulls.setSavingGrinderBlocks(true);
        CoreSkulls.instance.info("Started Saving customblocks");
        try {
            final Map<String, AbilityData> grinderBlocks = AbilityData.getAbilityDataMap();
            for (Map.Entry<String, AbilityData> entry : grinderBlocks.entrySet()) {
                AbilityData data = entry.getValue();
                if (data.getAbilityType() == AbilityType.BLAZE_STONE_TOTEM) {

                    File file = new File(CoreSkulls.instance.getDataFolder().toString() + "/data/" + AbilityData.getUUID(data.getLocation()) + ".yml");

                    if (!file.exists()) {
                        continue;
                    }

                    YamlConfiguration ymlFile = YamlConfiguration.loadConfiguration(file);

                    AbilityData mobGrinder = CoreSkulls.instance.getMobGrinderManager().get(data.getUuid());

                    ymlFile.set("lastActivated", mobGrinder.getLastActivated());
                    ymlFile.set("level", mobGrinder.getLevel());

                    mobGrinder.setLastActivated(mobGrinder.getLastActivated());

                        // The single-block writer and loader both use SOLAR for the legacy "grinder" key.
                        ymlFile.set("grinder", mobGrinder.getCrateKeys().get(CrateType.SOLAR));
                        ymlFile.set("xp", mobGrinder.getXp());
                        mobGrinder.setLevel(mobGrinder.getLevel());
                        mobGrinder.setLastActivated(mobGrinder.getLastActivated());
                        mobGrinder.getCrateKeys().put(CrateType.SOLAR, mobGrinder.getCrateKeys().get(CrateType.SOLAR));
                        mobGrinder.setXp(mobGrinder.getXp());



                    try {
                        ymlFile.save(file);
                    } catch (Exception exception) {
                        CoreSkulls.instance.info("Unable to save yml file information. Printing stack...");
                        exception.printStackTrace();
                    }
                }
            }
            CoreSkulls.setSavingGrinderBlocks(false);
            CoreSkulls.instance.info("Finished saving customblocks");
        } catch (ConcurrentModificationException e) {
            CoreSkulls.setSavingGrinderBlocks(false);
            e.printStackTrace();
        } finally {
            CoreSkulls.setSavingGrinderBlocks(false);
        }

    }

}
