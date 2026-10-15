package net.astropvp.loottable.guis;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Getter;

import net.astropvp.loottable.LootTablePlugin;
import net.astropvp.loottable.data.LootTableData;
import net.astropvp.loottable.struct.LootTable;
import org.bukkit.entity.Player;

@Getter

public class LootTableMainGUI {
    private PaginatedGui mainGUI;

    public LootTableMainGUI(Player player) {
        this.mainGUI = new PaginatedGui(3, 27, "Loottable Menu");
        mainGUI.setDefaultClickAction(event -> event.setCancelled(true));
        for (LootTable tableType : LootTable.values()) {
            LootTableData lootTable = LootTablePlugin.getInstance().getLootTableManager().getLootTable(tableType);
            mainGUI.addItem(new GuiItem(new ItemBuilder(tableType.getMaterial(), CC.translate(tableType.getColor() + "&l" + tableType.getFancy()), CC.translate("&7Click to open this loottable.")).addLoreLine("").addLoreLine(CC.translate(tableType.getColor() + "Eco Update: &f" + lootTable.getCurrentEcoUpdateLoot().getFancy() + " &7(&f" + lootTable.getCurrentEcoUpdateLoot().getWeek() + "&7)")).build(), event -> {
                EcoWeeksGUI lootTableEditGUI = new EcoWeeksGUI(player, tableType);
                lootTableEditGUI.getEcoWeeksGUI().open(event.getWhoClicked());
            }));
        }
    }
}
