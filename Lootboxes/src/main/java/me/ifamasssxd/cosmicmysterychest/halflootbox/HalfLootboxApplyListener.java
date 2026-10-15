package me.ifamasssxd.cosmicmysterychest.halflootbox;

import com.cosmicpvp.cosmicutils.exceptions.PrefixedException;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest;
import me.ifamasssxd.cosmicmysterychest.struct.MysteryChest;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryItemType;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class HalfLootboxApplyListener implements Listener {


    public boolean acceptsInventoryClickType(InventoryItemType inventoryItemType) {
        return inventoryItemType == InventoryItemType.CURSOR;
    }

    @EventHandler
    public void onApply(InventoryClickEvent e) throws PrefixedException {

        ItemStack clicked = e.getCurrentItem();
        ItemStack cursor = e.getCursor();


        Player player = (Player) e.getWhoClicked();
        if (e.getClickedInventory() != e.getView().getBottomInventory()) {
            return;
        }

        if (clicked == null || clicked.getType() == Material.AIR || cursor == null || cursor.getType() == Material.AIR) {
            return;
        }
        NBTWrapper clickedWrapper = new NBTWrapper(clicked);
        NBTWrapper cursorWrapper = new NBTWrapper(cursor);

        if (cursorWrapper == null || clickedWrapper == null) {

            return;
        }


        if (HalfLootbox.isHalfLootboxItem(cursor) && HalfLootbox.isHalfLootboxItem(clicked)) {


            HalfLootboxType halfLootboxTypeCursor = HalfLootbox.getHalfLootboxType(cursor);
            HalfLootboxType halfLootboxTypeClicked = HalfLootbox.getHalfLootboxType(clicked);


            if (halfLootboxTypeClicked == HalfLootboxType.LEFT && halfLootboxTypeCursor == HalfLootboxType.RIGHT || halfLootboxTypeClicked == HalfLootboxType.RIGHT && halfLootboxTypeCursor == HalfLootboxType.LEFT) {

                MysteryChest cosmicMysteryChestCursor = CosmicMysteryChest.get().getChestManager().getMysteryChestMap().get(new NBTWrapper(cursor).getString("lootbox_name"));
                MysteryChest cosmicMysteryChestClicked = CosmicMysteryChest.get().getChestManager().getMysteryChestMap().get(new NBTWrapper(clicked).getString("lootbox_name"));


                if (!cosmicMysteryChestClicked.getInternalName().equals(cosmicMysteryChestCursor.getInternalName())) {
                    return;
                }


                int cursorAmount = cursor.getAmount();
                int clickedAmount = clicked.getAmount();

                ItemStack lootbox = cosmicMysteryChestCursor.createClaimableItem(false);
                if (cursorAmount > 1 || clickedAmount > 1) {
                    int diff = Math.max(clickedAmount, cursorAmount) - Math.min(clickedAmount, cursorAmount);
                    if (diff != 0) {
                        if (clickedAmount > cursorAmount) {
                            ItemStack clickedHalf = new HalfLootbox(cosmicMysteryChestClicked.getInternalName(), halfLootboxTypeClicked).getHalfLootboxItem().clone();
                            clickedHalf.setAmount(diff);
                            PlayerUtil.giveOrDropProtectedItem(player, clickedHalf, 60);
                        } else {
                            ItemStack cursorHalf = new HalfLootbox(cosmicMysteryChestCursor.getInternalName(), halfLootboxTypeCursor).getHalfLootboxItem().clone();
                            cursorHalf.setAmount(diff);
                            PlayerUtil.giveOrDropProtectedItem(player, cursorHalf, 60);
                        }
                    }
                    lootbox.setAmount(Math.min(cursorAmount, clickedAmount));
                }
                e.setCancelled(true);
                e.getWhoClicked().setItemOnCursor(null);
                e.setCurrentItem(lootbox);
                player.sendMessage(CC.translate("&a&l(!)&a You have created " + cosmicMysteryChestCursor.getDisplayName() + " &7(&fx" + Math.min(cursorAmount, clickedAmount) + "&7)"));
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 0.75f);
                player.playEffect(EntityEffect.WITCH_MAGIC);

            }
        }
    }


}

