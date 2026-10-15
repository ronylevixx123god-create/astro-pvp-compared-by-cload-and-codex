package org.arkhamnetwork.enchantments.enchantments.heroic;

import java.text.*;

import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.skins.axes.GoldenAdmirer;
import me.astropvp.astropvpskins.objects.skins.boots.SinkOrSwipFlippers;
import me.astropvp.astropvpskins.utils.DamageIndicatorUtils;
import org.bukkit.event.entity.*;
import java.util.stream.*;
import org.arkhamnetwork.enchantments.*;
import org.bukkit.metadata.*;
import org.bukkit.plugin.*;
import com.cosmicpvp.cosmicutils.utils.*;
import org.bukkit.*;
import java.util.*;
import org.bukkit.entity.*;

public class HeroicTrap extends HeroicCustomEnchantment
{
    private DecimalFormat df;

    public HeroicTrap() {
        super("Titan Trap", "Trap");
        this.df = new DecimalFormat("#.##");
        this.max = 3;
    }

    public void applyEffect(final LivingEntity player, final LivingEntity target, final int level, final EntityDamageByEntityEvent e) {
        if (!(target instanceof Player)) {
            return;
        }
        if (((Player)player).getWalkSpeed() < 0.2f) {
            return;
        }
        if (AstroItemSkinsAPI.hasSkinEquipped((Player) target, SkinType.THANKSGIVING_HELMET_WITCH) && !GoldenAdmirer.isAdmirerSilenced((Player) target))
            return;
        if (AstroItemSkinsAPI.hasSkinEquipped((Player) target, SkinType.VIRUS_HELMET_HAZMAT))
            return;

        final double random = Math.random();
        double chance = 0.0095 * level;
        final double durationTicks = 35 + level * 10;
        if (random < chance) {
            final Player ptarget = (Player)target;
            if (target.hasMetadata("metaphysicalEnchant")) {
                final int metaphysicalLevel = target.getMetadata("metaphysicalEnchant").get(0).asInt();
                chance -= metaphysicalLevel * 0.0125;
                chance = Math.max(chance, 0.01);
                if (random > chance) {
                    ptarget.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "** METAPHYSICAL (" + ChatColor.DARK_GRAY + "Trap blocked!" + ChatColor.BOLD + ") **");
                    return;
                }
            }
            if (AstroItemSkinsAPI.hasSkinEquipped(ptarget, SkinType.CHRISTMAS_BOOTS_SLIPPERS)) {
                ptarget.sendMessage(CC.translate("&3&l* TEDDY SLIPPERS &7Reflected &dTitan Trap &3&l*"));
                return;
            }

            if (AstroItemSkinsAPI.hasSkinEquipped(ptarget, SkinType.HALLOWEEN_2022_HELMET)) {
                ptarget.sendMessage(CC.translate("&4&l* GRIM HOOD BLOCKED &7[Titan Trap] &4&l*"));
                return;
            }

            if (((Player)ptarget).getInventory().getBoots() != null && AstroItemSkinsAPI.hasSkinEquipped((Player) ptarget, SkinType.HALLOWEEN_2021_BOOTS))
                return;
            if (AstroItemSkinsAPI.hasSkinEquipped(ptarget, SkinType.FLAMING_HALO) && Math.random() < 0.50) {
                ptarget.sendMessage(CC.translate("&6&l* FLAMING HALO BLOCKED &7[Titan Trap] &6&l*"));
                return;
            }


            if (AstroItemSkinsAPI.hasSkinEquipped((Player) ptarget, SkinType.SPRING_BREAK_2022_BOOTS) || AstroItemSkinsAPI.hasSkinEquipped((Player) ptarget, SkinType.SPRING_BREAK_2022_MUTATED_BOOTS)) {
                SinkOrSwipFlippers.addSinkStack((Player) ptarget);
                ptarget.sendMessage(CC.translate("&a&l* BLOCKED FREEZE &7[+1 Stack] &a&l*"));
                return;
            }

            if (AstroItemSkinsAPI.hasSkinEquipped(ptarget, SkinType.CHRISTMAS_AMULET_SNOWFLAKE) && !AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.BACK_TO_SCHOOL_2021_AXE)) {
                return;
            }

            if (AstroItemSkinsAPI.hasSkinEquipped(ptarget, SkinType.THANKSGIVING_HELMET_WITCH) && (!GoldenAdmirer.isAdmirerSilenced(ptarget))) {
                ptarget.sendMessage(CC.translate("&e&l* WITCH HAT &7Blocked &dTitan Trap &e&l*"));
                return;
            }
            ptarget.setWalkSpeed(0.001f);
            final Location location = ptarget.getEyeLocation();
            final Material typeItWas = location.getBlock().getType();
            List<Player> pl = ptarget.getNearbyEntities(32.0, 32.0, 32.0).stream().filter(ent -> ent instanceof Player).map(ent -> (Player) ent).collect(Collectors.toList());
            pl.add(ptarget);
            for (final Player near : pl) {
                near.sendBlockChange(location, Material.STATIONARY_WATER, (byte)0);
            }
            ptarget.playSound(ptarget.getLocation(), Sound.WATER, 10.0f, 1.1f);
            ptarget.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "** TITAN TRAP [" + ChatColor.GRAY + this.df.format(durationTicks / 20.0) + "s" + ChatColor.RED + ChatColor.BOLD + "] **");
            ptarget.setMetadata("is_trapped", new FixedMetadataValue((Plugin)Enchantments.getInstance(), true));
            DamageIndicatorUtils.sendDamageIndicator((Player) ptarget, CC.DarkAquaB + "* TITAN TRAP " + CC.Gray + "[" + durationTicks / 20 + "]" + CC.DarkAquaB + "*" + CC.DarkPurpleB + "*", "TRAP");

            Enchantments.getInstance().getServer().getScheduler().runTaskLater((Plugin) Enchantments.getInstance(), () -> {
                ptarget.setWalkSpeed(0.2f);
                ptarget.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "** FREE [" + (Object) ChatColor.GRAY + "Titan Trap" + (Object) ChatColor.GREEN + (Object) ChatColor.BOLD + "] **");
                for (Player tar : pl) {
                    tar.sendBlockChange(location, typeItWas, (byte) 0);
                }
            }, (long) ((int) durationTicks));
            Location l = ptarget.getLocation();
        }
    }
}

