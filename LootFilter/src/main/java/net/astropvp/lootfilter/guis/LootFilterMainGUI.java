package net.astropvp.lootfilter.guis;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.Getter;
import net.astropvp.lootfilter.AstroLootFilterPlugin;
import net.astropvp.loottable.struct.LootTable;
import org.bukkit.Bukkit;

import java.util.UUID;

@Getter
public class LootFilterMainGUI {
    private final Gui lootFilterMainGUI;

    public LootFilterMainGUI(UUID uuid) {
        this.lootFilterMainGUI = new Gui(3, "Loot Filter Menu");
        this.lootFilterMainGUI.setDefaultClickAction(event -> event.setCancelled(true));
        for (LootTable lootTable : LootTable.values()) {
            lootFilterMainGUI.addItem(new GuiItem(new ItemBuilder(lootTable.getMaterial(), CC.translate(lootTable.getColor() + "&l" + lootTable.getFancy() + " Filter"))
                    .addLoreLine(CC.translate("&7Click to edit this loot filter."))
                    .addLoreLine("")
                    .addLoreLine(CC.translate(lootTable.getColor() + "Filtered Items: &f" + AstroLootFilterPlugin.getInstance().getLootFilterManager().getLootFilterData(uuid).getFilteredItemsAmount(lootTable)))
                    .build(), event -> {
            LootFilterSubGUI lootFilterSubGUI = new LootFilterSubGUI(uuid, lootTable);
            lootFilterSubGUI.getLootFilterSubGUI().open(Bukkit.getPlayer(uuid));
            }));
        }
    }
}
