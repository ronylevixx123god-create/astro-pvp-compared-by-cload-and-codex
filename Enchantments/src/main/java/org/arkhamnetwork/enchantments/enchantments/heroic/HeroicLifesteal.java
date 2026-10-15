/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.MaskUtils
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 */
package org.arkhamnetwork.enchantments.enchantments.heroic;

import com.cosmicpvp.cosmicutils.utils.MaskUtils;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.ifamasssxd.cosmicmasks.MaskType;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class HeroicLifesteal
        extends HeroicCustomEnchantment {
    public HeroicLifesteal() {
        super("Demonic Lifesteal", "Lifesteal", GeneralUtil.swords, 2);
        this.max = 3;
    }

    public void applyEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageByEntityEvent event) {
        double chance;
        double random = Math.random();
        chance = 0.015;
        int toHeal = enchantLevel == 1 ? 2 : (enchantLevel == 2 ? 4 : (enchantLevel == 3 ? 4 : 5));
        int health = (int)user.getHealth() + toHeal;
        if ((double)health > user.getMaxHealth()) {
            health = (int)user.getMaxHealth();
        }
        if (random < chance) {
            if (target instanceof Player && MaskType.hasEquipped((Player)((Player)target), (String)"NECROMANCER")) {
                return;
            }
            GeneralUtil.setPlayerHealth(user, health);
        }
    }
}

