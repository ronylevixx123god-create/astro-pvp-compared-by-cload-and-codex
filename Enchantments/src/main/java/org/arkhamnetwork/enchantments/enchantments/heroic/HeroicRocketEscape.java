/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.GeneralUtil
 *  me.cosmicpvp.CosmicFactionUtils.CosmicFactionUtils
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.World$Environment
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.player.PlayerVelocityEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.util.Vector
 */
package org.arkhamnetwork.enchantments.enchantments.heroic;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.GeneralUtil;
import com.cosmicpvp.cosmicutils.utils.MaskUtils;
import de.inventivegames.particle.ParticleEffect;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.ifamasssxd.cosmicmasks.MaskType;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.enchantments.armor.RocketEscape;
import org.arkhamnetwork.enchantments.libs.PlayerPotionEffects;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.UUID;

public class HeroicRocketEscape
        extends HeroicCustomEnchantment {
    public HeroicRocketEscape() {
        super("Guided Rocket Escape", "Rocket Escape", false);
        this.max = 3;
        this.setNaturalMaterials(GeneralUtil.boots);
        this.base = 25.0;
    }

    private boolean canEscape(Player pl) {
        if (!RocketEscape.last_rocket_escape.containsKey(pl.getUniqueId().toString())) {
            return true;
        }
        return System.currentTimeMillis() - RocketEscape.last_rocket_escape.get(pl.getUniqueId().toString()) > 15000L;
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (player.hasMetadata("neutralized"))
            return;
        if (player instanceof Player) {
            double dmg = event.getFinalDamage();
            if (player.getWorld().getEnvironment() == World.Environment.THE_END) {
                return;
            }
            if (player.hasMetadata("inDungeonParkour")) {
                return;
            }
            if (player.getWorld().getName().equals("world_koth")) {
                return;
            }
            if (!(target instanceof Player)) return;

            if (player.getHealth() - dmg <= 0.0 && this.canEscape((Player) player)) {
                Player pl = (Player) player;

                if (((Player) player).getPlayer().getInventory().getChestplate() != null && (AstroItemSkinsAPI.hasSkinEquipped(((Player) player).getPlayer(), SkinType.THANKSGIVING_2021_BACKPACK) || AstroItemSkinsAPI.hasSkinEquipped(((Player) player).getPlayer(), SkinType.THANKSGIVING_2021_MUTATED_BACKPACK))) {
                    event.setDamage(event.getDamage()*1.5);
                    player.sendMessage(CC.translate("&2&l* TOMMY TAIL [&7Heroic Rocket Escape 2x Damage&2&l] *"));
                }

                RocketEscape.last_rocket_escape.put(pl.getUniqueId().toString(), System.currentTimeMillis());
                if (pl.hasMetadata("sabotage") && System.currentTimeMillis() - ((MetadataValue) pl.getMetadata("sabotage").get(0)).asLong() <= 1000L) {
                    int sabotageLevel = ((MetadataValue) pl.getMetadata("sabotageLevel").get(0)).asInt();
                    double negationChance = (double) sabotageLevel * 0.1;
                    if (Math.random() < negationChance) {
                        pl.sendMessage(CC.RedB + " ** " + CC.Gray + "Guided Rocket Escape:" + CC.RedB + " SABOTAGED **");
                        return;
                    }
                }
                if (target instanceof Player) {
                    if (AstroItemSkinsAPI.hasSkinEquipped((Player) target, SkinType.HALLOWEEN_BACKPACK)) {
                        target.sendMessage(CC.translate("&c&l ** &7" + pl.getName() + "'s Rocket Escape: &4&lBLOCKED &7[Cursed Wings] &c&l**"));
                        pl.sendMessage(CC.translate("&c&l ** &7Rocket Escape: &4&lBLOCKED &7[" + target.getName() + "'s Cursed Wings] &c&l**"));
                        return;
                    }
                    if (MaskType.hasEquipped((Player) target, "TERMINATOR") && Math.random() < 0.05) {
                        pl.sendMessage(CC.translate("&4&l ** &7Guided Rocket Escape &4&lBLOCKED &7(" + target.getName() + "'s Terminator Mask)"));
                        target.sendMessage(CC.translate("&4&l ** &7" + pl.getName() + "'s Guided Rocket Escape &4&lBLOCKED &7(Terminator Mask)"));
                        return;
                    }
                }
                event.setDamage(0.0);
                pl.sendMessage("");
                pl.sendMessage(CC.GreenB + "(!) " + CC.Green + "Your Guided Rocket Escape boots have activated, flight temporarily enabled, recover while they last!");
                pl.sendMessage("");
                pl.playSound(pl.getLocation(), Sound.EXPLODE, 1.0f, 0.54f);
                pl.setMetadata("no_fall_damage", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), (Object) true));
                Vector velocity = new Vector(0, pl.getWorld().getName().equals("world_duels") || pl.getWorld().getName().equals("world_duels2") ? 2 + level : 4 + level * 2, 0);
                Bukkit.getPluginManager().callEvent((Event) new PlayerVelocityEvent(pl, velocity));
                pl.setVelocity(velocity);
                PlayerPotionEffects.untrackPotionEffect(pl, PotionEffectType.SLOW);
                PlayerPotionEffects.untrackPotionEffect(pl, PotionEffectType.SLOW_DIGGING);
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * (level + 2), level));
                UUID uuid = player.getUniqueId();
                RocketEscape.rocket_escapists.add(uuid);
                Location loc = pl.getLocation();
                try {
                    ParticleEffect.CLOUD.sendToPlayers(Bukkit.getOnlinePlayers(), loc, (float) Math.random() * 2.0f, (float) Math.random() * 2.0f, (float) Math.random() * 2.0f, 1.25f, 69);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                long flightDuration = level;
                pl.setMetadata("allow_flight", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), (Object) (System.currentTimeMillis() + flightDuration * 1000L)));
                pl.setAllowFlight(true);
                pl.setFlying(true);
                pl.setFlySpeed(0.2f + (float) level * 0.03f);
                Bukkit.getScheduler().runTaskLater((Plugin) Enchantments.getInstance(), () -> {
                    pl.setFlySpeed(0.1f);
                    pl.setFlying(false);
                    pl.setAllowFlight(false);
                }, 20L * flightDuration);
                Bukkit.getScheduler().runTaskLater((Plugin) Enchantments.getInstance(), () -> {
                    Player onlinePlayer = Bukkit.getPlayer((UUID) uuid);
                    if (onlinePlayer != null) {
                        try {
                            ParticleEffect.CLOUD.sendToPlayers(Bukkit.getOnlinePlayers(), onlinePlayer.getLocation(), (float) Math.random() * 2.0f, (float) Math.random() * 2.0f, (float) Math.random() * 2.0f, 1.25f, 69);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }

                    }
                    RocketEscape.rocket_escapists.remove(uuid);
                }, (long) (20L * (level + 2) + 5));
            }
        }
    }
}

