package com.conaxgames.customskulls.data;

import com.conaxgames.customskulls.CoreSkulls;
import com.conaxgames.customskulls.ability.AbilityData;
import com.conaxgames.customskulls.blocks.impl.MobGrinder;
import com.conaxgames.customskulls.enums.AbilityType;
import com.conaxgames.customskulls.struct.MobGrinderManager;
import me.astropvp.cratekeys.enums.CrateType;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.UUID;

public class LoadCustomBlocks {

    public static void load() {

        // Looping through all the files in a directory.
        File directory = new File(CoreSkulls.instance.getDataFolder() + File.separator + "data");
        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        for (File file : files) {
            if (file == null) { // Null check because those pesky files!
                return;
            }
            // Load the file into YAML because we need to get some values from it!
            YamlConfiguration ymlFile = YamlConfiguration.loadConfiguration(file);

            String owner = ymlFile.getString("owner");
            String ability = ymlFile.getString("ability");
            Location location = (Location) ymlFile.get("location");
            AbilityType abilityType = AbilityType.valueOf(ability);
            long lastActivated = ymlFile.getLong("lastActivated");
            int level = ymlFile.getInt("level");

            // This CustomBlock doesn't exist? Oh noes, we better delete it! It's always nice to keep things clean.
            if (CoreSkulls.instance.getServer().getWorld(location.getWorld().getName()).getBlockAt(location).getType() != abilityType.getMaterial()) {
                CoreSkulls.instance.info("[CustomSkulls] The block at a location was not the material. We can safely say it's not a CustomBlock.");
                CoreSkulls.instance.info(CoreSkulls.instance.getServer().getWorld(location.getWorld().getName()).getBlockAt(location).getType().toString());
                CoreSkulls.instance.info(abilityType.getMaterial().toString());
                file.delete();
                continue;
            }

            AbilityData data = AbilityData.getByUUID(AbilityData.getUUID(location));
            data.setOwner(UUID.fromString(owner));
            data.setUuid(AbilityData.getUUID(location));
            data.setLocation(location);
            data.setAbilityType(AbilityType.valueOf(ability));

            if (data.getAbilityType() == AbilityType.BLAZE_STONE_TOTEM) {
                AbilityData mobGrinder = CoreSkulls.instance.getMobGrinderManager().get(data.getUuid());
                mobGrinder.setLastActivated(lastActivated);
                mobGrinder.setLevel(level);

                int godKeys = ymlFile.getInt("grinder");
                double xp =  ymlFile.getInt("xp");
                mobGrinder.getCrateKeys().put(CrateType.GRINDER, godKeys);
                mobGrinder.setXp(xp);

                if (mobGrinder.isActive()) {
                    CoreSkulls.instance.getMobGrinderManager().getActiveMobGrinders().put(data.getUuid(), mobGrinder);
                }
            }


            // This is a debug message which can be deleted at a later date!
        }
    }

}
