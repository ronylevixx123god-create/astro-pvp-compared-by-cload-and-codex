package org.arkhamnetwork.enchantments.enchantments.heroic;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import org.arkhamnetwork.enchantments.enchantments.axes.Bleed;
import org.arkhamnetwork.enchantments.util.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.arkhamnetwork.enchantments.*;
import org.bukkit.plugin.*;
import org.bukkit.metadata.*;

public class HeroicDeepWounds
        extends HeroicCustomEnchantment {
    public HeroicDeepWounds() {
        super("Bloody Deep Wounds", "Deep Wounds", GeneralUtil.weapons, 2);
        this.max = 3;
        this.base = 15.0;
        this.interval = 15.0;
    }

    public void applyEffect(LivingEntity player, LivingEntity playerHit, int level, EntityDamageByEntityEvent e) {
        if (playerHit instanceof Player) {
            playerHit.setMetadata("deepWounds", new FixedMetadataValue((Plugin)((Object)Enchantments.getInstance()), System.currentTimeMillis() + (long)(level * 1500L)));
            double mod = (double)Bleed.getBleedStack(playerHit) * 0.008 + (double)level * 0.01 + 1.0;
            e.setDamage(e.getDamage() * mod);
        }
    }
}
