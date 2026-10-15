package me.ifamasssxd.cosmicskycustoms.struct;

import com.cosmicpvp.cosmicutils.itembuilder.CosmicItemBuilder;
import com.google.common.collect.Lists;
import java.util.List;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CustomItemBuilder
extends CosmicItemBuilder {
    private String item;
    private CosmicItem cosmicItem;
    private List<Object> data;

    public CustomItemBuilder(String customItem, Object ... data) {
        super(Material.PAPER);
        this.item = customItem;
        this.data = Lists.newArrayList(data);
    }

    public CustomItemBuilder(CosmicItems item, Object ... data) {
        super(Material.PAPER);
        this.item = item.getInternalName();
        this.cosmicItem = item.getItem();
        this.data = Lists.newArrayList(data);
    }

    public ItemStack build() {
        if (this.cosmicItem == null) {
            this.cosmicItem = CosmicItems.getCustomItem(this.item);
        }
        if (this.cosmicItem == null) {
            Bukkit.getLogger().info("Null cosmic item for " + this.item + "!");
            return null;
        }
        ItemStack item = this.cosmicItem.createItem(null, this.data.toArray());
        int amount = this.getAmount();
        if (amount > 1 && item != null) {
            item.setAmount(amount);
        }
        if (item != null) {
            return item;
        }
        return super.build();
    }

    public String getItem() {
        return this.item;
    }
}

