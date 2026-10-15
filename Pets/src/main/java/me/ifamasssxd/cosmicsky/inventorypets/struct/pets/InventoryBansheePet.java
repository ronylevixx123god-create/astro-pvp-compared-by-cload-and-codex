package me.ifamasssxd.cosmicsky.inventorypets.struct.pets;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.CooldownUtils;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.TimeUtils;
import com.google.common.collect.Lists;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.perms.Relation;
import com.rit.sucy.EnchantmentAPI;
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
import me.ifamasssxd.cosmicsky.inventorypets.struct.PetCooldown;
import me.ifamasssxd.cosmicsky.inventorypets.utils.CrankedAmuletUtil;
import me.ifamasssxd.cosmicsky.inventorypets.utils.DamageIndicatorUtils;
import me.kingsavaqe.cagepvp.AstroCagePvP;
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

public class InventoryBansheePet extends InventoryPet {


    public InventoryPets getOwner() {
        return InventoryPets.BANSHEE;
    }

    public List getPetDescription() {
        return Lists.newArrayList(CC.Gray + "Call upon the deadly screeming Banshee.", CC.Gray, CC.WhiteB + "Ability", CC.Gray + "Activates " + CC.DarkRedB + "[RKO]:", CC.DarkRed + "* " + CC.Gray + "1.5x DMG to all players (8s) ", CC.DarkRed + "* " + CC.Gray + "Immune to freezes (8s) ", CC.DarkRed + "* " + CC.Gray + "AoE (8x8) Silence IV (8s) ", CC.DarkRed + "* " + CC.Gray + "Negates enemy Phoenix (8s) ", CC.DarkRed + "* " + CC.Gray + "Negates enemy Holy White Scrolls (8s) ",
                CC.Gray, CC.WhiteB + "Cooldown", CC.Gray + " " + TimeUtils.formatSeconds(getCooldown()));
    }


    public int getExpForLevel(int level) {
        return 700 + 100 * level;
    }

    public int getMaxLevel() {
        return 5;
    }

    public int getCooldown() {
        return 360;
    }

