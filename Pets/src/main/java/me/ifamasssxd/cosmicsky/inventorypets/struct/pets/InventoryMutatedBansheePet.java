package me.ifamasssxd.cosmicsky.inventorypets.struct.pets;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.TimeUtils;
import com.google.common.collect.Lists;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.perms.Relation;
import com.rit.sucy.EnchantmentAPI;
import me.ifamasssxd.cosmicmasks.MaskType;
import me.ifamasssxd.cosmicsky.inventorypets.CosmicSkyInventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.PetException;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.PrefixedPetException;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPet;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPetItem;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.utils.DamageIndicatorUtils;
import org.arkhamnetwork.Arkkit.patches.CombatLog;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class InventoryMutatedBansheePet extends InventoryPet {

    public InventoryPets getOwner() {
        return InventoryPets.MUTATED_BANSHEE;
    }

    public List<String> getPetDescription() {
        return Lists.newArrayList(CC.Gray + "Call upon the deadly screeming Banshee.",
                CC.Gray,
                CC.DarkGreenB + "Ability",
                CC.Gray + "Activates " + CC.GreenB + "[Mutated RKO]:",
                CC.Green + "* " + CC.Gray + "1.75x DMG to all players (10s)",
                CC.Green + "* " + CC.Gray + "Bypass all ItemSkins",
                CC.Green + "* " + CC.Gray + "Immune to freezes (10s)",
                CC.Green + "* " + CC.Gray + "AoE (8x8) Silence IV (10s)",
                CC.Green + "* " + CC.Gray + "Negates enemy Phoenix (10s)",
                CC.Green + "* " + CC.Gray + "Negates enemy Holy White Scrolls (10s)",
                CC.Gray,
                CC.DarkGreenB + "Cooldown", CC.Gray + " " + TimeUtils.formatSeconds(getCooldown()));
    }


    public int getExpForLevel(int level) {
        return 700 + 100 * level;
    }

    public int getMaxLevel() {
        return 5;
    }

    public int getCooldown() {
        return 900;
    }

    public boolean onPetItemInteract(final Player player, ItemStack item, InventoryPetItem petItem, PlayerInteractEvent event) throws PetException {
        long lastUse = petItem.getLastUsed();
        long delay = TimeUnit.SECONDS.toMillis(this.getCooldown());
        long timeTillCanUse = delay - (System.currentTimeMillis() - lastUse);


        if (lastUse > 0L && timeTillCanUse > 0L && (!player.isOp() || !player.isSneaking()))
            throw new PrefixedPetException("Ability on cooldown: " + CC.RedU + TimeUtils.formatSeconds((int) (timeTillCanUse / 1000L)));
        List<Player> nearbyEnemies = Lists.newArrayList();

        Faction faction = FactionUtils.getFaction(player);

        for (Entity other : player.getNearbyEntities(8.0D, 8.0D, 8.0D)) {
            if (other instanceof Player) {
                Player nearPlayer = (Player) other;
                Faction nearFac = FactionUtils.getFaction(nearPlayer);
                if (nearPlayer.getGameMode() == GameMode.CREATIVE)
                    continue;
                if (nearFac != null && nearFac.getRelationTo(faction) != Relation.ENEMY && nearFac.getRelationTo(faction) != Relation.NEUTRAL)
                    continue;





/*                if (CosmicSkyInventoryPets.get().getCooldownUtils().isOnCooldown(nearPlayer, "bansheeCooldown")) {
                    player.sendMessage(CC.translate("&c&lPET: &c" + nearPlayer.getName() + " is still on banshee cooldown for &n" + CosmicSkyInventoryPets.get().getCooldownUtils().getRemainingTimeFormatted(nearPlayer, "bansheeCooldown") + "&c."));
                    continue;
                }*/

                nearbyEnemies.add(nearPlayer);
            }
        }

        if (nearbyEnemies.isEmpty()) {
            throw new PrefixedPetException("Could not find a valid target!");
        }

        petItem.setLastUsed(System.currentTimeMillis());


        int duration;


        if (MaskType.hasEquipped(player, "ELF")) {
            duration = 14;
        } else {
            duration = 10;
        }

        nearbyEnemies.forEach(enemy -> {
            startBansheeEffect(player, enemy, duration);

        });
        return true;
    }

    public boolean onTickInHotBar(Player var1, int var2, InventoryPetItem var3) {
        return false;
    }


    public static void startBansheeEffect(Player player, Player enemy, int duration) {

/*        if (CosmicSkyInventoryPets.get().getCooldownUtils().isOnCooldown(enemy, "bansheeCooldown")) {
            player.sendMessage(CC.translate("&c&lPET: &c" + enemy.getName() + " is still on banshee cooldown for &n" + CosmicSkyInventoryPets.get().getCooldownUtils().getRemainingTimeFormatted(enemy, "bansheeCooldown") + "&c."));
            return;
        }

        int bansheeCooldownInSeconds = 20;
        CosmicSkyInventoryPets.get().getCooldownUtils().add(enemy, "bansheeCooldown", bansheeCooldownInSeconds * 1000);*/


        enemy.playSound(enemy.getLocation(), Sound.WITHER_HURT, 1.0f, 0.25f);
        enemy.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "* SILENCED " + ChatColor.GRAY + " 7s] " + ChatColor.DARK_PURPLE + ChatColor.BOLD + "*");
        enemy.setMetadata("noDefenseProcs", new FixedMetadataValue(CosmicSkyInventoryPets.get(), Boolean.valueOf(true)));
        enemy.setMetadata("banshee_scrolls", new FixedMetadataValue(CosmicSkyInventoryPets.get(), Boolean.valueOf(true)));
        enemy.sendMessage("");
        enemy.sendMessage(CC.translate(String.format("&2&l*** " + player.getName() + "&4&l's &nMUTATED&r &a&lBANSHEE PET [&6" + duration + "s&a&l] - RKO! **", Integer.valueOf(duration))));
        enemy.sendMessage(CC.translate(String.format("&2( %ds Silence, Phoenix - BLOCKED, Holy White Scrolls - BLOCKED )", Integer.valueOf(duration))));
        enemy.sendMessage("");
        DamageIndicatorUtils.sendDamageIndicator(enemy, CC.translate("&a&l* &2&lMUTATED&r &a&lBANSHEE RKO [&7" + duration + "s&a&l] *"), "RKO");
        CombatLog.flagCombat(player);
        player.setMetadata("immune_freeze", new FixedMetadataValue(CosmicSkyInventoryPets.get(), Boolean.valueOf(true)));
        player.setMetadata("banshee_active", new FixedMetadataValue(CosmicSkyInventoryPets.get(), Boolean.valueOf(true)));
        player.setMetadata("mutated_banshee", new FixedMetadataValue(CosmicSkyInventoryPets.get(), Boolean.valueOf(true)));

        player.sendMessage("");
        player.sendMessage(CC.translate("&2&lPET: ** BANSHEE [&6" + duration + "s&2&l] - RKO! **"));
        player.sendMessage("");
        player.playSound(player.getLocation(), Sound.LAVA_POP, 1.1F, 1.0F);
        player.playSound(player.getLocation(), Sound.GHAST_SCREAM2, 2.0F, 2.0F);

        Bukkit.getScheduler().runTaskLater(CosmicSkyInventoryPets.get(), () -> {
            player.sendMessage(CC.translate("&2&lPET:  BANSHEE - RKO has worn off! "));
            player.removeMetadata("immune_freeze", CosmicSkyInventoryPets.get());
            player.removeMetadata("banshee_active", CosmicSkyInventoryPets.get());
            player.removeMetadata("mutated_banshee", CosmicSkyInventoryPets.get());

            EnchantmentAPI.getEnchantment("Gears").applyUnequipEffect(player, 5);

            enemy.removeMetadata("noDefenseProcs", CosmicSkyInventoryPets.get());
            enemy.removeMetadata("banshee_scrolls", CosmicSkyInventoryPets.get());
            enemy.sendMessage(CC.translate("&2&lPET: &a&l[RKO] &efrom " + player.getName() + "'s Mutated Banshee Pet &e&nEXPIRED!"));
        }, 20L * duration);


    }

}
