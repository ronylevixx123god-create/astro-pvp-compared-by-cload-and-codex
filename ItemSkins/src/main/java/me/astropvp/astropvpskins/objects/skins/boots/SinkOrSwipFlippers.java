package me.astropvp.astropvpskins.objects.skins.boots;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.rit.sucy.EnchantmentAPI;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.objects.ItemSkin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SinkOrSwipFlippers extends ItemSkin {

    public static List<Player> insanityDevourSilenced;
    public static Map<Player, Integer> sinkCounter;
    public static List<Player> sinked;


    public SinkOrSwipFlippers() {
        insanityDevourSilenced = new ArrayList<>();
        sinkCounter = new HashMap<>();
        sinked = new ArrayList<>();
    }

    @Override
    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {
        if(!(damaged instanceof Player))
            return;
        if (event.isCancelled()) return;

    }

    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if(!(damaged instanceof Player))
            return;
        if (event.isCancelled()) return;

        if (sinkCounter.containsKey(damager)) {
            if (sinkCounter.get(damager) >= 7) {
                sinked.add((Player) damaged);
                sinkCounter.put(damager, 0);
                damaged.sendMessage(CC.translate("&a&l** " + damager.getName() + "'s &cSink Effect &7[15s] &a&l**"));
                damager.sendMessage(CC.translate("&a&l** &cSink Effect &7[" + damaged.getName() + "&7] &a&l**"));
                Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
                    sinked.remove(damaged);
                }, 15*20);
            }
        }

        if (damager.getItemInHand() != null) {
            if (damager.getItemInHand().getType().name().endsWith("_SWORD")) {

                if (isPlayerBehind(damager, damaged)) {
                    event.setDamage(event.getDamage() * 1.25);
                    damager.sendMessage(CC.translate("&c&lx1.25 DMG &7(Sink Or Swim Flippers)"));
                }
                if (Math.random() < 0.06) {
                    if (!insanityDevourSilenced.contains(damaged)) {
                        insanityDevourSilenced.add((Player) damaged);
                        damaged.sendMessage(CC.translate("&a&l* " + damager.getName() + "'s Sink or Swim Flippers &7[(Extreme) Insanity & (Vampiric) Devour Silenced - 4s] &a&l*"));
                        Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
                            insanityDevourSilenced.remove(damaged);
                        }, 4*20);
                    }
                }
            }
        }



    }

    public static void addSinkStack(Player damager) {
        if (sinkCounter.containsKey(damager)) {
            int amount = sinkCounter.get(damager);
            sinkCounter.put(damager, amount+1);
        } else {
            sinkCounter.put(damager, 1);
        }
    }

    public void onEquip(Player player) {
        EnchantmentAPI.getEnchantment("Gears").applyEquipEffect(player, 4);
    }

    public void onUnequip(Player player) {
        EnchantmentAPI.getEnchantment("Gears").applyUnequipEffect(player, 4);
    }

    public boolean isPlayerBehind(final Player player, final Entity theEntity) {
        Location playerLoc = player.getLocation();
        Location targetLoc = theEntity.getLocation();
        double pvecy = -Math.sin(Math.toRadians(playerLoc.getPitch()));
        double pvecx = -Math.cos(Math.toRadians(playerLoc.getPitch())) * Math.sin(Math.toRadians(playerLoc.getYaw()));
        double pvecz = Math.cos(Math.toRadians(playerLoc.getPitch())) * Math.cos(Math.toRadians(playerLoc.getYaw()));
        double tvecy = -Math.sin(Math.toRadians(targetLoc.getPitch()));
        double tvecx = -Math.cos(Math.toRadians(targetLoc.getPitch())) * Math.sin(Math.toRadians(targetLoc.getYaw()));
        double tvecz = Math.cos(Math.toRadians(targetLoc.getPitch())) * Math.cos(Math.toRadians(targetLoc.getYaw()));
        double dot = tvecx * pvecx + tvecy * pvecy + tvecz * pvecz;
        return dot > 0.6;
    }
}
