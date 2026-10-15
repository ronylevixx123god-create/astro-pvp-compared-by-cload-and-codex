package me.ifamasssxd.cosmicsky.inventorypets.utils;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.google.common.collect.Lists;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.ifamasssxd.cosmicsky.inventorypets.CosmicSkyInventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPets;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class PetUtils {
    public static void addPetCooldown(Player player, InventoryPets pet, long cooldown) {
        if (AstroItemSkinsAPI.hasSkinEquipped(player, SkinType.SPRING_2020_SWORD) && Math.random() < 0.1D) {
            player.sendMessage(String.valueOf(CC.RedB) + "[BYPASS COOLDOWN] " + CC.Red + "The Popsicle Sword Skin bypassed your pet cooldown!");
            return;
        }
        List<String> cooldowns = Lists.newArrayList();
        if (player.hasMetadata("pet_cooldowns"))
            cooldowns = (List<String>)((MetadataValue)player.getMetadata("pet_cooldowns").get(0)).value();
        for (int i = 0; i < cooldowns.size(); i++) {
            if (((String)cooldowns.get(i)).startsWith(pet.name())) {
                cooldowns.remove(i);
                break;
            }
        }
        cooldowns.add(String.valueOf(pet.name()) + ":" + (System.currentTimeMillis() + cooldown));
        player.setMetadata("pet_cooldowns", (MetadataValue)new FixedMetadataValue((Plugin) CosmicSkyInventoryPets.get(), cooldowns));
    }

    public static int getSecondsOnCooldown(Player player, InventoryPets pet) {
        if (!isOnCooldown(player, pet))
            return -1;
        List<String> cooldowns = (List<String>)((MetadataValue)player.getMetadata("pet_cooldowns").get(0)).value();
        for (int i = 0; i < cooldowns.size(); i++) {
            if (((String)cooldowns.get(i)).startsWith(pet.name())) {
                long time = Long.valueOf(((String)cooldowns.get(i)).split(":")[1]).longValue();
                return (int)(time - System.currentTimeMillis()) / 1000;
            }
        }
        return -1;
    }

    public static boolean isOnCooldown(Player player, InventoryPets pet) {
        List<String> cooldowns = Lists.newArrayList();
        if (player.hasMetadata("pet_cooldowns"))
            cooldowns = (List<String>)((MetadataValue)player.getMetadata("pet_cooldowns").get(0)).value();
        for (int i = 0; i < cooldowns.size(); i++) {
            if (((String)cooldowns.get(i)).startsWith(pet.name())) {
                long time = Long.valueOf(((String)cooldowns.get(i)).split(":")[1]).longValue();
                if (time - System.currentTimeMillis() > 0L)
                    return true;
                return false;
            }
        }
        return false;
    }
}
