package net.astropvp.astroevents.inventories;

import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Getter;
import net.astropvp.astroevents.AstroEvents;
import net.astropvp.astroevents.game.MiniGameState;
import net.astropvp.astroevents.game.MiniGameType;
import net.astropvp.astroevents.utils.Utils;
import net.astropvp.astroevents.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class MainEventsUI {

    @Getter
    private final PaginatedGui eventsUI;

    public MainEventsUI(AstroEvents plugin, Player player) {
        this.eventsUI = new PaginatedGui(3, 27, "Events Menu");
        this.eventsUI.setDefaultClickAction(event -> event.setCancelled(true));

        /*BukkitTask runnable = Bukkit.getScheduler().runTaskTimer(plugin, () -> eventsUI.updateItem(4, new GuiItem(
                new ItemBuilder(Material.CHEST)
                        .setName("&6&lAstro Events Menu")
                        .addLore(
                                "&7In this menu you can find out when the next event",
                                "&7will happen as well as which event is currently active",
                                "",
                                "&6* &7Next event: &6" + Utils.toNiceString(plugin.getMiniGameManager().getNextMiniGame().name()),
                                "&6* &7Next event will happen in &6" + plugin.getMiniGameManager().getTimeUntilNextMiniGame()
                        )
                        .colorize()
                        .build())), 20L, 20L);*/

        //this.eventsUI.setCloseGuiAction(event -> runnable.cancel());

        plugin.getMiniGameRegister().getMiniGames().forEach(((miniGameType, miniGame) -> {

            ItemStack itemStack;
            if (miniGame.getMiniGameState() != MiniGameState.NOT_STARTED) {

                itemStack = new ItemBuilder(miniGame.getGuiMaterial())
                        .setName("&a&l" + Utils.toNiceString(miniGameType.name() + " Event"))
                        .addLore("&a* &7Event state: &a" + Utils.toNiceString(miniGame.getMiniGameState().name()),
                                "&a* &7Min/Max players: &a" + miniGame.getMinPlayers() + "&8/&a" + miniGame.getMaxPlayers(),
                                "&a* &7Amount of participants: &a" + miniGame.getParticipants().size(),
                                "",
                                "&7&nLeft-Click&7 to join the " + Utils.toNiceString(miniGameType.name() + " Minigame!"),
                                "&7&nRight-Click&7 to preview the loot.")
                        .colorize()
                        .build();


            } else {
                itemStack = new ItemBuilder(Material.BARRIER)
                        .setName("&a&l" + Utils.toNiceString(miniGameType.name() + " Event"))
                        .addLore("&cThis event is currently not running.",
                                "&cJoin the /discord and react to #events",
                                "&cto stay updated when we do event like these.",
                                "",
                                "&7&nRight-Click&7 to preview the loot.")
                        .colorize()
                        .build();
            }

            this.eventsUI.setItem(4, new GuiItem(
                    new ItemBuilder(Material.CHEST)
                            .setName("&6&lAstro Events Menu")
                            .addLore(
                                    "&7In this menu you can find out when the next event",
                                    "&7will happen as well as which event is currently active"
                            )
                            .colorize()
                            .build()
            ));

            if (miniGameType == MiniGameType.PARKOUR) {
                this.eventsUI.setItem(23, new GuiItem(itemStack, event -> {
                    if (event.isLeftClick()) {
                        if (miniGame.getMiniGameState() == MiniGameState.STARTING)
                            miniGame.joinMiniGame(player);
                    } else {
                        LootPreviewUI lootPreviewUI = new LootPreviewUI(plugin, player, miniGame);
                        lootPreviewUI.getEventsUI().open(player);
                        player.playSound(player.getLocation(), Sound.CHEST_OPEN, 2, 2);
                    }
                }));
            } else {
                this.eventsUI.setItem(21, new GuiItem(itemStack, event -> {
                    if (event.isLeftClick()) {
                        if (miniGame.getMiniGameState() == MiniGameState.STARTING)
                        miniGame.joinMiniGame(player);
                    } else {
                        LootPreviewUI lootPreviewUI = new LootPreviewUI(plugin, player, miniGame);
                        lootPreviewUI.getEventsUI().open(player);
                        player.playSound(player.getLocation(), Sound.CHEST_OPEN, 2, 2);
                    }

                }));
            }
        }));
    }


}
