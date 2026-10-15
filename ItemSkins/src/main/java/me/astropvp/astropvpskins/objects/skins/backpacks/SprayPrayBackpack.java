package me.astropvp.astropvpskins.objects.skins.backpacks;

import me.astropvp.astropvpskins.objects.ItemSkin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SprayPrayBackpack extends ItemSkin {


    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if(!(damaged instanceof Player)) return;

        ItemStack wieldedItem = ((Player) damaged).getInventory().getItemInHand();
        if(!wieldedItem.getType().name().endsWith("_AXE")) return;

        event.setDamage(event.getDamage() * 1.05);
    }

    @Override
    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {
    }

    public static List<Player> sprayed;
    public List<Player> sprayer;


    public SprayPrayBackpack() {
        sprayed = new ArrayList<>();
        sprayer = new ArrayList<>();
    }
    /*

    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation()))
            return;
        if (!(damaged instanceof Player)) return;
        if (sprayed.contains(damaged)) {
            event.setDamage(event.getDamage() * 1.06);
        }
        if (Math.random() < 0.02) {
            damager.sendMessage(CC.translate("&a&l*&e&l* &a&lSPRAY AND PRAY ABILITY &7[10s - " + damaged.getName() + "] &e&l*&a&l*"));
            damaged.sendMessage(CC.translate("&a&l*&e&l* &a&l" + damager.getName() + "'s SPRAY AND PRAY ABILITY &7[10s] &e&l*&a&l*"));
            sprayed.add((Player) damaged);
            sprayer.add(damager);
            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
                sprayed.remove((damaged));
                sprayer.remove(damager);
            }, 10*20);

        }
    }

    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {
        if (!WorldGuardUtils.isPvPEnabled(damager.getLocation())) return;
        if (event.isCancelled()) return;


        if (sprayer.contains(damaged)) {
            event.setDamage(event.getDamage() * 0.94);
        }
    }


     */
}
