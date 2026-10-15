package org.arkhamnetwork.enchantments.enchantments.heroic;

import java.text.*;

import com.rit.sucy.EnchantmentAPI;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import org.arkhamnetwork.enchantments.enchantments.axes.Bleed;
import org.arkhamnetwork.enchantments.util.*;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.event.entity.*;
import org.arkhamnetwork.enchantments.*;
import org.bukkit.plugin.*;
import org.bukkit.metadata.*;
import me.ifamasssxd.cosmicmasks.*;
import org.bukkit.entity.*;
import org.bukkit.potion.*;
import java.util.*;
import org.bukkit.*;
import com.cosmicpvp.cosmicutils.utils.*;

public class HeroicBleed extends HeroicCustomEnchantment
{
    private DecimalFormat df = new DecimalFormat("#.##");
    public HeroicBleed() {
        super("Deep Bleed", "Bleed", GeneralUtil.axe, 2);
        this.max = 6;
    }

    public void applyEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageByEntityEvent event) {
        if (!event.isCancelled() && event.getDamage() > 0.0 && user instanceof Player) {
            double bleedChance = 0.1 * (double)enchantLevel + 0.4;
            if (Math.random() < bleedChance && HeroicBleed.getBleedStack(target) < this.getMaxBleedStack(enchantLevel)) {
                this.addBleedStack(user, target);
            }
        }
    }

    private int getMaxBleedStack(int level) {
        return 20;
    }

    public static int getBleedStack(LivingEntity le) {
        return le.hasMetadata("bleedStack") ? ((MetadataValue)le.getMetadata("bleedStack").get(0)).asInt() : 0;
    }

    public static void clearBleedEffect(LivingEntity le) {
        le.removeMetadata("bleedStack", (Plugin)Enchantments.getInstance());
        if (le instanceof Player) {
            //((Player)le).setWalkSpeed(0.2f);
        } else {
            le.removePotionEffect(PotionEffectType.SLOW);
        }
    }

    private void addBleedStack(LivingEntity leDamager, LivingEntity le) {
        le.setMetadata("bleedStack", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)(HeroicBleed.getBleedStack(le) + 1)));
        int bleedStack = Math.max(1, HeroicBleed.getBleedStack(le) / 2);
        if (le instanceof Player) {
            Player p = (Player)le;
            //p.setWalkSpeed((float)((double)0.2f - 0.0075 * (double)bleedStack));
            if (bleedStack >= 5 && bleedStack % 5 == 0) {
                p.sendMessage(ChatColor.RED + "** DEEP BLEED [" + ChatColor.DARK_RED + "-" + this.df.format((double)bleedStack * 0.0075 / (double)0.2f * 100.0) + ChatColor.RED + "% Speed] **");
            }
            for (Entity ent : p.getNearbyEntities(7.0, 7.0, 7.0)) {
                Player pEnt;
                if (ent.getEntityId() == leDamager.getEntityId() || !(ent instanceof Player) || !ent.hasMetadata("bloodLust") || !GeneralUtil.isAlly(p, pEnt = (Player)ent)) continue;
                int bloodLustLevel = ((MetadataValue)pEnt.getMetadata("bloodLust").get(0)).asInt();
                if (Math.random() >= 0.2 + (double)bloodLustLevel * 0.05) continue;
                double amountToHeal = Math.max(4.0, (double)bleedStack * 0.1 * (double)bloodLustLevel);
                GeneralUtil.healPlayer((LivingEntity)pEnt, amountToHeal);
                pEnt.playSound(pEnt.getLocation(), Sound.EAT, 0.4f, 0.6f);
            }
        } else {
            //le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, bleedStack * 10, Math.max(1, bleedStack / 3)));
        }
        this.playBleedEffect(le.getLocation().add(0.0, 1.0, 0.0), bleedStack);
    }

    private void playBleedEffect(Location loc, int bleedStack) {
        for (int i = Math.max(1, bleedStack / 2); i > 0; --i) {
            WorldUtil.sendWorldEffect(loc.getBlock(), (int)Material.BEDROCK.getId(), (int)2001);
        }
    }
}
