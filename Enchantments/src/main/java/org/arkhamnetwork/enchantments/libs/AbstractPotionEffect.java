package org.arkhamnetwork.enchantments.libs;

import org.bukkit.potion.PotionEffectType;

public class AbstractPotionEffect {
    PotionEffectType effectType;
    int potionLevel;
    int originalLevel;

    public AbstractPotionEffect(final PotionEffectType effect, final int level) {
        this.effectType = effect;
        this.potionLevel = level;
        this.originalLevel = this.potionLevel;
    }

    public void resetOriginalLevel() {
        this.potionLevel = this.originalLevel;
    }

    @Override
    public String toString() {
        return this.effectType.getName() + ":" + this.potionLevel;
    }

    public PotionEffectType getEffectType() {
        return this.effectType;
    }

    public int getPotionLevel() {
        return this.potionLevel;
    }

    public void setPotionLevel(final int potionLevel) {
        this.potionLevel = potionLevel;
    }

    public int getOriginalLevel() {
        return this.originalLevel;
    }

    public void setOriginalLevel(final int originalLevel) {
        this.originalLevel = originalLevel;
    }
}
