/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.nms.MinecraftServerUtils
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.NBTWrapper
 *  com.rit.sucy.CustomEnchantment
 *  com.rit.sucy.EnchantmentAPI
 *  me.ifamasssxd.killtracker.KillTrackerAPI
 *  org.bukkit.Material
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.MetadataValue
 */
package org.arkhamnetwork.enchantments.enchantments.axes;


import com.cosmicpvp.cosmicutils.nms.MinecraftServerUtils;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.skins.amulets.CannonballAmulet;
import me.astropvp.astropvpskins.objects.skins.axes.BoostedChainsaw;
import net.astropvp.armorsets.struct.sets.LunaArmorSet;
import net.astropvp.core.killtracker.KillTrackerAPI;
import org.arkhamnetwork.enchantments.cmds.CommandEnchDust;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class HeroKiller
        extends CustomEnchantment {
    public HeroKiller() {
        super("Hero Killer", GeneralUtil.axe, 2);
        this.max = 3;
        this.base = 10.0;
        this.interval = 10.0;
    }

    public void applyEffect(LivingEntity player, LivingEntity playerHit, int level, EntityDamageByEntityEvent e) {
        if (!CommandEnchDust.soulEnabled) {
            return;
        }
        if (!(playerHit instanceof Player)) {
            return;
        }
        Player p = (Player) player;
        ItemStack used = p.getItemInHand();
        if (used == null || !EnchantmentAPI.itemHasEnchantment(used, this.enchantName)) {
            return;
        }
        if (p.hasMetadata("witcher_ability"))
            return;
        if (((Player) playerHit).getPlayer().getInventory().getChestplate() != null && (AstroItemSkinsAPI.hasSkinEquipped(((Player) playerHit).getPlayer(), SkinType.THANKSGIVING_2021_BACKPACK) || AstroItemSkinsAPI.hasSkinEquipped(((Player) playerHit).getPlayer(), SkinType.THANKSGIVING_2021_MUTATED_BACKPACK))) {
            return;
        }

        if (p.hasMetadata("noSoulProc"))
            return;

        if (((Player) playerHit).getPlayer().getInventory().getChestplate() != null && (AstroItemSkinsAPI.hasSkinEquipped(((Player) playerHit).getPlayer(), SkinType.CHRISTMAS_2021_AMULET) || AstroItemSkinsAPI.hasSkinEquipped(((Player) playerHit).getPlayer(), SkinType.CHRISTMAS_2021_MUTATED_AMULET))) {
            return;
        }

        if (((Player) playerHit).getPlayer().getInventory().getBoots() != null && (AstroItemSkinsAPI.hasSkinEquipped(((Player) playerHit).getPlayer(), SkinType.BACK_TO_SCHOOL_2021_BOOTS))) {
            return;
        }

        if (CannonballAmulet.affected.contains(playerHit)) {
            return;
        }

        if (AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.CHRISTMAS_2022_AMULET_ACTIVE)) {
            return;
        }


        if (AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.JULY_2021_BACKPACK) && !playerHit.hasMetadata("noCape")) {
            return;
        }


        if (AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.JULY4TH_2022_HELMET)) {
            return;
        }

        if (AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.EASTER_2022_BACKPACK) || AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.EASTER_2022_BACKPACK)) return;
        if (AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.SPRING_BREAK_2022_BACKPACK) || AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.SPRING_BREAK_2022_MUTATED_BACKPACK)) return;

        if (((Player) playerHit).getPlayer().getInventory().getChestplate() != null && AstroItemSkinsAPI.hasSkinEquipped(((Player) playerHit).getPlayer(), SkinType.VALENTINES_2021_BACKPACK)) {
            return;
        }
        if (p.hasMetadata("lastItemHeldChange") && (long) MinecraftServerUtils.getCurrentServerTick() - p.getMetadata("lastItemHeldChange").get(0).asLong() <= 5L) {
            return;
        }
        if (p.hasMetadata("soulTrap") && p.getMetadata("soulTrap").get(0).asLong() > System.currentTimeMillis()) {
            return;
        }

        if (((BoostedChainsaw) SkinType.BACK_TO_SCHOOL_2021_AXE.getItemSkin()).getChainSawedPlayers().contains(p.getName())) {
            return;
        }

        if (AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.VALENTINES_2022_MUTATED_AMULET) || AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.VALENTINES_2022_AMULET))
            return;

        if (KillTrackerAPI.hasGodMode(p) && KillTrackerAPI.getAllSouls(p) > 0) {
            if (!this.isWearingHeroic((Player) playerHit)) {
                return;
            }
            if (LunaArmorSet.isLunaStriked((Player) playerHit)) return;
            if (!p.hasMetadata("lastSoulRemoveEvent") || System.currentTimeMillis() - p.getMetadata("lastSoulRemoveEvent").get(0).asLong() > 1000L) {
                KillTrackerAPI.removeSoulsFromGems(p, 4);
            }
            double increase = 0.1D * (double) level;
/*            if (AstroArmorSetsAPI.getBonusCrystalsEquipped((Player) player).containsKey(ArmorSetType.LUNA)) {
                increase += 0.1;
            }*/

            if (AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.EASTER_2022_SWORD) || AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.EASTER_2022_MUTATED_SWORD)) {
                increase += 0.3;
            }

            if (player.hasMetadata("carver_damage")) {
                increase += 0.3;
            }
            if (AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.THANKSGIVING_SWORD_HARVESTER) && !AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.VALENTINES_2021_HELMET)) {
                increase += 0.3;
            }
            if (AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.JULY_2021_BACKPACK) && player.getHealth() <= player.getMaxHealth() / 2 && !player.hasMetadata("noCape")) {
                increase += 0.15;
            }

            if (AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.CHRISTMAS_2022_AMULET_ACTIVE)) {
                increase+=1.15;
            }



            if (AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.CHRISTMAS_2021_HELMET) || AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.CHRISTMAS_2021_MUTATED_HELMET)) {
                increase -= 0.1;
            }
            e.setDamage(e.getDamage() * (1 + increase));
        }
    }

    private boolean isWearingHeroic(Player player) {
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            Material type;
            if (armor == null || (type = armor.getType()) != Material.LEATHER_BOOTS && type != Material.LEATHER_LEGGINGS && type != Material.LEATHER_CHESTPLATE && type != Material.LEATHER_HELMET || !new NBTWrapper(armor).getString("cosmicType").equals("HEROIC_ARMOR"))
                continue;
            return true;
        }
        return false;
    }
}

