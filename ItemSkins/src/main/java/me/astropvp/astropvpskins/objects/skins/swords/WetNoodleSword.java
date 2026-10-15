package me.astropvp.astropvpskins.objects.skins.swords;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.ifamasssxd.cosmicduel.DuelAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.Map;

public class WetNoodleSword extends ItemSkin {
    public static Map<Player, Map<Player, Integer>> wetNoodleAbility;

    public WetNoodleSword() {
        wetNoodleAbility = new HashMap<>();
    }


    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (DuelAPI.isSpectator(damager)) return;
        if (damager.hasMetadata("frozenSkrrt")) return;

        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation()))
            return;

        if (!(damaged instanceof Player)) return;
        if (damager.getHealth() < 14) {
            event.setDamage(event.getDamage() * 1.33);
        }

        if (Math.random() < 0.025) {
            damager.setMetadata("wetNoodleAbility", new FixedMetadataValue(AstroItemSkinsPlugin.getInstance(), true));
            if (damager.hasMetadata("wetNoodleAbility")) {
                addNoodleStack(damager, (Player) damaged);
            }
            damager.sendMessage(CC.translate("&e&l*&f&l* &e&lWET NOODLE ABILITY &7[10s] &f&l*&e&l*"));
            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
                damager.removeMetadata("wetNoodleAbility", AstroItemSkinsPlugin.getInstance());
                damager.sendMessage(CC.translate("&e&l*&f&l* &e&lWET NOODLE ABILITY &cExpired &f&l*&e&l*"));
                if (wetNoodleAbility.containsKey(damager)) {
                    damager.sendMessage(CC.translate("&e&l* &eDamaging " + wetNoodleAbility.get(damager).size() + " enemies for &716s&e."));
                }
                for (Map.Entry<Player, Integer> entry : wetNoodleAbility.get(damager).entrySet()) {
                    Player affected = entry.getKey();
                    int stacks = entry.getValue();
                    double damage = 0.25 * stacks;
                    if (damage >= affected.getHealth() / 2) {
                        damage = affected.getHealth() / 2;
                    }
                    affected.damage(damage);
                    affected.sendMessage(CC.translate("&c&l-" + Math.round(damage) + " HP &7(" + stacks + " Wet Noodle Stacks)"));
                }
                wetNoodleAbility.remove(damager);
            }, 10 * 20);
        }


    }

    public void addNoodleStack(Player damager, Player damaged) {
        if (!wetNoodleAbility.containsKey(damager)) {
            Map<Player, Integer> newStacks = new HashMap<>();
            newStacks.put(damaged, 1);
            wetNoodleAbility.put(damager, newStacks);
            damaged.sendMessage(CC.translate("&e&l*&f&l* " + damager.getName() + "'s &e&lWET NOODLE ABILITY &7[&a1 Stack&7] &f&l*&e&l*"));
        } else {
            Map<Player, Integer> currentStacks = wetNoodleAbility.get(damager);
            if (!currentStacks.containsKey(damaged)) {
                currentStacks.put(damaged, 1);
                damaged.sendMessage(CC.translate("&e&l*&f&l* " + damager.getName() + "'s &e&lWET NOODLE ABILITY &7[&a1 Stack&7] &f&l*&e&l*"));
            } else {
                int currentAmount = currentStacks.get(damaged);
                currentStacks.put(damaged, currentAmount + 1);
                int display = currentStacks.get(damaged);
                damaged.sendMessage(CC.translate("&e&l*&f&l* " + damager.getName() + "'s &e&lWET NOODLE ABILITY &7[&a" + display + " Stack&7] &f&l*&e&l*"));
            }
            wetNoodleAbility.put(damager, currentStacks);
        }
    }


    public static boolean isWetNoodled(Player player) {

        for (Map<Player, Integer> map : wetNoodleAbility.values()) {
            if (map.containsKey(player)) {
                return true;
            }
        }
        return false;
    }
}
