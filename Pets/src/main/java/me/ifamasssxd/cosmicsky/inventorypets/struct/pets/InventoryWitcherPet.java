package me.ifamasssxd.cosmicsky.inventorypets.struct.pets;

import com.cosmicpvp.cosmicutils.utils.*;
import com.google.common.collect.Lists;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.skins.helmets.UndertowHelmet;
import me.ifamasssxd.cosmicmasks.MaskType;
import me.ifamasssxd.cosmicsky.inventorypets.CosmicSkyInventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.NonPrefixedPetException;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.PetException;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPet;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPetItem;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.struct.PetCooldown;
import me.ifamasssxd.cosmicsky.inventorypets.utils.CrankedAmuletUtil;
import me.ifamasssxd.cosmicsky.inventorypets.utils.DamageIndicatorUtils;
import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class InventoryWitcherPet extends InventoryPet {
    public static List<UUID> no_kb = new ArrayList<>();
    public InventoryPets getOwner() {
        return InventoryPets.ANTI_TELEBLOCK;
    }

    public List<String> getPetDescription() {
        return Lists.newArrayList(CC.Gray + "Call upon the deadly screaming Banshee.", "",
                CC.WhiteB + "Ability", CC.Gray + "Activates " + CC.DarkRedB + "[Witcher]:",
                CC.DarkRedB + "* " + CC.Gray + "Immune to almost all knockback (" + (5 * 2) + "s)",
                CC.DarkRedB + "* " + CC.Gray + "Teleblocks enemies in a 8x8 radius for (" + (5 * 2) + "s)",
                CC.DarkRedB + "* " + CC.Gray + "Negates enemy ragdoll in a 8x8 radius for (" + (5 * 2) + "s)",
                CC.DarkRedB + "* " + CC.Gray + "Silences enemy soul enchantments in a 8x8 radius for (" + (5 * 2) + "s)",
                CC.DarkRedB + "* " + CC.Gray + "Freezes enemies in a 8x8 radius for 1s", "",
                CC.WhiteB + "Cooldown",
                String.valueOf(CC.Gray) + (getCooldown() / 60) + " Minutes");
    }


    public int getExpForLevel(int level) {
        return 575 * level;
    }

    public int getMaxLevel() {
        return 5;
    }

    public int getCooldown() {
        return 300;
    }

    public boolean onPetItemInteract(Player player, ItemStack item, InventoryPetItem petItem, PlayerInteractEvent event) throws PetException {
        /*if (PetUtils.isOnCooldown(player, petItem.getPetType()) && (!player.isOp() || !player.isSneaking()))
            throw new PrefixedPetException(CC.Red + "Ability on cooldown: " + CC.RedU +
                    TimeUtils.formatSeconds(PetUtils.getSecondsOnCooldown(player, petItem.getPetType())));*/

        long lastUse = petItem.getLastUsed();
        long delay = TimeUnit.SECONDS.toMillis(this.getCooldown());
        long timeTillCanUse = delay - (System.currentTimeMillis() - lastUse);
        if (lastUse > 0L && timeTillCanUse > 0L && (!player.isOp() || !player.isSneaking()))
            throw new NonPrefixedPetException(CC.RedB + "PET: " + CC.Red + "Ability on cooldown: " + CC.RedU + TimeUtils.formatSeconds((int) (timeTillCanUse / 1000L)));
        /*if (CooldownUtils.isCooldownActive(player, petItem.getPetType().name())) {
            player.sendMessage(CC.translate("&c&lPET: &cYou are still on" + petItem.getPetType().getPetName() + " &cfor another &7" + CooldownUtils.getFormattedCooldown(player, petItem.getPetType().name() + "&c.")));
            return true;
        }*/

        //PetUtils.addPetCooldown(player, petItem.getPetType(), (getCooldown(5) * 1000));
        //CooldownUtils.registerCooldown(player, new PetCooldown(petItem.getPetType().name(), getCooldown()));
        petItem.setLastUsed(System.currentTimeMillis());



        player.playSound(player.getLocation(), Sound.WITHER_SHOOT, 1.1F, 3.0F);
        int sum = petItem.getLevel() * 2;

        if(MaskType.hasEquipped(player, "ELF")) {
            sum*=1.5;
        }



        procAbility(player, sum, 5);


        return true;
    }

    public static void procAbility(final Player player, int sum, int level) {
        UUID uuid = player.getUniqueId();
        final List<Player> players = Lists.newArrayList();
        for (Entity entity : player.getNearbyEntities(8.0D, 8.0D, 8.0D)) {
            if (!(entity instanceof Player))
                continue;
            Player p = (Player) entity;
            if (GeneralUtil.isAtleastTruce(p, player))
                continue;
            if (!WorldGuardUtils.isPvPEnabled(p.getLocation()))
                continue;

            if (CooldownUtils.isCooldownActive(p, "witcherCooldown")) {
                player.sendMessage(CC.translate("&c&lPET: &c" + p.getName() + "&c is still on witcher cooldown for &n" + CooldownUtils.getFormattedCooldown(p, "witcherCooldown") + "&c."));
                continue;
            }

            if (AstroItemSkinsAPI.hasSkinEquipped(p, SkinType.BACK_TO_SCHOOL_2021_BOOTS) || AstroItemSkinsAPI.hasSkinEquipped(p, SkinType.MMM_BACK_TO_SCHOOL_2021_BOOTS)) {
                p.sendMessage(CC.translate("&8&l* BIG TIMBS &7Blocked &4Witcher Pet &7[" + player.getName() + "&7] &8&l*"));
                player.sendMessage(CC.translate("&8&l* &f" + p.getName() + " &7Blocked &4Witcher Pet &7[&8&lBig Timbs&7] &8&l*"));
                continue;
            }

            if (AstroItemSkinsAPI.hasSkinEquipped(p, SkinType.VALENTINES_2022_BOOTS) || AstroItemSkinsAPI.hasSkinEquipped(p, SkinType.VALENTINES_2022_MUTATED_BOOTS)) {
                p.sendMessage(CC.translate("&6&l* GOLDEN BOOTS &7Blocked &4Witcher Pet &7[" + player.getName() + "&7] &6&l*"));
                player.sendMessage(CC.translate("&6&l* &f" + p.getName() + " &7Blocked &4Witcher Pet &7[&6&lGolden Boots&7] &6&l*"));
                continue;
            }

            if (UndertowHelmet.undertowField.contains(p)) {
                p.sendMessage(CC.translate("&a&l* UNDERTOW HELMET &7Blocked &4Witcher Pet &7[&f" + player.getName() + "&7] &a&l*"));
                player.sendMessage(CC.translate("&a&l* &f" + p.getName() + " &7Blocked &4Witcher Pet &7[&a&lUndertow Helmet&7] &a&l*"));
                continue;
            }





            CrankedAmuletUtil.tryHatchAbility(p, player);
            CooldownUtils.registerCooldown(p, new PetCooldown("witcherCooldown", 120));
            p.sendMessage(CC.translate("&cWitcher Pet - ( " + sum + "s Ragdoll, Soul Enchantments - BLOCKED)"));
            p.playSound(p.getLocation(), Sound.GHAST_MOAN, 2.0F, 2.0F);
            if (!AstroItemSkinsAPI.hasSkinEquipped(p, SkinType.CHRISTMAS_BOOTS_SLIPPERS)) {
                p.setWalkSpeed(0.0F);
            }
            p.setMetadata("witcher_ability", new FixedMetadataValue(CosmicSkyInventoryPets.get(), Boolean.valueOf(true)));

            int witcherCooldown = 120;
            //CosmicSkyInventoryPets.get().getCooldownUtils().add(p, "witcherCooldown", witcherCooldown * 1000);
            if(!AstroItemSkinsAPI.hasSkinEquipped(p, SkinType.CHRISTMAS_BOOTS_SLIPPERS) && !MaskType.hasEquipped(p, "GLITCH")) {
                p.setMetadata("teleblock", new FixedMetadataValue(Enchantments.getInstance(), Long.valueOf(System.currentTimeMillis() + (level * 2000))));
            } else {
                p.sendMessage(CC.translate("&c&lSNOWFLAKE SLIPPERS: &7Blocked &cTeleblock!"));
            }
            players.add(p);
        }


        player.sendMessage("");
        player.sendMessage(CC.translate("&4&l** WITCHER PET [&6" + sum + "s&4&l] AOE - Ragdoll, Soul Enchantments - BLOCKED **"));
        //player.sendMessage(CC.translate("&4&l** WITCHER PET &7[Knockback Immunity 5s] &4&l**"));
        player.sendMessage("");
        //no_kb.add(player.getUniqueId());
        //player.setMetadata("no_knockback", new FixedMetadataValue(CosmicSkyInventoryPets.get(), null));

        DamageIndicatorUtils.sendDamageIndicator(player, CC.translate("&4&l* WITHER PET *"), "WITHER");

        Bukkit.getScheduler().runTaskLater(CosmicSkyInventoryPets.get(), ()-> {
            for (Player pp : players) {
                if (pp == null || !player.isOnline())
                    continue;
                pp.setWalkSpeed(0.2F);
            }

        }, 20L);

        Bukkit.getScheduler().runTaskLater(CosmicSkyInventoryPets.get(), ()-> {
            no_kb.remove(uuid);
            for (Player pp : players) {
                if (pp == null || !player.isOnline())
                    continue;
                if (pp.hasMetadata("witcher_ability"))
                    pp.removeMetadata("witcher_ability", CosmicSkyInventoryPets.get());
            }
            player.sendMessage(CC.translate("&4&lPET:  Witcher Pet - WITCHER has worn off! "));
        }, (sum * 20L));
    }


    public boolean onTickInHotBar(Player player, int inventorySlot, InventoryPetItem item) {
        return false;
    }

}
