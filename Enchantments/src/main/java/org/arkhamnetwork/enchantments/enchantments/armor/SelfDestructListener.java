/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.massivecraft.factions.FPlayer
 *  com.massivecraft.factions.FPlayers
 *  com.massivecraft.factions.iface.RelationParticipator
 *  com.massivecraft.factions.net.astropvp.armorsets.struct.Relation
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Sound
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityExplodeEvent
 *  org.bukkit.metadata.MetadataValue
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.perms.Relation;
import de.inventivegames.particle.ParticleEffect;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.arkhamnetwork.enchantments.util.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.metadata.MetadataValue;

final class SelfDestructListener
implements Listener {
    SelfDestructListener() {
    }

    @EventHandler
    public void onBlockExplode(EntityExplodeEvent e) {
        if (e.getEntity() != null && SelfDestruct.tntList.containsKey(e.getEntity().getUniqueId())) {
            e.setYield(0.0f);
            e.blockList().clear();
            if (WorldGuardUtils.isPvPDisabled(e.getLocation())) {
                return;
            }
            Location loc = e.getLocation();
            loc.getWorld().playSound(loc, Sound.EXPLODE, 1.0f, 1.0f);

            Player pExploder = SelfDestruct.tntList.get(e.getEntity().getUniqueId());
            FPlayer fp = pExploder != null ? (FPlayer)FPlayers.getInstance().getByPlayer(pExploder) : null;
            for (Entity ent : e.getEntity().getNearbyEntities(4.0, 4.0, 4.0)) {
                FPlayer fpEnt;
                if (!(ent instanceof LivingEntity) || ent.hasMetadata("spectator") || ent instanceof Player && fp != null && fp.getRelationTo((RelationParticipator)(fpEnt = (FPlayer)FPlayers.getInstance().getByPlayer((Player) ent))).isAtLeast(Relation.TRUCE) || WorldGuardUtils.isPvPDisabled(ent.getLocation())) continue;
                ((LivingEntity)ent).damage(10.0);
                ((LivingEntity)ent).setFireTicks(40);
                GeneralUtil.pushAwayEntity((LivingEntity)ent, e.getEntity(), (double)1.7f);
            }
            e.getEntity().remove();
        }
    }
}

