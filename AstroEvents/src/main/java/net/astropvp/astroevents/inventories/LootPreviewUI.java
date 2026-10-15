package net.astropvp.astroevents.inventories;

import com.cosmicpvp.cosmicutils.utils.CC;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Getter;
import lombok.SneakyThrows;

import net.astropvp.astroevents.AstroEvents;
import net.astropvp.astroevents.game.MiniGame;
import net.astropvp.astroevents.utils.Utils;
import net.astropvp.astroevents.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class LootPreviewUI {
    @Getter
    private final PaginatedGui eventsUI;

    @SneakyThrows
    public LootPreviewUI(AstroEvents plugin, Player player, MiniGame miniGame) {
        this.eventsUI = new PaginatedGui(1, 9,  Utils.toNiceString(miniGame.getMiniGameType().name()) + " Loot Preview");

        this.eventsUI.setDefaultClickAction(event -> event.setCancelled(true));
        Optional<ItemStack> mysteryChest = Optional.of(miniGame.getMysteryChest().createClaimableItem(false)) ;
            this.eventsUI.setItem(4, new GuiItem(mysteryChest.orElseThrow(() -> new Exception("Mystery Chest has not been found"))));

        ItemStack homePage = new ItemBuilder(Material.BED).setName("&c&lMain Menu").addLore("&7Click to return to the main menu.").colorize().build();
        eventsUI.setItem(1, 1, new GuiItem(homePage, event -> {
            MainEventsUI teamListGUI = new MainEventsUI(plugin, player);
            teamListGUI.getEventsUI().open(player);
        }));


    }


}
