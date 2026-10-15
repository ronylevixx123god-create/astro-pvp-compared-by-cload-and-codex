package net.crankedpvp.slotbot.inventory;

import com.cosmicpvp.cosmicutils.utils.CC;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.Getter;

import net.astropvp.lootfilter.LootFilterAPI;
import net.astropvp.loottable.LootTableAPI;
import net.astropvp.loottable.struct.LootTable;
import net.crankedpvp.slotbot.SlotBotAPI;
import net.crankedpvp.slotbot.SlotBotPlugin;
import net.crankedpvp.slotbot.struct.GameStatus;
import net.crankedpvp.slotbot.struct.SlotBotType;
import net.crankedpvp.slotbot.utils.ItemBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class SlotBotInventory {

    private GameStatus gameStatus = GameStatus.WAITING;
    private final Player player;
    private boolean isSpinning = false;
    private int insertedTickets = 0;
    private int taskID = 0;
    private boolean given = false;

    private SlotBotType slotBotType = null;

    @Getter
    private final Gui slotBotInventory;

    public SlotBotInventory(Player player) {
        this.player = player;
        this.slotBotInventory = new Gui(3, "Slot Bot");
        this.slotBotInventory.setDefaultClickAction(event -> event.setCancelled(true));

        handleInventoryClosure();
        enableSlotBotAddition();
        setupHowToPlayItem();
        setupLootTableItem();
        updateSpinItem();
        setupGlassSlots();
    }


    private void putSlotBotInMenu() {
        ItemStack itemStack = this.slotBotType.build();
        int index = (3 + this.insertedTickets);
        this.slotBotInventory.updateItem(1, index, new GuiItem(getAvailableRollGlass()));
        this.slotBotInventory.updateItem(3, index, new GuiItem(getAvailableRollGlass()));
        this.slotBotInventory.updateItem(2, index, new GuiItem(itemStack, event -> {
            this.slotBotInventory.updateItem(2, index, new GuiItem(getRewardSlot()));
            this.slotBotInventory.updateItem(1, index, new GuiItem(getFillingGlass()));
            this.slotBotInventory.updateItem(3, index, new GuiItem(getFillingGlass()));
            this.insertedTickets--;
            this.player.getInventory().addItem(itemStack);
            this.updateSpinItem();
        }));
    }

    private void updateSpinItem() {
        ItemStack spinItem = getSpinItem();
        this.slotBotInventory.updateItem(2, 9, new GuiItem(spinItem, event -> {
            if(this.isSpinning) {
                handleItemRewarding();
                return;
            }

            if(checkNoInsertedTickets()) return;
            this.gameStatus = GameStatus.SPINNING;
            this.isSpinning = true;
            updateSpinItem();
            updateItemTask(player);
            this.slotBotInventory.updateItem(2, 3, new GuiItem(getRewardRowGlass()));
            this.slotBotInventory.updateItem(2, 7, new GuiItem(getRewardRowGlass()));
        }));
    }

    private boolean checkNoInsertedTickets() {
        if(this.insertedTickets == 0) {
            this.player.sendMessage(CC.translate("&cYou have no tickets inserted"));
            this.player.playSound(this.player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
            return true;
        }
        return false;
    }

    private void handleItemRewarding() {
        SlotBotPlugin plugin = SlotBotPlugin.getInstance();
        plugin.getServer().getScheduler().cancelTask(this.taskID);
        int rewards = this.insertedTickets;
        this.insertedTickets = 0;
        this.isSpinning = false;
        this.gameStatus = GameStatus.FINISHING;
        if(this.player.getOpenInventory().getTopInventory().getTitle().equals("Slot Bot")) {
            for(int i = 0; i < rewards; i++) {
                ItemStack randomReward = LootTableAPI.getRandomLootItem(LootTable.SLOTS);
                this.slotBotInventory.updateItem(12 + i, new GuiItem(randomReward));
                LootFilterAPI.giveItem(player, LootTable.SLOTS, randomReward);
            }
            resetSlotBotInventory();
            return;
        }

        for(int i = 0; i < rewards; i++) {
            ItemStack randomReward = this.slotBotType.getRandomItem();
            this.player.getInventory().addItem(randomReward);
        }
    }

    private void moveItemSlotsDownOneRow() {
        for(int i = 0; i < this.insertedTickets; i++) {
            this.slotBotInventory.updateItem(21 + i, Objects.requireNonNull(this.slotBotInventory.getGuiItem(21 + i - 9)));
            this.slotBotInventory.updateItem(21 + i - 9, Objects.requireNonNull(this.slotBotInventory.getGuiItem(21 + i - 9 - 9)));
            this.slotBotInventory.updateItem(21 + i - 9 - 9, new GuiItem(this.slotBotType.getRandomItem()));
        }
    }

    private void updateItemTask(Player player) {
        SlotBotPlugin plugin = SlotBotPlugin.getInstance();
        AtomicInteger counter = new AtomicInteger();
        this.taskID = player.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, () -> {
            if(!player.getOpenInventory().getTopInventory().getTitle().contains("Slot Bot")) plugin.getServer().getScheduler().cancelTask(this.taskID);
            player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1.0F, 1.0F);
            moveItemSlotsDownOneRow();
            counter.getAndIncrement();
            if(counter.get() == 20) {
                plugin.getServer().getScheduler().cancelTask(this.taskID);
                plugin.getServer().getScheduler().runTaskLater(plugin, this::handleItemRewarding, 5L);
            }
        }, 0L, 4L);
    }

    private void resetSlotBotInventory() {
        SlotBotPlugin.getInstance().getServer().getScheduler().runTaskLater(SlotBotPlugin.getInstance(), () -> {
            setupGlassSlots();
            updateSpinItem();
            this.isSpinning = false;
            this.slotBotInventory.removeItem(2, 3);
            this.slotBotInventory.removeItem(2, 7);
            this.gameStatus = GameStatus.WAITING;
        }, 40L);
    }

    private ItemStack getSpinItem() {
        Material material = this.insertedTickets == 0 ? Material.MAGMA_CREAM : Material.SLIME_BALL;
        String name = this.gameStatus == GameStatus.SPINNING ? "&a&lSpinning..." : this.insertedTickets == 0 ? "&c&lSpin (&7Missing Ticket&c&l)" : "&a&lSpin (&7" + this.insertedTickets + " Tickets&a&l)";

        List<String> lore =
                this.isSpinning ?
                        Arrays.asList(
                        "&7Your roll bot tickets are",
                        "&7currently rolling in the machine",
                        "",
                        "&7Click to skip the animation")

                : this.insertedTickets == 0 ? Arrays.asList(
                        "&7You must insert at least",
                        "&7one ticket before you can",
                        "&7roll the roll bot machine") : Collections.singletonList("&7Click to spin the bot");

        ItemBuilder itemBuilder = new ItemBuilder(material, name).setLore(lore).colorize();
        return itemBuilder.colorize().build();
    }

    private void enableSlotBotAddition() {
        this.slotBotInventory.setPlayerInventoryAction(event -> {
            if(this.isSpinning) return;
            if(this.gameStatus == GameStatus.FINISHING) return;
            ItemStack clickedItem = event.getCurrentItem();
            if(!SlotBotAPI.isSlotBot(clickedItem)) return;
            if(this.slotBotType != null && !this.slotBotType.equals(clickedItem)) return;
            if(this.insertedTickets == 3) return;
            this.insertedTickets++;
            final int amount = clickedItem.getAmount();
            this.slotBotType = SlotBotAPI.getSlotBotType(clickedItem);
            if (amount <= 1) {
                event.setCurrentItem(null);
            } else {
                clickedItem.setAmount(amount - 1);
                event.setCurrentItem(clickedItem);
            }
            putSlotBotInMenu();
            this.updateSpinItem();
            this.player.updateInventory();
        });
    }











    private void setupLootTableItem() {
        ItemBuilder itemBuilder = new ItemBuilder(Material.BEACON, "&b&lLoot Table",
                "&7Click to view all of the possible",
                "&7loot inside of the roll bot machine"
        );
        this.slotBotInventory.setItem(1, 1, new GuiItem(itemBuilder.colorize().build(), event -> {
            this.player.closeInventory();
            LootPreviewInventory lootPreviewInventory = new LootPreviewInventory(this.player);
            lootPreviewInventory.getUI().open(this.player);
        }));
    }

    private void handleInventoryClosure() {
        this.slotBotInventory.setCloseGuiAction(inventoryCloseEvent -> {
            if(inventoryCloseEvent.getPlayer().hasMetadata("SlotBotReturnTickets") || this.given) {
                System.out.println("[SlotBot] RETURNING INVENTORY CLOSURE - DUPE PREVENTION");
                return;
            }
            if(this.isSpinning) {
                SlotBotPlugin.getInstance().getServer().getScheduler().cancelTask(this.taskID);
                for(int i = 0; i < this.insertedTickets; i++) {
                    ItemStack randomReward = this.slotBotType.getRandomItem();
                    this.player.getInventory().addItem(randomReward);
                }
                this.given = true;
                return;
            }

            if(insertedTickets == 0) return;
            this.player.getInventory().addItem(this.slotBotType.build(this.insertedTickets));
            this.player.sendMessage(CC.translate("&aYour &n" + this.insertedTickets + "&r &aroll tickets were returned to your inventory"));
            this.given = true;
        });
    }

    private void setupHowToPlayItem() {
        ItemBuilder itemBuilder = new ItemBuilder(Material.BOOK, "&b&lHow to Play",
                "&e&l1. &7Insert up to 3 roll tickets",
                "&7into the machine from your inventory",
                "",
                "&e&l2. &7Click \"Spin\" (on the right) to roll.",
                "&7the bot or, click \"Return\" (on the left)",
                "&7to withdraw all inserted tickets",
                "",
                "&e&l3. &7Watch as the bot spins items.",
                "&7The item which lands on the reward slot",
                "&7when the bot finishes spinning, is the item",
                "&7that you'll receive.",
                "",
                "&e&lTIP: &7Exiting the machine early will",
                "&7give you the rewards you would of won if",
                "&7you had stayed in the machine while spinning.",
                "&7If the machine is not spinning when you exit,",
                "&7your tickets will be refunded and no items will",
                "&7be given."
        );

        this.slotBotInventory.setItem(3, 1, new GuiItem(itemBuilder.colorize().build()));
    }

    private ItemStack getRewardSlot() {
        return new ItemBuilder(Material.STAINED_GLASS_PANE, 1, DyeColor.LIME.getData(), "&a&lReward Slot",
                "&7The item which lands on this slot",
                "&7will be the item that you receive").colorize().build();
    }

    private ItemStack getRewardRowGlass() {
        return new ItemBuilder(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getData(), "&a&lReward Row",
                "&7The item(s) which land in this row",
                "&7are the item(s) you will receive").setGlowing().colorize().build();
    }

    private ItemStack getFillingGlass() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("");
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getAvailableRollGlass() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.WHITE.getData());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        return item;
    }

    private void setupGlassSlots() {
        this.slotBotInventory.updateItem(2, 4, new GuiItem(getRewardSlot()));
        this.slotBotInventory.updateItem(2, 5, new GuiItem(getRewardSlot()));
        this.slotBotInventory.updateItem(2, 6, new GuiItem(getRewardSlot()));

        this.slotBotInventory.updateItem(1, 4, new GuiItem(getFillingGlass()));
        this.slotBotInventory.updateItem(1, 5, new GuiItem(getFillingGlass()));
        this.slotBotInventory.updateItem(1, 6, new GuiItem(getFillingGlass()));

        this.slotBotInventory.updateItem(3, 4, new GuiItem(getFillingGlass()));
        this.slotBotInventory.updateItem(3, 5, new GuiItem(getFillingGlass()));
        this.slotBotInventory.updateItem(3, 6, new GuiItem(getFillingGlass()));
    }
}
