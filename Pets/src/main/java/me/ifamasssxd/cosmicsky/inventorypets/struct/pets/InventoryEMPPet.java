package me.ifamasssxd.cosmicsky.inventorypets.struct.pets;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.TimeUtils;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.google.common.collect.Lists;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.perms.Relation;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.skins.axes.GoldenAdmirer;
import me.astropvp.astropvpskins.objects.skins.helmets.UndertowHelmet;
import me.astropvp.astropvpskins.objects.skins.swords.SpinalTab;
import me.ifamasssxd.cosmicmasks.MaskType;
import me.ifamasssxd.cosmicsky.inventorypets.CosmicSkyInventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.NonPrefixedPetException;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.PetException;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.PrefixedPetException;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPet;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPetItem;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.utils.CrankedAmuletUtil;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.libs.RomanNumber;
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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class InventoryEMPPet extends InventoryPet {
    public InventoryPets getOwner() {
        return InventoryPets.GAIA;
    }

    public List getPetDescription() {
        return Lists.newArrayList(CC.Gray + "An old school pet",
                CC.Gray + "othat goes back in time...",
                "",
                CC.WhiteB + "Ability",
                CC.Gray + "Enemy skins that prevent the usage of",
                CC.Gray + "pets are disabled for 5 seconds.",
                CC.Gray + "This pet will work in a 6x6 radius.",
                "",
                CC.WhiteB + "Cooldown",
                CC.Gray + " 10 Minutes");
    }

    public int getExpForLevel(int level) {
        return 1000 + level * 500;
    }

    public int getMaxLevel() {
        return 5;
    }

    public int getCooldown() {
        return (int) TimeUnit.MINUTES.toSeconds(10L);
    }

    public boolean onPetItemInteract(final Player player, ItemStack item, InventoryPetItem petItem, PlayerInteractEvent event) throws PetException {
        long lastUse = petItem.getLastUsed();
        long delay = TimeUnit.SECONDS.toMillis((long) this.getCooldown());
        long timeTillCanUse = delay - (System.currentTimeMillis() - lastUse);


        if (lastUse > 0L && timeTillCanUse > 0L && (!player.isOp() || !player.isSneaking()))
            throw new PrefixedPetException("Ability on cooldown: " + CC.RedU + TimeUtils.formatSeconds((int) (timeTillCanUse / 1000L)));
        List<Player> nearbyEnemies = Lists.newArrayList();

        Faction faction = FactionUtils.getFaction(player);

        for (Entity other : player.getNearbyEntities(6.0D, 6.0D, 6.0D)) {
            if (other instanceof Player) {
                Player nearPlayer = (Player) other;
                Faction nearFac = FactionUtils.getFaction(nearPlayer);
                if (nearPlayer.getGameMode() == GameMode.CREATIVE)
                    continue;
                if (nearFac != null && nearFac.getRelationTo((RelationParticipator) faction) != Relation.ENEMY && nearFac.getRelationTo((RelationParticipator) faction) != Relation.NEUTRAL)
                    continue;
                nearbyEnemies.add(nearPlayer);
            }
        }

        if (nearbyEnemies.isEmpty()) {
            throw new PrefixedPetException("Could not find a valid target!");
        }

        petItem.setLastUsed(System.currentTimeMillis());

        nearbyEnemies.forEach(enemy -> {

        });
        player.updateInventory();
        return true;
    }

    public boolean onTickInHotBar(Player player, int inventorySlot, InventoryPetItem item) {
        return false;
    }
}
