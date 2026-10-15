package me.astropvp.astrospawners.xpboosters;

import com.cosmicpvp.cosmicutils.itembuilder.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BoosterItem {
    public static ItemStack getXPBooster(double multiplier, int duration) {
        return new ItemBuilder(Material.EMERALD)
                .setName(CC.translate("&b&lPersonal &nXP&b&l Booster"))
                .addLoreLine(CC.translate("&7Multiplies XP gained by you from"))
                .addLoreLine(CC.translate("&7killing mobs while active."))
                .addLoreLine("")
                .addLoreLine(CC.translate("&7Right-Click to activate."))
                .addLoreLine("")
                .addLoreLine(CC.translate("&b&lMULTIPLIER"))
                .addLoreLine(CC.translate("&7" + multiplier + "x"))
                .addLoreLine("")
                .addLoreLine(CC.translate("&b&lDURATION"))
                .addLoreLine(CC.translate("&7" + duration + "m"))
                .build();
    }

    public static ItemStack getDropBooster(double multiplier, int duration) {
        return new ItemBuilder(Material.EMERALD)
                .setName(CC.translate("&6&lPersonal &nDrops & Keys&6&l Booster"))
                .addLoreLine(CC.translate("&7Multiplies the chance of getting"))
                .addLoreLine(CC.translate("&7more and better keys from"))
                .addLoreLine(CC.translate("&7killing mobs while active."))
                .addLoreLine("")
                .addLoreLine(CC.translate("&7Right-Click to activate."))
                .addLoreLine("")
                .addLoreLine(CC.translate("&6&lMULTIPLIER"))
                .addLoreLine(CC.translate("&7" + multiplier + "x"))
                .addLoreLine("")
                .addLoreLine(CC.translate("&6&lDURATION"))
                .addLoreLine(CC.translate("&7" + duration + "m"))
                .build();
    }
}
