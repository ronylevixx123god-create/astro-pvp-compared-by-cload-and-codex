/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.earth2me.essentials.Essentials
 *  com.rit.sucy.CustomEnchantment
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.ProjectileLaunchEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.enchantments.enchantments.bow;

import com.earth2me.essentials.Essentials;
import com.rit.sucy.CustomEnchantment;
import de.inventivegames.particle.ParticleEffect;
import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.Random;

public class Hellfire
extends CustomEnchantment {
    protected static String hellfireEncahntmentMetadata = "hellfire_enchantment";
    protected static String hellfireEncahntmentOwnerMetadata = "hellfire_enchantment_owner";
    protected static Essentials ess3;

    public Hellfire() {
        super("Hellfire", new Material[]{Material.BOW}, 2);
        this.max = 5;
        this.base = 15.0;
        this.interval = 5.0;
        ess3 = (Essentials)Bukkit.getPluginManager().getPlugin("Essentials");
        Bukkit.getPluginManager().registerEvents((Listener)new FireballListener(), (Plugin)Enchantments.getInstance());
    }

    public void applyEffect(LivingEntity player, LivingEntity playerHit, int level, EntityDamageByEntityEvent e) {
        Player p2;
        Player p1;
        if (playerHit instanceof Player && (!(p1 = (Player)player).canSee(p2 = (Player)playerHit) || p1.getGameMode() != GameMode.SURVIVAL)) {
            return;
        }
        playerHit.setFireTicks(level * 40);
        try {
            ParticleEffect.FLAME.sendToPlayers(Bukkit.getOnlinePlayers(), playerHit.getLocation().add(0.0, 1.0, 0.0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.8f, 85);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        try {
            ParticleEffect.LAVA.sendToPlayers(Bukkit.getOnlinePlayers(), playerHit.getLocation().add(0.0, 1.0, 0.0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.8f, 85);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public void applyProjectileEffect(LivingEntity player, int level, ProjectileLaunchEvent e) {
        if (e.getEntity() instanceof Arrow && player instanceof Player) {
            ((Arrow)e.getEntity()).setFireTicks(Integer.MAX_VALUE);
            e.getEntity().setMetadata(hellfireEncahntmentMetadata, (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)level));
            e.getEntity().setMetadata(hellfireEncahntmentOwnerMetadata, (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)((Player)player).getName()));
        }
    }
}

