package org.arkhamnetwork.enchantments.enchantments.swords;

import com.astropvp.cc.CCVerify;
import com.astropvp.cc.player.CosmicPlayer;
import com.astropvp.cc.player.CosmicPlayers;
import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Shiva extends CustomEnchantment {
    public Shiva() {
        super("Shiva", GeneralUtil.swords, 5);
        this.max = 5;
        this.base = 15.0;
        this.interval = 6.0;
    }

    public void applyEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageByEntityEvent event) {
        if (user instanceof Player && target instanceof Player) {
            CosmicPlayer cosmicPlayer = CosmicPlayers.get().getCosmicPlayer((Player) user);
            CosmicPlayer cosmicTarget = CosmicPlayers.get().getCosmicPlayer((Player) target);

            if (cosmicPlayer.isOnCosmicClient() && !cosmicTarget.isOnCosmicClient()) {
                event.setDamage(event.getDamage()*1.25);
            }
        }
    }
}

