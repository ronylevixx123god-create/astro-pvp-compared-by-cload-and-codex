package org.arkhamnetwork.enchantments;

import com.cosmicpvp.cosmicutils.utils.EssentialsUtils;
import com.cosmicpvp.cosmicutils.utils.VanishUtils;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.rit.sucy.CustomEnchantment;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

public class MasteryCustomEnchantment extends CustomEnchantment {
    protected Random random;
    protected String armorSet;

    public MasteryCustomEnchantment(final String name, final Material[] naturalItems, final int weight) {
        super(name, naturalItems, weight);
        this.random = new Random();
    }

    public boolean canStack() {
        return false;
    }

    public void applyDamagedOtherPlayer(final Player damager, final Player damaged, final EntityDamageByEntityEvent event, final int enchantLevel) {
    }

    public double getMissingHealthPercent(final LivingEntity player) {
        final double health = player.getHealth();
        final double maxHealth = player.getMaxHealth();
        if (health >= maxHealth) {
            return 0.0;
        }
        final double missing = maxHealth - health;
        return missing / maxHealth;
    }

    public boolean canEffectPlayer(final Player player) {
        return player.getGameMode() == GameMode.SURVIVAL && !player.hasMetadata("spectator") && !player.hasMetadata("NPC") && !VanishUtils.isVanished(player) && !EssentialsUtils.isInGodMode(player) && !WorldGuardUtils.isPvPDisabled(player.getLocation());
    }

    public String getArmorSet() {
        return this.armorSet;
    }
}
