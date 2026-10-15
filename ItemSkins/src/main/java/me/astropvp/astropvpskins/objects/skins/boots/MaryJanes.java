package me.astropvp.astropvpskins.objects.skins.boots;

import com.rit.sucy.EnchantmentAPI;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.ifamasssxd.cosmicmasks.MaskType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class MaryJanes extends ItemSkin {
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (damaged instanceof Player && damager != null) {
            if (isCritical(damager) && !MaskType.hasEquipped((Player) damaged, "KILLER_CLOWN")) {
                event.setDamage(event.getDamage() * 1.15);
            }
        }
    }

    public boolean isCritical(Player p) {
        return (p.getVelocity().getY() + 0.0784000015258789) <= 0;
    }

    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {

    }

    public void onEquip(Player player) {
        EnchantmentAPI.getEnchantment("Gears").applyEquipEffect(player, 4);
    }

    public void onUnequip(Player player) {
        EnchantmentAPI.getEnchantment("Gears").applyUnequipEffect(player, 4);
    }
}
