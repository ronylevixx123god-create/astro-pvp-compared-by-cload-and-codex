package net.crankedpvp.pvptop.inventory;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import de.tr7zw.nbtapi.NBTItem;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.crankedpvp.pvptop.PvPTopPlugin;
import net.crankedpvp.pvptop.data.FactionData;
import net.crankedpvp.pvptop.utils.GuiUtil;
import net.crankedpvp.pvptop.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

public class PvPTopInventory {

    private static final DecimalFormat moneyFormat = new DecimalFormat("#,###");

    private final Gui pvpTopInventory;

    public Gui getUI() {
        return this.pvpTopInventory;
    }

    public PvPTopInventory(PvPTopPlugin plugin, boolean firstLoad) {
        this.pvpTopInventory = new Gui(6, "PvP Top");
        this.pvpTopInventory.setDefaultClickAction(event -> event.setCancelled(true));

        this.pvpTopInventory.getFiller().fillBorder(GuiUtil.createFillerGuiItem());
        this.pvpTopInventory.getFiller().fillBetweenPoints(2, 2, 2, 8, GuiUtil.createFillerGuiItem());
        this.pvpTopInventory.getFiller().fillBetweenPoints(5, 2, 5, 8, GuiUtil.createFillerGuiItem());


        ItemStack info = new ItemBuilder(Material.BOOK, "&b&lTop Point Factions",
                "&fThe top factions amongst Astro are",
                "&flisted below, sorted by &bFaction Points"
        ).colorize().build();

        ItemStack facPoints = new ItemBuilder(Material.SIGN, "&3&lObtaining Points",
                "&fThere are numerous ways to obtain",
                "&ffaction points, and you need these",
                "&fpoints to show up on the leaderboard",
                "&fand to compete for the /pvptop payouts.",
                "",
                "&3Obtaining Point Methods:",
                " &f• Player Kills",
                " &f• Capturing KOTHs",
                "",
                "&7&oClick to view all methods of obtaining points"
        ).colorize().build();

        ItemStack questionMark = new ItemBuilder(Material.SKULL_ITEM, "&f&l???", "&7Click me to make player skulls visible"
        ).colorize().build();

        this.pvpTopInventory.setItem(1, 5, new GuiItem(info));
        this.pvpTopInventory.setItem(6, 4, new GuiItem(facPoints, event -> {
            event.getWhoClicked().closeInventory();
            PvPTopPlugin.getInstance().getObtainPointsMethodsInventory().getUI().open(event.getWhoClicked());
        }));
        this.pvpTopInventory.setItem(6, 6, new GuiItem(questionMark));

        if(firstLoad) return;
        int count = 1;
        for(FactionData value : plugin.getUpdateLeaderboardTask().getSortedFactionData()) {
            ItemStack itemToAddToInventory = this.buildItemOfFactionData(value, count);
            if(itemToAddToInventory == null) continue;
            this.pvpTopInventory.addItem(new GuiItem(itemToAddToInventory, event -> {
                event.getWhoClicked().closeInventory();
                FactionPointsBreakdownInventory factionPointsBreakdownInventory = new FactionPointsBreakdownInventory(Factions.getInstance().getFactionById(value.getFactionId()));
                factionPointsBreakdownInventory.getUI().open(event.getWhoClicked());
            }));
            count++;
            if(count == 15) return;
        }
    }

    private ItemStack buildItemOfFactionData(FactionData value, int count) {
        Faction faction = Factions.getInstance().getFactionById(value.getKey());
        if(faction == null) {
            PvPTopPlugin.getInstance().getPvpTopManager().removeFromCache(value);
            PvPTopPlugin.getInstance().getUpdateLeaderboardTask().update();
            return null;
        }
        ItemBuilder itemBuilder = new ItemBuilder(getSkull(faction.getLeader().getName()))
                .setName("&3" + faction.getTag() + " &7(Points #" + count + ")")
                .setLore(
                        "",
                        "&b&lInformation",
                        " &f• Leader: &3" + faction.getLeader().getName(),
                        "",
                        "&b&lStatistics",
                        " &f• Faction Points: &3" + moneyFormat.format(value.calculateAllPoints())
                );
        return itemBuilder.colorize().build();

    }

    private ItemStack getSkull(String playerName) {
        ItemStack playerSkull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        NBTItem nbtItem = new NBTItem(playerSkull);
        nbtItem.setString("SkullOwner", playerName);
        return nbtItem.getItem();
    }

}
