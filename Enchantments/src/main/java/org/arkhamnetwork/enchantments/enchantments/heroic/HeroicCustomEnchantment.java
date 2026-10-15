/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  com.rit.sucy.EnchantmentAPI
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.enchantments.enchantments.heroic;

import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.util.Random;

public abstract class HeroicCustomEnchantment
extends CustomEnchantment {
    protected Random random = new Random();

    public HeroicCustomEnchantment(String name, String nonHeroicName, Material[] naturalItems, int weight) {
        this(name, nonHeroicName, naturalItems, weight, true, false);
    }

    public HeroicCustomEnchantment(String name, String nonHeroicName) {
        this(name, nonHeroicName, new Material[0], 0, true, true);
    }

    public HeroicCustomEnchantment(String name, String nonHeroicName, boolean copyall) {
        this(name, nonHeroicName, new Material[0], 0, true, copyall);
    }

    public HeroicCustomEnchantment(String name, String nonHeroicName, Material[] naturalItems, int weight, boolean useDefaultBase, boolean copyAllDefaultStats) {
        super(name, naturalItems, weight);
        Bukkit.getScheduler().runTaskLater((Plugin)Enchantments.getInstance(), () -> {
            this.heroicEnchantment = true;
            this.nonHeroicVersion = EnchantmentAPI.getEnchantment((String)nonHeroicName);
            if (copyAllDefaultStats || useDefaultBase) {
                this.base = this.nonHeroicVersion.getBase();
                this.interval = this.nonHeroicVersion.getInterval();
                if (copyAllDefaultStats) {
                    this.max = this.nonHeroicVersion.getMaxLevel();
                    this.naturalItems = this.nonHeroicVersion.getNaturalMaterials();
                    this.setWeight(this.nonHeroicVersion.getWeight());
                }
            }
            this.nonHeroicVersion.heroicVersion = this;
        }, 2L);
    }

    public void debug(String msg) {
        Enchantments.getInstance().getLogger().info("[" + this.name() + "] " + msg);
    }
}

