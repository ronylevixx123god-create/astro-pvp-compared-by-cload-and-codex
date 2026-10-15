package me.ifamasssxd.cosmicsky.inventorypets.struct.pets;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.TimeUtils;
import com.google.common.collect.Lists;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.perms.Relation;
import com.rit.sucy.EnchantmentAPI;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.skins.axes.GoldenAdmirer;
import me.astropvp.astropvpskins.objects.skins.helmets.UndertowHelmet;
import me.astropvp.astropvpskins.objects.skins.swords.SpinalTab;
import me.ifamasssxd.cosmicsky.inventorypets.CosmicSkyInventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.NonPrefixedPetException;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.PetException;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.PrefixedPetException;
import me.ifamasssxd.cosmicsky.inventorypets.libs.Vector3D;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPet;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPetItem;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.utils.CrankedAmuletUtil;
import me.ifamasssxd.cosmicsky.inventorypets.utils.DamageIndicatorUtils;
import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class InventoryBloodhoundPet extends InventoryPet {
   static Map items = new HashMap();
   static Map armor = new HashMap();
   public int petLevel = 5;

   public InventoryPets getOwner() {
      return InventoryPets.BLOODHOUND;
   }

   public List getPetDescription() {
      return Lists.newArrayList(CC.Gray + "Mark your target for assured destruction.", CC.Gray, CC.WhiteB + "Ability", CC.Gray + "Affects target player with " + CC.DarkRedB + "[TAG]:", CC.DarkRed + "* " + CC.Gray + "Auto-/f focus target player ", CC.DarkRed + "* " + CC.Gray + "Anti-Bless Slowness I (10s) ", CC.DarkRed + "* " + CC.Gray + "Silence target player (10s) ", CC.DarkRed + "* " + CC.Gray + "1.5x DMG to target players (10s) ", CC.DarkRed + "* " + CC.Gray + "Target is visable on /near for (30s) ", CC.Gray, CC.WhiteB + "Cooldown", CC.Gray + " " + TimeUtils.formatSeconds((long) this.getCooldown()));
   }

   public int getCooldown() {

      return 360;
   }
   
   public int getExpForLevel(int level) {
      return 1000 + 100 * level;
   }

   public int getMaxLevel() {
      return 6;
   }

   public boolean onPetItemInteract(final Player player, ItemStack item, InventoryPetItem petItem, PlayerInteractEvent event) throws PetException {
      long lastUse = petItem.getLastUsed();
      long delay = TimeUnit.SECONDS.toMillis((long) this.getCooldown());
      long timeTillCanUse = delay - (System.currentTimeMillis() - lastUse);


      /*if (CooldownUtils.isCooldownActive(player, petItem.getPetType().name())) {
         player.sendMessage(CC.translate("&c&lPET: &cYou are still on" + petItem.getPetType().getPetName() + " &cfor another &7" + CooldownUtils.getFormattedCooldown(player, petItem.getPetType().name() + "&c.")));
         return true;
      }*/

      if (lastUse > 0L && timeTillCanUse > 0L && (!player.isOp() || !player.isSneaking()))
         throw new NonPrefixedPetException(CC.RedB + "PET: " + CC.Red + "Ability on cooldown: " + CC.RedU + TimeUtils.formatSeconds((int) (timeTillCanUse / 1000L)));


      List<Entity> targets = player.getNearbyEntities(10,10,10);
      Entity target = null;


      for (Entity targett : targets) {
         if (targett != player && targett instanceof Player) {
            if (FactionUtils.getFPlayer((Player) targett).getFaction() != FactionUtils.getFPlayer(player).getFaction() && !FactionUtils.isInAllyFaction(player, (Player) targett)) {
               target = targett;
               break;

            }
         }
      }

      if (target == null)
         throw new PrefixedPetException(String.valueOf(CC.Red + "No target found!"));
      final Player pTarget = (Player)target;
      FPlayer fPlayer = FactionUtils.getFPlayer(player);
      Relation relation = FactionUtils.getFPlayer(pTarget).getRelationTo((RelationParticipator)fPlayer);
      if (relation == Relation.ALLY || relation == Relation.TRUCE || relation.isMember())
         throw new PrefixedPetException(String.valueOf(CC.Red + "No valid target found!"));




      final String name = player.getName();

      if (AstroItemSkinsAPI.hasSkinEquipped(pTarget, SkinType.THANKSGIVING_HELMET_WITCH) && (!GoldenAdmirer.isAdmirerSilenced(pTarget)) && !(((SpinalTab)SkinType.HALLOWEEN_2021_SWORD.getItemSkin())).getWitchSilencedPlayers().contains(pTarget.getName()) && !AstroItemSkinsAPI.hasSkinEquipped(player, SkinType.BACK_TO_SCHOOL_2021_BELT)) {
         pTarget.sendMessage(CC.translate("&e&l* WITCH HAT &7Blocked &4Bloodhound Pet &7[&f" + player.getName() + "&7] &e&l*"));
         player.sendMessage(CC.translate("&e&l* &f" + pTarget.getName() + " &7Blocked &4Bloodhound Pet &7[&e&lWitch Hat&7] &e&l*"));
         return true;
      }



      if (AstroItemSkinsAPI.hasSkinEquipped(pTarget, SkinType.BACK_TO_SCHOOL_2021_BOOTS) || AstroItemSkinsAPI.hasSkinEquipped(pTarget, SkinType.MMM_BACK_TO_SCHOOL_2021_BOOTS)) {
         pTarget.sendMessage(CC.translate("&8&l* BIG TIMBS &7Blocked &4Bloodhound Pet &7[" + player.getName() + "&7] &8&l*"));
         player.sendMessage(CC.translate("&8&l* &f" + pTarget.getName() + " &7Blocked &4Bloodhound Pet &7[&8&lBig Timbs&7] &8&l*"));
         return true;
      }

      if (UndertowHelmet.undertowField.contains(pTarget)) {
         pTarget.sendMessage(CC.translate("&a&l* UNDERTOW HELMET &7Blocked &4Bloodhound Pet &7[&f" + player.getName() + "&7] &a&l*"));
         player.sendMessage(CC.translate("&a&l* &f" + pTarget.getName() + " &7Blocked &4Bloodhound Pet &7[&a&lUndertow Helmet&7] &a&l*"));
         return true;
      }

      //petItem.setLastUsed(System.currentTimeMillis());
      petItem.setLastUsed(System.currentTimeMillis());
      //CooldownUtils.registerCooldown(player, new PetCooldown(petItem.getPetType().name(), getCooldown()));

      pTarget.setMetadata("noDefenseProcs", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), Boolean.valueOf(true)));
      pTarget.setMetadata("bloodhound_" + name, (MetadataValue)new FixedMetadataValue((Plugin)CosmicSkyInventoryPets.get(), null));
      pTarget.setMetadata("bloodhound", (MetadataValue)new FixedMetadataValue((Plugin)CosmicSkyInventoryPets.get(), null));
      pTarget.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (4 + 5) * 20, 0));
      FPlayer fPTarget = FactionUtils.getFPlayer(pTarget);
      if (fPTarget.hasFaction() && fPlayer.hasFaction())
         focusTargetPlayer(player, ((Player) target).getPlayer());
      int sum = 4 + 5;
      if (AstroItemSkinsAPI.hasSkinEquipped(player, SkinType.REINDEER_ANTLERS) && Math.random() < 0.05D) {
         sum *= 2;
         player.sendMessage(String.valueOf(CC.RedB) + "* REINDEER ITEM SKIN " + CC.Gray + "(" + CC.White + "2x Pet Length" + CC.Gray + ")");
      }

      CrankedAmuletUtil.tryHatchAbility(pTarget, player);


      startBoodHoundPet(player, pTarget);

      return true;
   }



   private void startBoodHoundPet(Player player, final Player pTarget) {
      player.sendMessage("");
      player.sendMessage(CC.translate("&4&l*** BLOODHOUND PET [&6" + pTarget.getName() + "s&4&l] - HUNT! **"));
      player.sendMessage("");

      pTarget.sendMessage("");
      pTarget.sendMessage(CC.translate("&4&l*** " + player.getName() + "&4&l's BLOODHOUND PET [&6" + 10 + "s&4&l] - HUNT! **"));
      pTarget.sendMessage("");

      pTarget.setMetadata("noDefenseProcs", new FixedMetadataValue(Enchantments.getInstance(), true));
      pTarget.setMetadata("bloodHoundNoBless", new FixedMetadataValue(CosmicSkyInventoryPets.get(), System.currentTimeMillis()));
      pTarget.removePotionEffect(PotionEffectType.SLOW);
      pTarget.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 0));
      this.removeNearMeta(player, pTarget);
      pTarget.sendMessage("");
      pTarget.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "** BLOODHOUND PET: (No Bless) for (" + ChatColor.GRAY + "10s" + ChatColor.RED + ") **");

      pTarget.sendMessage("");
      DamageIndicatorUtils.sendDamageIndicator(pTarget, CC.translate("&c&l* BLOODHOUND *"), "RKO");

      (new BukkitRunnable() {
         public void run() {
            pTarget.removeMetadata("noDefenseProcs", Enchantments.getInstance());
            pTarget.removeMetadata("bloodHoundNoBless", CosmicSkyInventoryPets.get());
            //CmdUnfocus.unfocusPlayer(FactionUtils.getFaction(player), player);
         }
      }).runTaskLater(CosmicSkyInventoryPets.get(), 200L);
   }

   private void removeNearMeta(Player player, final Player pTarget) {
      if (pTarget.hasMetadata("heroicGhostEnchantment")) {
         pTarget.setMetadata("heroicGhostEnchantmentTemp", new FixedMetadataValue(CosmicSkyInventoryPets.get(), System.currentTimeMillis()));
         pTarget.removeMetadata("heroicGhostEnchantment", Enchantments.getInstance());
      }

      if (pTarget.hasMetadata("ghostEnchantment")) {
         pTarget.setMetadata("ghostEnchantmentTemp", new FixedMetadataValue(CosmicSkyInventoryPets.get(), System.currentTimeMillis()));
         pTarget.removeMetadata("ghostEnchantment", Enchantments.getInstance());
      }

      (new BukkitRunnable() {
         public void run() {
            InventoryBloodhoundPet.this.saveAndRemoveBoots(pTarget);
         }
      }).runTaskLater(CosmicSkyInventoryPets.get(), 600L);
   }

   private void saveAndRemoveBoots(Player player) {
      UUID uuid = player.getUniqueId();
      ItemStack[] armcont = player.getInventory().getArmorContents();
      armor.put(uuid, armcont);
      player.getInventory().setBoots((ItemStack)null);
      player.updateInventory();
      this.giveBootsBack(player);
   }

   private void giveBootsBack(Player player) {
      UUID uuid = player.getUniqueId();
      ItemStack[] armorContents = (ItemStack[])armor.get(uuid);
      if (armorContents != null) {
         player.getInventory().setBoots(armorContents[0]);
         if (player.hasMetadata("heroicGhostEnchantmentTemp")) {
            EnchantmentAPI.getEnchantment("Ghostly Ghost").applyEquipEffect(player, 3);
         }

         if (player.hasMetadata("ghostEnchantmentTemp")) {
            EnchantmentAPI.getEnchantment("Ghost").applyEquipEffect(player, 3);
         }

         player.updateInventory();
      }

   }

   private void focusTargetPlayer(Player player, Player pTarget) {
      if (pTarget instanceof Player) {
         FPlayer fPlayer = (FPlayer)FPlayers.getInstance().getByPlayer(player);
         Faction myFaction = fPlayer.getFaction();
         FPlayer targetPlayer = (FPlayer)FPlayers.getInstance().getByPlayer(pTarget);
         if (myFaction.isNormal()) {
            //CmdFocus.focusPlayer(fPlayer, myFaction, targetPlayer, false);
         }
      }

   }


   public LivingEntity getTargetEntity(Player player, int maxRangeInBlocks) {
      Location observerPos = player.getEyeLocation();
      Vector3D observerDir = new Vector3D(observerPos.getDirection());
      Vector3D observerStart = new Vector3D(observerPos);
      Vector3D observerEnd = observerStart.add(observerDir.multiply(maxRangeInBlocks));
      LivingEntity target = null;
      for (Entity possibleTarget : player.getNearbyEntities(maxRangeInBlocks, maxRangeInBlocks, maxRangeInBlocks)) {
         if (!(possibleTarget instanceof LivingEntity))
            continue;
         Vector3D targetPos = new Vector3D(possibleTarget.getLocation());
         Vector3D minimum = targetPos.add(-0.5D, 0.0D, -0.5D);
         Vector3D maximum = targetPos.add(0.5D, 1.67D, 0.5D);
         if (hasIntersection(observerStart, observerEnd, minimum, maximum))
            target = (LivingEntity)possibleTarget;
      }
      return target;
   }

   private boolean hasIntersection(Vector3D p1, Vector3D p2, Vector3D min, Vector3D max) {
      Vector3D d = p2.subtract(p1).multiply(0.5D);
      Vector3D e = max.subtract(min).multiply(0.5D);
      Vector3D c = p1.add(d).subtract(min.add(max).multiply(0.5D));
      Vector3D ad = d.abs();
      return (Math.abs(c.x) <= e.x + ad.x && Math.abs(c.y) <= e.y + ad.y && Math.abs(c.z) <= e.z + ad.z &&
              Math.abs(d.y * c.z - d.z * c.y) <= e.y * ad.z + e.z * ad.y &&
              Math.abs(d.z * c.x - d.x * c.z) <= e.z * ad.x + e.x * ad.z &&
              Math.abs(d.x * c.y - d.y * c.x) <= e.x * ad.y + e.y * ad.x);
   }

   public boolean onTickInHotBar(Player var1, int var2, InventoryPetItem var3) {
      return false;
   }
}
