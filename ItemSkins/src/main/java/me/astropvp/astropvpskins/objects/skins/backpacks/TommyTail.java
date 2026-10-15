package me.astropvp.astropvpskins.objects.skins.backpacks;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import lombok.Getter;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.ifamasssxd.cosmicsky.inventorypets.struct.pets.InventoryBansheePet;
import net.astropvp.damagehandler.AstroDamageHandler;
import org.arkhamnetwork.enchantments.enchantments.swords.DoubleStrike;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.List;

public class TommyTail extends ItemSkin {
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
        if (Math.random() < 0.0035) {
            if (damaged instanceof Player) {
                InventoryBansheePet.startBansheeEffect(damager, (Player) damaged, 8);
            }
        }


    }
}



