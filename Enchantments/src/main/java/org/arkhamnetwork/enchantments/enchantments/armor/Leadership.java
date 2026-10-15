package org.arkhamnetwork.enchantments.enchantments.armor;

import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.ConcurrentHashMap;

public class Leadership extends CustomEnchantment {
    protected static ConcurrentHashMap<String, Integer> Leaderships;
    private LeadershipTask LeadershipTask;

    public Leadership() {
        super("Leadership", GeneralUtil.armor, 2);
        this.max = 5;
        this.base = 20.0;
        this.interval = 5.0;
        Leadership.Leaderships = new ConcurrentHashMap<String, Integer>();
        Bukkit.getPluginManager().registerEvents((Listener) new LeadershipListener(), (Plugin) Enchantments.getInstance());
        (this.LeadershipTask = new LeadershipTask()).runTaskTimer((Plugin) Enchantments.getInstance(), 20L, 120L);
    }

    public void applyEquipEffect(final Player player, final int enchantLevel) {
        //Leadership.Leaderships.put(player.getName(), enchantLevel);
    }

    public void applyUnequipEffect(final Player player, final int enchantLevel) {
        //Leadership.Leaderships.remove(player.getName());
    }
}