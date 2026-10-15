package net.astropvp.armorsets.listeners;

import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.perms.Relation;
import me.astropvp.astropvpskins.utils.ItemSkinUtils;
import me.ifamasssxd.cosmicduel.DuelAPI;
import net.astropvp.armorsets.AstroArmorSets;
import net.astropvp.armorsets.AstroArmorSetsAPI;
import net.astropvp.armorsets.struct.ArmorSetType;
import net.astropvp.armorsets.struct.cache.ArmorSetCache;
import net.astropvp.armorsets.struct.sets.TravelerArmorSet;
import net.astropvp.armorsets.utils.config.ConfigManager;
import net.astropvp.core.AstroCore;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ArmorSetDamageListener implements Listener {
    @EventHandler(
            priority = EventPriority.LOWEST)
    public void onAttack(EntityDamageByEntityEvent e) {
        Entity attacker = e.getDamager();
        Entity attacked = e.getEntity();

        if (attacker instanceof Player) {
            if (DuelAPI.isSpectator((Player) attacker)) return;
        }
        if (attacker.getWorld().getName().equals("world_outpost_pluto")) {
            return;
        }
        if (e.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(attacked.getLocation()) || !WorldGuardUtils.isPvPEnabled(attacker.getLocation()))
            return;
        if (attacker instanceof Player && attacked instanceof Player) {
            if (!ItemSkinUtils.canPvP((Player)attacker, (Player)attacked, e)) {
                return;
            }
        }

        ArmorSetCache cache;
        if (attacker instanceof Player) {
            cache = (ArmorSetCache) AstroCore.getInstance().getCacheManager().getCachedPlayer((Player) attacker).getCache("armor_set");
            if (cache.getCurrentArmorSet() != null) {
                cache.getCurrentArmorSet().getArmorSet().onAttack((Player) attacker, attacked, e);
            }

            if (!cache.getCurrentSetCrystals().isEmpty()) {
                Map<ArmorSetType, Integer> crystals = cache.getCurrentSetCrystals();
                for (Map.Entry<ArmorSetType, Integer> entry : crystals.entrySet()) {
                    ArmorSetType armorSetType = entry.getKey();
                    int amount = entry.getValue();
                    armorSetType.getArmorSet().onAttackCrystal((Player) attacker, attacked, e, amount);

                }
            }

        }

    }


    @EventHandler
    public void onBlockDamage(EntityChangeBlockEvent event) {
        TravelerArmorSet travelerArmorSet = (TravelerArmorSet) ArmorSetType.DIMENSIONAL_TRAVELER.getArmorSet();
        if (event.getEntity().getType() != EntityType.FALLING_BLOCK) return;
        FallingBlock fallingBlock = (FallingBlock)event.getEntity();

        TravelerArmorSet.TravelerFallingBlock block = travelerArmorSet.getTravelerBlock(fallingBlock.getUniqueId());

        if (block == null) return;

        event.setCancelled(true);
        travelerArmorSet.getFallingBlocks().remove(block.getUUID());
        FPlayer blockFPlayer = FPlayers.getInstance().getByPlayer(block.getPlayer());

        List<Player> nearbyEnemies = event.getBlock().getLocation().getWorld()
                .getNearbyEntities(event.getBlock().getLocation(), 2.0D, 2.0D, 2.0D)
                .stream()
                .filter(entity -> entity instanceof Player)
                .map(entity -> (Player)entity).filter(player -> {
                    FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
                    Relation relationTo = fPlayer.getRelationTo(blockFPlayer);
                    return ((relationTo == Relation.ENEMY || relationTo == Relation.NEUTRAL) && !WorldGuardUtils.isPvPDisabled(player.getLocation()));
                }).collect(Collectors.toList());

        nearbyEnemies.remove(block.getPlayer());
        nearbyEnemies.forEach(player -> {
            player.damage(1.0D, block.getPlayer());
            player.damage(travelerArmorSet.getRandomDamage());
        });
    }

    @EventHandler(
            priority = EventPriority.LOWEST)
    public void onDamaged(EntityDamageByEntityEvent e) throws Exception {
        Entity attacker = e.getDamager();
        Entity attacked = e.getEntity();
        if (attacker instanceof Player) {
            if (DuelAPI.isSpectator((Player) attacker)) return;
        }
        if (e.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(attacked.getLocation()) || !WorldGuardUtils.isPvPEnabled(attacker.getLocation()))
            return;


        if (attacker.getWorld().getName().equals("world_outpost_pluto")) {
            return;
        }
        ArmorSetCache cache;
        if (attacker instanceof Player && attacked instanceof Player) {
            cache = (ArmorSetCache) AstroCore.getInstance().getCacheManager().getCachedPlayer((Player) attacked).getCache("armor_set");
            //armor set damaged
            if (cache != null && cache.getCurrentArmorSet() != null) {
                cache.getCurrentArmorSet().getArmorSet().onDamaged(attacker, (Player) attacked, e);
            }

            //crystals damaged
            if (cache != null && !cache.getCurrentSetCrystals().isEmpty()) {
                Map<ArmorSetType, Integer> crystals = AstroArmorSetsAPI.getBonusCrystalsEquippedIncludingCorruption((Player) attacker, (Player) attacked);
                //if (attacked.hasMetadata("blackout")) return;
                for (Map.Entry<ArmorSetType, Integer> entry : crystals.entrySet()) {
                    ArmorSetType armorSetType = entry.getKey();
                    int amount = entry.getValue();

                    if (attacked.hasMetadata("noDefensiveCrystals") && AstroArmorSetsAPI.isDefensiveCrystal(armorSetType)) continue;
                    armorSetType.getArmorSet().onDamagedCrystal(attacker, (Player) attacked, e, amount);

                }
            }
        }

    }


    @EventHandler(
            priority = EventPriority.LOW)
    public void onHitLadderSystem(EntityDamageByEntityEvent e) {
        Entity attacker = e.getDamager();
        Entity attacked = e.getEntity();

        if (attacker instanceof Player) {
            if (DuelAPI.isSpectator((Player) attacker)) return;
        }
        if (attacker.getWorld().getName().equals("world_outpost_pluto")) {
            return;
        }
        if (e.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(attacked.getLocation()) || !WorldGuardUtils.isPvPEnabled(attacker.getLocation()))
            return;
        if (attacker instanceof Player && attacked instanceof Player) {
            if (!ItemSkinUtils.canPvP((Player)attacker, (Player)attacked, e)) {
                return;
            }
        }

        ArmorSetCache cacheAttacker;
        ArmorSetCache cacheAttacked;
        if (attacker instanceof Player && attacked instanceof Player) {
            cacheAttacker = (ArmorSetCache) AstroCore.getInstance().getCacheManager().getCachedPlayer((Player) attacker).getCache("armor_set");
            cacheAttacked = (ArmorSetCache) AstroCore.getInstance().getCacheManager().getCachedPlayer((Player) attacked).getCache("armor_set");

            //add armorset pvpladder multi

            //crystals pvp
            if(cacheAttacker == null || cacheAttacked == null) return;
            if (!cacheAttacker.getCurrentSetCrystals().isEmpty() && !cacheAttacked.getCurrentSetCrystals().isEmpty()) {
                int ladderAttacker = 0;
                int ladderAttacked = 0;
                Map<ArmorSetType, Integer> crystalsAttacker = cacheAttacker.getCurrentSetCrystals();
                Map<ArmorSetType, Integer> crystalsAttacked = cacheAttacked.getCurrentSetCrystals();

                for (Map.Entry<ArmorSetType, Integer> entry : crystalsAttacker.entrySet()) {
                    ArmorSetType armorSetType = entry.getKey();
                    int amount = entry.getValue();
                    ladderAttacker +=  armorSetType.getArmorSet().getLadderTier() * amount;
                }
                for (Map.Entry<ArmorSetType, Integer> entry : crystalsAttacked.entrySet()) {
                    ArmorSetType armorSetType = entry.getKey();
                    int amount = entry.getValue();
                    ladderAttacked +=  armorSetType.getArmorSet().getLadderTier() * amount;
                }

                int ladderDifference = ladderAttacker - ladderAttacked;
                if (ladderDifference < 0) ladderDifference=0;
                double damagePercentChange = ladderDifference * AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("crystalsLadderDamageDifference");
                e.setDamage(e.getDamage() * (1 + (damagePercentChange  *  0.01)));
            }
        }
    }



}