package org.arkhamnetwork.enchantments.enchantments.heroic;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldUtil;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import org.arkhamnetwork.enchantments.util.DamageIndicatorUtils;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class HeroicExecute extends HeroicCustomEnchantment{
    public HeroicExecute() {
        super("Permanent Execute","Execute", GeneralUtil.swords, 2);
        this.max = 7;
        this.base = 15.0;
        this.interval = 3.0;
    }

    public void applyEffect(LivingEntity player, LivingEntity target, int level, EntityDamageByEntityEvent e) {
        if (!(target instanceof Player)) {
            return;
        }

        double healThres = 12.0;
        if (((Player)player).getInventory().getBoots() != null && AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.HALLOWEEN_2021_BOOTS))
            healThres*=1.3;

        if (target.getHealth() > healThres) {
            return;
        }
        if (GeneralUtil.isEffectedByRage(target))
            return;
        double random = Math.random();
        double chance = 0.08D * level;
        double mod = 4.0D + (level/2D);

        if (random < chance) {
            e.setDamage(e.getDamage() * mod);
            final Location l = target.getLocation();
            WorldUtil.sendWorldEffect(l.getBlock(), Material.REDSTONE_WIRE.getId(), 2001);
            DamageIndicatorUtils.sendDamageIndicator((Player) e.getEntity(), CC.RedB + "* PERMANENT EXECUTE *", "EXECUTE");

        }
    }
}
