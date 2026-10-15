/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.gmail.nossr50.mcMMO
 *  com.rit.sucy.CustomEnchantment
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.IronGolem
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.rit.sucy.CustomEnchantment;
import de.inventivegames.particle.ParticleEffect;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Guardians
        extends CustomEnchantment {
    public Guardians() {
        super("Guardians", GeneralUtil.armor, 2);
        this.max = 10;
        this.base = 20.0;
        this.interval = 5.0;
        Bukkit.getPluginManager().registerEvents((Listener) new GuardiansListener(), (Plugin) Enchantments.getInstance());
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
        if (e instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) e).getDamager() instanceof Player) {
            if (player.hasMetadata("noDefenseProcs"))
                return;
            if (player.hasMetadata("exterminated") || player.hasMetadata("neutralized"))
                return;
            if (player.hasMetadata("guardianEvent") && System.currentTimeMillis() < ((MetadataValue) player.getMetadata("guardianEvent").get(0)).asLong()) {
                return;
            }
            if (player.hasMetadata("inDungeonParkour")) {
                return;
            }
            if (attacker == null) return;
            if (((Player) attacker).getInventory().getChestplate() != null) {
                if (AstroItemSkinsAPI.hasSkinEquipped((Player) attacker, SkinType.BACK_TO_SCHOOL_2021_AMULET))
                    return;
            }

            if (AstroItemSkinsAPI.hasSkinEquipped((Player) attacker, SkinType.JULY_2021_BELT)) {
                attacker.sendMessage(CC.translate("&9&l* INFINITY BELT &7Blocked &eGuardians &9&l*"));
                return;
            }

            boolean KOTH_or_duel = player.getWorld().getName().equals("world_koth") || player.getWorld().getName().equals("world_duels") || player.getWorld().getName().equals("world_duels2");
            double chance = 0.001;
            if (KOTH_or_duel) {
                chance /= 2.0;
            }
            if (Math.random() < chance) {
                player.setMetadata("guardianEvent", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), (Object) (System.currentTimeMillis() + 10000L)));
                int guardian_count = level / 10 + 1;
                if (KOTH_or_duel) {
                    guardian_count = 1;
                }
                Player p = (Player) player;
                Location spawnLoc = player.getLocation().add(0.0, 2.0, 0.0);
                try {
                    //ParticleEffect.SPELL.sendToPlayers(Bukkit.getOnlinePlayers(), spawnLoc, (float)Math.random(), (float)Math.random(), (float) Math.random(), 0.64f, 45);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                while (guardian_count > 0) {
                    --guardian_count;
                    if (spawnLoc.getChunk().getEntities().length >= 100) continue;
                    IronGolem ig = Guardians.spawnGuardianEntity(spawnLoc, p, level);
                    ig.setTarget(attacker);
                }
            }
        }
    }

    public static IronGolem spawnGuardianEntity(Location spawnLocation, Player summoner, int level) {
        IronGolem ig = (IronGolem) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.IRON_GOLEM);
        ig.setMetadata("guardianSummoner", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), (Object) summoner.getName()));
        ig.setMetadata("no_loot", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), (Object) true));
        ig.setCanPickupItems(false);
        ig.setCustomName(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + summoner.getName() + "'s Guardian");
        ig.setCustomNameVisible(true);
        double customHp = 50;
        ig.setMaxHealth(customHp);
        ig.setHealth(customHp);
        ig.getWorld().playSound(ig.getLocation(), Sound.IRONGOLEM_DEATH, 1.0f, 0.55f);
        ig.setPlayerCreated(false);
        ig.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
        if (level >= 4) {
            ig.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
        }
        if (level >= 6) {
            ig.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0));
        }
        if (level >= 8) {
            ig.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
        }
        if (level == 10) {
            ig.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));
        }
        final IronGolem golem = ig;
        int target_task_id = new BukkitRunnable() {

            public void run() {
                if (!golem.isDead()) {
                    Player ptarg = Guardians.getClosestPlayer((Entity) golem);
                    if (ptarg != null) {
                        golem.setTarget((LivingEntity) ptarg);
                    }
                } else {
                    this.cancel();
                    if (golem.hasMetadata("targetTaskID")) {
                        golem.removeMetadata("targetTaskID", (Plugin) Enchantments.getInstance());
                    }
                }
            }
        }.runTaskTimer((Plugin) Enchantments.getInstance(), 20L, 20L).getTaskId();
        ig.setMetadata("targetTaskID", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), (Object) target_task_id));
        final IronGolem igf = ig;
        Bukkit.getScheduler().runTaskLater((Plugin) Enchantments.getInstance(), new Runnable() {

            @Override
            public void run() {
                igf.damage(igf.getHealth());
                if (!igf.isDead()) {
                    igf.remove();
                }
            }
        }, 600L);
        return ig;
    }

    private static Player getClosestPlayer(Entity boss) {
        if (!boss.hasMetadata("guardianSummoner")) {
            return null;
        }
        String summoner = ((MetadataValue) boss.getMetadata("guardianSummoner").get(0)).asString();
        Player pSummoner = Bukkit.getPlayer((String) summoner);
        if (pSummoner != null) {
            for (Entity ent : pSummoner.getNearbyEntities(9.0, 9.0, 9.0)) {
                if (!(ent instanceof Player) || ent.hasMetadata("spectator") || ((Player) ent).getName().equals(summoner) || GeneralUtil.isAtleastTruce(pSummoner, (Player) ent))
                    continue;
                return (Player) ent;
            }
        }
        return null;
    }
}

