package me.astropvp.astropvpskins.objects.skins.belts;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import me.astropvp.astropvpskins.objects.ItemSkin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class MatrixMasterSkin extends ItemSkin {

    @Override
    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {
        if (damager instanceof Player) {
            event.setDamage(event.getDamage() * 0.85);
            if (Math.random() < 0.1) {
                event.setDamage(event.getDamage() * 0.7);
                damaged.sendMessage(CC.translate("&e&l* MATRIX MASTER BELT &7[Blocked Soul Damage Multiplier] &e&l*"));
            }
        }
    }

    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {

    }
}