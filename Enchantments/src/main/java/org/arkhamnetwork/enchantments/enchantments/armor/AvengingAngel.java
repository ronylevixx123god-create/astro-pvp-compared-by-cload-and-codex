/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.FactionUtils
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.NumberUtils
 *  com.google.common.collect.MapMaker
 *  com.massivecraft.factions.Faction
 *  com.massivecraft.factions.iface.RelationParticipator
 *  com.massivecraft.factions.net.astropvp.armorsets.struct.Relation
 *  com.rit.sucy.CustomEnchantment
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.google.common.cache.CacheBuilder;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.perms.Relation;
import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.libs.PlayerPotionEffects;
import org.arkhamnetwork.enchantments.libs.SpecificCustomEnchantment;
import org.arkhamnetwork.enchantments.listeners.CustomEnchantListener;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class AvengingAngel
extends CustomEnchantment {
    private final ConcurrentMap<Object, Object> cooldowns = CacheBuilder.newBuilder().expireAfterWrite(20L, TimeUnit.SECONDS).build().asMap();

    public AvengingAngel() {
        super("Avenging Angel", GeneralUtil.armor, 10);
        this.max = 4;
        this.base = 25.0;
        this.interval = 10.0;
        Bukkit.getPluginManager().registerEvents(new AvengingAngelListener(this), Enchantments.getInstance());
    }

    public void applyEquipEffect(Player player, int level, ItemStack item) {
        this.applyEquipEffect(player, level);
    }

    public void applyEquipEffect(Player player, int level) {
        player.setMetadata("avenging_angel", new FixedMetadataValue(Enchantments.getInstance(), level));
    }

    public void applyUnequipEffect(Player player, int enchantLevel) {
        player.removeMetadata("avenging_angel", Enchantments.getInstance());
    }

    public void applyUnequipEffect(Player player, int enchantLevel, ItemStack item) {
        this.applyEquipEffect(player, enchantLevel);
    }

    class AvengingAngelListener
    extends CustomEnchantListener<AvengingAngel> {
        public AvengingAngelListener(AvengingAngel enchant) {
            super(enchant);
        }

        @EventHandler
        public void onPlayerDeath(PlayerDeathEvent event) {
            Faction faction = FactionUtils.getFaction(event.getEntity(), false);
            if (faction == null || !faction.isNormal()) {
                return;
            }
            Player dead = event.getEntity();
            double distance = 10000.0;
            Location location = event.getEntity().getLocation();
            for (Player pl : event.getEntity().getWorld().getPlayers()) {
                int angelLevel;
                double distanceNeeded;
                Faction fac;
                double distanceFromPlayer;
                if (pl.getUniqueId().equals(dead.getUniqueId()) || AvengingAngel.this.cooldowns.containsKey(pl.getUniqueId()) || !pl.hasMetadata("avenging_angel") || !(pl.getHealth() < pl.getMaxHealth()) || pl.isDead() || !((distanceFromPlayer = pl.getLocation().distanceSquared(location)) < distance) || (fac = FactionUtils.getFaction(pl, false)) == null || !faction.getRelationTo(fac).isAtLeast(Relation.ALLY) || !((distanceNeeded = Math.min(50 + (angelLevel = pl.getMetadata("avenging_angel").get(0).asInt()) * 12, 100)) * distanceNeeded >= distanceFromPlayer)) continue;
                int duration = 6 + angelLevel;
                AvengingAngel.this.cooldowns.put(pl.getUniqueId(), System.currentTimeMillis() + 10000L);
                double healed = pl.getMaxHealth() - pl.getHealth();
                //GeneralUtil.healPlayer((LivingEntity)pl, healed);
                pl.sendMessage(CC.YellowB + "* AVENGING ANGEL [" + CC.Gray + "+" + NumberUtils.formatMoney(healed) + " HP (" + dead.getName() + ")" + CC.YellowB + " *");
                Bukkit.getScheduler().runTaskLater(Enchantments.getInstance(), () -> {
                    PlayerPotionEffects.untrackPotionEffect(pl, new SpecificCustomEnchantment(this.customEnchant, angelLevel));
                    pl.removePotionEffect(PotionEffectType.ABSORPTION);
                }, 20L * duration);
                Bukkit.getLogger().info("[Enchantments] Healing " + pl.getName() + " to full HP due to " + dead.getName() + " dieing!");
            }
        }
    }
}

