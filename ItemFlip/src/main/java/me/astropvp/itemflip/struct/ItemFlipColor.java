package me.astropvp.itemflip.struct;

import java.util.Arrays;

import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.ChatColor;

public enum ItemFlipColor
{
    PURPLE("Purple", ChatColor.LIGHT_PURPLE, (short)10),
    BLUE("Blue", ChatColor.AQUA, (short)3),
    RED("Red", ChatColor.RED, (short)14),
    GREEN("Green", ChatColor.GREEN, (short)5),
    BLACK("Black", ChatColor.DARK_GRAY, (short)15),
    YELLOW("Yellow", ChatColor.YELLOW, (short)4),
    ORANGE("Orange", ChatColor.GOLD, (short)1),
    WHITE("White", ChatColor.WHITE, (short)0),
    GRAY("Gray", ChatColor.GRAY, (short)7);

    private String name;
    private ChatColor color;
    private short woolId;

    private ItemFlipColor(final String name, final ChatColor color, final short woolId) {
        this.name = CommonsUtil.color(name);
        this.color = color;
        this.woolId = woolId;
    }

    public String getName() {
        return this.name;
    }

    public ChatColor getColor() {
        return this.color;
    }

    public ItemStack getWoolItem() {
        return new ItemStack(Material.WOOL, 1, this.getWoolId());
    }

    public short getWoolId() {
        return this.woolId;
    }

    public String getColoredName() {
        return CommonsUtil.color(this.getColor() + "&l" + this.getName());
    }

    public static ItemFlipColor getByWoolId(final short woolId) {
        return Arrays.stream(values()).filter(c -> c.getWoolId() == woolId).findFirst().get();
    }
}
