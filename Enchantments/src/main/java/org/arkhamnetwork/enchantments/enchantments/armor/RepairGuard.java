/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.nms.MinecraftServerUtils
 *  com.rit.sucy.CustomEnchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitTask
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import com.cosmicpvp.cosmicutils.nms.MinecraftServerUtils;
import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class RepairGuard
extends CustomEnchantment {
    HashMap<String, BukkitTask> playerTasks = new HashMap();

    public RepairGuard() {
        super("Repair Guard", GeneralUtil.armor, 2);
        this.max = 3;
        this.base = 10.0;
        this.interval = 5.0;
    }

    public void applyUnequipEffect(Player player, int level, ItemStack item) {
        double duraPercent = 0.15;
        if (level == 2) {
            duraPercent = 0.2;
        }
        if (level == 3) {
            duraPercent = 0.25;
        }
        if ((double)this.getDurabilityPercent(item) <= duraPercent) {
            int cooldownTicks = 600;
            if (player.hasMetadata("lastRepairGuardProc") && (long)MinecraftServerUtils.getCurrentServerTick() - ((MetadataValue)player.getMetadata("lastRepairGuardProc").get(0)).asLong() < (long)cooldownTicks) {
                return;
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, (2 + level) * 20, 2 + level), true);
            player.setMetadata("lastRepairGuardProc", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)MinecraftServerUtils.getCurrentServerTick()));
        }
    }

    private float getDurabilityPercent(ItemStack i) {
        if (i.getDurability() == 0 || i.getType().getMaxDurability() == 0) {
            return 1.0f;
        }
        return i.getDurability() / i.getType().getMaxDurability();
    }
}

