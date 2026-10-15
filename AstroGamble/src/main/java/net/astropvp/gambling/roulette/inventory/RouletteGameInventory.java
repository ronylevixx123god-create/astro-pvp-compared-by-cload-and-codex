package net.astropvp.gambling.roulette.inventory;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;

import net.astropvp.gambling.GamblingPlugin;
import  net.astropvp.gambling.roulette.struct.RouletteColour;
import net.astropvp.gambling.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class RouletteGameInventory {

    private int spins;
    private int maxSpins;
    private Player player;

    private RouletteColour yourRouletteColour;

    private final Map<Integer, RouletteColour> rouletteColours = new HashMap<>();

    private final Gui rouletteGameInventory;

    public Gui getUI() {
        return this.rouletteGameInventory;
    }

    private void setupRouletteColours() {
        this.rouletteColours.put(0, RouletteColour.BLACK);
        this.rouletteColours.put(2, RouletteColour.BLACK);
        this.rouletteColours.put(6, RouletteColour.BLACK);
        this.rouletteColours.put(8, RouletteColour.BLACK);
        this.rouletteColours.put(26, RouletteColour.BLACK);
        this.rouletteColours.put(44, RouletteColour.BLACK);
        this.rouletteColours.put(52, RouletteColour.BLACK);
        this.rouletteColours.put(50, RouletteColour.BLACK);
        this.rouletteColours.put(48, RouletteColour.BLACK);
        this.rouletteColours.put(46, RouletteColour.BLACK);
        this.rouletteColours.put(36, RouletteColour.BLACK);

        this.rouletteColours.put(4, RouletteColour.GREEN);
        this.rouletteColours.put(49, RouletteColour.GREEN);

        this.rouletteColours.put(1, RouletteColour.RED);
        this.rouletteColours.put(3, RouletteColour.RED);
        this.rouletteColours.put(5, RouletteColour.RED);
        this.rouletteColours.put(7, RouletteColour.RED);
        this.rouletteColours.put(17, RouletteColour.RED);
        this.rouletteColours.put(35, RouletteColour.RED);
        this.rouletteColours.put(53, RouletteColour.RED);
        this.rouletteColours.put(51, RouletteColour.RED);
        this.rouletteColours.put(47, RouletteColour.RED);
        this.rouletteColours.put(45, RouletteColour.RED);
    }

    public RouletteGameInventory(Player player, RouletteColour rouletteColour) {

        this.spins = 0;
        this.maxSpins = ThreadLocalRandom.current().nextInt(60, 80);
        this.player = player;
        this.yourRouletteColour = rouletteColour;

        this.rouletteGameInventory = new Gui(6, "Roulette Game");
        this.rouletteGameInventory.setDefaultClickAction(event -> event.setCancelled(true));
        this.rouletteColours.forEach((integer, colour) -> this.rouletteGameInventory.setItem(integer, new GuiItem(createColourSquare(rouletteColour))));
        this.rouletteGameInventory.setItem(22, new GuiItem(createInfoItem()));
        this.recursiveSpin(0, 2);
    }

    private void recursiveSpin(int index, long speed) {

        this.spins++;
        if(this.spins >= this.maxSpins) {
            RouletteColour rouletteColour = rouletteColours.get(index);
            return;
        }

        if(index >= this.rouletteColours.size()) {
            recursiveSpin(0, speed);
            return;
        }

        setGlowing(index);
        int lastSpun = index - 1;
        if(lastSpun >= 0) {
            removeGlowing(lastSpun);
        } else {
            removeGlowing(this.rouletteColours.size()-1);
        }
        this.player.updateInventory();


        if(spins > 70) {
            GamblingPlugin.getInstance().getServer().getScheduler().runTaskLater(GamblingPlugin.getInstance(), () -> recursiveSpin((index + 1), 4L), speed);
        } else if(spins > 50) {
            GamblingPlugin.getInstance().getServer().getScheduler().runTaskLater(GamblingPlugin.getInstance(), () -> recursiveSpin((index + 1), 3L), speed);
        } else {
            GamblingPlugin.getInstance().getServer().getScheduler().runTaskLater(GamblingPlugin.getInstance(), () -> recursiveSpin((index + 1), 1L), speed);
        }
    }


    private ItemStack createInfoItem() {
        ItemBuilder itemBuilder = new ItemBuilder(Material.DIAMOND, "&e&lRoulette Information",
                "&fSelected: &e" + this.yourRouletteColour.name().toUpperCase(),
                "&fMultiplier: &a" + this.yourRouletteColour.getMultiplier() + "x");
        return itemBuilder.colorize().build();
    }


    private ItemStack createColourSquare(RouletteColour rouletteColour) {
        ItemBuilder itemBuilder = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, rouletteColour.getGlassColour());
        itemBuilder.setName(rouletteColour.getColour() + rouletteColour.name().toUpperCase(Locale.ROOT));
        itemBuilder.setLore("&fMultiplier: &a" + rouletteColour.getMultiplier() + "x");
        return itemBuilder.colorize().build();
    }

    private void setGlowing(int index) {
        ItemBuilder currentItemBuilder = new ItemBuilder(this.rouletteGameInventory.getGuiItem(index).getItemStack());
        currentItemBuilder.setGlowing();
        this.rouletteGameInventory.updateItem(index, new GuiItem(currentItemBuilder.colorize().build()));
    }

    private void removeGlowing(int index) {
        ItemBuilder lastItemBuilder = new ItemBuilder(this.rouletteGameInventory.getGuiItem(index).getItemStack());
        lastItemBuilder.removeGlow();
        this.rouletteGameInventory.updateItem(index, new GuiItem(lastItemBuilder.colorize().build()));
    }
}
