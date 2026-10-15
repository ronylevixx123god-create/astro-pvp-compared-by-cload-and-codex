package me.ifamasssxd.cosmicskycustoms.struct;

import java.util.Arrays;

import lombok.Getter;
import me.ifamasssxd.cosmicskycustoms.CosmicSkyCustomItems;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public enum CosmicItems {
    ;

    private final String internalName;
    private final CosmicItem item;

    private CosmicItems(CosmicItem item) {
        this.internalName = item.getInternalName();
        this.item = item;
    }

    public String getInternalName() { return this.internalName; }

    public CosmicItem getItem() { return this.item; }

    public static ItemStack createItem(String itemName, Object ... args) {
        CosmicItem item = CosmicSkyCustomItems.get().getItemManager().getCustomItem(itemName);
        if (item == null) {
            return null;
        }
        return item.createItem(null, args);
    }

    public ItemStack createItem(Player player, Object ... args) {
        return this.item.createItem(player, args);
    }

    public static CosmicItems getCosmicItem(String internalName) {
        return Arrays.stream(CosmicItems.values()).filter(f -> f.getInternalName().equalsIgnoreCase(internalName)).findFirst().orElse(null);
    }

    public static CosmicItem getCustomItem(String internalName) {
        return Arrays.stream(CosmicItems.values()).filter(f -> f.getInternalName().equalsIgnoreCase(internalName)).findFirst().map(CosmicItems::getItem).orElse(CosmicSkyCustomItems.get().getItemManager().getCustomItem(internalName));
    }

}