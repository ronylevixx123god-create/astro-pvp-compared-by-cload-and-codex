package org.arkhamnetwork.enchantments.enchantments.armor;

import com.rit.sucy.*;

import java.util.concurrent.*;

import org.arkhamnetwork.enchantments.enchantments.swords.Rage;
import org.arkhamnetwork.enchantments.util.*;
import org.arkhamnetwork.enchantments.*;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.event.*;
import org.bukkit.plugin.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;
import com.cosmicpvp.cosmicutils.utils.*;
import org.bukkit.*;

public class Cactus extends CustomEnchantment {
    protected static ConcurrentHashMap<String, Integer> cactus;

    public Cactus() {
        super("Cactus", GeneralUtil.armor, 2);
        this.max = 2;
        this.base = 20.0;
        this.interval = 5.0;
        Cactus.cactus = new ConcurrentHashMap<String, Integer>();
        Bukkit.getPluginManager().registerEvents(new CactusListener(), (Plugin) Enchantments.getInstance());
    }

    public void applyEquipEffect(final Player player, final int enchantLevel) {
        Cactus.cactus.put(player.getName(), enchantLevel);
    }

    public void applyUnequipEffect(final Player player, final int enchantLevel) {
        Cactus.cactus.remove(player.getName());
    }

    public void applyDefenseEffect(final LivingEntity player, final LivingEntity attacker, final int level, final EntityDamageEvent e) {
        if (player instanceof Player) {
            if (player.hasMetadata("noDefenseProcs")) {
                return;
            }

            final Player p = (Player) player;
            if (attacker instanceof Player) {
                final Player pAttacker = (Player) attacker;
                //pAttacker.damage(0.5 * level);
                RageStacksCache rageStacksCache = CacheUtils.getRageStackCache((Player) pAttacker);
                rageStacksCache.resetRageStack();
                final Location l = p.getLocation();
                WorldUtil.sendWorldEffect(l.getBlock(), 81, 2001);
            }

        }
    }
}
