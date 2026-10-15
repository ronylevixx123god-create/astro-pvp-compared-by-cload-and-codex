package org.arkhamnetwork.enchantments.mastery;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import me.ifamasssxd.cosmicmasks.MaskType;
import org.arkhamnetwork.enchantments.MasteryCustomEnchantment;
import org.arkhamnetwork.enchantments.events.CustomEnchantProcOnEntityEvent;
import org.arkhamnetwork.enchantments.libs.PlayerPotionEffects;
import org.arkhamnetwork.enchantments.libs.RomanNumber;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MasteryMortalCoil extends MasteryCustomEnchantment {
    private static Map<UUID, Long> affectedPlayers;
    private static Map<UUID, Integer> affectedPlayerLevels;

    static {
        MasteryMortalCoil.affectedPlayers = new HashMap<UUID, Long>();
        MasteryMortalCoil.affectedPlayerLevels = new HashMap<UUID, Integer>();
    }

    public MasteryMortalCoil() {
        super("Mortal Coil", GeneralUtil.armor, 11);
        this.max = 5;
        this.armorSet = "NECROMANCER";
    }

    public static boolean isAffected(final Player player) {
        final Long found = MasteryMortalCoil.affectedPlayers.get(player.getUniqueId());
        return found != null && found > System.currentTimeMillis();
    }

    public static int getMortalCoilLevel(final Player player) {
        return MasteryMortalCoil.affectedPlayerLevels.containsKey(player.getUniqueId()) ? MasteryMortalCoil.affectedPlayerLevels.get(player.getUniqueId()) : 0;
    }

    public void applyMortalEffect(final Player player, final Player damager, final double duration, final int level) {
        if (MaskType.hasEquipped(player, "LOVER")) {
            return;
        }
        if (player.hasMetadata("revoked")) {
            return;
        }
        player.sendMessage(CC.RedB + "* MORTAL COIL " + RomanNumber.toRoman(level) + " (" + CC.Gray + damager.getName() + " [" + NumberUtils.formatSeconds(duration) + "s]" + CC.RedB + ") *");
        MasteryMortalCoil.affectedPlayers.put(player.getUniqueId(), (long) (System.currentTimeMillis() + duration * 1000.0));
        MasteryMortalCoil.affectedPlayerLevels.put(player.getUniqueId(), level);
        final double currentHP = player.getHealth();
        player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
        PlayerPotionEffects.applyPotionEffects(player, false);
        if (player.getMaxHealth() < currentHP) {
            player.setHealth(player.getMaxHealth());
        }
    }

    @Override
    public void applyDamagedOtherPlayer(final Player damager, final Player damaged, final EntityDamageByEntityEvent event, final int enchantLevel) {
        final double chance = 0.025 + enchantLevel * 0.015;
        /*if (Math.random() <= chance) {
            if (isAffected(damaged)) {
                return;
            }
            if (!new CustomEnchantProcOnEntityEvent((LivingEntity) damager, (LivingEntity) damaged, (Event) event, this, enchantLevel).callEvent()) {
                return;
            }
            this.applyMortalEffect(damaged, damager, 2.0 + enchantLevel * 0.4, enchantLevel);
        }*/
    }
}
