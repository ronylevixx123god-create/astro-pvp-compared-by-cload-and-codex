package com.conaxgames.customskulls.data;

import com.conaxgames.customskulls.CoreSkulls;
import com.conaxgames.customskulls.ability.AbilityData;
import com.conaxgames.customskulls.enums.AbilityType;
import me.astropvp.cratekeys.enums.CrateType;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class SaveCustomBlock {
    public static void saveCustomBlock(Player player, AbilityType abilityType, Block block) {
        File file = new File(CoreSkulls.instance.getDataFolder().toString() + "/data/" + AbilityData.getUUID(block.getLocation()) + ".yml");

        if (!file.exists()) {
            return;
        }

        YamlConfiguration ymlFile = YamlConfiguration.loadConfiguration(file);

        ymlFile.set("owner", player.getUniqueId().toString());
        ymlFile.set("ability", abilityType.getName());
        ymlFile.set("location", block.getLocation());

        AbilityData data = AbilityData.getByUUID(AbilityData.getUUID(block.getLocation()));
        AbilityData mobGrinder = CoreSkulls.instance.getMobGrinderManager().get(data.getUuid());
        ymlFile.set("lastActivated", mobGrinder.getLastActivated());
        ymlFile.set("level", mobGrinder.getLevel());

        data.setOwner(player.getUniqueId());
        data.setUuid(AbilityData.getUUID(block.getLocation()));
        data.setLocation(block.getLocation());
        data.setAbilityType(abilityType);


        if (abilityType == AbilityType.BLAZE_STONE_TOTEM) {
            mobGrinder.setLastActivated(mobGrinder.getLastActivated());
            mobGrinder.setLevel(mobGrinder.getLevel());
            ymlFile.set("grinder", mobGrinder.getCrateKeys().get(CrateType.GRINDER));
            ymlFile.set("xp", mobGrinder.getXp());

            mobGrinder.setLastActivated(mobGrinder.getLastActivated());
            mobGrinder.setLevel(mobGrinder.getLevel());
            mobGrinder.getCrateKeys().put(CrateType.GRINDER, mobGrinder.getCrateKeys().get(CrateType.GRINDER));
            mobGrinder.setXp(mobGrinder.getXp());

        }

        try {
            ymlFile.save(file);
        }
        catch (IOException exception) {
            CoreSkulls.instance.info("Unable to save yml file information. Printing stack...");
            exception.printStackTrace();
        }
    }
}

