package me.ifamasssxd.cosmicsky.inventorypets.struct.pets;

import com.cosmicpvp.cosmicutils.compat.ASound;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import com.cosmicpvp.cosmicutils.utils.TimeUtils;
import com.google.common.collect.Lists;
import com.massivecraft.factions.Faction;
import java.util.List;
import java.util.concurrent.TimeUnit;
import me.ifamasssxd.cosmicsky.inventorypets.CosmicSkyInventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.NonPrefixedPetException;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.PetException;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.PrefixedPetException;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPet;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPetItem;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPets;
import me.ifamasssxd.cosmicskycustoms.struct.CustomItemBuilder;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryBannerPet extends InventoryPet {
   public InventoryPets getOwner() {
      return InventoryPets.BANNER;
   }

   public List getPetDescription() {
      return Lists.newArrayList(CC.Gray + "A rallying entity that", CC.Gray + "has the magical power to", CC.Gray + "summon /f banners at its", CC.Gray + "owner's location at will.", "", CC.WhiteB + "Ability", CC.Gray + "Spawn, Place /f banner", CC.Gray + "at current location.", CC.Gray + "(no soul cost)", "", CC.WhiteB + "Cooldown", CC.Gray + " " + TimeUtils.formatSeconds((long)this.getCooldown()));
   }

   public int getCooldown() {
      return Math.max(120, 720 - 60 * (5 - 1));
   }

   public int getExpForLevel(int level) {
      return 1000;
   }

   public int getMaxLevel() {
      return 10;
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
         Faction faction = FactionUtils.getFaction(player);
         if (faction == null) {
            throw new PrefixedPetException("You must be in a faction to use this pet!");
         } else {
            ItemStack banner = (new CustomItemBuilder("FACTION_BANNER", new Object[]{faction, player, false, "Banner Pet"})).build();
            if (banner == null) {
               player.sendMessage(CC.Red + "No banner found!");
               return false;
            } else {
               petItem.setLastUsed(System.currentTimeMillis());
               boolean xp = true;
               player.playSound(player.getLocation(), Sound.LAVA_POP, 1.1F, 1.0F);
               NBTTagCompoundBuilder cosmicData = petItem.getWrapper().getconaxData();
               boolean skipped = false;
               if (cosmicData.hasKey("lastExpGained")) {
                  long gained = cosmicData.getLong("lastExpGained");
                  long timeSince = System.currentTimeMillis() - gained;
                  if (timeSince < TimeUnit.DAYS.toMillis(1L)) {
                     CosmicSkyInventoryPets.get().getLogger().info("Skipping exp gain since: " + timeSince);
                     skipped = true;
                  }
               }

               if (!skipped) {
                  cosmicData.setLong("lastExpGained", System.currentTimeMillis());
                  petItem.getWrapper().setNBTBase("cosmicData", cosmicData.get());
               }

               PlayerUtil.giveOrDropProtectedItem(player, banner, -1);
               ASound.ITEM_PICKUP.play(player, 1.1F);
               petItem.getPetType().sendMsg(player, CC.Green, "Banner added to me.astropvp.cratekeys.inventory!");
               return true;
            }
         }
      } else {
         throw new NonPrefixedPetException(CC.RedB + "PET: " + CC.Red + "Ability on cooldown: " + CC.RedU + TimeUtils.formatSeconds((long)((int)(timeTillCanUse / 1000L))));
      }
   }

   public NBTWrapper onItemLoad(NBTWrapper wrapper, InventoryPetItem instance) {
      return super.onItemLoad(wrapper, instance);
   }

   public NBTWrapper onItemCreate(NBTWrapper wrapper, InventoryPetItem instance) {
      if (instance.getWrapper() != null) {
         NBTTagCompoundBuilder oldData = instance.getWrapper().getconaxData();
         NBTTagCompoundBuilder newData = wrapper.getconaxData();
         if (oldData != null && newData != null && oldData.hasKey("lastExpGained")) {
            newData.setLong("lastExpGained", oldData.getLong("lastExpGained"));
            wrapper.setNBTBase("cosmicData", newData.get());
            CosmicSkyInventoryPets.get().getLogger().info("Saving last EXp gained!");
         }
      }

      return super.onItemCreate(wrapper, instance);
   }

   public boolean onTickInHotBar(Player player, int inventorySlot, InventoryPetItem item) {
      return false;
   }
}
