package org.arkhamnetwork.enchantments.mastery;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.MasteryCustomEnchantment;
import org.arkhamnetwork.enchantments.util.DamageIndicatorUtils;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.concurrent.ThreadLocalRandom;

public class MasteryNeutralize extends MasteryCustomEnchantment
{
    public MasteryNeutralize() {
        super("Neutralize", GeneralUtil.weapons, 3);
        this.max = 5;
        this.base = 25.0;
        this.interval = 10.0;
    }

    public void applyEffect(final LivingEntity player, final LivingEntity target, final int level, final EntityDamageByEntityEvent e) {
        if (!(target instanceof Player)) {
            return;
        }
        Player user = (Player)player;
        Player pTarget = (Player)target;
        int multi = 1;
        try {
            multi = AstroItemSkinsAPI.hasSkinEquipped(user, SkinType.CORRUPT_SWORD) ? 2 : 1;
        }
        catch (Exception exception) {
            // empty catch block
        }


        if ((level) * multi >= ThreadLocalRandom.current().nextInt(180)) {
            if (AstroItemSkinsAPI.hasSkinEquipped(pTarget, SkinType.RUINS_AMULET) && Math.random() < 0.5) {
                pTarget.sendMessage("§5§l* BLOCKED " + user.getName() + "'s NEUTRALIZE §7(Lost Amulet) §5§l*");
                return;
            }
            if (pTarget.hasMetadata("blackout")) {
                return;
            }

            if (pTarget.hasMetadata("neutralized")) {
                return;
            }
            pTarget.sendMessage(CC.translate("&5&l* NEUTRALIZED &7[" + user.getName() + "&7]&5&l *"));
            if (!pTarget.hasMetadata("noDefenseProcs")) {
                pTarget.setMetadata("noDefenseProcs", new FixedMetadataValue(Enchantments.getInstance(), "banana"));
            } else {
                pTarget.setMetadata("silenced", new FixedMetadataValue(Enchantments.getInstance(), "banana"));
            }
            pTarget.playSound(pTarget.getLocation(), Sound.ENDERMAN_SCREAM, 1.0f, 0.3f);
            user.sendMessage(CC.translate("&5&l* NEUTRALIZED &7[" + pTarget.getName() + "&7]&5&l *"));
            DamageIndicatorUtils.sendDamageIndicator(pTarget, CC.DarkPurpleB + "* NEUTRALIZED" + CC.Gray +  "[5s]" + CC.Gray + " *", "NEUTRALIZE");
            pTarget.setMetadata("neutralized", new FixedMetadataValue(Enchantments.getInstance(), "true"));
            Enchantments.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(Enchantments.getInstance(), () -> pTarget.removeMetadata("neutralized", Enchantments.getInstance()), 20L * 4);        }
    }
}


