package me.astropvp.astropvpskins.objects.skins.helmets;

import me.astropvp.astropvpskins.objects.ItemSkin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class MutatedUndertowHelmet extends ItemSkin {

    public MutatedUndertowHelmet() {
    }

    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (damaged instanceof Player) {
            double addition = 0.1;
            if(damager.getLocation().getBlock().isLiquid()) {
                addition += 0.1;
            }
            event.setDamage(event.getDamage() * (1 + addition));
        }
    }

    /*
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation())) return;
        if (damager.hasMetadata("frozenSkrrt")) return;
        if (damaged.hasMetadata("frozenSkrrt")) return;

        if (damaged instanceof Player) {
            if (UndertowFieldTask.affected.contains(damaged)) {
                event.setDamage(event.getDamage() * 1.05);
            }
        }
    }

    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation())) return;
        if (damager.hasMetadata("frozenSkrrt")) return;
        if (damaged.hasMetadata("frozenSkrrt")) return;

        if (Math.random() < 0.015) {
            UndertowHelmet.undertowField.add(damaged);
            damaged.sendMessage(CC.translate("&a&l*&f&l* &a&lUNDERTOW FIELD &7[15s] &f&l*&a&l*"));
            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
                UndertowHelmet.undertowField.remove(damaged);
                damaged.sendMessage(CC.translate("&a&l*&f&l* &a&lUNDERTOW FIELD &cExpired &f&l*&a&l*"));
            },15*20);
        }
    }

     */
}
