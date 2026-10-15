package me.ifamasssxd.cosmicskycustoms.struct;

import com.cosmicpvp.cosmicutils.exceptions.CosmicException;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public interface PlayerInteractable {
    public void onPlayerInteractable(Player var1, ItemStack var2, NBTWrapper var3, PlayerInteractEvent var4) throws CosmicException;
}

