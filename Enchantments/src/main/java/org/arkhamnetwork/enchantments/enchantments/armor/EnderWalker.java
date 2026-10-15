package org.arkhamnetwork.enchantments.enchantments.armor;

import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class EnderWalker extends CustomEnchantment {
    public EnderWalker() {
        super("Ender Walker", GeneralUtil.boots, 2);
        this.max = 5;
        this.base = 20.0;
        this.interval = 10.0;

    }



    public void applyEquipEffect(final Player player, final int level) {
        player.playSound(player.getLocation(), Sound.WITHER_HURT, 1.0f, 0.6f);
        EnderWalkerListener.enderWalkerPlayers.add(player.getUniqueId());
    }

    public void applyUnequipEffect(final Player player, final int level) {
        EnderWalkerListener.enderWalkerPlayers.remove(player.getUniqueId());
    }

}