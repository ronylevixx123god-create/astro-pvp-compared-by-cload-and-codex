package org.arkhamnetwork.enchantments.enchantments.axes;

import com.rit.sucy.*;
import org.arkhamnetwork.enchantments.util.*;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.arkhamnetwork.enchantments.*;
import org.bukkit.plugin.*;
import org.bukkit.metadata.*;
import com.cosmicpvp.cosmicutils.utils.*;
import org.bukkit.*;

public class Overwhelm extends CustomEnchantment
{
    public Overwhelm() {
        super("Overwhelm", GeneralUtil.axe, 7);
        this.max = 4;
        this.base = 20.0;
        this.interval = 5.0;
    }

    public void applyEffect(final LivingEntity player, final LivingEntity playerHit, final int level, final EntityDamageByEntityEvent e) {
        if (playerHit instanceof Player && Math.random() < 0.1) {
            disableReflectiveBlock((Player)playerHit, (Player)player, 6);
        }
    }

    public static void disableReflectiveBlock(final Player to, final Player from, final int seconds) {
        if (to.hasMetadata("noReflectiveBlock")) {
            return;
        }
        to.setMetadata("noReflectiveBlock", new FixedMetadataValue((Plugin)Enchantments.getInstance(), from.getName()));
        to.sendMessage(CC.translate("&c&l* DISABLED REFLECTIVE BLOCK [" + seconds + "s] &7[by " + from.getName() + "] &c&l*"));
        Bukkit.getScheduler().runTaskLater((Plugin)Enchantments.getInstance(), () -> {
            to.removeMetadata("noReflectiveBlock", (Plugin)Enchantments.getInstance());
        }, 20L * seconds);
    }
}
