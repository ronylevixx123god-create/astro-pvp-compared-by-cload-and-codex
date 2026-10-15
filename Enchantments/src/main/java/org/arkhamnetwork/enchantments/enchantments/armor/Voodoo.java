package org.arkhamnetwork.enchantments.enchantments.armor;

import com.rit.sucy.CustomEnchantment;
import lombok.SneakyThrows;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Voodoo extends CustomEnchantment {
    public Voodoo() {
        super("Voodoo", GeneralUtil.armor, 2);
        this.max = 6;
        this.base = 17.0;
        this.interval = 6.0;
    }

    @SneakyThrows
    public void applyDefenseEffect(final LivingEntity player, final LivingEntity attacker, final int level, final EntityDamageEvent e) {
        final int time = (int) (1.5 * level);
        final double random = Math.random();
        final double chance = 0.025 * level;
        if (random < chance && e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK && attacker != null) {
            //attacker.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, time * 20 + 25, (int) ((level >= 4) ? 1 : 0)));
            de.inventivegames.particle.ParticleEffect.MOB_SPELL.sendToPlayers(Bukkit.getOnlinePlayers(), attacker.getEyeLocation(), 0,0.5f,0,0.4f,15);
        }
    }
}