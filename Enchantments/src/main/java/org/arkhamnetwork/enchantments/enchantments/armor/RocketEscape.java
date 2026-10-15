/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.rit.sucy.CustomEnchantment
 *  me.cosmicpvp.CosmicFactionUtils.CosmicFactionUtils
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.World$Environment
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.util.Vector
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.rit.sucy.CustomEnchantment;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.libs.PlayerPotionEffects;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class RocketEscape
        extends CustomEnchantment {
    public static HashMap<String, Long> last_rocket_escape = new HashMap();
    public static HashSet<UUID> rocket_escapists = new HashSet();

    public RocketEscape() {
        super("Rocket Escape", GeneralUtil.boots, 3);
        this.max = 3;
        this.base = 25.0;
        this.interval = 5.0;
    }

    @Override
    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, org.bukkit.event.entity.EntityDamageEvent e) {
        if (player instanceof Player) {
            double dmg = e.getFinalDamage();
            if (player.getWorld().getEnvironment() == World.Environment.THE_END) {
                return;
            }
            if (player.hasMetadata("neutralized"))
                return;
            if (player.hasMetadata("inDungeonParkour")) {
                return;
            }
            if (player.getWorld().getName().equals("world_koth")) {
                return;
            }
            if (!(attacker instanceof Player)) return;

            if (player.getHealth() - dmg <= 0.0 && this.canEscape((Player) player)) {
                if (AstroItemSkinsAPI.hasSkinEquipped((Player) attacker, SkinType.HALLOWEEN_BACKPACK)) {
                    player.sendMessage(CC.translate("&c&l ** &7" + attacker.getName() + "'s Rocket Escape: &4&lBLOCKED &7[Cursed Wings] &c&l**"));
                    attacker.sendMessage(CC.translate("&c&l ** &7Rocket Escape: &4&lBLOCKED &7[" + player.getName() + "'s Cursed Wings] &c&l**"));
                    return;
                }

                if (AstroItemSkinsAPI.hasSkinEquipped((Player) attacker, SkinType.BACK_TO_SCHOOL_2022_BACKPACK)) {
                    player.sendMessage(CC.translate("&8&l ** &7" + attacker.getName() + "'s &8KEG &7Blocked Rocket Escape &8&l**"));
                    attacker.sendMessage(CC.translate("&8&l ** &8KEG &7Blocked " + player.getName() + "'s Rocket Escape &8&l**"));
                    return;
                }

                if (((Player) player).getPlayer().getInventory().getChestplate() != null && (AstroItemSkinsAPI.hasSkinEquipped(((Player) player).getPlayer(), SkinType.THANKSGIVING_2021_BACKPACK) || AstroItemSkinsAPI.hasSkinEquipped(((Player) player).getPlayer(), SkinType.THANKSGIVING_2021_MUTATED_BACKPACK))) {
                    e.setDamage(e.getDamage()*1.5);
                    player.sendMessage(CC.translate("&2&l* TOMMY TAIL [&7Heroic Rocket Escape 2x Damage&2&l] *"));
                }

                Player pl = (Player) player;
                last_rocket_escape.put(pl.getUniqueId().toString(), System.currentTimeMillis());
                if (pl.hasMetadata("sabotage") && System.currentTimeMillis() - pl.getMetadata("sabotage").get(0).asLong() <= 1000L) {
                    int sabotageLevel = pl.getMetadata("sabotageLevel").get(0).asInt();
                    double negationChance = (double) sabotageLevel * 0.1;
                    if (Math.random() < negationChance) {
                        pl.sendMessage(CC.RedB + " ** " + CC.Gray + "Rocket Escape:" + CC.RedB + " SABOTAGED **");
                        return;
                    }
                }
                e.setDamage(0.0);
                pl.sendMessage("");
                pl.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "Your Rocket Escape boots have activated, recover while they last!");
                pl.sendMessage("");
                pl.playSound(pl.getLocation(), Sound.EXPLODE, 1.0f, 0.54f);
                pl.setMetadata("no_fall_damage", new FixedMetadataValue(Enchantments.getInstance(), true));
                pl.setVelocity(new Vector(0, pl.getWorld().getName().equals("world_duels") || pl.getWorld().getName().equals("world_duels2") ? 2 + level : 4 + level * 2, 0));
                PlayerPotionEffects.untrackPotionEffect(pl, PotionEffectType.SLOW);
                PlayerPotionEffects.untrackPotionEffect(pl, PotionEffectType.SLOW_DIGGING);
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * (level + 2), level));
                final UUID uuid = player.getUniqueId();
                rocket_escapists.add(uuid);
                Location loc = pl.getLocation();
                try {
                    //ParticleEffect.CLOUD.sendToPlayers(Bukkit.getOnlinePlayers(), loc, (float) Math.random() * 2.0f, (float) Math.random() * 2.0f, (float) Math.random() * 2.0f, 1.25f, 69);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                Bukkit.getScheduler().runTaskLater(Enchantments.getInstance(), new Runnable() {

                    @Override
                    public void run() {
                        if (Bukkit.getPlayer(uuid) != null) {
                            try {
                                //ParticleEffect.CLOUD.sendToPlayers(Bukkit.getOnlinePlayers(), loc, (float) Math.random() * 2.0f, (float) Math.random() * 2.0f, (float) Math.random() * 2.0f, 1.25f, 69);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }

                        }
                        rocket_escapists.remove(uuid);
                    }
                }, 20L * (level + 2) + 5);
            }
        }
    }

    private boolean canEscape(Player pl) {
        if (!last_rocket_escape.containsKey(pl.getUniqueId().toString())) {
            return true;
        }
        return System.currentTimeMillis() - last_rocket_escape.get(pl.getUniqueId().toString()) > 30000L;
    }

    public static boolean isEscaping(Player pl) {
        return rocket_escapists.contains(pl.getUniqueId());
    }
}

