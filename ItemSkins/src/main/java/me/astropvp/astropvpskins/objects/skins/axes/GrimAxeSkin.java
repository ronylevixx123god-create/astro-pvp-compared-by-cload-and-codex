package me.astropvp.astropvpskins.objects.skins.axes;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.objects.ItemSkin;
import org.arkhamnetwork.enchantments.mastery.OpeningCombo;
import org.arkhamnetwork.enchantments.mastery.SoulMaster;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class GrimAxeSkin extends ItemSkin {

    @Override
    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {

    }

    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (!(damaged instanceof Player)) return;
        (new OpeningCombo()).applyEffect((LivingEntity) damager, (LivingEntity) damaged, 5, event);
        (new SoulMaster()).applyEffect((LivingEntity) damager, (LivingEntity) damaged, 6, event);
        event.setDamage(event.getDamage()*1.15);

        if (Math.random() < 0.05) {
            damaged.setMetadata("noHarvester", new FixedMetadataValue(AstroItemSkinsPlugin.getInstance(), true));
            damaged.sendMessage(CC.translate("&4&l* " + damager.getName() + "'s GRIM AXE &7[3s No Soul Harvester Skin] &4&l*"));
            damager.sendMessage(CC.translate("&4&l* GRIM AXE &7[Disabled " + damaged.getName() + "'s Soul Harvester] &4&l*"));

            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
                damaged.removeMetadata("noHarvester", AstroItemSkinsPlugin.getInstance());

            }, 3 * 20L);
        }
    }
}