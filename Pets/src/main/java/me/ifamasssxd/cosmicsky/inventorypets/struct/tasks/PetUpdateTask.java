package me.ifamasssxd.cosmicsky.inventorypets.struct.tasks;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPetItem;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicPersistentItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class PetUpdateTask
        extends BukkitRunnable
{
    public void run() {
        for (final Player p : Bukkit.getOnlinePlayers()) {
            final Inventory topInventory = p.getOpenInventory().getTopInventory();
            final Inventory bottomInventory = p.getOpenInventory().getBottomInventory();
            if (topInventory.getContents().length != 0) {
                if (CC.strip(topInventory.getTitle()).contains("Death Inventory")) continue;
                for (int i = 0; i < topInventory.getSize(); ++i) {
                    final ItemStack item = topInventory.getItem(i);
                    if (item != null && item.getType() == Material.SKULL_ITEM) {
                        final NBTWrapper wrapper = new NBTWrapper(item);
                        if (wrapper.hasKey("cosmicType") && wrapper.getString("cosmicType").equals("inventory_pet")) {
                            if (wrapper.hasKey("SkullOwner")) {
                                final InventoryPetItem petItem = (InventoryPetItem) CosmicPersistentItem.constructItem(item, wrapper, (Class) InventoryPetItem.class);
                                if (petItem != null && petItem.getPetType() != null) {
                                    final NBTWrapper newWrapper = new NBTWrapper(petItem.build());
                                    final String newBlob = newWrapper.getCompound("SkullOwner").get().getCompound("Properties").getList("textures", 10).get(0).getString("Value");
                                    final String oldBlob = wrapper.getCompound("SkullOwner").get().getCompound("Properties").getList("textures", 10).get(0).getString("Value");
                                    if (!newBlob.equals(oldBlob)) {
                                        topInventory.setItem(i, petItem.build());
                                        p.updateInventory();
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (bottomInventory.getContents().length != 0) {
                if (CC.strip(bottomInventory.getTitle()).contains("Death Inventory")) continue;

                for (int i = 0; i < bottomInventory.getSize(); ++i) {
                    final ItemStack item = bottomInventory.getItem(i);
                    if (item != null && item.getType() == Material.SKULL_ITEM) {
                        final NBTWrapper wrapper = new NBTWrapper(item);
                        if (wrapper.hasKey("cosmicType") && wrapper.getString("cosmicType").equals("inventory_pet")) {
                            if (wrapper.hasKey("SkullOwner")) {
                                final InventoryPetItem petItem = (InventoryPetItem) CosmicPersistentItem.constructItem(item, wrapper, (Class) InventoryPetItem.class);
                                if (petItem != null && petItem.getPetType() != null) {
                                    final NBTWrapper newWrapper = new NBTWrapper(petItem.build());
                                    final String newBlob = newWrapper.getCompound("SkullOwner").get().getCompound("Properties").getList("textures", 10).get(0).getString("Value");
                                    final String oldBlob = wrapper.getCompound("SkullOwner").get().getCompound("Properties").getList("textures", 10).get(0).getString("Value");
                                    if (!newBlob.equals(oldBlob)) {
                                        bottomInventory.setItem(i, petItem.build());
                                        p.updateInventory();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
