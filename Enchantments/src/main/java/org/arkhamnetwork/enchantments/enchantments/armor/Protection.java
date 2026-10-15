package org.arkhamnetwork.enchantments.enchantments.armor;

import com.cosmicpvp.cosmicutils.utils.ParticleEffect;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.perms.Relation;
import com.rit.sucy.CustomEnchantment;

import java.util.Collection;
import java.util.HashMap;

import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class Protection extends CustomEnchantment {
    private static HashMap<Integer, BukkitTask> protectionTasks = new HashMap<>();

    private Multimap<Integer, Player> protectionPlayers = HashMultimap.create();

    public Protection() {
        super("Protection", GeneralUtil.armor, 2);
        this.max = 5;
        this.base = 10.0D;
        this.interval = 5.0D;
        /*for (int i = 1; i <= this.max; i++) {
            final int level = i;
            BukkitTask existing = protectionTasks.get(Integer.valueOf(i));
            if (existing != null)
                existing.cancel();
            final int radius = level * 2;
            BukkitTask task = (new BukkitRunnable() {
                public void run() {
                    if (Math.random() < 0.1) {
                        Collection<Player> leveledPlayers = Protection.this.protectionPlayers.get(Integer.valueOf(level));
                        if (leveledPlayers == null || leveledPlayers.isEmpty())
                            return;
                        leveledPlayers.forEach(player -> {
                            if (!player.isValid() || player.isDead() || player.hasMetadata("spectator") || player.hasMetadata("in_duel"))
                                return;
                            FPlayer fplayer = (FPlayer) FPlayers.getInstance().getByPlayer( player);
                            int amountToHeal = (level > 4) ? 2 : 1;
                            for (Entity ent : player.getNearbyEntities(radius, radius, radius)) {
                                if (ent instanceof Player) {
                                    Player p = (Player) ent;
                                    FPlayer fp = (FPlayer) FPlayers.getInstance().getByPlayer( p);
                                    if (fp != null && fp.getFaction() != null && fp.getFaction().getRelationTo((RelationParticipator) fplayer.getFaction()).isAtLeast(Relation.ALLY)) {
                                        if (p.getHealth() + amountToHeal < p.getMaxHealth() && p.getHealth() > 0.0D && !p.isDead()) {
                                            //p.setHealth(p.getHealth() + amountToHeal);
                                        }
                                        if (level >= 1 && Math.random() <= 0.05D) {
                                            //p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, level * 20, (level > 2) ? 1 : 0));
                                        }
                                        if (level >= 2 && Math.random() <= 0.05D) {
                                            //p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, level * 20 * 2, level + 1));
                                        }
                                        if (level >= 3 && Math.random() <= 0.005D) {
                                            //p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, level * 20 * 2, level + 1));
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }).runTaskTimer((Plugin) Enchantments.getInstance(), (40 * 5 / level), (40 * 5 / level));
            protectionTasks.put(Integer.valueOf(i), task);
        }*/
    }

    public void applyEquipEffect(Player player, int level) {
        //player.setMetadata(name() + "_enchant", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), Integer.valueOf(level)));
        //this.protectionPlayers.put(Integer.valueOf(level), player);
    }

    public void applyUnequipEffect(Player player, int level) {
        //player.removeMetadata(name() + "_enchant", (Plugin) Enchantments.getInstance());
        //this.protectionPlayers.remove(Integer.valueOf(level), player);
    }
}