package org.arkhamnetwork.enchantments.enchantments.armor;

import com.earth2me.essentials.Essentials;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.perms.Relation;
import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.arkhamnetwork.enchantments.util.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class Destruction extends CustomEnchantment {
    private static HashMap<Integer, BukkitTask> destructionTasks;

    static {
        Destruction.destructionTasks = new HashMap<Integer, BukkitTask>();
    }

    Essentials ess3;
    private Multimap<Integer, Player> destructionPlayers;

    public Destruction() {
        super("Destruction", GeneralUtil.helmets, 2);
        this.destructionPlayers = HashMultimap.create();
        this.max = 5;
        this.base = 10.0;
        this.interval = 5.0;
        this.ess3 = (Bukkit.getPluginManager().isPluginEnabled("Essentials") ? ((Essentials) Bukkit.getPluginManager().getPlugin("Essentials")) : null);
        for (int i = 1; i <= this.max; ++i) {
            final int level = i;
            final BukkitTask existing = Destruction.destructionTasks.get(i);
            if (existing != null) {
                existing.cancel();
            }
            final BukkitTask task = new BukkitRunnable() {
                public void run() {
                    final Collection<Player> leveledPlayers = (Collection<Player>) Destruction.this.destructionPlayers.get(level);
                    if (leveledPlayers == null || leveledPlayers.isEmpty()) {
                        return;
                    }
                    final int radius = level;

                    leveledPlayers.forEach(player -> {
                        int val$level = level;
                        Player p = player;
                        if (player.isValid() && !player.isDead() && !player.hasMetadata("spectator") && !Destruction.this.ess3.getUser(player).isGodModeEnabled()) {
                            FPlayer fplayer = (FPlayer) FPlayers.getInstance().getByPlayer( player);
                            int n = 4;
                            Iterator<Entity> iterator = player.getNearbyEntities((double) n, (double) n, (double) n).iterator();
                            while (iterator.hasNext()) {
                                Entity ent = iterator.next();
                                if (ent instanceof Player) {
                                    Player pl = (Player) ent;
                                    if (p.getGameMode() == GameMode.SURVIVAL) {
                                        if (p.hasMetadata("NPC")) {
                                            continue;
                                        }
                                        else if (!p.hasMetadata("spectator") && !Destruction.this.ess3.getUser(p).isVanished()) {
                                            FPlayer fp = (FPlayer) FPlayers.getInstance().getByPlayer( p);
                                            if (fp.getFaction().getRelationTo((RelationParticipator) fplayer.getFaction()).isAtMost(Relation.NEUTRAL) && !WorldGuardUtils.isPvPDisabled(p.getLocation())) {
                                                if (p.getHealth() > 0.0 && !p.isDead()) {
                                                    //p.damage((val$level == 5) ? 2.0 : 1.0);
                                                    try {
                                                        de.inventivegames.particle.ParticleEffect.LAVA.sendToPlayers(Bukkit.getOnlinePlayers(),p.getLocation(), 0,1,0,0.5f,20);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                if (val$level >= 1 && Math.random() <= 0.15) {
                                                    //PotionEffect potionEffect = new PotionEffect(PotionEffectType.SLOW, val$level * 20, (int) ((val$level > 2) ? 1 : 0));
                                                    try {
                                                        de.inventivegames.particle.ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(),p.getLocation(), 0,1,0,0.5f,10);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                if (val$level >= 2 && Math.random() <= 0.1) {
                                                    try {
                                                        de.inventivegames.particle.ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(),p.getLocation(), 0,1,0,0.5f,10);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                if (val$level >= 3 && Math.random() <= 0.085) {
                                                    //p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, val$level * 20 * 2, 1));
                                                    try {
                                                        de.inventivegames.particle.ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(),p.getLocation(), 0,1,0,0.5f,10);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
            }.runTaskTimer((Plugin) Enchantments.getInstance(), 60L * (5 / level), 60L * (5 / level));
            Destruction.destructionTasks.put(i, task);
        }
    }

    public void applyEquipEffect(final Player player, final int level) {
        player.setMetadata(this.name() + "_enchant", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), (Object) level));
        this.destructionPlayers.put(level, player);
    }

    public void applyUnequipEffect(final Player player, final int level) {
        player.removeMetadata(this.name() + "_enchant", (Plugin) Enchantments.getInstance());
        this.destructionPlayers.remove((Object) level, (Object) player);
    }
}