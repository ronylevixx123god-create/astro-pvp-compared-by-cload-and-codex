package me.ifamasssxd.cosmicskycustoms.struct;

import com.cosmicpvp.cosmicutils.exceptions.CosmicException;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryItemType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public interface InventoryInteractable {
    public boolean acceptsInventoryClickType(InventoryItemType var1);

    public void onInventoryClick(Player var1, ItemStack var2, NBTWrapper var3, NBTWrapper var4, ItemStack var5, InventoryClickEvent var6) throws CosmicException;
}

