package net.astropvp.armorsets.struct.antiMkit;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import lombok.Getter;
import me.ifamasssxd.cosmicskycustoms.struct.CustomItemBuilder;
import net.astropvp.armorsets.AstroArmorSetsAPI;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

@Getter
public enum AntiMkit {
    GHOST("GHOST", 0, ChatColor.DARK_AQUA, CC.DarkAquaB + "Ghost",
            new MaterialData(Material.INK_SACK, DyeColor.SILVER.getDyeData())),
    NECROMANCER("NECROMANCER", 1, ChatColor.DARK_GREEN,
            CC.DarkGreenB + "Necromancer",
            new MaterialData(Material.INK_SACK, DyeColor.GREEN.getDyeData())),
    DEATH_KNIGHT("DEATH_KNIGHT", 2, ChatColor.BLUE,
            CC.BlueB + "Death Knight", new MaterialData(Material.INK_SACK, DyeColor.CYAN.getDyeData()));


    private final ChatColor color;

    private final String displayName;

    private final MaterialData masteryShardItem;



    @Deprecated
    public ItemStack createAntiMkitShard(int success) {
        return (new CustomItemBuilder("anti_mkit", Integer.valueOf(success), this)).build();
    }

    public ItemStack createMasteryShard(boolean applyIID) {
        ItemStack item = (new NBTWrapper((new ItemBuilder(this.masteryShardItem.getItemType(),
                CC.GoldB + "Mastery Shard " + CC.Gold + "(" + this.displayName + CC.Gold + ")",
                CC.Gray + "Apply to an enchantment book",
                CC.Gray + "to imbue it with the power to",
                CC.Gray + "affect " + this.displayName + CC.Gray + " Mastery Kit items.")).data(this.masteryShardItem.getData()).build())).setString("cosmicType", "MASTERY_SHARD")
                .setNBTBase("cosmicData", (new NBTTagCompoundBuilder())
                        .setString("masteryShard", name()).get())
                .build();

        return item;
    }

    public static AntiMkit getSetFromMasteryShard(ItemStack masteryShard) {
        NBTWrapper cursorWrapper = new NBTWrapper(masteryShard);
        if (cursorWrapper.getString("cosmicType").equals("MASTERY_SHARD")) {
            NBTTagCompoundBuilder builder = cursorWrapper.getCompound("cosmicData");
            if (builder != null && builder.hasKey("masteryShard"))
                return AstroArmorSetsAPI.mkitFromName(builder.getString("masteryShard"));
        }
        return null;
    }

    public NBTWrapper applyAbilityNBT(NBTWrapper wrapper) {
        NBTTagCompoundBuilder builder = wrapper.getCompoundOrCreate("cosmicData");
        builder.setString("abilitySet", name());
        builder.setShort("noEnchants", (short) 1);
        wrapper.setNBTBase("cosmicData", builder.get());
        return wrapper;
    }

    public String getFirstDisplayNameLetter() {
        return this.color + CC.Bold + name().charAt(0);
    }

    AntiMkit(String name, int ordinal, ChatColor color, String displayName, MaterialData masteryShardItem) {
        this.color = color;
        this.displayName = displayName;
        this.masteryShardItem = masteryShardItem;
    }
}
