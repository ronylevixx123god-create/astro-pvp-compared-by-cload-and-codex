package com.conaxgames.customskulls.data;

import com.conaxgames.customskulls.CoreSkulls;
import com.conaxgames.customskulls.ability.AbilityData;
import com.conaxgames.customskulls.blocks.CustomBlock;
import com.conaxgames.customskulls.blocks.impl.MobGrinder;
import com.conaxgames.customskulls.enums.AbilityType;
import com.conaxgames.customskulls.util.CC;
import me.astropvp.cratekeys.enums.CrateType;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class MakeCustomBlocks {

    public static void newFileItem(Player player, AbilityType abilityType, Block block, CustomBlock customBlock) {
        File file = new File(CoreSkulls.getInstance().getDataFolder().toString() + "/data/" + AbilityData.getUUID(block.getLocation()) + ".yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                CoreSkulls.getInstance().info("Unable to create yml file. Printing stack...");
                exception.printStackTrace();
            }
        }

        YamlConfiguration ymlFile = YamlConfiguration.loadConfiguration(file);


        ymlFile.set("owner", player.getUniqueId().toString());
        ymlFile.set("ability", abilityType.getName());
        ymlFile.set("location", block.getLocation());

        AbilityData data = AbilityData.getByUUID(AbilityData.getUUID(block.getLocation()));

        if (abilityType == AbilityType.BLAZE_STONE_TOTEM) {
            AbilityData mobGrinder = CoreSkulls.instance.getMobGrinderManager().get(data.getUuid());

            ymlFile.set("lastActivated", mobGrinder.getLastActivated());
            ymlFile.set("level", mobGrinder.getLevel());

            ymlFile.set("grinder", 0);
            ymlFile.set("xp", 0);
            mobGrinder.setLastActivated(mobGrinder.getLastActivated());
            mobGrinder.setLevel(mobGrinder.getLevel());
            mobGrinder.getCrateKeys().put(CrateType.SOLAR, 0);
            mobGrinder.setXp(0);

        }

        data.setOwner(player.getUniqueId());
        data.setUuid(AbilityData.getUUID(block.getLocation()));
        data.setLocation(block.getLocation());
        data.setAbilityType(abilityType);


        try {
            ymlFile.save(file);
        } catch (IOException exception) {
            CoreSkulls.getInstance().info("Unable to save yml file information. Printing stack...");
            exception.printStackTrace();
        }
        player.sendMessage("");
        player.sendMessage(CC.translate("&a&l(!)&a You placed a " + customBlock.getName() + "&a Custom Block!"));
        player.sendMessage(CC.GRAY + "Stand in the same chunk as the custom block to gain its respective effects.");
        player.sendMessage("");
    }
}
