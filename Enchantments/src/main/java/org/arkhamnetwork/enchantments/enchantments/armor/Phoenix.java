package org.arkhamnetwork.enchantments.enchantments.armor;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.TimeUtils;
import com.rit.sucy.CustomEnchantment;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.skins.axes.BoostedChainsaw;
import me.astropvp.astropvpskins.objects.skins.boots.BarrysBoltsSkin;
import me.astropvp.astropvpskins.tasks.UndertowFieldTask;
import me.ifamasssxd.cosmicmasks.MaskType;
import net.astropvp.armorsets.AstroArmorSetsAPI;
import net.astropvp.armorsets.struct.ArmorSetType;
import net.astropvp.core.killtracker.KillTrackerAPI;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.mastery.MasteryFeignDeath;
import org.arkhamnetwork.enchantments.util.DamageIndicatorUtils;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.github.paperspigot.Title;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Phoenix extends CustomEnchantment {
    public static HashMap<String, Long> last_phoenix;
    public static Map<UUID, Integer> phoenixEquipped;

    public Phoenix() {
        super("Phoenix", GeneralUtil.boots, 2);
        this.max = 3;
        this.base = 15.0;
        this.interval = 20.0;
        Bukkit.getPluginManager().registerEvents(new PhoenixListener(this), Enchantments.getInstance());
        //Bukkit.getScheduler().runTaskTimer(Enchantments.getEssentials(), new reducePhoenixCount(), 12000L, 12000L);
    }

    public static boolean hasPhoenixEquipped( Player player) {
        return Phoenix.phoenixEquipped.containsKey(player.getUniqueId());
    }

    public void applyEquipEffect( Player player,  int enchantLevel) {
        Phoenix.phoenixEquipped.put(player.getUniqueId(), enchantLevel);
    }

    public void applyUnequipEffect( Player player,  int level,  ItemStack item) {
        Phoenix.phoenixEquipped.remove(player.getUniqueId());
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
        if (player instanceof Player) {
            double dmg = e.getFinalDamage();
            if (Phoenix.procPhoenix((Player) player, attacker, level, dmg)) {
                e.setDamage(0.0);
                e.setCancelled(true);
            }
        }
    }

    public void applyUnequipEffect( Player player,  int enchantLevel) {
        this.applyUnequipEffect(player, enchantLevel, null);
    }

    public static boolean procPhoenix( Player player,  LivingEntity damager,  double dmg) {
         Integer level = Phoenix.phoenixEquipped.get(player.getUniqueId());
        return level != null && procPhoenix(player, damager, level, dmg);
    }

    public static boolean procPhoenix( Player player,  LivingEntity damager,  int level, double dmg) {
        if (!(damager instanceof Player)) {
            return false;
        }

        if (player.getHealth() - dmg > 0.0) {
            return false;
        }

         boolean utilityBelt = AstroItemSkinsAPI.hasSkinEquipped(player, SkinType.CORRUPT_BELT_UTILITY);
         boolean deathKnightBlockedPheonix = Math.random() <= 0.5 && MaskType.hasEquipped((Player) damager, "DEATH_KNIGHT");
         boolean thanosBlockedPheonix = MaskType.hasEquipped((Player) damager, "THANOS");

         boolean canUse = canUsePhoenix(player);

        if (!canUse) {
            if (120L - (System.currentTimeMillis() - Phoenix.last_phoenix.get(player.getUniqueId().toString())) / 1000L != 0L) {
                player.sendMessage("");
            }
            player.sendMessage(CC.translate("&c&l** PHOENIX ON COOLDOWN ** &7[" + TimeUtils.formatSeconds(120L - (System.currentTimeMillis() - Phoenix.last_phoenix.get(player.getUniqueId().toString())) / 1000L) + "]"));
            player.sendMessage("");
        }
        if (canUse) {
            if (!utilityBelt) {
                if (deathKnightBlockedPheonix) {
                    player.sendMessage("");
                    player.sendMessage(CC.RedB + "* PHOENIX BLOCKED [" + CC.Gray + damager.getName() + CC.RedB + "] *");
                    player.sendMessage(CC.translate("&7You are now on &nPhoenix Cooldown&7 for 2 minutes."));
                    player.sendMessage(CC.translate("&7&oUse /phoenixcooldown&7 to check your cooldown timer."));
                    player.sendMessage("");
                    damager.sendMessage(CC.translate(""));
                    damager.sendMessage(CC.RedB + "* DEATH KNIGHT MASK [" + CC.Gray + player.getName() + "'s Phoenix Blocked" + CC.RedB + "] *");
                    damager.sendMessage(CC.translate("&7They are now on &nPhoenix Cooldown&7 for 2 minutes."));
                    damager.sendMessage(CC.translate(""));
                    Phoenix.last_phoenix.put(player.getUniqueId().toString(), System.currentTimeMillis());
                    Bukkit.getLogger().info("Phoenix blocked from proccing for " + player.getName() + " from damager " + damager.getName());
                    return false;
                }
                if (player.hasMetadata("witcher_ability")) {
                    player.sendMessage("");
                    player.sendMessage(CC.RedB + "* PHOENIX BLOCKED (WITCHER) [" + CC.Gray + damager.getName() + CC.RedB + "] *");
                    player.sendMessage(CC.translate("&7You are now on &nPhoenix Cooldown&7 for 2 minutes."));
                    player.sendMessage(CC.translate("&7&oUse /phoenixcooldown&7 to check your cooldown timer."));
                    player.sendMessage("");
                    damager.sendMessage(CC.translate(""));
                    damager.sendMessage(CC.RedB + "* WITCHER PET [" + CC.Gray + player.getName() + "'s Phoenix Blocked" + CC.RedB + "] *");
                    damager.sendMessage(CC.translate("&7They are now on &nPhoenix Cooldown&7 for 2 minutes."));
                    damager.sendMessage(CC.translate(""));
                    Phoenix.last_phoenix.put(player.getUniqueId().toString(), System.currentTimeMillis());
                    return false;
                }

                if (player.hasMetadata("neutralized")) {
                    player.sendMessage(CC.translate("&5&l* PHOENIX BLOCKED &7[Neutralized] &5&l*"));
                    return false;
                }

                if (player.hasMetadata("soulSilenced")) {
                    player.sendMessage(CC.translate("&4&l* PHOENIX BLOCKED &7[Soul Enchants Silenced] &4&l*"));
                    return false;
                }

                if (thanosBlockedPheonix) {
                    player.sendMessage("");
                    player.sendMessage(CC.RedB + "* PHOENIX BLOCKED [" + CC.Gray + damager.getName() + CC.RedB + "] *");
                    player.sendMessage(CC.translate("&7You are now on &nPhoenix Cooldown&7 for 2 minutes."));
                    player.sendMessage(CC.translate("&7&oUse /phoenixcooldown&7 to check your cooldown timer."));
                    player.sendMessage("");
                    damager.sendMessage(CC.translate(""));
                    damager.sendMessage(CC.RedB + "* THANOS MASK [" + CC.Gray + player.getName() + "'s Phoenix Blocked" + CC.RedB + "] *");
                    damager.sendMessage(CC.translate("&7They are now on &nPhoenix Cooldown&7 for 2 minutes."));
                    damager.sendMessage(CC.translate(""));
                    Phoenix.last_phoenix.put(player.getUniqueId().toString(), System.currentTimeMillis());
                    return false;
                }
                if (damager.hasMetadata("bansheed") || damager.hasMetadata("champion_ability")) {
                    player.sendMessage("");
                    player.sendMessage(CC.RedB + "* PHOENIX BLOCKED [" + CC.Gray + damager.getName() + CC.RedB + "] *");
                    player.sendMessage(CC.translate("&7You are now on &nPhoenix Cooldown&7 for 2 minutes."));
                    player.sendMessage(CC.translate("&7&oUse /phoenixcooldown&7 to check your cooldown timer."));
                    player.sendMessage("");
                    damager.sendMessage(CC.translate(""));
                    damager.sendMessage(CC.RedB + "* BANSHEE RKO [" + CC.Gray + player.getName() + "'s Phoenix Blocked" + CC.RedB + "] *");
                    damager.sendMessage(CC.translate("&7They are now on &nPhoenix Cooldown&7 for 2 minutes."));
                    damager.sendMessage(CC.translate(""));
                    Phoenix.last_phoenix.put(player.getUniqueId().toString(), System.currentTimeMillis());
                    return false;
                }

                if (((BoostedChainsaw) SkinType.BACK_TO_SCHOOL_2021_AXE.getItemSkin()).getChainSawedPlayers().contains(player.getName())) {
                    player.sendMessage(CC.translate("&c&l* PHOENIX BLOCKED [&7Chainsaw Silence Ability&c&l] *"));
                    return false;

                }

                if (player.hasMetadata("banshee_scrolls")) {
                    player.sendMessage(CC.translate("&c&l* PHOENIX BLOCKED [&7BANSHEE RKO&c&l] *"));
                    damager.sendMessage(CC.RedB + "* BANSHEE RKO [" + CC.Gray + player.getName() + "'s Phoenix Blocked" + CC.RedB + "] *");
                    return false;

                }

                if (UndertowFieldTask.affected.contains(player)) {
                    player.sendMessage(CC.translate("&4&l* PHOENIX BLOCKED &7Undertow Field&4&l *"));
                    return false;
                }

                if (((BarrysBoltsSkin) SkinType.JULY_2021_BOOTS.getItemSkin()).getCurrentSilencedPlayers().contains(player)) {
                    player.sendMessage(CC.translate("&c&l** &7Enemy Garry's Bolt &c&lBLOCKED PHOENIX **"));
                    return false;
                }
            }

            //==========================================
            //if the player has gotten utility belt on:
            //==========================================


            if (MaskType.hasEquipped((Player) damager, "CRANKED") && Math.random() < 0.5) {
                damager.sendMessage(CC.translate("&4&l* CRANKED MASK BLOCKED PHOENIX [&c" + player.getName() + "&4&l] *"));
                player.sendMessage(CC.translate("&4&l* PHOENIX BLOCKED [&c" + damager.getName() + " Cranked Mask&4&l] *"));
                Phoenix.last_phoenix.put(player.getUniqueId().toString(), System.currentTimeMillis());
                return false;
            }

            if (MaskType.hasEquipped((Player) damager, "M_CRANKED") && Math.random() < 0.75) {
                damager.sendMessage(CC.translate("&4&l* &2&l&nMUTATED&r &4&lCRANKED MASK BLOCKED PHOENIX [&c" + player.getName() + "&4&l] *"));
                player.sendMessage(CC.translate("&4&l* PHOENIX BLOCKED [&c" + damager.getName() + " &2&l&nMutated&r &4&lCranked Mask&4&l] *"));
                Phoenix.last_phoenix.put(player.getUniqueId().toString(), System.currentTimeMillis());
                return false;
            }

            if ((AstroItemSkinsAPI.hasSkinEquipped((Player) damager, SkinType.SPRING_BREAK_2022_HELMET) || AstroItemSkinsAPI.hasSkinEquipped((Player) damager, SkinType.SPRING_BREAK_2022_MUTATED_HELMET)) && Math.random() < 0.1) {
                damager.sendMessage(CC.translate("&a&l* UNDERTOW HELMET BLOCKED PHOENIX [&c" + player.getName() + "&a&l] *"));
                player.sendMessage(CC.translate("&c&l* PHOENIX BLOCKED [&4" + damager.getName() + " Undertow Helmet &c&l] *"));
                Phoenix.last_phoenix.put(player.getUniqueId().toString(), System.currentTimeMillis());
                return false;
            }

            if (AstroItemSkinsAPI.hasSkinEquipped((Player) damager, SkinType.VALENTINES_2022_MUTATED_SWORD) && Math.random() < 0.25) {
                damager.sendMessage(CC.translate("&c&l* SKELETON KEY BLOCKED PHOENIX [&c" + player.getName() + "&c&l] *"));
                player.sendMessage(CC.translate("&c&l* PHOENIX BLOCKED [&4" + damager.getName() + " Skeleton Key&c&l] *"));
                Phoenix.last_phoenix.put(player.getUniqueId().toString(), System.currentTimeMillis());
                return false;
            }

            if (AstroItemSkinsAPI.hasSkinEquipped((Player) damager, SkinType.EASTER_2022_HELMET) || AstroItemSkinsAPI.hasSkinEquipped((Player) damager, SkinType.EASTER_2022_MUTATED_HELMET)) {
                damager.sendMessage(CC.translate("&e&l* &d&lMAD HAT &7Blocked &cPhoenix &d&l[&c" + player.getName() + "&d&l] &e&l*"));
                player.sendMessage(CC.translate("&e&l* &d&lPHOENIX BLOCKED [&c" + damager.getName() + " Mad Hat&d&l] *"));
                Phoenix.last_phoenix.put(player.getUniqueId().toString(), System.currentTimeMillis());
                return false;
            }

            if (AstroArmorSetsAPI.getCurrentArmorSet((Player) damager) != null && AstroArmorSetsAPI.getCurrentArmorSet((Player) damager) == ArmorSetType.ULTRA && Math.random() < 0.4) {
                damager.sendMessage(CC.translate("&b&l* &7&lULTRA PHOENIX NEGATION &7Blocked &cPhoenix &b&l[&c" + player.getName() + "&b&l] &b&l*"));
                player.sendMessage(CC.translate("&b&l* &7&lPHOENIX BLOCKED [&c" + damager.getName() + " Ultra Set&b&l] *"));
                Phoenix.last_phoenix.put(player.getUniqueId().toString(), System.currentTimeMillis());
                return false;
            }

            if (AstroArmorSetsAPI.getBonusCrystalsEquipped((Player) damager).containsKey(ArmorSetType.ULTRA) && Math.random() < 0.10) {
                damager.sendMessage(CC.translate("&b&l* &7&lULTRA PHOENIX NEGATION &7Blocked &cPhoenix &b&l[&c" + player.getName() + "&b&l] &b&l*"));
                player.sendMessage(CC.translate("&b&l* &7&lPHOENIX BLOCKED [&c" + damager.getName() + " Ultra Set&b&l] *"));
                Phoenix.last_phoenix.put(player.getUniqueId().toString(), System.currentTimeMillis());
                return false;
            }

            if(player.hasMetadata("champion_ability")) {
                damager.sendMessage(CC.translate("&6&l* &6&lCHAMPION PHOENIX NEGATION &7Blocked &cPhoenix &6&l[&c" + player.getName() + "&6&l] &6&l*"));
                player.sendMessage(CC.translate("&c&l* &c&lPHOENIX BLOCKED &6&l[&c" + damager.getName() + " Champion Wrath&6&l] *"));
                Phoenix.last_phoenix.put(player.getUniqueId().toString(), System.currentTimeMillis());
                return false;
            }


            int soulCost = getPhoenixProcSoulCost(player);
            if (KillTrackerAPI.hasSoulAmount(player, soulCost)) {
                KillTrackerAPI.removeSoulsFromGems(player, soulCost);
                Phoenix.last_phoenix.put(player.getUniqueId().toString(), System.currentTimeMillis());
                player.setMetadata("phoenixProcs", new FixedMetadataValue(Enchantments.getInstance(), player.hasMetadata("phoenixProcs") ? (player.getMetadata("phoenixProcs").get(0).asInt() + 1) : 1));
                GeneralUtil.setPlayerHealth(player, player.getMaxHealth());
                player.sendMessage("");
                player.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + "*** " + ChatColor.UNDERLINE + "PHOENIX SOUL" + ChatColor.GOLD + ChatColor.BOLD + " ***");
                player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "- " + soulCost + " Soul Gems");
                player.sendMessage(ChatColor.GRAY + "You have " + ChatColor.UNDERLINE + KillTrackerAPI.getAllSouls(player) + ChatColor.GRAY + " souls left.");
                player.sendMessage("");
                player.sendMessage(CC.translate("&7You are now on &nPhoenix Cooldown&7 for 2 minutes."));
                player.sendMessage(CC.translate("&7&oUse /phoenixcooldown&7 to check your cooldown timer."));
                player.sendMessage("");
                Bukkit.getScheduler().runTaskLater(Enchantments.getInstance(), () -> {
                    player.sendMessage(CC.translate(""));
                    player.sendMessage(CC.translate("&a&l*** YOU ARE NO LONGER ON &a&l&nPHOENIX COOLDOWN&a&l ***"));
                    player.sendMessage(CC.translate(""));
                }, 2400L);


                if (AstroItemSkinsAPI.hasSkinEquipped(player, SkinType.CORRUPT_BELT_UTILITY)) {
                    try {
                        MasteryFeignDeath.vanishPlayer(player, 3);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0f, 1.25f);
                for ( Entity ent : player.getNearbyEntities(48.0, 48.0, 48.0)) {
                    if (ent instanceof Player) {
                        ent.sendMessage(CC.translate(""));
                        ent.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "*** PHOENIX SOUL (" + ChatColor.GRAY + player.getName() + ", -" + soulCost + " souls" + ChatColor.RED + ChatColor.BOLD + ") ***");
                        ent.sendMessage(CC.translate("&7They are now on &nPhoenix Cooldown&7 for 2 minutes."));
                        ent.sendMessage(CC.translate(""));
                        ((Player) ent).playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0f, 1.25f);
                    }
                }
                DamageIndicatorUtils.sendDamageIndicator(player, CC.translate("&e&l* PHOENIX *"), "PHOENIX");
                Title title = new Title(CC.translate("&c&l** PHOENIX**"), CC.translate("&7You have &n" + KillTrackerAPI.getAllSouls(player) + "&7 souls left."), 3, 30, 3);
                player.sendTitle(title);
                return true;
            }
            if (!player.hasMetadata("outOfSoulsMessage") || player.getMetadata("outOfSoulsMessage").get(0).asLong() < System.currentTimeMillis()) {
                player.playSound(player.getLocation(), Sound.ITEM_BREAK, 0.7f, 0.4f);
                player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "** OUT OF SOULS **");
                player.setMetadata("outOfSoulsMessage", new FixedMetadataValue(Enchantments.getInstance(), System.currentTimeMillis() + 15000L));
            }
        }
        return false;
    }

    public static boolean canUsePhoenix( Player pl) {
        return !Phoenix.last_phoenix.containsKey(pl.getUniqueId().toString()) || System.currentTimeMillis() - Phoenix.last_phoenix.get(pl.getUniqueId().toString()) > 120000L;
    }

    public static int getPhoenixProcSoulCost( Player player) {
        int soulCost = 500;
        int procsSinceOutOfCombat = player.hasMetadata("phoenixProcs") ? player.getMetadata("phoenixProcs").get(0).asInt() : 0;
        while (procsSinceOutOfCombat-- > 0) {
            soulCost *= 2;
        }

        soulCost = Math.min(soulCost, 2000);
        return soulCost;
    }

    static {
        Phoenix.last_phoenix = new HashMap<>();
        Phoenix.phoenixEquipped = new HashMap<>();
    }
}
