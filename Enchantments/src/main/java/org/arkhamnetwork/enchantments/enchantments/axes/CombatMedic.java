package org.arkhamnetwork.enchantments.enchantments.axes;

import com.cosmicpvp.cosmicutils.utils.GeneralUtil;
import com.rit.sucy.CustomEnchantment;
import lombok.SneakyThrows;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CombatMedic extends CustomEnchantment {
    public CombatMedic() {
        super("Combat Medic", GeneralUtil.axe, 2);
        this.max = 5;
        this.base = 10.0;
        this.interval = 8.0;
    }

    @SneakyThrows
    public void applyEffect(final LivingEntity user, final LivingEntity target, final int enchantLevel, final EntityDamageByEntityEvent event) {

    }
}
