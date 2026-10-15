package org.arkhamnetwork.enchantments.libs;

import com.rit.sucy.CustomEnchantment;

public class SpecificCustomEnchantment {
    CustomEnchantment enchantment;
    int enchantmentLevel;

    public SpecificCustomEnchantment(final CustomEnchantment ce, final int level) {
        this.enchantment = ce;
        this.enchantmentLevel = level;
    }

    @Override
    public int hashCode() {
        final int a = this.enchantment.uniqueId;
        final int b = this.enchantmentLevel;
        return (a >= b) ? (a * a + a + b) : (a + b * b);
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof SpecificCustomEnchantment)) {
            return false;
        }
        final SpecificCustomEnchantment k = (SpecificCustomEnchantment) o;
        return k.enchantment.uniqueId == this.enchantment.uniqueId && k.enchantmentLevel == this.enchantmentLevel;
    }

    @Override
    public String toString() {
        return this.getEnchantment().name() + ":" + this.getEnchantmentLevel();
    }

    public CustomEnchantment getEnchantment() {
        return this.enchantment;
    }

    public int getEnchantmentLevel() {
        return this.enchantmentLevel;
    }
}
