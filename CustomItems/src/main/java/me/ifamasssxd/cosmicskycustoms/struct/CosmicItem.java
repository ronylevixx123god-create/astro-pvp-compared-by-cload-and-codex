package me.ifamasssxd.cosmicskycustoms.struct;

import com.cosmicpvp.cosmicutils.exceptions.CosmicException;
import com.cosmicpvp.cosmicutils.itembuilder.CosmicItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public abstract class CosmicItem {
    private String internalName;
    private boolean acceptCancelled = false;
    protected String itemKey = "cosmicType";

    public CosmicItem(String internal) {
        this.internalName = internal;
    }

    public abstract ItemStack createItem(Player var1, Object ... var2);

    public void onItemUse(Player player, ItemStack item, NBTWrapper wrapper, PlayerInteractEvent event) throws CosmicException {
    }

    protected NBTWrapper createNBTWithData(Material mat, String name, List<String> lore) {
        return this.createNBTWithData(mat, name, lore.toArray(new String[lore.size()]));
    }

    public boolean isItem(ItemStack item) {
        return this.isItem(new NBTWrapper(item));
    }

    public boolean isItem(NBTWrapper wrapper) {
        return wrapper.getString(this.itemKey).equalsIgnoreCase(this.internalName);
    }

    protected NBTWrapper createNBTWithData(Material mat, String name, String ... lore) {
        ItemStack retr = new CosmicItemBuilder(mat).name(name).lore(lore).build();
        return this.applyItemData(retr);
    }

    protected ItemStack createWithData(Material mat, String name, String ... lore) {
        return this.createNBTWithData(mat, name, lore).build();
    }

    protected ItemStack createWithData(Material mat, String name, List<String> lore) {
        return this.createWithData(mat, name, lore.toArray(new String[lore.size()]));
    }

    protected ItemStack createWithData(MaterialData data, String name, String ... lore) {
        ItemStack retr = new CosmicItemBuilder(data.getItemType()).data((short)data.getData()).name(name).lore(lore).build();
        return this.applyItemData(retr).build();
    }

    protected NBTWrapper applyItemData(ItemStack item) {
        return new NBTWrapper(item).setString(this.itemKey, this.internalName);
    }

    public String getInternalName() {
        return this.internalName;
    }

    public void setAcceptCancelled(boolean acceptCancelled) {
        this.acceptCancelled = acceptCancelled;
    }

    public boolean isAcceptCancelled() {
        return this.acceptCancelled;
    }
}

