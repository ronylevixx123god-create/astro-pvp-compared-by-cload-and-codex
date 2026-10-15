/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class Reflect
extends CustomEnchantment {
    public Reflect() {
        super("Enchant Reflect", GeneralUtil.armor, 3);
        this.max = 10;
        this.base = 25.0;
        this.interval = 10.0;
    }

    public void applyEquipEffect(Player player, int enchantLevel) {
        //player.setMetadata("normal_reflect_enchant", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)enchantLevel));
    }

    public void applyUnequipEffect(Player player, int enchantLevel) {
        //player.removeMetadata("normal_reflect_enchant", (Plugin)Enchantments.getInstance());
    }

    public static int getReflectLevel(LivingEntity player, String key) {
        List found = player.getMetadata(key + "_reflect_enchant");
        if (found != null && !found.isEmpty()) {
            return ((MetadataValue)found.get(0)).asInt();
        }
        return 0;
    }
}

