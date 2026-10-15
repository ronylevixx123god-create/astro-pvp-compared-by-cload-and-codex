/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashMultimap
 *  com.google.common.collect.Multimap
 *  com.rit.sucy.CustomEnchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class Implants
extends CustomEnchantment {
    private static HashMap<Integer, BukkitTask> implantTasks = new HashMap();
    private Set<UUID> healthTickedPlayers = new HashSet<UUID>();
    private Multimap<Integer, Player> implantedPlayers = HashMultimap.create();

    public Implants() {
        super("Implants", GeneralUtil.helmets, 2);
        this.max = 3;
        this.base = 10.0;
        this.interval = 5.0;
        for (int i = 1; i <= this.max; ++i) {
            final int finalI = i;
            BukkitTask existing = implantTasks.get(i);
            if (existing != null) {
                existing.cancel();
            }
            BukkitTask task = new BukkitRunnable(){

                public void run() {
                    Collection<Player> leveledPlayers = Implants.this.implantedPlayers.get(finalI);
                    if (leveledPlayers == null || leveledPlayers.isEmpty()) {
                        return;
                    }
                    leveledPlayers.forEach(player -> {
                        if (Implants.this.healthTickedPlayers.remove(player.getUniqueId())) {
                            if (player.getHealth() + 1.0 < player.getMaxHealth() && player.getHealth() > 0.0 && !player.isDead()) {
                                player.setHealth(player.getHealth() + 1.0);
                            }
                        } else {
                            Implants.this.healthTickedPlayers.add(player.getUniqueId());
                        }
                        if (player.getFoodLevel() < 20 && player.getHealth() > 0.0 && !player.isDead()) {
                            player.setFoodLevel(player.getFoodLevel() + 1);
                        }
                    });
                }
            }.runTaskTimer((Plugin)Enchantments.getInstance(), (long)(40 * (3 / i)), (long)(40 * (3 / i)));
            implantTasks.put(i, task);
        }
    }

    public void applyEquipEffect(Player player, int level) {
        player.setMetadata(this.name() + "_enchant", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)level));
        this.implantedPlayers.put(level, player);
    }

    public void applyUnequipEffect(Player player, int level) {
        player.removeMetadata(this.name() + "_enchant", (Plugin)Enchantments.getInstance());
        this.implantedPlayers.remove((Object)level, (Object)player);
        this.healthTickedPlayers.remove(player.getUniqueId());
    }
}

