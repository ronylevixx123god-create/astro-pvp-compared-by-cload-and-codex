package org.arkhamnetwork.enchantments.enchantments.axes;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.perms.Relation;
import com.rit.sucy.*;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.bukkit.metadata.*;
import org.bukkit.potion.*;
import com.cosmicpvp.cosmicutils.utils.*;
import java.util.*;
import org.bukkit.*;

public class Pummel extends CustomEnchantment
{
    public Pummel() {
        super("Pummel", GeneralUtil.axe, 7);
        this.max = 3;
        this.base = 20.0;
        this.interval = 5.0;
    }

    public void applyEffect(final LivingEntity player, final LivingEntity playerHit, final int level, final EntityDamageByEntityEvent e) {
        final double random = Math.random();
        double chance = 0.06 * level;
        final double duration = level * 1.5;
        if (!(playerHit instanceof Player)) {
            return;
        }
        if (AstroItemSkinsAPI.hasSkinEquipped((Player)playerHit, SkinType.VIRUS_HELMET_HAZMAT) || playerHit.hasMetadata("immune_freeze")) {
            return;
        }
        final int radius = level;
        if (random < chance) {
            final FPlayer fplayer = (FPlayer)FPlayers.getInstance().getByOfflinePlayer((OfflinePlayer)player);
            for (final Entity ent : playerHit.getNearbyEntities(radius, radius, radius)) {
                if (ent.equals(player)) {
                    continue;
                }
                if (!(ent instanceof LivingEntity)) {
                    continue;
                }
                final LivingEntity le = (LivingEntity)ent;
                if (le instanceof Player) {
                    final Player p = (Player)le;
                    if (p.hasMetadata("metaphysicalEnchant")) {
                        final int metaphysicalLevel = p.getMetadata("metaphysicalEnchant").get(0).asInt();
                        chance -= metaphysicalLevel * 0.04;
                        if (random > chance) {
                            p.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "** METAPHYSICAL (" + ChatColor.DARK_GRAY + "Pummel blocked!" + ChatColor.BOLD + ") **");
                            continue;
                        }
                    }
                    final FPlayer fp = (FPlayer) FPlayers.getInstance().getByOfflinePlayer((OfflinePlayer)p);
                    if (fplayer.getRelationTo(fp).isAtLeast(Relation.TRUCE)) {
                        continue;
                    }

                    //le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int)duration * 20, level - 1));
                }
                final Location l = playerHit.getLocation().add(0.0, 0.5, 0.0);
                final int blockId = playerHit.getLocation().getBlock().getTypeId();
                WorldUtil.sendWorldEffect(l.getBlock(), (blockId != 0) ? blockId : 1, 2001);
            }
        }
    }
}
