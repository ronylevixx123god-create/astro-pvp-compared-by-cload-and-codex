package me.ifamasssxd.cosmicsky.inventorypets.items;

import com.cosmicpvp.cosmicutils.compat.ASound;
import com.cosmicpvp.cosmicutils.exceptions.CosmicException;
import com.cosmicpvp.cosmicutils.exceptions.PrefixedException;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.google.common.collect.Lists;
import java.util.List;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPetItem;
import me.ifamasssxd.cosmicskycustoms.exceptions.ItemDupedException;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicPersistentItem;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryInteractable;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryItemType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PetLeashItem extends CosmicItem implements InventoryInteractable {
    public PetLeashItem() {
        super("pet_leash");
    }

    public ItemStack createItem(Player player, Object... objects) {
        String displayName = String.valueOf(CC.GoldB) + "Pet Leash";
        List<String> lore = Lists.newArrayList(new String[] { String.valueOf(CC.Gray) + "Apply to an Inventory Pet", CC.Gray + "to keep it upon death!" });
        return applyItemData((new ItemBuilder(Material.LEASH, displayName, new String[0])).lore(lore).build()).assignIID().build();
    }

    public boolean acceptsInventoryClickType(InventoryItemType inventoryItemType) {
        return (inventoryItemType == InventoryItemType.CURSOR);
    }

    public void onInventoryClick(Player player, ItemStack itemStack, NBTWrapper nbtWrapper, NBTWrapper clickedWrapper, ItemStack clicked, InventoryClickEvent inventoryClickEvent) throws CosmicException {
        InventoryPetItem petItem = (InventoryPetItem)CosmicPersistentItem.constructItem(clicked, clickedWrapper, InventoryPetItem.class);
        if (petItem != null && petItem.getPetType() != null) {
            if (petItem.isLeashed())
                throw new PrefixedException("This pet is already leashed.");
            if (nbtWrapper.isBlacklistedIID())
                throw new ItemDupedException("Pet leash already used!");
            inventoryClickEvent.setCancelled(true);
            if (itemStack.getAmount() > 1) {
                itemStack.setAmount(itemStack.getAmount() - 1);
            } else {
                player.setItemOnCursor((ItemStack)null);
            }
            nbtWrapper.blacklistIID("pet leash used on " + petItem + ": " + player.getName());
            petItem.setLeashed(true);
            inventoryClickEvent.setCurrentItem(petItem.build());
            ASound.LEVEL_UP.play(player, 3.0F, 1.1F);
            player.updateInventory();
        }
    }
}
