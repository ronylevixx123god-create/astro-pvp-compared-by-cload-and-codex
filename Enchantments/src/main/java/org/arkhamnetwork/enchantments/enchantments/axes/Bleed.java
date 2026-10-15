package org.arkhamnetwork.enchantments.enchantments.axes;

import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.ifamasssxd.cosmicmasks.MaskType;
import net.minecraft.server.v1_8_R3.ItemStack;
import org.arkhamnetwork.enchantments.util.*;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.event.entity.*;
import com.rit.sucy.*;
import org.arkhamnetwork.enchantments.*;
import org.bukkit.plugin.*;
import org.bukkit.metadata.*;
import org.bukkit.entity.*;
import org.bukkit.potion.*;
import org.bukkit.*;
import com.cosmicpvp.cosmicutils.utils.*;

public class Bleed extends CustomEnchantment
{
    public Bleed() {
        super("Bleed", GeneralUtil.axe, 2);
        this.max = 6;
        this.base = 10.0;
        this.interval = 8.0;
    }

    public void applyEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageByEntityEvent event) {
        if (!event.isCancelled() && event.getDamage() > 0.0 && user instanceof Player) {
            double bleedChance = 0.08 * (double)enchantLevel + 0.4;
            if (Math.random() < bleedChance && Bleed.getBleedStack(target) < this.getMaxBleedStack(enchantLevel)) {
                if (Math.random() < 0.25 && user.hasMetadata("bleedStack") && EnchantmentAPI.itemHasEnchantment(((Player)user).getItemInHand(), (String)"Combat Medic")) {
                    Bleed.clearBleedEffect(user);
                }
                this.addBleedStack(user, target);
            }
        }
    }

    private int getMaxBleedStack(final int level) {
        return 20;
    }

    public static int getBleedStack(final LivingEntity le) {
        return le.hasMetadata("bleedStack") ? le.getMetadata("bleedStack").get(0).asInt() : 0;
    }

    public static void removeBleedStack(final LivingEntity le, final int toRemove) {
        if (!le.hasMetadata("bleedStack")) {
            return;
        }
        final int stacks = getBleedStack(le) - toRemove;
        if (stacks <= 0) {
            le.removeMetadata("bleedStack", (Plugin)Enchantments.getInstance());
            return;
        }
        le.setMetadata("bleedStack", new FixedMetadataValue((Plugin)Enchantments.getInstance(), stacks));
    }

    public static void clearBleedEffect(final LivingEntity le) {
        le.removeMetadata("bleedStack", (Plugin)Enchantments.getInstance());
        if (le instanceof Player) {
            //((Player)le).setWalkSpeed(0.2f);
        }
        else {
            le.removePotionEffect(PotionEffectType.SLOW);
        }
    }

    private void addBleedStack(final LivingEntity leDamager, final LivingEntity le) {
        le.setMetadata("bleedStack", new FixedMetadataValue((Plugin)Enchantments.getInstance(), getBleedStack(le) + 1));
        final int bleedStack = getBleedStack(le) / 2;
        if (le instanceof Player) {
            final Player p = (Player)le;
            //p.setWalkSpeed((float)(0.20000000298023224 - 0.005 * bleedStack));
            for (final Entity ent : p.getNearbyEntities(7.0, 7.0, 7.0)) {
                if (ent.getEntityId() == leDamager.getEntityId()) {
                    continue;
                }
                if (!(ent instanceof Player)) {
                    continue;
                }
                if (!ent.hasMetadata("bloodLust")) {
                    continue;
                }

                if (AstroItemSkinsAPI.hasSkinEquipped(p, SkinType.VIRUS_HELMET_HAZMAT)) {
                    p.sendMessage(CC.translate("&2&l* PRISON BREAK &7(Hazmat Helmet) &2&l*"));
                    return;
                }
                if (p.hasMetadata("immune_freeze")) {
                    p.sendMessage(CC.DarkGrayB + "* DRAGON SLAYER [" + ChatColor.GRAY + "Bleed slowness blocked!" + CC.DarkGrayB + "] *");
                    return;
                }
                if (MaskType.HOLY.hasEquipped(p)) {
                    p.sendMessage(CC.GoldB + "* HOLY MASK [" + ChatColor.GRAY + "Bleed slowness blocked!" + CC.GoldB + "] *");
                    return;
                }

                if (((Player)p).getInventory().getChestplate() != null && AstroItemSkinsAPI.hasSkinEquipped((Player) p, SkinType.BACK_TO_SCHOOL_2021_AMULET))
                    return;

                if (AstroItemSkinsAPI.hasSkinEquipped((Player) p, SkinType.JULY_2021_BOOTS)) {
                    p.sendMessage(CC.translate("&c&l** BARRY's BOLTS [&7Blocked Bleed&c&l] **"));
                    return;
                }
                final Player pEnt = (Player)ent;
                if (!GeneralUtil.isAlly(p, pEnt)) {
                    continue;
                }
                final int bloodLustLevel = pEnt.getMetadata("bloodLust").get(0).asInt();
                if (Math.random() >= 0.2 + bloodLustLevel * 0.05) {
                    continue;
                }
                final double amountToHeal = Math.max(2.0, bleedStack * 0.05 * bloodLustLevel);
                //AstroDamageHandler.getInstance().getDamageHandler().healPlayer((LivingEntity)pEnt, amountToHeal, "&e&lBleed");
                pEnt.playSound(pEnt.getLocation(), Sound.EAT, 0.4f, 0.6f);
            }
        }
        else {
            //le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, bleedStack * 20, Math.max(1, bleedStack / 3)));
        }
        this.playBleedEffect(le.getLocation().add(0.0, 1.0, 0.0), bleedStack);
    }

    private void playBleedEffect(final Location loc, final int bleedStack) {
        for (int i = Math.max(1, bleedStack / 2); i > 0; --i) {
            WorldUtil.sendWorldEffect(loc.getBlock(), Material.REDSTONE_BLOCK.getId(), 2001);
        }
    }
}
