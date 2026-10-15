package net.astropvp.core.utils;

import com.cosmicpvp.cosmicutils.exceptions.CosmicException;
import com.cosmicpvp.cosmicutils.exceptions.PrefixedException;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryInteractable;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryItemType;
import net.minecraft.server.v1_8_R3.NBTBase;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CleanseScroll extends CosmicItem
        implements InventoryInteractable {
    public CleanseScroll() {
        super("cleanse_scroll");
    }

    public ItemStack createItem(Player player, Object ... objects) {
        return this.applyItemData(ItemUtils.addGlow(new ItemBuilder(Material.PAPER, CC.RedB + "Cleanse Scroll"
                , CC.Gray + "Apply this scroll to any item"
                , CC.Gray + "that is corrupted or semi-corrupted,"
                , CC.Gray + "and it will cleanse the complete item.").build())).build();
    }

    public boolean acceptsInventoryClickType(InventoryItemType inventoryItemType) {
        return inventoryItemType == InventoryItemType.CURSOR;
    }

    public void onInventoryClick(Player player, ItemStack cursor, NBTWrapper wrapper, NBTWrapper clickedWrapper, ItemStack clicked, InventoryClickEvent e) throws CosmicException {
        if (clicked == null || clicked.getType() == Material.AIR || clickedWrapper == null) {
            return;
        }
        String type = clicked.getType().toString().toLowerCase();
        if ((!type.contains("sword")) && !type.contains("chestplate") && !type.contains("leggings") &&
                !type.contains("boots") && !type.contains("helmet") && !type.contains("pickaxe") && !type.contains("sword") &&
                !type.contains("bow") && !type.contains("axe")) {
            throw new PrefixedException("You cannot cleanse scroll this item!");
        }
        if (cursor.getAmount() > 1) {
            throw new PrefixedException("You cannot apply stacked Cleanse Scrolls to an item.");
        }
        NBTTagCompoundBuilder cosmicData = clickedWrapper.getCompoundOrCreate("conaxData");

        cosmicData.setInt("cleanseScroll", Integer.valueOf(1));
        e.setCancelled(true);
        e.getWhoClicked().setItemOnCursor((ItemStack)null);
        clickedWrapper.setNBTBase("conaxData", (NBTBase)cosmicData.get());
        clicked = clickedWrapper.build();
        HolyWhiteScrollUtils.setHolyWhiteScrollsApplied(clickedWrapper, 0);
        HolyWhiteScrollUtils.applyHolyWhitescroll(clicked, 1, HolyWhiteScrollUtils.getMaxHolyAllowed(clicked), false);
        HolyWhiteScrollUtils.removeCorrupt(clicked, clickedWrapper);
        e.setCurrentItem(clicked);
        player.sendMessage(CC.translate("&a&l(!)&a Successfully applied Cleanse Scroll to this item!"));
    }

    public void onItemUse(Player player, ItemStack item, NBTWrapper wrapper, PlayerInteractEvent event) throws CosmicException {
        event.setCancelled(true);
    }
}

