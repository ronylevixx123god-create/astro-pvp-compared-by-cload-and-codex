package me.astropvp.astropvpskins.objects.skins.axes;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.MaskUtils;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.ifamasssxd.cosmicmasks.MaskType;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class GoldenAdmirer extends ItemSkin {
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if(!(damaged instanceof Player))
            return;
        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation())) return;
        if(damager.hasMetadata("admirer_disabled"))
            return;
        if (damager.hasMetadata("frozenSkrrt")) return;

        double chance = 0.5;
        if (Math.random() < chance) {
            if (isAdmirerSilenced(((Player) damaged).getPlayer()))
                return;
            if (MaskUtils.hasMaskEquipped((Player) damaged,"WITCHER")) {
                damaged.sendMessage(CC.translate("&d&l** WITCHER MASK &7Blocked &6&lGolden Admirer &d&l**"));
                return;
            }
            damaged.setMetadata("admirerSilence", (MetadataValue) new FixedMetadataValue((Plugin) AstroItemSkinsPlugin.getInstance(), System.currentTimeMillis() + 10000));
            damager.sendMessage(CC.translate(CC.GoldB + "** GOLDEN ADMIRER &7Disabled " + damaged.getName() + "'s Witch Hat & Cursed Wings " + CC.Gray + "[10s] " + CC.GoldB + "**"));
            damaged.sendMessage(CC.translate(CC.GoldB + "** GOLDEN ADMIRER &7Disabled Witch Hat & Cursed Wings " + CC.Gray + "[" + damager.getName() + "] " + CC.GoldB + "**"));
        }
    }

    public static boolean isAdmirerSilenced(Player player) {
        return (player.hasMetadata("admirerSilence") && player.getMetadata("admirerSilence").get(0).asLong() > System.currentTimeMillis());
    }
}
