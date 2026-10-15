package org.arkhamnetwork.enchantments.enchantments.swords;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.GeneralUtil;
import com.cosmicpvp.cosmicutils.utils.WorldUtil;
import com.rit.sucy.CustomEnchantment;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.ActionBarMessageUtils;
import org.arkhamnetwork.enchantments.util.CacheUtils;
import org.arkhamnetwork.enchantments.util.RageStacksCache;
import org.arkhamnetwork.enchantments.util.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;


public class Rage extends CustomEnchantment {
    public Rage() {
        super("Rage", GeneralUtil.weapons, 2);
        this.max = 6;
        this.base = 10.0D;
        this.interval = 8.0D;
        Bukkit.getPluginManager().registerEvents(new RageListener(), Enchantments.getInstance());
    }

    public void applyEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageByEntityEvent event) {
        if (!event.isCancelled() && event.getDamage() > 0.0 && user instanceof Player) {
            if (!(target instanceof Player)) {
                return;
            }
            if (target.hasMetadata("doubledStriked") && System.currentTimeMillis() - target.getMetadata("doubleStriked").get(0).asLong() < 500L) {
                return;
            }

            RageStacksCache rageStacksCache = CacheUtils.getRageStackCache((Player) user);

            if (rageStacksCache.getLastRageStackTime() + 5000 < System.currentTimeMillis() || !rageStacksCache.isSameVictim((Player) target)) {
                rageStacksCache.updateLastRageStackTime();
                rageStacksCache.resetRageStack();
                rageStacksCache.updateVictim((Player) target);
                sendRageResetActionBar((Player) user);
                return;
            }

            /*
            if (AstroItemSkinsAPI.hasSkinEquipped(((Player) user).getPlayer(), SkinType.VALENTINES_2022_SWORD) || AstroItemSkinsAPI.hasSkinEquipped(((Player) user).getPlayer(), SkinType.VALENTINES_2022_MUTATED_SWORD)) {
                rageStacksCache.setMaxRageStack(8);
            }

            if (AstroItemSkinsAPI.hasSkinEquipped(((Player) target).getPlayer(), SkinType.VALENTINES_2022_SWORD) && Math.random() < 0.15) {
                target.sendMessage(CC.translate("&a&l** PRICKLE PEAR &7Reset Rage Stacks &a&l**"));
                rageStacksCache.resetRageStack();
            }

             */

            if (AstroItemSkinsAPI.hasSkinEquipped(((Player) target).getPlayer(), SkinType.MMM_THANKSGIVING_2022_SWORD_GREEN)) {
                if(Math.random() < 0.15) {
                    target.sendMessage(CC.translate("&a&l** &a&lYoda's Sabre &7Reset Rage Stacks &a&l**"));
                    rageStacksCache.resetRageStack();
                }
                if(Math.random() < 0.1) {
                    user.sendMessage(CC.translate("&a&l** &a&lYoda's Sabre &7[+1 Rage Stack] &a&l**"));
                    rageStacksCache.incrementRageStack();
                }
            }

            //double rageDamage = Math.max(1, rageStacksCache.getRageStack()/Enchantments.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getDouble("rageDivider"));
            double rageDamage = Math.max(1, (1 + (rageStacksCache.getRageStack() * Enchantments.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getDouble("rageDivider"))));

            if (AstroItemSkinsAPI.hasSkinEquipped((Player) user, SkinType.MMM_THANKSGIVING_2022_SWORD_GREEN)) {
                rageDamage *= 1.075;
                target.sendMessage(CC.translate("&a&l* YODAS SABRE &7[+7.5% Rage DMG] &a&l*"));
            }
            if (AstroItemSkinsAPI.hasSkinEquipped((Player) target, SkinType.VALENTINES_2021_AMULET)) {
                rageDamage *= 0.85;
                target.sendMessage(CC.translate("&c&l* ROSE AMULET &7[-15% Rage DMG] &c&l*"));
            }

            if (AstroItemSkinsAPI.hasSkinEquipped((Player) user, SkinType.SPRING_BREAK_2022_MUTATED_HELMET)) {
                rageDamage *= 1.035;
                user.sendMessage(CC.translate("&2&l* MUTATED UNDERTOW HELMET &7[+3.5% Rage DMG] &2&l*"));
            }


            if (AstroItemSkinsAPI.hasSkinEquipped((Player) user, SkinType.CHRISTMAS_2021_SWORD) || AstroItemSkinsAPI.hasSkinEquipped((Player) user, SkinType.CHRISTMAS_2021_MUTATED_SWORD)) {
                //rageDamage *= 1.05;
                user.sendMessage(CC.translate("&4&l* SEASON'S BEATING &7[+10% Rage DMG] &4&l*"));
            }
            if (AstroItemSkinsAPI.hasSkinEquipped((Player) user, SkinType.HALLOWEEN_2022_BACKPACK)) {
                //rageDamage *= 1.25;
                user.sendMessage(CC.translate("&4&l* GRIM CLOAK &7[+25% Rage DMG] &4&l*"));
            }

            if (org.arkhamnetwork.enchantments.util.GeneralUtil.isUsingAxe(user)) {
                rageDamage *= 1.05;
            }


            //if (WeaponBonusesAPI.hasWeaponBonuses(((Player) user).getItemInHand()) && WeaponBonusesAPI.hasBonus(((Player) user).getItemInHand(), BonusType.BONUS_RAGE_STACK_DMG)) {
                //rageDamage *= 1.05;
            //}

            if ((AstroItemSkinsAPI.hasSkinEquipped((Player) target, SkinType.EASTER_2022_AMULET) || AstroItemSkinsAPI.hasSkinEquipped((Player) target, SkinType.EASTER_2022_MUTATED_AMULET)) && Math.random() < 0.3) {
                //rageDamage *= 0.65;
                target.sendMessage(CC.translate("&e&l* CHESHIRE GRIN &7[-35% Rage DMG] &e&l*"));
            }

            if (AstroItemSkinsAPI.hasSkinEquipped((Player) target, SkinType.CHRISTMAS_2022_AMULET_ACTIVE)) {
                //rageDamage *= 0.75;
            }

            /*
            if (AstroItemSkinsAPI.hasSkinEquipped(((Player) user).getPlayer(), SkinType.VALENTINES_2022_SWORD) || AstroItemSkinsAPI.hasSkinEquipped(((Player) user).getPlayer(), SkinType.VALENTINES_2022_MUTATED_SWORD)) {
                //rageDamage *= 1.1;
                target.sendMessage(CC.translate("&c&l* SKELETON KEY &7[+10% Rage DMG] &c&l*"));
            }

             */

            if (AstroItemSkinsAPI.hasSkinEquipped((Player) target, SkinType.BACK_TO_SCHOOL_2021_BOOTS) && Math.random() < 0.1) {
                target.sendMessage(CC.translate("&8&l* BIG TIMBS &7[Cancelled Rage Stack DMG] &8&l*"));
            } else {
                //event.setDamage(event.getDamage() * (1 + rageDamage));
                this.sendRageActionBar((Player)user, (Player)target, rageStacksCache);
                this.playRageEffect(target.getLocation());
                if(rageDamage < 1) rageDamage = 1;
                event.setDamage(event.getDamage() * rageDamage);
            }


            rageStacksCache.incrementRageStack();
            rageStacksCache.updateLastRageStackTime();
            rageStacksCache.updateVictim((Player) target);

            target.setMetadata("effectedByRage", new FixedMetadataValue(Enchantments.getInstance(), System.currentTimeMillis()));
        }
    }

    private void sendRageActionBar(Player player, Player effected, RageStacksCache rageStacksCache) {
        ActionBarMessageUtils.sendMessage(player, CC.translate("&eRage Stacks &8| &6" + rageStacksCache.getRageStack() + "x &8| &6" + effected.getName()));
    }

    private void sendRageResetActionBar(Player player) {
        ActionBarMessageUtils.sendMessage(player, CC.translate("&eRage Stacks &8| &cReset"));
    }

    private void playRageEffect(Location loc) {
        WorldUtil.sendWorldEffect(loc.getBlock(), Material.REDSTONE_BLOCK.getId(), 2001);
    }
}

