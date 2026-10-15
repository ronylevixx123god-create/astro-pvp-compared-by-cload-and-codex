package net.crankedpvp.warzonechests.inventories;

import lombok.Getter;
import lombok.Setter;
import net.astropvp.loottable.LootTableAPI;
import net.astropvp.loottable.struct.LootTable;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.concurrent.ThreadLocalRandom;

@Getter
public class LootGUI {

    private final Inventory gui;
    private final long timeOfCreation;
    @Setter
    private boolean firstOpen;

    public LootGUI() {
        this.gui = Bukkit.createInventory(null, 27, "Warzone Chest");
        this.timeOfCreation = System.currentTimeMillis();
        this.firstOpen = true;
        this.populateGui();
    }

    private void populateGui() {
        for (int i = 0; i < randomAmountOfItems(); i++) {
            this.gui.setItem(randomSlot(), LootTableAPI.getRandomLootItem(LootTable.WARZONE_CHESTS));
        }
    }

    private int randomAmountOfItems() {
        return ThreadLocalRandom.current().nextInt(3, 5);
    }

    private int randomSlot() {
        return ThreadLocalRandom.current().nextInt(3, 25);
    }
}
