package me.ifamasssxd.cosmicsky.inventorypets.struct.pets;

import com.cosmicpvp.cosmicutils.utils.*;
import com.google.common.collect.Lists;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import java.util.List;
import java.util.concurrent.TimeUnit;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.NonPrefixedPetException;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.PetException;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPet;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPetItem;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.struct.PetCooldown;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.libs.RomanNumber;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryGaiaPet extends InventoryPet {
   public InventoryPets getOwner() {
      return InventoryPets.GAIA;
   }

   public List getPetDescription() {
      return Lists.newArrayList(CC.Gray + "A segment of the goddess", CC.Gray + "of the earth and nature itself.", "", CC.WhiteB + "Ability", CC.Gray + "Call upon the wrath of Nature.", CC.Gray + "[" + CC.Red + "Nature Wrath " + RomanNumber.toRoman(5) + CC.Gray + "] (50% duration)", "", CC.WhiteB + "Cooldown", CC.Gray + " 10 Minutes");
   }

   public int getExpForLevel(int level) {
      return 1000 + level * 500;
   }

   public int getMaxLevel() {
      return 4;
   }

   public int getCooldown() {
      return (int)TimeUnit.MINUTES.toSeconds(10L);
   }

   public boolean onPetItemInteract(Player player, ItemStack item, InventoryPetItem petItem, PlayerInteractEvent event) throws Exception {
      long lastUse = petItem.getLastUsed();
      long delay = TimeUnit.SECONDS.toMillis((long)this.getCooldown());
      long timeTillCanUse = delay - (System.currentTimeMillis() - lastUse);

      if (lastUse > 0L && timeTillCanUse > 0L && (!player.isOp() || !player.isSneaking())) {
         throw new NonPrefixedPetException(CC.RedB + "PET: " + CC.Red + "Ability on cooldown: " + CC.RedU + TimeUtils.formatSeconds((long)((int)(timeTillCanUse / 1000L))));
      }


         boolean inWarzone = FactionUtils.getFactionAt(new FLocation(player.getLocation())).isWarZone();
         int xpGain = inWarzone ? 50 : 5;
         int affected = ((Enchantments)Bukkit.getPluginManager().getPlugin("Enchantments")).getNaturesWrathEnchant().procNaturesWrath(player, player, petItem.getLevel(), null, 0.5D);
         if (affected == 0) {
            throw new NonPrefixedPetException(CC.RedB + "PET: " + CC.Red + "No valid enemy players nearby!");
         } else {
            player.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "PET: ** NATURE'S WRATH **");
            //petItem.setLastUsed(System.currentTimeMillis());
            petItem.setLastUsed(System.currentTimeMillis());
            player.playSound(player.getLocation(), Sound.DIG_SNOW, 1.1F, 3.0F);
            player.playSound(player.getLocation(), Sound.WITHER_SHOOT, 1.1F, 3.0F);
            return true;
         }

   }

   public boolean onTickInHotBar(Player player, int inventorySlot, InventoryPetItem item) {
      return false;
   }
}
