package net.astropvp.core.items;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.rit.sucy.EnchantmentAPI;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryInteractable;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryItemType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class GodlyTransmogScrollItem extends CosmicItem implements InventoryInteractable {
    public GodlyTransmogScrollItem() {
        super("godly_transmog_scroll");
    }

    public ItemStack createItem(Player player, Object... objects) {
        return applyItemData((new ItemBuilder(Material.PAPER, "&6&lGodly Transmog Scroll", "&7Apply to equipment to &6&ncustomize", "&7the order of &5custom &denchantments", "&7displayed on the item.", "", "&6&oPlace scroll on item to apply.")).build()).build();
    }

    public boolean acceptsInventoryClickType(InventoryItemType inventoryItemType) {
        return (inventoryItemType == InventoryItemType.CURSOR);
    }

    public void onInventoryClick(Player player, ItemStack cursor, NBTWrapper wrapper, NBTWrapper clickedWrapper, ItemStack clicked, InventoryClickEvent e) {
        if (clicked == null || clicked.getType() == Material.AIR || clickedWrapper == null)
            return;
        if (cursor.getAmount() > 1) {
            player.sendMessage(CC.translate("&c&l(!) &cYou cannot apply stacked Godly Transmog Scrolls."));
            return;
        }
        if (e.getClickedInventory().getType() != InventoryType.PLAYER)
            return;
        if (e.getSlotType() == InventoryType.SlotType.ARMOR)
            return;
        if (EnchantmentAPI.getEnchantments(clicked).size() > 0) {
            e.setCancelled(true);
            player.setItemOnCursor(null);
            player.getInventory().removeItem(clicked);
        }
    }
}
