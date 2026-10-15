package me.ifamasssxd.cosmicmysterychest.data;

import com.cosmicpvp.cosmicutils.utils.CC;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest;
import net.astropvp.storage.utils.KeyToSave;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class LootboxFilterData implements KeyToSave {

    private UUID uuid;
    private final Map<String, List<ItemStack>> filteredItems;

    public void filterItem(String lootboxID, ItemStack item) {
        ItemStack lootboxITem = CosmicMysteryChest.get().getChestManager().getMysteryChestMap().get(lootboxID).createClaimableItem(false);
        if (!filteredItems.containsKey(lootboxID)) {
            List<ItemStack> itemList = new ArrayList<>();
            getPlayer().sendMessage(CC.translate("&a&l(!)&a Added " + item.getItemMeta().getDisplayName() + " &ato " + lootboxITem.getItemMeta().getDisplayName() + " &aFilter."));
            itemList.add(item);
            filteredItems.put(lootboxID, itemList);
        } else if (filteredItems.get(lootboxID).contains(item)) {
            getPlayer().sendMessage(CC.translate("&c&l(!)&c Removed " + item.getItemMeta().getDisplayName() + " &cfrom "+ lootboxITem.getItemMeta().getDisplayName() + " &cFilter."));
            filteredItems.get(lootboxID).remove(item);
        } else {
            getPlayer().sendMessage(CC.translate("&a&l(!)&a Added " + item.getItemMeta().getDisplayName() + " &ato " + lootboxITem.getItemMeta().getDisplayName() + " &aFilter."));
            filteredItems.get(lootboxID).add(item);
        }
    }

    public boolean isFiltered(String lootboxID, ItemStack item) {
        if (!filteredItems.containsKey(lootboxID)) {
            return false;
        }
        return filteredItems.get(lootboxID).contains(item);
    }

    public int getFilteredItemsAmount(String lootboxID) {
        if (!filteredItems.containsKey(lootboxID)) {
            return 0;
        }
        return filteredItems.get(lootboxID).size();
    }

    public Player getPlayer () {
        return Bukkit.getPlayer(uuid);
    }

    @Override
    public String getKey() {
        return uuid.toString();
    }

    @Override
    public void setKey(String s) {
        this.uuid = UUID.fromString(s);
    }
}
