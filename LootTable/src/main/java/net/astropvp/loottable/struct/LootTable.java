// Decompiled with: CFR 0.151
// Class Version: 8
package net.astropvp.loottable.struct;

import com.cosmicpvp.cosmicutils.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;


@AllArgsConstructor
public enum LootTable {
    SOLAR(Material.TRIPWIRE_HOOK, ChatColor.YELLOW),
    COSMIC(Material.TRIPWIRE_HOOK, ChatColor.LIGHT_PURPLE),
    SEASON(Material.TRIPWIRE_HOOK, ChatColor.GOLD),
    SLOTS(Material.PAPER, ChatColor.YELLOW),
    ENVOY(Material.FIREWORK, ChatColor.AQUA),
    BAH(Material.ENDER_CHEST, ChatColor.GOLD),
    WARZONE_CHESTS(Material.CHEST, ChatColor.AQUA),
    GRINDING(Material.MOB_SPAWNER, ChatColor.YELLOW),
    SPACE(Material.ENDER_STONE, ChatColor.DARK_PURPLE),
    ABANDONED_SPACESHIP_DUNGEON(Material.EYE_OF_ENDER, ChatColor.GOLD),
    DESTROYED_OUTPOST_DUNGEON(Material.EYE_OF_ENDER, ChatColor.RED),
    PLANET_NUL_DUNGEON(Material.EYE_OF_ENDER, ChatColor.DARK_GRAY),
    ;

    private final Material material;
    private final ChatColor color;

    public String getFancy() {

        return StringUtils.getProperCase(this.name()).replace("_", " ");
    }


    public Material getMaterial() {
        return this.material;
    }

    public ChatColor getColor() {
        return this.color;
    }
}
