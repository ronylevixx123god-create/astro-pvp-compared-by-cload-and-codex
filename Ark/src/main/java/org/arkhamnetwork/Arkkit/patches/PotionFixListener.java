/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  net.minecraft.server.v1_7_R4.EntityLiving
 *  net.minecraft.server.v1_7_R4.EntityPlayer
 *  net.minecraft.server.v1_7_R4.MobEffectList
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftThrownPotion
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.ThrownPotion
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityRegainHealthEvent
 *  org.bukkit.event.entity.EntityRegainHealthEvent$RegainReason
 *  org.bukkit.event.entity.PotionSplashEvent
 *  org.bukkit.event.player.PlayerItemConsumeEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.PotionMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.Potion
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.potion.PotionType
 */
package org.arkhamnetwork.Arkkit.patches;

import com.google.common.collect.Lists;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MobEffectList;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;

final class PotionFixListener
        implements Listener {
    PotionFixListener() {
    }

    public static Potion fromDamage(int damage) {
        Potion potion;
        PotionType type = PotionType.getByDamageValue(damage & 0xF);
        if (type != null && (type != PotionType.WATER || damage == 0)) {
            int level = (damage & 0x20) >> 5;
            potion = new Potion(type, ++level);
        } else {
            potion = new Potion(damage & 0x3F);
        }
        if ((damage & 0x4000) > 0) {
            potion = potion.splash();
        }
        if ((damage & 0x40) > 0 && type != null && !type.isInstant()) {
            potion = potion.extend();
        }
        return potion;
    }



    @EventHandler
    public void onRegen(EntityRegainHealthEvent event) {
        if (PotionFix.EnabledFixes[1] || PotionFix.EnabledFixes[2]) {
            final LivingEntity entity = (LivingEntity) event.getEntity();
            int lvl = 0;
            for (PotionEffect effect : entity.getActivePotionEffects()) {
                if (effect.getType().getName() != "REGENERATION" && effect.getType().getName() != "HEAL") continue;
                lvl = effect.getAmplifier() + 1;
                break;
            }
            if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.MAGIC_REGEN && event.getAmount() == 1.0 && lvl > 0) {
                if (PotionFix.EnabledFixes[2]) {
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Arkkit.get(), new Runnable() {

                        @Override
                        public void run() {
                            if (entity.getMaxHealth() >= entity.getHealth() + 1.0 && entity.getHealth() > 0.0) {
                                entity.setHealth(entity.getHealth() + 1.0);
                            }
                        }
                    }, 50L / (long) (lvl * 2));
                }
            } else if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.MAGIC && event.getAmount() > 1.0 && lvl > 0 && PotionFix.EnabledFixes[1]) {
                event.setAmount(event.getAmount() * ((double) PotionFix.Amounts[1] * 0.25));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDrink(PlayerItemConsumeEvent e) {
        ItemStack is;
        if (e.getItem() != null && e.getItem().getType() == Material.POTION && (is = e.getItem()).getItemMeta() instanceof PotionMeta) {
            Potion pot = null;
            try {
                pot = Potion.fromItemStack(is);
            } catch (Exception err) {
                return;
            }
            e.setCancelled(true);
            PotionMeta potionMeta = (PotionMeta) is.getItemMeta();
            ArrayList<PotionEffect> effects = Lists.newArrayList((Iterable) pot.getEffects());

            if (potionMeta.hasCustomEffects()) {
                effects.addAll(potionMeta.getCustomEffects());
            }
            Player p = e.getPlayer();
            if (pot != null) {
                for (PotionEffect effect : effects) {
                    PotionEffect eff;
                    if (effect.getType().isInstant()) {
                        EntityPlayer ep = ((CraftPlayer) p).getHandle();
                        MobEffectList.byId[effect.getType().getId()].applyInstantEffect(ep, ep, ep, effect.getAmplifier(), 1.0);
                        continue;
                    }
                    if (p.hasPotionEffect(effect.getType()) && (eff = this.getPotionEffect(p, effect.getType())) != null && eff.getDuration() > effect.getDuration())
                        continue;
                    p.addPotionEffect(new PotionEffect(effect.getType(), effect.getDuration(), effect.getAmplifier()), true);
                }
                ItemStack bottleRetr = new ItemStack(Material.GLASS_BOTTLE, 1);
                if (is.getAmount() > 1) {
                    is.setAmount(is.getAmount() - 1);
                    if (p.getInventory().firstEmpty() != -1) {
                        p.getInventory().addItem(bottleRetr);
                    }
                } else {
                    p.setItemInHand(bottleRetr);
                }
                p.updateInventory();
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerSplash(PotionSplashEvent e) {
        if (e.getAffectedEntities().size() <= 0 || e.isCancelled()) {
            return;
        }
        for (Entity ent : e.getAffectedEntities()) {
            LivingEntity entity;
            double intensity;
            if (!(ent instanceof LivingEntity) || (intensity = e.getIntensity(entity = (LivingEntity) ent)) <= 0.0)
                continue;
            e.setIntensity(entity, 0.0);
            ThrownPotion pot = e.getEntity();
            Potion potion = PotionFixListener.fromDamage(pot.getItem().getDurability());
            PotionMeta potionMeta = (PotionMeta) pot.getItem().getItemMeta();
            ArrayList<PotionEffect> effects = (ArrayList<PotionEffect>) potion.getEffects();
            if (potionMeta.hasCustomEffects()) {
                effects.addAll(potionMeta.getCustomEffects());
            }
            for (PotionEffect effect : effects) {
                PotionEffect eff;
                int id = effect.getType().getId();
                if (effect.getType().isInstant()) {
                    if (pot.getShooter() instanceof CraftLivingEntity) {
                        MobEffectList.byId[id].applyInstantEffect(((CraftLivingEntity) pot.getShooter()).getHandle(), ((CraftLivingEntity) ent).getHandle(), ((CraftLivingEntity) ent).getHandle(), effect.getAmplifier(), intensity);
                        continue;
                    }
                    MobEffectList.byId[id].applyInstantEffect(null, ((CraftLivingEntity) ent).getHandle(), ((CraftLivingEntity) ent).getHandle(), effect.getAmplifier(), intensity);
                    continue;
                }
                int timeInTicks = (int) (intensity * (double) effect.getDuration() + 0.5);
                if (timeInTicks <= 20 || entity.hasPotionEffect(effect.getType()) && (eff = this.getPotionEffect(entity, effect.getType())) != null && eff.getDuration() > effect.getDuration())
                    continue;
                entity.addPotionEffect(new PotionEffect(effect.getType(), timeInTicks, effect.getAmplifier()), true);
            }
        }
    }

    public PotionEffect getPotionEffect(LivingEntity ent, PotionEffectType type) {
        for (PotionEffect effect : ent.getActivePotionEffects()) {
            if (!effect.getType().getName().equals(type.getName())) continue;
            return effect;
        }
        return null;
    }
}

