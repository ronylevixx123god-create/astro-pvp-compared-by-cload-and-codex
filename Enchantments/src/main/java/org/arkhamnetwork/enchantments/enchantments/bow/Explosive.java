/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.WitherSkull
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.ProjectileLaunchEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package org.arkhamnetwork.enchantments.enchantments.bow;

import com.rit.sucy.CustomEnchantment;
import de.inventivegames.particle.ParticleEffect;
import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Explosive
extends CustomEnchantment {
    protected static String explosiveEncahntmentMetadata = "explosive_enchantment";
    protected static String explosiveEncahntmentOwnerMetadata = "explosive_enchantment_owner";

    public Explosive() {
        super("Explosive", new Material[]{Material.BOW}, 2);
        this.max = 5;
        this.base = 15.0;
        this.interval = 5.0;
        Bukkit.getPluginManager().registerEvents(new ExplosiveListener(), Enchantments.getInstance());
    }

    public void applyEffect(LivingEntity player, LivingEntity playerHit, int level, EntityDamageByEntityEvent e) {
        if (!(playerHit instanceof Player)) {
            return;
        }
        playerHit.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * level, 1));
        try {
            ParticleEffect.LARGE_EXPLODE.sendToPlayers(Bukkit.getOnlinePlayers(), playerHit.getEyeLocation(), 0.0f, 0.0f, 0.0f, 0.25f, 1);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void applyProjectileEffect(LivingEntity player, int level, ProjectileLaunchEvent e) {
        if (e.getEntity() instanceof Arrow && player instanceof Player) {
            WitherSkull ws = (WitherSkull)e.getEntity().getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.WITHER_SKULL);
            ws.setIsIncendiary(false);
            ws.setYield(0.0f);
            e.getEntity().setPassenger(ws);
            e.getEntity().setMetadata(explosiveEncahntmentMetadata, new FixedMetadataValue(Enchantments.getInstance(), level));
            e.getEntity().setMetadata(explosiveEncahntmentOwnerMetadata, new FixedMetadataValue(Enchantments.getInstance(), player.getName()));
        }
    }
}

