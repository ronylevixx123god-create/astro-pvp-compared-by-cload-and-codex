package org.arkhamnetwork.enchantments.mastery;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.massivecraft.factions.perms.Relation;
import com.rit.sucy.EnchantmentAPI;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.MasteryCustomEnchantment;
import org.arkhamnetwork.enchantments.util.DamageIndicatorUtils;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class MasteryBlackout extends MasteryCustomEnchantment
{
    public MasteryBlackout() {
        super("Blackout", GeneralUtil.swords, 2);
        this.max = 4;
        this.base = 25.0;
        this.interval = 10.0;
    }

    public void applyEffect(final LivingEntity player, final LivingEntity target, final int level, final EntityDamageByEntityEvent e) {
        if (!(target instanceof Player)) {
            return;
        }
        if (e.isCancelled()) {
            return;
        }

        final Player pTarget = (Player)target;
        final Relation relation = FactionUtils.getRelationTo(FactionUtils.getFaction((Player)player), FactionUtils.getFaction(pTarget));
        if (relation != Relation.NEUTRAL && relation != Relation.ENEMY) {
            return;
        }
        if (Math.random() <= 0.06 * (double)level / 4.0) {
            procBlackout((Player) player, pTarget, level);
        }
    }

    private void procBlackout(final Player player, final Player pTarget, final int level) {
        if (pTarget.hasMetadata("blackout")) {
            return;
        }

        if (pTarget.hasMetadata("neutralized")) {
            return;
        }
        if (AstroItemSkinsAPI.hasSkinEquipped(pTarget, SkinType.VIRUS_BELT) && Math.random() < 0.5) {
            pTarget.sendMessage("§2§l* BLOCKED " + player.getName() + "'s BLACKOUT §7(First Aid Fannypack) §2§l*");
            player.sendMessage(CC.translate("&7&l* &7&l&mBLACKOUT&r &7[Blocked by " + pTarget.getName() + "'s First Aid Fannypack] &7&l*"));
            return;
        }

        if (AstroItemSkinsAPI.hasSkinEquipped(pTarget, SkinType.VIRUS_BELT) && Math.random() < 0.5) {
            player.sendMessage(CC.translate("&4&l* BLACKOUT BLOCKED * &7[&7" + pTarget.getName() + "'s &a&lFirst Aid Fannypack&7]"));
            return;
        }
        if (AstroItemSkinsAPI.hasSkinEquipped(pTarget, SkinType.CHRISTMAS_BELT_CRYSTALINE) && Math.random() < 0.2) {
            player.sendMessage(CC.translate("&4&l* BLACKOUT BLOCKED * &7[" + pTarget.getName() + "'s &c&lCrystalline Band&7]"));
            return;
        }
        if (player.hasMetadata("BlackoutCooldown") && System.currentTimeMillis() - player.getMetadata("BlackoutCooldown").get(0).asLong() <= 30000L) {
            return;
        }
        player.setMetadata("BlackoutCooldown", new FixedMetadataValue(Enchantments.getInstance(), System.currentTimeMillis()));
        long durationInTicks = level * 20L;
        if (AstroItemSkinsAPI.hasSkinEquipped(pTarget, SkinType.CHRISTMAS_HELMET_STAR)) {
            durationInTicks *= 0.7;
        }
        if (AstroItemSkinsAPI.hasSkinEquipped(pTarget, SkinType.BACK_TO_SCHOOL_2021_HELMET)) {
            durationInTicks *= 0.4;

        }
        pTarget.playSound(pTarget.getLocation(), Sound.WITHER_HURT, 1.0f, 0.25f);
        DamageIndicatorUtils.sendDamageIndicator(pTarget, "&5&l* BLACKOUT *", "blackout");
        player.sendMessage(CC.translate("&5&l* BLACKOUT &5[" + Math.round((double) (durationInTicks / 20L)) + "&5s on &e" + pTarget.getName() + "&5] &5&l*"));
        pTarget.sendMessage(CC.translate("&5&L* BLACKOUT &7[" + Math.round((double) (durationInTicks / 20L)) + "&7s] &5&l *"));
        pTarget.setMetadata("blackout", new FixedMetadataValue(Enchantments.getInstance(), "banana"));
        Enchantments.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(Enchantments.getInstance(), () -> pTarget.removeMetadata("blackout", Enchantments.getInstance()), AstroItemSkinsAPI.hasSkinEquipped(player, SkinType.CORRUPT_SWORD) ? (durationInTicks * 2L) : Math.round((double) (durationInTicks)));    }

    private int getLevel(final ItemStack piece, final String name) {
        return EnchantmentAPI.getEnchantments(piece).get(EnchantmentAPI.getEnchantments(piece).keySet().stream().filter(e -> e.name().equals(name)).findFirst().get());
    }
}
