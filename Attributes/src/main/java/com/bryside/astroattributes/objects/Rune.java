package com.bryside.astroattributes.objects;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public abstract class Rune {
    public int getMaxLevel() {
        return 10;
    }

    public String[] getLore(int level, int chance) {
        return null;
    }

    public String getDisplayName() {
        return null;
    }

    public Material getMaterial() {
        return null;
    }

    public ItemStack getItemStack(int level, int success) {
        return null;
    }

    public int getExpForLevel(int level) {
        return 1500 + level * 500;
    }

    public void procDamageEvent(EntityDamageByEntityEvent e, int level, Player damager) throws Exception {
    }
}
