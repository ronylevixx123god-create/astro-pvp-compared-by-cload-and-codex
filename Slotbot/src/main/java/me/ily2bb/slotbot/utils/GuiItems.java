package me.ily2bb.slotbot.utils;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import me.ily2bb.slotbot.CosmicSlotBotAPI;
import me.ily2bb.slotbot.SlotBotPlugin;
import me.ily2bb.slotbot.objects.Glow;
import net.astropvp.loottable.LootTableAPI;
import net.astropvp.loottable.struct.LootTable;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class GuiItems {
    public static List<ItemStack> getLootTableItems() {
        return LootTableAPI.getAllLootFromLootTable(LootTable.SLOTS);
    }

    public static List<ItemStack> getShowItems() {
        return LootTableAPI.getAllLootFromLootTable(LootTable.SLOTS);
    }



    public static ItemStack createLootTable() {
        final ItemStack lootbox = new ItemStack(Material.BEACON);
        final ItemMeta meta = lootbox.getItemMeta();
        meta.setDisplayName(CC.WhiteB + "Loot Table");
        final AbstractList<String> lore = new ArrayList<String>(Arrays.asList(CC.Gray + "The Roll Bot contains the items", CC.Gray + "from the following Lootboxes,", CC.Gray + "which will randomize every week:", ""));
        final List<ItemStack> items = getShowItems();
        int count = 0;
        for ( ItemStack x : items) {
            if (count > 20) break;
            final String display = ItemUtils.getDisplayName(x);
            lore.add(CC.WhiteB + " * " + ((x.getAmount() > 1) ? (x.getAmount() + "x ") : "") + display);
            count++;
        }
        lore.add("");
        lore.add(CC.Gray + "Click To View All Items");
        meta.setLore(lore);
        meta.addEnchant(new Glow(70), 1, true);
        lootbox.setItemMeta(meta);
        return lootbox;
    }

    public static ItemStack createPane(final int id) {
        final ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) id);
        final ItemMeta paneMeta = pane.getItemMeta();
        paneMeta.setDisplayName(String.valueOf(CC.DarkRed));
        pane.setItemMeta(paneMeta);
        return pane;
    }

    public static ItemStack createRewardSlot() {
        final ItemStack pane = createPane(5);
        final ItemMeta paneMeta = pane.getItemMeta();
        paneMeta.setDisplayName(CC.GreenB + "Reward");
        final List<String> lore = Arrays.asList(CC.Gray + "The item that will display here", CC.Gray + "will be the item you receive.");
        paneMeta.setLore(lore);
        pane.setItemMeta(paneMeta);
        return pane;
    }

    public static ItemStack createSpinTicket(final boolean spinning) {
        if (!spinning) {
            final ItemStack item = new ItemStack(Material.MAGMA_CREAM);
            final ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(CC.RedB + "Spin (" + CC.Gray + "Missing Ticket" + CC.RedB + ")");
            itemMeta.setLore(Arrays.asList(CC.Gray + "Insert a Roll Bot Ticket", CC.Gray + "to use the bot.", "", CC.Gray + "Purchase Roll Bot Tickets", CC.Gray + "at " + CC.White + "buy.astropvp.net"));
            item.setItemMeta(itemMeta);
            return item;
        }
        final ItemStack item = new ItemStack(Material.SLIME_BALL);
        final ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(CC.GreenB + "Spin");
        itemMeta.setLore(Collections.singletonList(CC.Gray + "Click to spin the bot."));
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack createHowToPlayBook() {
        final ItemStack item = new ItemStack(Material.BOOK);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.AquaB + "How to Play");
        final List<String> lore = Arrays.asList(CC.LightPurpleB + "1. " + CC.Gray + "Insert up to 3 Roll Bot Tickets", CC.Gray + "into the bot from your inventory.", "", CC.LightPurpleB + "2. " + CC.Gray + "Click ''Spin'' (on the right) to use", CC.Gray + "the bot or, click ''Return'' (on the left)", CC.Gray + "to withdraw all inserted tickets.", "", CC.LightPurpleB + "3. " + CC.Gray + "When the bot has finished generating", CC.Gray + "the item which is displayed", CC.Gray + "will be the item you receive.", "", CC.WhiteB + "TIP: " + CC.Gray + "Closing the bot while spinning will", CC.Gray + "give the rewards you would have", CC.Gray + "received, otherwise your inserted", CC.Gray + "tickets will be refunded.");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }


    public static ItemStack createRoller(final int x) {
        if (x > 0) {
            final ItemStack item = createPane(14);
            final ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(CC.RedB + "LOCKED");
            final List<String> lore = Arrays.asList(CC.Gray + "Requires " + x + " more " + ((x > 1) ? "tickets" : "ticket") + " to use.", "", CC.Gray + "Purchase Roll Bot Tickets", CC.Gray + "at " + CC.White + "buy.astropvp.net");
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);
            item.setAmount(x);
            return item;
        }
        final ItemStack item = CosmicSlotBotAPI.createSlotBotItem().clone();
        final ItemMeta itemMeta = item.getItemMeta();
        final List<String> lore = Collections.singletonList(CC.Gray + "Click use to roll the bot.");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack createBackButton() {
        final ItemStack item = new ItemStack(Material.NETHER_STAR);
        final ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(CC.RedB + "Back");
        final List<String> list = Arrays.asList(CC.Gray + "Click to return to", CC.Gray + "the Roll Bot.");
        itemMeta.setLore(list);
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack createReturnSlotBotsButton(final int slots) {
        if (slots <= 0) {
            return null;
        }
        final ItemStack item = new ItemStack(Material.NETHER_STAR);
        final ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(CC.RedB + "Return");
        final List<String> list = Arrays.asList(CC.Gray + "Click to return your " + slots + " tickets.");
        itemMeta.setLore(list);
        item.setItemMeta(itemMeta);
        return item;
    }
}
