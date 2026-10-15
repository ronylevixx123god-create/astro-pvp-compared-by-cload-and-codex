package com.cosmicpvp.cosmicwildpvp.settings;

import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import org.bukkit.Material;
import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.inventory.ItemStack;

public class CagesSetting extends MatchSetting
{
    public CagesSetting() {
        super("cages");
    }

    @Override
    public ItemStack getMatchItem(final boolean enabled) {
        final String status = enabled ? CC.translate("&a&lENABLED") : CC.translate("&c&lDISABLED");
        return new ItemBuilder(Material.IRON_FENCE, CC.translate("&e&lCAGE MATCH"), new String[] { status, "", CC.translate("&7If this setting is enabled all players"), CC.translate("&7will be sent into an inescapable cage."), "", CC.translate("&7The cage will despawn &n60 seconds&7 after"), CC.translate("&7the pvp match begins."), "", CC.translate("&7Click here to &ntoggle&7 this setting") }).build();
    }
}
