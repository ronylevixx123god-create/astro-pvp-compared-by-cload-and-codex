package org.arkhamnetwork.enchantments.enchantments.axes;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import lombok.SneakyThrows;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.skins.amulets.CannonballAmulet;
import me.astropvp.astropvpskins.objects.skins.axes.BoostedChainsaw;
import me.astropvp.astropvpskins.objects.skins.swords.Carver;
import net.astropvp.core.killtracker.KillTrackerAPI;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.DamageIndicatorUtils;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.text.DecimalFormat;

public class Rogue extends CustomEnchantment {

    public Rogue() {
        super("Rogue", GeneralUtil.weapons, 2);
        this.max = 3;
        this.base = 10.0;
        this.interval = 10.0;
    }

    @SneakyThrows
    public void applyEffect(final LivingEntity player, final LivingEntity playerHit, final int level, final EntityDamageByEntityEvent e) {
        if (!(player instanceof Player)) // rooThink
            return;
        if (!(playerHit instanceof Player)) return;

        final Player p = (Player) player;
        final ItemStack used = p.getItemInHand();
        if (used == null || !EnchantmentAPI.itemHasEnchantment(used, this.enchantName)) {
            return;
        }
        if (this.isPlayerBehind(p, playerHit)) {
            double multi = 1.0D + (0.166666666666666D * (double) level * 0.4) + (Math.random() / 2.0D);
            final int soulCost = 150;
            if (soulCost > KillTrackerAPI.getAllSouls(p)) {
                return;
            }

            if (p.hasMetadata("noSoulProc"))
                return;
            if (playerHit.hasMetadata("immuneRogue"))
                return;

            if (AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.SUMMER_2021_AMULET) && Math.random() <= 0.20) {
                playerHit.sendMessage(CC.translate("&c&l* LUAU LEI &7Blocked Rogue &c&l*"));
                return;
            }

            if (AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.CHRISTMAS_2022_BACKPACK) && playerHit.getHealth() < 10) {
                playerHit.sendMessage(CC.translate("&9&l* Arctic Excursion Pack &7Blocked Rogue &9&l*"));
                return;
            }

            if (((Player) playerHit).getPlayer().getInventory().getChestplate() != null && (AstroItemSkinsAPI.hasSkinEquipped(((Player) playerHit).getPlayer(), SkinType.THANKSGIVING_2021_BACKPACK) || AstroItemSkinsAPI.hasSkinEquipped(((Player) playerHit).getPlayer(), SkinType.THANKSGIVING_2021_MUTATED_BACKPACK)) && Math.random() < 0.075) {
                return;
            }


