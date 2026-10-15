package net.astropvp.lootfilter.data;

import com.cosmicpvp.cosmicutils.utils.CC;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.astropvp.loottable.struct.LootTable;
import net.astropvp.storage.utils.KeyToSave;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
public class LootFilterData implements KeyToSave {

    private UUID uuid;
    private Map<String, List<ItemStack>> filteredItems;

    public void filterItem(LootTable lootTable, ItemStack item) {
        if (!filteredItems.containsKey(lootTable.name())) {
            List<ItemStack> itemList = new ArrayList<>();
            getPlayer().sendMessage(CC.translate("&a&l(!)&a Added " + item.getItemMeta().getDisplayName() + " &ato " + lootTable.getColor() + "&l" + lootTable.getFancy() + " &aFilter."));
            itemList.add(item);
            filteredItems.put(lootTable.name(), itemList);
        } else if (filteredItems.get(lootTable.name()).contains(item)) {
            getPlayer().sendMessage(CC.translate("&c&l(!)&c Removed " + item.getItemMeta().getDisplayName() + " &cfrom " + lootTable.getColor() + "&l" + lootTable.getFancy() + " &cFilter."));
            filteredItems.get(lootTable.name()).remove(item);
        } else {
            getPlayer().sendMessage(CC.translate("&a&l(!)&a Added " + item.getItemMeta().getDisplayName() + " &ato " + lootTable.getColor() + "&l" + lootTable.getFancy() + " &aFilter."));
            filteredItems.get(lootTable.name()).add(item);
        }
    }

    public boolean isFiltered(LootTable lootTable, ItemStack item) {
        if (!filteredItems.containsKey(lootTable.name())) {
            return false;
        }
        return filteredItems.get(lootTable.name()).contains(item);
    }

    public int getFilteredItemsAmount(LootTable lootTable) {
        if (!filteredItems.containsKey(lootTable.name())) {
            return 0;
        }
        return filteredItems.get(lootTable.name()).size();
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
