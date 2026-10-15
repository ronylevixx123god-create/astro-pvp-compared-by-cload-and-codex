package org.arkhamnetwork.enchantments.enchantments.heroic;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.perms.Relation;
import de.inventivegames.particle.ParticleEffect;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.ArmorUtil;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Random;

public class HeroicPlagueCarrier extends HeroicCustomEnchantment {
    public HeroicPlagueCarrier() {
        super("Epidemic Carrier", "Plague Carrier", GeneralUtil.leggings, 2);
        this.max = 8;
        this.base = 25.0;
        this.interval = 10.0;
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
        if (player instanceof Player) {

            Player p = (Player)player;
            double dmg = e.getFinalDamage();
            if (player.getHealth() - dmg <= 0.0) {
                if (p.hasMetadata("PlagueCarrier") && System.currentTimeMillis() - ((MetadataValue)p.getMetadata("PlagueCarrier").get(0)).asLong() <= 10000L) {
                    return;
                }
                p.setMetadata("PlagueCarrier", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)System.currentTimeMillis()));
                int radius = Math.round((float)level * 1.5f);
                ArrayList<LivingEntity> victims = new ArrayList<LivingEntity>();
                FPlayer fp = (FPlayer) FPlayers.getInstance().getByPlayer(p);
                for (Entity ent : player.getNearbyEntities((double)radius, (double)radius, (double)radius)) {
                    Player pEnt;
                    FPlayer fpEnt;
                    if (!(ent instanceof LivingEntity) || ent instanceof Player && (fpEnt = (FPlayer)FPlayers.getInstance().getByPlayer(((Player)ent))).getRelationTo((RelationParticipator)fp).isAtLeast(Relation.TRUCE)) continue;
                    victims.add((LivingEntity)ent);
                }
                try {
                    ParticleEffect.CLOUD.sendToPlayers(Bukkit.getOnlinePlayers(), p.getLocation().add(0.0, 1.0, 0.0),1.0f, 1.0f, 1.0f, 0.1f, 75);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                p.getWorld().playSound(p.getLocation(), Sound.CREEPER_HISS, 2.0f, 0.75f);
                for (LivingEntity victim : victims) {
                    if (!(victim instanceof Player)) continue;
                    ((Player)victim).sendMessage(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.LIGHT_PURPLE + p.getName() + " has died, and was a Epidemic Carrier! RUN!");
                    ((Player)victim).getWorld().playSound(victim.getLocation(), Sound.CREEPER_HISS, 1.6f, 0.75f);
                    ((Player)victim).addPotionEffect(new PotionEffect(PotionEffectType.POISON, (2 + level) * 20, level >= 7 ? 1 : 0));
                }
                for (int plagueCarriers = radius / 2; plagueCarriers > 0; --plagueCarriers) {
                    this.spawnPlagueCreeper(p.getLocation().add(0.0, 1.0, 0.0), level);
                }
                if (p.getHealth() > 0.0) {
                    p.damage(p.getHealth());
                }
            }
        }
    }

    private void spawnPlagueCreeper(Location l, int level) {
        Creeper c = (Creeper)l.getWorld().spawnEntity(l, EntityType.CREEPER);
        c.setMetadata("plagueCarrier", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)level));
        c.setCustomName((Object)ChatColor.LIGHT_PURPLE + "Epidemic Carrier");
        c.setCanPickupItems(false);
        c.setMetadata("no_loot", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)true));
        c.setCustomNameVisible(true);
        c.setPowered(true);

        try {
            ParticleEffect.CLOUD.sendToPlayers(Bukkit.getOnlinePlayers(), c.getLocation().add(0.0, 1.0, 0.0), 0.5f, 0.5f, 0.5f, 0.1f, 20);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        if (c != null && !c.isDead()) {
            Bukkit.getScheduler().runTaskLater(Enchantments.getInstance(), c::remove, 600L);
        }

    }

    private Location getNearbyLocation(Location l, int radius, int y_boost) {
        Random rand = new Random();
        Location rand_loc = l.clone();
        rand_loc.add((double)((rand.nextBoolean() ? 1 : -1) * (rand.nextInt(radius) + 1)), 0.0, (double)((rand.nextBoolean() ? 1 : -1) * (rand.nextInt(radius) + 1)));
        rand_loc.add(0.5, (double)y_boost, 0.5);
        while ((rand_loc.getBlock().getType() != Material.AIR || rand_loc.getBlock().getLocation().subtract(0.0, 1.0, 0.0).getBlock().getType() != Material.AIR) && rand_loc.getY() < 255.0) {
            rand_loc = rand_loc.add(0.0, 1.0, 0.0);
        }
        return rand_loc;
    }
}
