package org.arkhamnetwork.enchantments.enchantments.armor;

import com.rit.sucy.*;
import com.earth2me.essentials.*;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.*;
import org.bukkit.*;
import org.arkhamnetwork.enchantments.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;

public class Immortal extends CustomEnchantment
{
    protected static boolean factionUpgrades;
    Essentials ess;

    public Immortal() {
        super("Immortal", GeneralUtil.armor, 2);
        this.max = 4;
        this.base = 15.0;
        this.interval = 3.0;
        Immortal.factionUpgrades = Bukkit.getPluginManager().isPluginEnabled("FactionUpgrades");
        this.ess = (Essentials)Bukkit.getPluginManager().getPlugin("Essentials");
        Bukkit.getPluginManager().registerEvents((Listener)new ImmortalListener(), (Plugin) Enchantments.getInstance());
    }
}
