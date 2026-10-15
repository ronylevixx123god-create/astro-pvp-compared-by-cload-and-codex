package com.conaxgames.customskulls.data;

import com.conaxgames.customskulls.CoreSkulls;
import com.conaxgames.customskulls.ability.AbilityData;
import com.conaxgames.customskulls.blocks.CustomBlock;
import com.conaxgames.customskulls.enums.AbilityType;
import com.conaxgames.customskulls.util.CC;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;

public class DeleteCustomBlocks {
    public static void func(Location location, Player player, CustomBlock customBlock) {
        File file = new File(CoreSkulls.getInstance().getDataFolder().toString() + "/data/" + AbilityData.getUUID(location) + ".yml");

        // Delete the file as we don't need it anymore!
        if (file.exists()) {
            file.delete();
        }
        // Delete the data from memory as we don't need it anymore!

        AbilityData.getAbilityDataMap().remove(AbilityData.getUUID(location));

        if (player != null) {
            player.sendMessage(CC.translate("&c&l(!)&c You broke a " + customBlock.getName() + "&c Custom Block!"));
        }

    }
}
