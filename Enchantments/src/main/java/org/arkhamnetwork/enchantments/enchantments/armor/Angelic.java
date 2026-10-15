package org.arkhamnetwork.enchantments.enchantments.armor;

import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Angelic extends CustomEnchantment {
    HashMap<String, BukkitTask> playerTasks;

    public Angelic() {
        super("Angelic", GeneralUtil.armor, 2);
        this.playerTasks = new HashMap<String, BukkitTask>();
        this.max = 5;
        this.base = 15.0;
        this.interval = 3.0;
    }

    public void applyDefenseEffect(final LivingEntity player, final LivingEntity attacker, final int level, final EntityDamageEvent e) {
        final double random = Math.random();
        final double chance = 0.015 * level;
        if (random < chance) {
            long healDelay = 100L;
            if (level <= 1) {
                healDelay = 100L;
            }
            if (level == 2) {
                healDelay = 90L;
            }
            if (level == 3) {
                healDelay = 80L;
            }
            if (level == 4) {
                healDelay = 70L;
            }
            if (level >= 5) {
                healDelay = 60L;
            }
            long healDuration = Math.max(healDelay + 2L, (long)level * 20L + (10L + (long)ThreadLocalRandom.current().nextInt(16)));


            final double healAmount = 1.0;
            if (player.hasMetadata("angelicTaskID")) {
                return;
            }
            final int taskID = new RegenerationTask((Player) player, healDuration, healDelay, healAmount).runTaskTimer((Plugin) Enchantments.getInstance(), 0L, healDelay).getTaskId();
            Bukkit.getScheduler().runTaskLaterAsynchronously((Plugin) Enchantments.getInstance(), (Runnable) new Runnable() {
                @Override
                public void run() {
                    Bukkit.getScheduler().cancelTask(taskID);
                    Bukkit.getScheduler().runTaskAsynchronously((Plugin) Enchantments.getInstance(), () -> player.removeMetadata("angelicTaskID", (Plugin) Enchantments.getInstance()));
                }
            }, healDuration);
            player.setMetadata("angelicTaskID", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), (Object) taskID));
        }
    }
}