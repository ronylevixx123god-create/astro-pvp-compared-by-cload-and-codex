package org.arkhamnetwork.enchantments.listeners;

import com.rit.sucy.CustomEnchantment;
import org.bukkit.event.Listener;

public class CustomEnchantListener<T extends CustomEnchantment> implements Listener {
    protected T customEnchant;

    public CustomEnchantListener(final T enchant) {
        this.customEnchant = enchant;
    }
}
