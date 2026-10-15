/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.WorldUtil
 *  com.rit.sucy.CustomEnchantment
 *  com.rit.sucy.EnchantmentAPI
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.entity.ProjectileLaunchEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.enchantments.enchantments.heroic;

import com.cosmicpvp.cosmicutils.utils.WorldUtil;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class HeroicSniper
extends CustomEnchantment {
    public HeroicSniper() {
        super("Lethal Sniper", new Material[]{Material.BOW}, 2);
        this.max = 5;
        this.base = 10.0;
        this.interval = 8.0;
        Bukkit.getScheduler().runTaskLater((Plugin)Enchantments.getInstance(), new Runnable(){

            @Override
            public void run() {
                HeroicSniper.this.heroicEnchantment = true;
                HeroicSniper.this.nonHeroicVersion = EnchantmentAPI.getEnchantment((String)"Sniper");
            }
        }, 2L);
    }

    public void applyProjectileEffect(LivingEntity user, int enchantLevel, ProjectileLaunchEvent event) {
        event.getEntity().setMetadata("lethalSniper", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)enchantLevel));
    }

    public void applyEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageByEntityEvent event) {
        boolean headshot;
        if (!(target instanceof Player)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE) {
            return;
        }
        Projectile proj = (Projectile)event.getDamager();
        if (!(proj.getShooter() instanceof Player)) {
            return;
        }

        if (((Player) target).getPlayer().getInventory().getChestplate() != null && (AstroItemSkinsAPI.hasSkinEquipped(((Player) target).getPlayer(), SkinType.THANKSGIVING_2021_BACKPACK) || AstroItemSkinsAPI.hasSkinEquipped(((Player) target).getPlayer(), SkinType.THANKSGIVING_2021_MUTATED_BACKPACK))) {
        return;
        }

        if (AstroItemSkinsAPI.hasSkinEquipped((Player) target, SkinType.CHRISTMAS_2021_HELMET) || AstroItemSkinsAPI.hasSkinEquipped((Player) target, SkinType.CHRISTMAS_2021_MUTATED_HELMET)) {
            return;
        }
            LivingEntity victima = target;
        double y = proj.getLocation().getY();
        double victimay = victima.getLocation().getY();
        double level = enchantLevel;
        boolean bl = headshot = y - victimay > 1.9;
        if (headshot && Math.random() < 0.625 + level * 0.075) {

            double multiplier = Math.floor(Math.random() *(14 - 4 + 1) + 4);
            event.setDamage(event.getDamage() * multiplier);
            this.playBleedEffect(target.getEyeLocation(), enchantLevel);
            if (target instanceof Player) {
                ((Player)target).sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "*** HEADSHOT [+" + multiplier + "x DMG] ***");
                ((Player)target).playSound(target.getLocation(), Sound.HURT_FLESH, 2.0f, 0.3f);
            }
        }
    }

    private void playBleedEffect(Location loc, int bleedStack) {
        for (int i = Math.max(1, bleedStack / 2); i > 0; --i) {
            WorldUtil.sendWorldEffect((Block)loc.getBlock(), (int)Material.REDSTONE_BLOCK.getId(), (int)2001);
        }
    }
}

