package me.ifamasssxd.cosmicsky.inventorypets.struct.pets;

import com.cosmicpvp.cosmicutils.utils.*;
import com.google.common.collect.Lists;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.perms.Relation;
import com.rit.sucy.EnchantmentAPI;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.skins.helmets.UndertowHelmet;
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
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class InventorySkrrtPet extends InventoryPet {
    public int petLevel = 5;

    public InventoryPets getOwner() {
        return InventoryPets.SKRRT;
    }

    public List getPetDescription() {
        return Lists.newArrayList(CC.Gray + "Yeeeeeeeeeeet.", CC.Gray, CC.WhiteB + "Ability", CC.Gray + "Activates " + CC.DarkRedB + "[Skrrt]:", CC.DarkRed + "* " + CC.Gray + "Freezes enemy players within 16x16 for " + this.freezeDuration(this.petLevel) + "s", CC.DarkRed + "* " + CC.Gray + "Disables /fly, combat tags enemy player within 16x16 ", CC.DarkRed + "* " + CC.Gray + "Apply Gears V for (" + this.freezeDuration(this.petLevel) + "s)", CC.DarkRed + "* " + CC.Gray + "Invulnerable for (" + this.freezeDuration(this.petLevel) + "s)", CC.DarkRed + "* " + CC.Gray + "Cannot attack for (" + this.freezeDuration(this.petLevel) + "s)", CC.Gray, CC.WhiteB + "Cooldown", CC.Gray + " " + TimeUtils.formatSeconds((long) this.getCooldown()));
    }

    public int getCooldown() {
        return 360;
    }

    private int freezeDuration(int level) {
        return 5;
    }

    public int getExpForLevel(int level) {
        return 1000 * level;
    }

    public int getMaxLevel() {
        return 5;
    }

    public boolean onPetItemInteract(Player player, ItemStack itemStack, InventoryPetItem petItem, PlayerInteractEvent event) throws PetException {

        long lastUse = petItem.getLastUsed();
        long delay = TimeUnit.SECONDS.toMillis((long) this.getCooldown());
        long timeTillCanUse = delay - (System.currentTimeMillis() - lastUse);

        /*if (CooldownUtils.isCooldownActive(player, petItem.getPetType().name())) {
            player.sendMessage(CC.translate("&c&lPET: &cYou are still on" + petItem.getPetType().getPetName() + " &cfor another &7" + CooldownUtils.getFormattedCooldown(player, petItem.getPetType().name() + "&c.")));
            return true;
        }*/

        if (lastUse > 0L && timeTillCanUse > 0L && (!player.isOp() || !player.isSneaking()))
            throw new NonPrefixedPetException(CC.RedB + "PET: " + CC.Red + "Ability on cooldown: " + CC.RedU + TimeUtils.formatSeconds((int) (timeTillCanUse / 1000L)));

        petItem.setLastUsed(System.currentTimeMillis());
        //CooldownUtils.registerCooldown(player, new PetCooldown(petItem.getPetType().name(), getCooldown()));

        this.petLevel = 5;
        this.givePlayerGearFive(player);
        this.skrrtAbilities(player);
        return true;
    }


    private void givePlayerGearFive(final Player player) {

        EnchantmentAPI.getEnchantment("Gears").applyEquipEffect(player, 5);
        player.sendMessage(CC.translate("&4&l** SKRRT PET &7[Speed V] &4&l**"));

        (new BukkitRunnable() {
            public void run() {
                EnchantmentAPI.getEnchantment("Gears").applyUnequipEffect(player, 5);
                player.sendMessage(CC.translate("&4&l** SKRRT PET &7[Speed V] &4&lEXPIRED! &4&l**"));

            }
        }).runTaskLater(CosmicSkyInventoryPets.get(), (long) this.freezeDuration(this.petLevel) * 20L);
    }

    private void skrrtAbilities(final Player player) throws PrefixedPetException {
        Iterator var2 = player.getNearbyEntities(16.0D, 16.0D, 16.0D).iterator();
        if (player.getNearbyEntities(16.0D, 16.0D, 16.0D).isEmpty()) {

            player.sendMessage(CC.translate("&c&lPET: &cCould not find a valid target!"));
        }

        while (true) {
            Player p;
            do {
                do {
                    do {
                        Entity ent;
                        do {
                            if (!var2.hasNext()) {
                                return;
                            }

                            ent = (Entity) var2.next();
                        } while (!(ent instanceof Player));

                        p = (Player) ent;
                    } while (EssentialsUtils.isVanished(p));
                } while (p.hasMetadata("NPC"));
            } while (p.hasMetadata("in_duel"));


            FPlayer castingPlayer = (FPlayer) FPlayers.getInstance().getByPlayer(player);
            FPlayer surroundingPlayer = (FPlayer) FPlayers.getInstance().getByPlayer(p);

            if (CooldownUtils.isCooldownActive(p, "skrrtCooldown")) {
                player.sendMessage(CC.translate("&c&lPET: &c" + p.getName() + "&c is still on skrrt cooldown for &n" + CooldownUtils.getFormattedCooldown(p, "skrrtCooldown") + "&c."));
                continue;
            }

            if (AstroItemSkinsAPI.hasSkinEquipped(p, SkinType.BACK_TO_SCHOOL_2021_BOOTS) || AstroItemSkinsAPI.hasSkinEquipped(p, SkinType.MMM_BACK_TO_SCHOOL_2021_BOOTS)) {
                p.sendMessage(CC.translate("&8&l* BIG TIMBS &7Blocked &4Skrrt Pet &7[" + player.getName() + "&7] &8&l*"));
                player.sendMessage(CC.translate("&8&l* &f" + p.getName() + " &7Blocked &4Skrrt Pet &7[&8&lBig Timbs&7] &8&l*"));
                continue;
            }

            if (AstroItemSkinsAPI.hasSkinEquipped(p, SkinType.EASTER_2022_MUTATED_AMULET)) {
                p.sendMessage(CC.translate("&e&l* &2&lMutated&r &e&lCheshire &7Blocked &4Skrrt Pet &7[" + player.getName() + "&7] &e&l*"));
                player.sendMessage(CC.translate("&e&l* &f" + p.getName() + " &7Blocked &4Skrrt Pet &7[&2&lMutated&r &e&lCheshire&7] &e&l*"));
                continue;
            }


            if (AstroItemSkinsAPI.hasSkinEquipped(p, SkinType.SUMMER_2021_BOOTS)) {
                p.sendMessage(CC.translate("&6&l* ROLLER SKATES &7Blocked &4Skrrt Pet &7[" + player.getName() + "&7] &6&l*"));
                player.sendMessage(CC.translate("&6&l* &f" + p.getName() + " &7Blocked &4Skrrt Pet &7[Rolles Skates] &6&l*"));
                continue;
            }

            if (AstroCagePvP.getInstance().getImmuneManager().hasImmune(p)) {
                p.sendMessage(CC.translate("&c&lPET: &cYou cannot use pets one someone who has immunity."));
                continue;
            }


            if (AstroItemSkinsAPI.hasSkinEquipped(p, SkinType.EASTER_2022_MUTATED_AMULET)) {
                p.sendMessage(CC.translate("&5&l* Cheshire Grin &7Blocked &4Skrrt Pet &7[" + player.getName() + "&7] &5&l*"));
                player.sendMessage(CC.translate("&5&l* &f" + p.getName() + " &7Blocked &4Skrrt Pet &7[&5&lCheshire Grin&7] &5&l*"));
                continue;
            }



            if (UndertowHelmet.undertowField.contains(p)) {
                p.sendMessage(CC.translate("&a&l* UNDERTOW HELMET &7Blocked &4Skrrt Pet &7[&f" + player.getName() + "&7] &a&l*"));
                player.sendMessage(CC.translate("&a&l* &f" + p.getName() + " &7Blocked &4Skrrt Pet &7[&a&lUndertow Helmet&7] &a&l*"));
                continue;
            }

            if (!WorldGuardUtils.isPvPEnabled(p.getLocation())) {
                player.sendMessage(CC.translate("&c" + p.getName() + " is in a non-pvp enabled region."));
                continue;
            }
            if (castingPlayer.getRelationTo(surroundingPlayer) == Relation.ENEMY || castingPlayer.getRelationTo(surroundingPlayer) == Relation.NEUTRAL) {
                player.playSound(player.getLocation(), Sound.DIG_SNOW, 1.1F, 3.0F);
                player.playSound(player.getLocation(), Sound.WITHER_SHOOT, 1.1F, 3.0F);
                player.sendMessage("");
                player.sendMessage(CC.translate("&4&l*** SKRRT PET [&6" + p.getName() + "&4&l] - SKRRT'D! **"));
                player.sendMessage("");

                p.sendMessage("");
                p.sendMessage(CC.translate("&4&l*** " + player.getName() + "&4&l's SKRRT PET [&6" + freezeDuration(5) + "s&4&l] - SKRRT! **"));
                p.sendMessage("");

                DamageIndicatorUtils.sendDamageIndicator(p, CC.translate("&4&l* SKRRT PET *"), "SKRRT");

                int skrrtCooldown = 120;
                CooldownUtils.registerCooldown(p, new PetCooldown("skrrtCooldown", skrrtCooldown));
                CrankedAmuletUtil.tryHatchAbility(p, player);

                CombatLog.flagCombat(p);
                freezeThePlayer(p);
            }
        }

    }

    public boolean onTickInHotBar(Player var1, int var2, InventoryPetItem var3) {
        return false;
    }

    private void freezeThePlayer(final Player p) {
        try {
            p.playSound(p.getLocation(), Sound.STEP_SNOW, 1.0F, 1.4F);
            p.setWalkSpeed(0.0F);
            //p.removePotionEffect(PotionEffectType.JUMP);
            //p.removePotionEffect(PotionEffectType.SLOW);
            //p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, (int) ((long) this.freezeDuration(this.petLevel) * 20L), 128));
            //p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) ((long) this.freezeDuration(this.petLevel) * 20L), 128));
            p.setMetadata("frozenSkrrt", new FixedMetadataValue(CosmicSkyInventoryPets.get(), System.currentTimeMillis()));
            Bukkit.getScheduler().runTaskLater(CosmicSkyInventoryPets.get(), () -> {
                p.removeMetadata("frozenSkrrt", CosmicSkyInventoryPets.get());
                //p.removePotionEffect(PotionEffectType.SLOW);
                //p.removePotionEffect(PotionEffectType.JUMP);
                p.setWalkSpeed(0.2F);
            }, this.freezeDuration(this.petLevel) * 20L);

        } catch (Exception e) {
            p.removeMetadata("frozenSkrrt", CosmicSkyInventoryPets.get());
        }
    }
}
