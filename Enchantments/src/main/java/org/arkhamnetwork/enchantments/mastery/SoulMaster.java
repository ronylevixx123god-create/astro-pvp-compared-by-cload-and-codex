package org.arkhamnetwork.enchantments.mastery;

import org.arkhamnetwork.enchantments.MasteryCustomEnchantment;
import org.arkhamnetwork.enchantments.util.GeneralUtil;

public class SoulMaster extends MasteryCustomEnchantment {
    public SoulMaster() {
        super("Soul Master", GeneralUtil.weapons, 10);
        this.max = 5;
    }
}
