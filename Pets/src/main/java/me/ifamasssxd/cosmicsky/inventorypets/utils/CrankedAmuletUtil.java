package me.ifamasssxd.cosmicsky.inventorypets.utils;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.ifamasssxd.cosmicsky.inventorypets.CosmicSkyInventoryPets;
import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class CrankedAmuletUtil {
    public static void tryHatchAbility(Player playerHit, Player petter) {
        if (playerHit.getHealth() > 8) return;
        if (playerHit.hasMetadata("hatchAbilityCD")) return;
        if (petter.hasMetadata("hatched")) return;
        if (playerHit.getInventory().getChestplate() != null && (AstroItemSkinsAPI.hasSkinEquipped(playerHit, SkinType.CHRISTMAS_2021_MUTATED_AMULET) || AstroItemSkinsAPI.hasSkinEquipped(playerHit, SkinType.CHRISTMAS_2021_AMULET))) {
            playerHit.setMetadata("hatchAbilityCD", new FixedMetadataValue(CosmicSkyInventoryPets.get(), true));
            petter.setMetadata("hatched", new FixedMetadataValue(CosmicSkyInventoryPets.get(), true));

            petter.playSound(petter.getLocation(), Sound.EXPLODE, 1.0f, 0.54f);
            petter.setVelocity(new Vector(0, petter.getWorld().getName().equals("world_duels") || petter.getWorld().getName().equals("world_duels2") ? 2 + 3 : 4 + 3 * 2, 0));
            petter.sendMessage(CC.translate("&4&l* " + playerHit.getName() + "'s HATCH ABILITY LAUNCH *"));
            playerHit.sendMessage(CC.translate("&4&l* HATCH ABILITY [&7" + petter.getName() + "&4&l] *"));
            Bukkit.getScheduler().runTaskLaterAsynchronously(CosmicSkyInventoryPets.get(), () -> {
                playerHit.removeMetadata("hatchAbilityCD", CosmicSkyInventoryPets.get());
            }, 120 * 20);
            Bukkit.getScheduler().runTaskLaterAsynchronously(CosmicSkyInventoryPets.get(), () -> {
                petter.removeMetadata("hatched", CosmicSkyInventoryPets.get());
            }, 10 * 20);
        }
    }
}
