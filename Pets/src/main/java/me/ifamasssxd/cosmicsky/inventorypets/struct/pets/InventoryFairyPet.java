package me.ifamasssxd.cosmicsky.inventorypets.struct.pets;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.TimeUtils;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicsky.inventorypets.CosmicSkyInventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.NonPrefixedPetException;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.PetException;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPet;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPetItem;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPets;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class InventoryFairyPet extends InventoryPet {

    public InventoryPets getOwner() {
        return InventoryPets.AETHER_FAIRY;
    }



    public List getPetDescription() {
        return Lists.newArrayList(CC.Gray + "A tranquil pixie ready to protect the",
                CC.Gray + "beholder from the comfort of the oasis",
                CC.Gray, CC.WhiteB + "Ability",
                CC.Gray + "Gives caster Aether Fairy buff for 30s,",
                CC.Gray + "decreasing incoming damage by 5%.",
                CC.Gray + "Does not stack.",
                CC.Gray, CC.WhiteB + "Cooldown",
                CC.Gray + " " + TimeUtils.formatSeconds(getCooldown()));
    }

    public int getCooldown() {
        return 360;
    }

    public int getExpForLevel(int level) {
        return 700 + 100 * level;
    }

    public int getMaxLevel() {
        return 5;
    }

    public boolean onPetItemInteract(final Player player, ItemStack item, InventoryPetItem petItem, PlayerInteractEvent event) throws PetException {
        long lastUse = petItem.getLastUsed();
        long delay = TimeUnit.SECONDS.toMillis((long) this.getCooldown());
        long timeTillCanUse = delay - (System.currentTimeMillis() - lastUse);
        if (item.getAmount() > 1) {
            player.sendMessage(CC.translate("&cYou cannot use stacked pets!"));

            return false;
        }
        if (lastUse > 0L && timeTillCanUse > 0L && (!player.isOp() || !player.isSneaking()))
            throw new NonPrefixedPetException(CC.RedB + "PET: " + CC.Red + "Ability on cooldown: " + CC.RedU + TimeUtils.formatSeconds((int) (timeTillCanUse / 1000L)));
        if (!WorldGuardUtils.isPvPEnabled(player.getLocation()))
            throw new NonPrefixedPetException(String.valueOf(CC.RedB) + "PET: " + CC.Red + "Not allowed in this region!");
        petItem.setLastUsed(System.currentTimeMillis());
        final List<Player> nearbyEnemies = Lists.newArrayList();

        int duration = 30;

        player.setMetadata("fairy_pet", (MetadataValue)new FixedMetadataValue((Plugin)CosmicSkyInventoryPets.get(), Boolean.TRUE));

        player.sendMessage(CC.translate("&e&lPET: ** AETHER FAIRY PET [&6" + duration + "s&e&l] - Aether Fairy! **"));
        player.sendMessage("");
        (new BukkitRunnable() {
            public void run() {
                player.sendMessage(CC.translate("&e&lPET: AETHER FAIRY PET - Aether Fairy has worn off! "));
                player.removeMetadata("fairy_pet", (Plugin)CosmicSkyInventoryPets.get());
            }
        }).runTaskLater((Plugin)CosmicSkyInventoryPets.get(), (20 * duration));
        player.playSound(player.getLocation(), Sound.LAVA_POP, 1.1F, 1.0F);
        player.playSound(player.getLocation(), Sound.GHAST_SCREAM2, 2.0F, 2.0F);
        return true;
    }

    public boolean onTickInHotBar(Player var1, int var2, InventoryPetItem var3) {
        return false;
    }


}
