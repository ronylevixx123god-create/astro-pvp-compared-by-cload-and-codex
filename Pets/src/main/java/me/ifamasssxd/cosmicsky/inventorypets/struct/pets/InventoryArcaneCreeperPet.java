package me.ifamasssxd.cosmicsky.inventorypets.struct.pets;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.cosmicpvp.cosmicutils.utils.TimeUtils;
import com.google.common.collect.Lists;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.TimeUnit;
import me.ifamasssxd.cosmicsky.inventorypets.CosmicSkyInventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.NonPrefixedPetException;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.PetException;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPet;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPetItem;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPets;
import net.minecraft.server.v1_8_R3.EntityCreeper;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreeper;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class InventoryArcaneCreeperPet extends InventoryPet {
   public InventoryPets getOwner() {
      return InventoryPets.ARCANE_CREEPER;
   }

   public List getPetDescription() {
      return Lists.newArrayList(CC.Gray + "A creeper imbued with extremely,", CC.Gray + "powerful, unstable arcane magic.", CC.WhiteB, CC.WhiteB + "Ability", CC.Gray + "Spawn an " + CC.Aqua + "Arcane Creeper", CC.Gray + "A speciality creeper that banishes all", CC.Gray + "Enchantment Tables in a 3x3 radius.", CC.WhiteB, CC.WhiteB + "Cooldown", CC.Gray + (10 - (5 - 1)) + " Minutes");
   }

   public int getExpForLevel(int level) {
      return 2500 * level;
   }

   public int getMaxLevel() {
      return 10;
   }

   public int getCooldown() {
      return (int)TimeUnit.MINUTES.toSeconds((long)(10 - (5 - 1)));
   }

   public boolean onPetItemInteract(Player player, ItemStack item, InventoryPetItem petItem, PlayerInteractEvent event) throws PetException {
      long lastUse = petItem.getLastUsed();
      long delay = TimeUnit.SECONDS.toMillis((long)this.getCooldown());
      long timeTillCanUse = delay - (System.currentTimeMillis() - lastUse);
      if (item.getAmount() > 1) {
         player.sendMessage(CC.translate("&cYou cannot use stacked pets!"));

         return false;
      }
      if (lastUse <= 0L || timeTillCanUse <= 0L || player.isOp() && player.isSneaking()) {
         petItem.setLastUsed(System.currentTimeMillis());
         if (event.getClickedBlock() == null) {
            return true;
         } else {
            Block blockClicked = event.getClickedBlock();
            Location fixedLocation = blockClicked.getLocation().add(0.5D, 1.0D, 0.5D);
            Creeper arcaneCreeper = (Creeper)blockClicked.getWorld().spawnEntity(fixedLocation, EntityType.CREEPER);
            arcaneCreeper.setCustomName(CC.AquaB + "Arcane Creeper");
            arcaneCreeper.setCustomNameVisible(true);
            arcaneCreeper.setNoDamageTicks(1000);
            arcaneCreeper.setMetadata("arcaneCreeper", new FixedMetadataValue(CosmicSkyInventoryPets.get(), arcaneCreeper.getCustomName()));
            this.fuzeCreeper(arcaneCreeper, 0, 0);
            fixedLocation.getWorld().strikeLightningEffect(fixedLocation);
            boolean xpGain = true;
            player.playSound(player.getLocation(), Sound.DIG_SNOW, 1.1F, 3.0F);
            player.playSound(player.getLocation(), Sound.WITHER_SHOOT, 1.1F, 3.0F);
            return true;
         }
      } else {
         throw new NonPrefixedPetException(CC.RedB + "PET: " + CC.Red + "Ability on cooldown: " + CC.RedU + TimeUtils.formatSeconds((long)((int)(timeTillCanUse / 1000L))));
      }
   }

   public void fuzeCreeper(Creeper creeper, int fuse, int radius) {
      EntityCreeper entCreeper = ((CraftCreeper)creeper).getHandle();
      Field fuseF = null;
      Field radiusF = null;

      try {
         fuseF = EntityCreeper.class.getDeclaredField("maxFuseTicks");
         radiusF = EntityCreeper.class.getDeclaredField("explosionRadius");
      } catch (Exception var9) {
         var9.printStackTrace();
      }

      fuseF.setAccessible(true);
      radiusF.setAccessible(true);

      try {
         fuseF.setInt(entCreeper, fuse);
         radiusF.setInt(entCreeper, radius);
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public boolean onTickInHotBar(Player player, int inventorySlot, InventoryPetItem item) {
      return false;
   }
}
