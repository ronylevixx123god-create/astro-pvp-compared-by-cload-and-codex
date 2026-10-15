package net.crankedpvp.arcade.guis;

import com.cosmicpvp.cosmicutils.itembuilder.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.Getter;
import net.crankedpvp.arcade.ArcadeAPI;
import net.crankedpvp.arcade.ArcadePlugin;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public class ArcadeGUI {
    private Gui arcadeGui;
    private DyeColor color = DyeColor.WHITE;

    public ArcadeGUI() {
        this.arcadeGui = new Gui(1, "Astro Arcade");
        this.arcadeGui.setDefaultClickAction(event -> event.setCancelled(true));
        this.arcadeGui.setItem(4, new GuiItem(getArcadeItem(color), event -> {
            ((Player) event.getWhoClicked()).chat("/warp arcade");
        }));
        Bukkit.getScheduler().scheduleSyncRepeatingTask(ArcadePlugin.getInstance(), () -> {
            color = DyeColor.values()[(color.ordinal() + 1) % DyeColor.values().length];
            if (!ArcadePlugin.getInstance().getArcadeManager().isEnabled()) {
                color = DyeColor.RED;
            }
            this.arcadeGui.updateItem(4, new GuiItem(getArcadeItem(color), event -> {
                if (ArcadePlugin.getInstance().getArcadeManager().isEnabled()) {
                    event.getWhoClicked().closeInventory();
                    ((Player) event.getWhoClicked()).chat("/warp arcade");
                } else {
                    event.getWhoClicked().sendMessage(CC.translate("&d&l(!)&d The arcade warp opens in &f" + ArcadePlugin.getInstance().getArcadeManager().timeUntilArcadeEnabled() + "&d."));
                }
            }));
        }, 0L, 10L);


    }


    public ItemStack getArcadeItem(DyeColor color) {

        ItemBuilder itemBuilder = new ItemBuilder(
                Material.STAINED_CLAY, 1, color.getWoolData())
                .setName(CC.translate("&d&lArcade Warp"))
                .addLoreLine(CC.translate("&7This are is a warp meant"))
                .addLoreLine(CC.translate("&7only for the warriors who hold"))
                .addLoreLine(CC.translate("&7the greatest sets of armor on"))
                .addLoreLine(CC.translate("&7the realm. You must have an armor"))
                .addLoreLine(CC.translate("&7set equipped to join this warp."))
                .addLoreLine("")
                .addLoreLine(CC.translate("&dCurrent Info:"))
                .addLoreLine(CC.translate("&fPlayers: &7" + Bukkit.getWorld(ArcadeAPI.WORLD_NAME).getPlayers().size()))
                .addLoreLine("")
                .addLoreLine(CC.translate("&dStatus"))
                .addLoreLine(CC.translate("&f" + (ArcadePlugin.getInstance().getArcadeManager().isEnabled() ? "&a&lOPEN" : "&c&lCLOSED &7(&fOpens in &d" + ArcadePlugin.getInstance().getArcadeManager().timeUntilArcadeEnabled() + "&7)")))
                .addLoreLine("")
                .addLoreLine(CC.translate("&7Click to teleport to the arcade."));

        return itemBuilder.build();
    }
}
