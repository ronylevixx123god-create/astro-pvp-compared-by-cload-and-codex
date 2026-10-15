package me.astropvp.astropvpskins.objects.skins.swords;

import com.cosmicpvp.cosmicutils.itembuilder.YijkiStaffBuilder;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.ifamasssxd.cosmicbosses.dungeonboss.destroyedoutpost.MotherOfYijki;
import org.arkhamnetwork.enchantments.enchantments.swords.Headless;
import org.arkhamnetwork.enchantments.mastery.MasteryLifesteal;
import org.arkhamnetwork.enchantments.mastery.MasterySoulSiphon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BladeOfDarknessSkin extends ItemSkin {
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (damager.hasMetadata("frozenSkrrt")) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation())) return;
        if(Math.random() >= 0.02) {
            //TODO: YIJKI ABILITY
        }
        if(Math.random() >= 0.04 && damaged instanceof Player) {
            new MasteryLifesteal().applyDamagedOtherPlayer(damager, (Player) damaged, event, 5);
        }
        new Headless().applyEffect(damager, (LivingEntity) damaged, 3, event);
        if(damaged instanceof Player)
            new MasterySoulSiphon().applyDamagedOtherPlayer(damager, (Player) damaged, event, 3);
    }
}