            if (AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.THANKSGIVING_2022_BOOTS) && player.getHealth() < 20 && Math.random() < 0.05) {
                playerHit.setMetadata("immuneRogue", new FixedMetadataValue(Enchantments.getInstance(), true));
                playerHit.sendMessage(CC.translate("&6&l* MAINFRAME BOOTS &7[5s Immune to Rogue] &6&l*"));
                Bukkit.getScheduler().runTaskLaterAsynchronously(Enchantments.getInstance(), () -> {
                    playerHit.removeMetadata("immuneRogue", Enchantments.getInstance());
                }, 20 * 5L);
            }


            if (((Carver) SkinType.THANKSGIVING_2021_SWORD.getItemSkin()).getRogueSilenced().contains(p.getName())) {
                return;
            }

            if (AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.HALLOWEEN_2022_AMULET)) {
                return;
            }

            if ((AstroItemSkinsAPI.hasSkinEquipped(p, SkinType.VALENTINES_2022_BELT) && Math.random() < 0.1) || (AstroItemSkinsAPI.hasSkinEquipped(p, SkinType.VALENTINES_2022_MUTATED_BELT) && Math.random() < 0.1)) {
                p.sendMessage(CC.translate("&c&l* ROGUE &7Blocked [Bling Belt] &c&l*"));
                return;
            }

            if (CannonballAmulet.affected.contains(playerHit)) {
                return;
            }

            if (((BoostedChainsaw) SkinType.BACK_TO_SCHOOL_2021_AXE.getItemSkin()).getChainSawedPlayers().contains(p.getName())) {
                return;
            }
            if (KillTrackerAPI.hasGodMode(p) && KillTrackerAPI.getAllSouls(p) > 0) {
                if (!p.hasMetadata("lastSoulRemoveEvent") || System.currentTimeMillis() - p.getMetadata("lastSoulRemoveEvent").get(0).asLong() > 1000L) {
                    KillTrackerAPI.removeSoulsFromGems(p, soulCost);
                }
                double damage = e.getDamage();
                if (!(e.getDamager() instanceof Player)) return;
                if (((Player) e.getDamager()).getItemInHand() == null) return;
                /*
                if (EnchantmentAPI.itemHasEnchantment(((Player) e.getDamager()).getItemInHand(), "Soul Master")) {
                    damage *= 1.1;
                }

                 */
                /*
                if (AstroItemSkinsAPI.hasSkinEquipped(p, SkinType.VALENTINES_2022_MUTATED_BELT)) {
                    damage *= 1.15;

                }

                if (AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.JULY_2021_BACKPACK) && player.getHealth() <= player.getMaxHealth() / 2 && !player.hasMetadata("noCape")) {
                    multi *= 1.15;
                }

                if (AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.CHRISTMAS_2022_AMULET_ACTIVE)) {
                    multi *= 1.15;
                }
                if (AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.HALLOWEEN_2022_BACKPACK)) {
                    multi *= 0.5;
                }


                if (AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.EASTER_2022_SWORD) || AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.EASTER_2022_MUTATED_SWORD)) {
                    multi *= 1.1;
                }

                if (AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.VALENTINES_2022_BACKPACK) || AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.VALENTINES_2022_MUTATED_BACKPACK)) {
                    multi *= 0.75;
                }

                if (player.hasMetadata("carver_damage")) {
                    multi *= 1.1;
                }


                if (AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.CHRISTMAS_2021_HELMET) || AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.CHRISTMAS_2021_MUTATED_HELMET)) {
                    multi *= 0.9;
                }

                if (AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.EASTER_2022_MUTATED_BOOTS)) {
                    multi *= 0.67;
                }*/

                if (multi <= 1) {
                    multi = 1.15;
                }

                if (AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.THANKSGIVING_SWORD_HARVESTER) && !AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.VALENTINES_2021_HELMET)) {
                    playerHit.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l** ROGUE &7[" + new DecimalFormat("##.##").format(multi) + "x] &c&l-" + soulCost + " SOULS ** (&e&lSoul Harvester&c&l)"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&l** ROGUE &7[" + new DecimalFormat("##.##").format(multi) + "x] ** (&e&lSoul Harvester&3&l)"));

                } else if (AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.SUMMER_2021_SWORD)) {
                    playerHit.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l** ROGUE &7[" + new DecimalFormat("##.##").format(multi) + "x] &c&l-" + soulCost + " SOULS ** (&c&lSoul Maui's Hook&c&l)"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&l** ROGUE &7[" + new DecimalFormat("##.##").format(multi) + "x] ** (&c&lSoul Maui's Hook&3&l)"));

                } else if (AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.DOOMSDAY_SWORD)) {
                    playerHit.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l** ROGUE &7[" + new DecimalFormat("##.##").format(multi) + "x] &c&l-" + soulCost + " SOULS ** (&2&lDoomsday Machete&c&l)"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&l** ROGUE &7[" + new DecimalFormat("##.##").format(multi) + "x] ** (&2&lDoomsday Machete&3&l)"));

                } else {
                    playerHit.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l** ROGUE &7[" + new DecimalFormat("##.##").format(multi) + "x] &c&l-" + soulCost + " SOULS **"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&l** ROGUE &7[" + new DecimalFormat("##.##").format(multi) + "x] **"));

                }


                //e.setDamage(damage + multi * 0.1);
                e.setDamage(damage * multi);

                de.inventivegames.particle.ParticleEffect.CRIT.sendToPlayers(Bukkit.getOnlinePlayers(), player.getLocation(), 0, 1, 0, 0.2f, 20);

                KillTrackerAPI.removeSoulsFromGems(p, soulCost);
                DamageIndicatorUtils.sendDamageIndicator((Player) playerHit, CC.DarkAquaB + "* " + "ROGUE" + CC.Gray + "[" + new DecimalFormat("##.##").format(multi) + "x]" + CC.DarkAquaB + "*", "ROGUE");
            }
        }
    }

    public boolean isPlayerBehind(final Player player, final Entity theEntity) {
        Location playerLoc = player.getLocation();
        Location targetLoc = theEntity.getLocation();
        double pvecy = -Math.sin(Math.toRadians(playerLoc.getPitch()));
        double pvecx = -Math.cos(Math.toRadians(playerLoc.getPitch())) * Math.sin(Math.toRadians(playerLoc.getYaw()));
        double pvecz = Math.cos(Math.toRadians(playerLoc.getPitch())) * Math.cos(Math.toRadians(playerLoc.getYaw()));
        double tvecy = -Math.sin(Math.toRadians(targetLoc.getPitch()));
        double tvecx = -Math.cos(Math.toRadians(targetLoc.getPitch())) * Math.sin(Math.toRadians(targetLoc.getYaw()));
        double tvecz = Math.cos(Math.toRadians(targetLoc.getPitch())) * Math.cos(Math.toRadians(targetLoc.getYaw()));
        double dot = tvecx * pvecx + tvecy * pvecy + tvecz * pvecz;
        return dot > 0.6;
    }
}
