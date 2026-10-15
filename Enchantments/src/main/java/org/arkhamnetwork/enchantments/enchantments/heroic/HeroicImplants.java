/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashMultimap
 *  com.google.common.collect.Multimap
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.FoodLevelChangeEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package org.arkhamnetwork.enchantments.enchantments.heroic;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class HeroicImplants
extends HeroicCustomEnchantment {
    private Set<UUID> implantedPlayers = new HashSet<UUID>();
    private static HashMap<Integer, BukkitTask> heroicImplantTasks = new HashMap();
    private Set<UUID> healthTickedPlayers = new HashSet<UUID>();
    private Multimap<Integer, Player> heroicImplantPlayers = HashMultimap.create();

    public HeroicImplants() {
        super("Alien Implants", "Implants", GeneralUtil.helmets, 2);
        this.max = 3;
        Bukkit.getPluginManager().registerEvents((Listener)new HeroicImplantsListener(), (Plugin)Enchantments.getInstance());
        final int healthIncrease = 1;
        for (int i = 1; i <= this.max; ++i) {
            final int finalI = i;
            BukkitTask existing = heroicImplantTasks.get(i);
            if (existing != null) {
                existing.cancel();
            }
            BukkitTask task = new BukkitRunnable(){

                public void run() {
                    Collection<Player> leveledPlayers = HeroicImplants.this.heroicImplantPlayers.get(finalI);
                    if (leveledPlayers == null || leveledPlayers.isEmpty()) {
                        return;
                    }
                    leveledPlayers.forEach(player -> {
                        if (HeroicImplants.this.healthTickedPlayers.remove(player.getUniqueId())) {
                            if (player.getHealth() + (double)healthIncrease < player.getMaxHealth() && player.getHealth() > 0.0 && !player.isDead()) {
                                player.setHealth(player.getHealth() + (double)healthIncrease);
                            }
                        } else {
                            HeroicImplants.this.healthTickedPlayers.add(player.getUniqueId());
                        }
                        if (player.getFoodLevel() < 20 && player.getHealth() > 0.0 && !player.isDead()) {
                            player.setFoodLevel(player.getFoodLevel() + 1);
                        }
                    });
                }
            }.runTaskTimer((Plugin)Enchantments.getInstance(), (long)(40 * (3 / i)), (long)(40 * (3 / i)));
            heroicImplantTasks.put(i, task);
        }
    }

    public void applyEquipEffect(Player player, int level) {
        player.setMetadata(this.name() + "_enchant", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)level));
        this.heroicImplantPlayers.put(level, player);
        this.implantedPlayers.add(player.getUniqueId());
    }

    public void applyUnequipEffect(Player player, int level) {
        this.implantedPlayers.remove(player.getUniqueId());
        this.healthTickedPlayers.remove(player.getUniqueId());
        this.heroicImplantPlayers.remove((Object)level, (Object)player);
        player.removeMetadata(this.name() + "_enchant", (Plugin)Enchantments.getInstance());
    }

    class HeroicImplantsListener
    implements Listener {
        HeroicImplantsListener() {
        }

        @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=true)
        public void onFoodLoss(FoodLevelChangeEvent event) {
            Player player;
            if (event.getEntity() instanceof Player && (player = (Player)event.getEntity()).getFoodLevel() > event.getFoodLevel() && HeroicImplants.this.implantedPlayers.contains(player.getUniqueId())) {
            }
        }
    }
}
