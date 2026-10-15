package me.ifamasssxd.cosmicskycustoms.struct;

import com.cosmicpvp.cosmicutils.exceptions.CosmicException;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public interface PlaceableBlockItem {
    public void onPlayerPlace(Player var1, ItemStack var2, NBTWrapper var3, BlockPlaceEvent var4) throws CosmicException;
}

