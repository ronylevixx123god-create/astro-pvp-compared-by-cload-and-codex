package me.astropvp.astropvpskins.objects.skins.boots;

import com.rit.sucy.EnchantmentAPI;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.ifamasssxd.cosmicmasks.MaskType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ValentinesBoots extends ItemSkin {
    public void onEquip(Player player) {
        EnchantmentAPI.getEnchantment("Gears").applyEquipEffect(player, 4);
    }

    public void onUnequip(Player player) {
        EnchantmentAPI.getEnchantment("Gears").applyUnequipEffect(player, 4);
    }

    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (isCritical(damager) && !MaskType.hasEquipped((Player) damaged, "KILLER_CLOWN")) {
            event.setDamage(event.getDamage() * 1.1);
        }
    }
    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {
        if (!(damager instanceof Player)) return;
        /*
        if (Math.random() < 0.1) {
            damaged.setMetadata("no_knockback", new FixedMetadataValue(CosmicSkyInventoryPets.get(), true));
            Bukkit.getScheduler().runTaskLaterAsynchronously(CosmicSkyInventoryPets.get(), ()-> {
                damaged.removeMetadata("no_knockback", CosmicSkyInventoryPets.get());
            },20L);
        }

         */

    }

    public boolean isCritical(Player p) {
        return (p.getVelocity().getY() + 0.0784000015258789) <= 0;
    }
}
