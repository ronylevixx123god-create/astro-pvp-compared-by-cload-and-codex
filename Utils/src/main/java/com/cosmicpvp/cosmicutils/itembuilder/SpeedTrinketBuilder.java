package com.cosmicpvp.cosmicutils.itembuilder;

import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import org.arkhamnetwork.Arkkit.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SpeedTrinketBuilder
        extends ItemBuilder {
    public SpeedTrinketBuilder() {
        super(Material.NETHER_STAR);
    }

    @Override
    public ItemStack build() {
        ItemStack i = ItemUtils.buildItem((Material) this.getType(), (short) 0, (String) ChatColor.translateAlternateColorCodes('&', "&3&lSpeed Trinket &7(Right Click)"), (String[]) new String[]{(Object) ((Object) ChatColor.GRAY) + "A mythic soul trinket that imbues", (Object) ((Object) ChatColor.GRAY) + "the owner with extreme speed.", "", ChatColor.translateAlternateColorCodes('&', "&3&nEffect:&b SPEED IV &7(7s)"), ChatColor.translateAlternateColorCodes('&', "&3&nSoul Cost:&b 100/use"), ChatColor.translateAlternateColorCodes('&', "&3&nCooldown:&b 90s")});
        NBTWrapper wrapper = new NBTWrapper(i);
        wrapper.setString("cosmicType", "speedTrinket");
        i = wrapper.build();
        return i;
    }
}

