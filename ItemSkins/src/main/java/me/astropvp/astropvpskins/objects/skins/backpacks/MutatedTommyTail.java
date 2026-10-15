package me.astropvp.astropvpskins.objects.skins.backpacks;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import lombok.Getter;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.ifamasssxd.cosmicsky.inventorypets.struct.pets.InventoryBansheePet;
import net.astropvp.damagehandler.AstroDamageHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.List;

public class MutatedTommyTail extends ItemSkin {
    @Getter
    private final List<String> chainSawedPlayers = new ArrayList<>();

    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation()))
            return;
        if (damager.getItemInHand() != null && damager.getItemInHand().getType().name().endsWith("_SWORD")) {
            event.setDamage(event.getDamage()*1.05);
        }
        if (Math.random() < 0.004) {
            if (damaged instanceof Player) {
                InventoryBansheePet.startBansheeEffect(damager, (Player) damaged,8);
                damaged.sendMessage(CC.translate("&2&l* " + damager.getName() + "'s TOMMY TAIL &7Banshee Effect &2&l*"));
            }
        }

        if (damager.getHealth() <= damager.getMaxHealth()*0.6) {
            event.setDamage(event.getDamage()*1.1);
            damager.sendMessage(CC.translate("&2&l* TOMMY TAIL [&7+10% DMG&2&l] *"));
        }
    }
}



