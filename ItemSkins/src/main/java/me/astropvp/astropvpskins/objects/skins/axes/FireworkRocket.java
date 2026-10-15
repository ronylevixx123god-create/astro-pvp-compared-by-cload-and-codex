package me.astropvp.astropvpskins.objects.skins.axes;

import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.ifamasssxd.factionupgrades.struct.upgrades.WarzoneControl;
import org.arkhamnetwork.enchantments.enchantments.heroic.HeroicTrap;
import org.arkhamnetwork.enchantments.enchantments.swords.DoubleStrike;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FireworkRocket extends ItemSkin {

    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (!WorldGuardUtils.isPvPEnabled(damager.getLocation())) return;
        if (!(damager instanceof Player)) return;
        if (!(damaged instanceof Player)) return;
        if (damager.hasMetadata("frozenSkrrt")) return;


        if (Math.random() < 0.02) {
            new HeroicTrap().applyEffect(damager, (LivingEntity) damaged, 3, event);

        }
    }
}
