package net.astropvp.adminitems.struct;

import com.cosmicpvp.cosmicutils.utils.GlintUtils;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import net.astropvp.adminitems.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Locale;

public abstract class AdminItem implements Listener {

    public abstract String getName();

    public abstract String getDisplayName();

    public abstract List<String> getPerks();

    public abstract List<String> getLore();

    public abstract ChatColor getColor();

    public abstract Material getMaterial();

    public abstract String getCooldownID();


    public String getID() {
        return this.getName().toLowerCase(Locale.ROOT).replace(" ", "");
    }

    public ItemStack getItem(int uses) {
        ItemStack item  = new ItemBuilder(getMaterial()).setName(this.getDisplayName())
                .addLore(this.getLore())
                .addLore("")
                .addLore(this.getColor() + "&l" + "Ability")
                .addLore(this.getPerks())
                .addLore("")
                .addLore(this.getColor() + "&l" + "Uses")
                .addLore("&f" +(uses == -1? "Unlimited": Integer.toString(uses)))
                .colorize()
                //.setGlowing()
                .build();

        NBTItem wrapper = new NBTItem(item);
        wrapper.setString("cosmicType", "adminItem");
        NBTCompound cosmicData = null;
        if (wrapper.getCompound("cosmicData") == null) {
            wrapper.addCompound("cosmicData");
        }
        cosmicData = wrapper.getCompound("cosmicData");
        cosmicData.setString("adminItemType", this.getID());
        cosmicData.setInteger("adminItemUses", uses);
        item = wrapper.getItem();
        GlintUtils.addGlint(item, Color.PURPLE);
        return item;
    }
}