    public boolean onPetItemInteract(final Player player, ItemStack item, InventoryPetItem petItem, PlayerInteractEvent event) throws PetException {
        long lastUse = petItem.getLastUsed();
        long delay = TimeUnit.SECONDS.toMillis((long) this.getCooldown());
        long timeTillCanUse = delay - (System.currentTimeMillis() - lastUse);
        /*if (CooldownUtils.isCooldownActive(player, petItem.getPetType().name())) {
            player.sendMessage(CC.translate("&c&lPET: &cYou are still on" + petItem.getPetType().getPetName() + " &cfor another &7" + CooldownUtils.getFormattedCooldown(player, petItem.getPetType().name() + "&c.")));
            return true;
        }*/


        List<Player> nearbyEnemies = Lists.newArrayList();

        Faction faction = FactionUtils.getFaction(player);

        for (Entity other : player.getNearbyEntities(8.0D, 8.0D, 8.0D)) {
            if (other instanceof Player) {
                Player nearPlayer = (Player) other;
                Faction nearFac = FactionUtils.getFaction(nearPlayer);
                if (nearPlayer.getGameMode() == GameMode.CREATIVE)
                    continue;
                if (nearFac != null && nearFac.getRelationTo((RelationParticipator) faction) != Relation.ENEMY && nearFac.getRelationTo((RelationParticipator) faction) != Relation.NEUTRAL)
                    continue;
                if (AstroItemSkinsAPI.hasSkinEquipped(nearPlayer, SkinType.SPRING_2020_AXE)) {
                    nearPlayer.sendMessage(CC.translate("&c&l* SPIKED BAT SKIN BLOCKED BANSHEE *"));
                    continue;
                }

                /*if (CooldownUtils.isCooldownActive(nearPlayer, "bansheeCooldown")) {
                    throw new PrefixedPetException("&c&lPET: &c" + nearPlayer.getName() + "&c is still on banshee cooldown for &n" + CooldownUtils.getFormattedCooldown(nearPlayer, "bansheeCooldown") + "&c.");
                }*/

                if (lastUse > 0L && timeTillCanUse > 0L && (!player.isOp() || !player.isSneaking()))
                    throw new NonPrefixedPetException(CC.RedB + "PET: " + CC.Red + "Ability on cooldown: " + CC.RedU + TimeUtils.formatSeconds((int) (timeTillCanUse / 1000L)));

                if (AstroItemSkinsAPI.hasSkinEquipped(nearPlayer, SkinType.THANKSGIVING_HELMET_WITCH) && (!GoldenAdmirer.isAdmirerSilenced(nearPlayer)) && !(((SpinalTab) SkinType.HALLOWEEN_2021_SWORD.getItemSkin())).getWitchSilencedPlayers().contains(nearPlayer.getName()) && !AstroItemSkinsAPI.hasSkinEquipped(player, SkinType.BACK_TO_SCHOOL_2021_BELT)) {

                    nearPlayer.sendMessage(CC.translate("&e&l* WITCH HAT &7Blocked &4Banshee Pet &7[&f" + player.getName() + "&7] &e&l*"));
                    player.sendMessage(CC.translate("&e&l* &f" + nearPlayer.getName() + " &7Blocked &4Banshee Pet &7[&e&lWitch Hat&7] &e&l*"));
                    continue;
                }

                if (UndertowHelmet.undertowField.contains(nearPlayer)) {
                    nearPlayer.sendMessage(CC.translate("&a&l* UNDERTOW HELMET &7Blocked &4Banshee Pet &7[&f" + player.getName() + "&7] &a&l*"));
                    player.sendMessage(CC.translate("&a&l* &f" + nearPlayer.getName() + " &7Blocked &4Banshee Pet &7[&a&lUndertow Helmet&7] &a&l*"));
                    continue;
                }


                if (AstroCagePvP.getInstance().getImmuneManager().hasImmune(nearPlayer)) {
                    nearPlayer.sendMessage(CC.translate("&c&lPET: &cYou cannot use pets one someone who has immunity."));
                    continue;
                }



                if (AstroItemSkinsAPI.hasSkinEquipped(nearPlayer, SkinType.BACK_TO_SCHOOL_2021_BOOTS) || AstroItemSkinsAPI.hasSkinEquipped(nearPlayer, SkinType.MMM_BACK_TO_SCHOOL_2021_BOOTS)) {
                    nearPlayer.sendMessage(CC.translate("&8&l* BIG TIMBS &7Blocked &4Banshee Pet &7[" + player.getName() + "&7] &8&l*"));
                    player.sendMessage(CC.translate("&8&l* &f" + nearPlayer.getName() + " &7Blocked &4Banshee Pet &7[&8&lBig Timbs&7] &8&l*"));
                    continue;
                }

                if (AstroItemSkinsAPI.hasSkinEquipped(nearPlayer, SkinType.HALLOWEEN_2021_HELMET)) {
                    nearPlayer.sendMessage(CC.translate("&6&l* MINEMAN MUZZLE &7Blocked &4Banshee Pet &7[" + player.getName() + "&7] &6&l*"));
                    player.sendMessage(CC.translate("&6&l* &f" + nearPlayer.getName() + " &7Blocked &4Banshee Pet &7[&6&lMineman Muzzle&7] &6&l*"));
                    continue;
                }

                if (AstroItemSkinsAPI.hasSkinEquipped(nearPlayer, SkinType.CHRISTMAS_2022_HELMET_ACTIVE)) {
                    nearPlayer.sendMessage(CC.translate("&9&l* FROST MONSTER TROPHY &7Blocked &4Banshee Pet &7[" + player.getName() + "&7] &9&l*"));
                    player.sendMessage(CC.translate("&9&l* &f" + nearPlayer.getName() + " &7Blocked &4Banshee Pet &7[&9&lFrost Monster Trophy&7] &9&l*"));
                    continue;
                }



                nearbyEnemies.add(nearPlayer);
            }
        }

        if (nearbyEnemies.isEmpty()) {
            throw new PrefixedPetException("Could not find a valid target!");
        }

        //petItem.setLastUsed(System.currentTimeMillis());
        petItem.setLastUsed(System.currentTimeMillis());
        //CooldownUtils.registerCooldown(player, new PetCooldown(petItem.getPetType().name(), getCooldown()));

        int duration;


        if (MaskType.hasEquipped(player, "ELF")) {
            duration = 12;
        } else {
            duration = 8;
        }

        if (AstroItemSkinsAPI.hasSkinEquipped(player, SkinType.EASTER_2022_MUTATED_HELMET) || AstroItemSkinsAPI.hasSkinEquipped(player, SkinType.EASTER_2022_MUTATED_HELMET)) {
            duration*=1.25;
        }




        int finalDuration = duration;
        nearbyEnemies.forEach(enemy -> {

            if (AstroItemSkinsAPI.hasSkinEquipped(enemy, SkinType.THANKSGIVING_HELMET_WITCH) && AstroItemSkinsAPI.hasSkinEquipped(player, SkinType.BACK_TO_SCHOOL_2021_BELT)) {
                enemy.sendMessage(CC.translate("&8&l* " + player.getName() + "'s BANDOLIER BELT [&7Bypassed Witch Hat&8&l] *"));
                player.sendMessage(CC.translate("&8&l* BANDOLIER BELT [&7Bypassed " + enemy.getName() + "'s Witch Hat&8&l] *"));
            }
            CrankedAmuletUtil.tryHatchAbility(enemy, player);


            if ((AstroItemSkinsAPI.hasSkinEquipped(enemy, SkinType.VALENTINES_2022_HELMET) || AstroItemSkinsAPI.hasSkinEquipped(enemy, SkinType.VALENTINES_2022_MUTATED_HELMET)) && Math.random() < 0.25 && !enemy.hasMetadata("SSFCooldown")) {
                startBansheeEffect(enemy, player, finalDuration);
                enemy.sendMessage(CC.translate("&d&l** SSF HELMET [&7Banshee Reflect on " + player.getName() + " &d&l] **"));
                player.sendMessage(CC.translate("&d&l** " + enemy.getName() + "'s SSF HELMET [&7Reflected Banshee&d&l] **"));
                enemy.setMetadata("SSFCooldown", new FixedMetadataValue(CosmicSkyInventoryPets.get(), true));
                Bukkit.getScheduler().runTaskLaterAsynchronously(CosmicSkyInventoryPets.get(), () -> {
                    enemy.removeMetadata("SSFCooldown", CosmicSkyInventoryPets.get());
                }, 20 * 360);
            } else {
                startBansheeEffect(player, enemy, finalDuration);
            }
        });
        player.updateInventory();
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

        CooldownUtils.registerCooldown(enemy, new PetCooldown("bansheeCooldown", 30*20));

        enemy.playSound(enemy.getLocation(), Sound.WITHER_HURT, 1.0f, 0.25f);
        enemy.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "* SILENCED " + ChatColor.GRAY + " [7s] " + ChatColor.DARK_PURPLE + ChatColor.BOLD + "*");
        enemy.setMetadata("noDefenseProcs", (MetadataValue) new FixedMetadataValue((Plugin) CosmicSkyInventoryPets.get(), Boolean.valueOf(true)));
        enemy.setMetadata("banshee_scrolls", (MetadataValue) new FixedMetadataValue((Plugin) CosmicSkyInventoryPets.get(), Boolean.valueOf(true)));
        enemy.sendMessage("");
        enemy.sendMessage(CC.translate(String.format("&4&l*** " + player.getName() + "&4&l's BANSHEE PET [&6" + duration + "s&4&l] - RKO! **", new Object[]{Integer.valueOf(duration)})));
        enemy.sendMessage(CC.translate(String.format("&c( %ds Silence, Phoenix - BLOCKED, Holy White Scrolls - BLOCKED )", new Object[]{Integer.valueOf(duration)})));
        enemy.sendMessage("");
        DamageIndicatorUtils.sendDamageIndicator(enemy, CC.translate("&c&l* BANSHEE RKO [&7" + duration + "s&c&l] *"), "RKO");
        CombatLog.flagCombat(player);
        player.setMetadata("immune_freeze", (MetadataValue) new FixedMetadataValue((Plugin) CosmicSkyInventoryPets.get(), Boolean.valueOf(true)));
        player.setMetadata("banshee_active", (MetadataValue) new FixedMetadataValue((Plugin) CosmicSkyInventoryPets.get(), Boolean.valueOf(true)));
        EnchantmentAPI.getEnchantment("Gears").applyEquipEffect(player, 5);
        player.sendMessage("");
        player.sendMessage(CC.translate("&4&lPET: ** BANSHEE [&6" + duration + "s&4&l] - RKO! **"));
        player.sendMessage("");
        player.playSound(player.getLocation(), Sound.LAVA_POP, 1.1F, 1.0F);
        player.playSound(player.getLocation(), Sound.GHAST_SCREAM2, 2.0F, 2.0F);

        Bukkit.getScheduler().runTaskLater(CosmicSkyInventoryPets.get(), () -> {
            player.sendMessage(CC.translate("&4&lPET:  BANSHEE - RKO has worn off! "));
            player.removeMetadata("immune_freeze", (Plugin) CosmicSkyInventoryPets.get());
            player.removeMetadata("banshee_active", (Plugin) CosmicSkyInventoryPets.get());
            EnchantmentAPI.getEnchantment("Gears").applyUnequipEffect(player, 5);

            enemy.removeMetadata("noDefenseProcs", (Plugin) CosmicSkyInventoryPets.get());
            enemy.removeMetadata("banshee_scrolls", (Plugin) CosmicSkyInventoryPets.get());
            enemy.sendMessage(CC.translate("&4&lPET: &4&l[RKO] &efrom " + player.getName() + "'s Banshee Pet &e&nEXPIRED!"));
        }, 20L *duration);


    }
}
