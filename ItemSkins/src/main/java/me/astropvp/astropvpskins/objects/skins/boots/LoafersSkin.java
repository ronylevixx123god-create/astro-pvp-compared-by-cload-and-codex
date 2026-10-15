package me.astropvp.astropvpskins.objects.skins.boots;

import com.rit.sucy.EnchantmentAPI;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.objects.ItemSkin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;

public class LoafersSkin extends ItemSkin {
    @Override
    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {
        if(!(damager instanceof Player))
            return;
        if(Math.random() < 0.025) {
            List<Entity> nearby = damaged.getNearbyEntities(6, 6, 6);
            for(Entity e : nearby) {
                if (!(e instanceof Player))
                    continue;
                if (!e.hasMetadata("petSilenced")) {
                    e.setMetadata("petSilenced", new FixedMetadataValue(AstroItemSkinsPlugin.getInstance(), true));
                    Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> damager.removeMetadata("petSilenced", AstroItemSkinsPlugin.getInstance()), 200L);
                }
            }
        }
    }

    public void onEquip(Player player) {
        EnchantmentAPI.getEnchantment("Gears").applyEquipEffect(player, 4);
    }

    public void onUnequip(Player player) {
        EnchantmentAPI.getEnchantment("Gears").applyUnequipEffect(player, 4);
    }
}
