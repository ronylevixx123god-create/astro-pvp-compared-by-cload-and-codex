package me.ily2bb.slotbot.utils;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.ily2bb.slotbot.CosmicSlotBotAPI;
import me.ily2bb.slotbot.SlotBotPlugin;
import net.astropvp.lootfilter.LootFilterAPI;
import net.astropvp.loottable.struct.LootTable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;

public class Inventories {
    public static Inventory createLootboxInventory(final boolean showPercentage) {
        final List<ItemStack> list = CosmicSlotBotAPI.getSlotBotItems();
        ;
        final int len = list.size() + 9;
        final int size = (len <= 9) ? 9 : ((len <= 18) ? 18 : ((len <= 27) ? 27 : ((len <= 36) ? 36 : ((len <= 45) ? 45 : ((len <= 54) ? 54 : 54)))));
        final Inventory inventory = Bukkit.createInventory(null, size, CC.DarkGray + "Astro Roll Bot Loot");
        for (ItemStack item : list) {
            inventory.addItem(item);
        }
        inventory.setItem(size - 5, GuiItems.createBackButton());
        for (int i = 0; i < inventory.getSize(); ++i) {
            final ItemStack x2 = inventory.getItem(i);
            if (x2 == null) {
                inventory.setItem(i, GuiItems.createPane(7));
            }
        }
        return inventory;
    }

    public static Inventory createInventory(final int slots) {
        final Inventory inventory = Bukkit.createInventory(null, 54, CC.DarkGray + "Astro Roll Bot");
        final ItemStack item = GuiItems.createLootTable();
        inventory.setItem(0, item);
        for (int i = 1; i < inventory.getSize() - 9; i += 9) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, GuiItems.createPane(15));
            }
        }
        for (int i = 7; i < inventory.getSize() - 9; i += 9) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, GuiItems.createPane(15));
            }
        }
        inventory.setItem(20, GuiItems.createRewardSlot());
        inventory.setItem(21, GuiItems.createRewardSlot());
        inventory.setItem(22, GuiItems.createRewardSlot());
        inventory.setItem(23, GuiItems.createRewardSlot());
        inventory.setItem(24, GuiItems.createRewardSlot());
        inventory.setItem(26, GuiItems.createSpinTicket(slots >= 1));
        inventory.setItem(53, GuiItems.createHowToPlayBook());
        inventory.setItem(51, GuiItems.createRoller(5 - slots));
        inventory.setItem(50, GuiItems.createRoller(4 - slots));
        inventory.setItem(49, GuiItems.createRoller(3 - slots));
        inventory.setItem(48, GuiItems.createRoller(2 - slots));
        inventory.setItem(47, GuiItems.createRoller(1 - slots));
        inventory.setItem(45, GuiItems.createReturnSlotBotsButton(slots));
        for (int i = 2; i < inventory.getSize() - 9; i += 9) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, GuiItems.createPane((slots >= 1) ? 0 : 8));
            }
        }
        for (int i = 3; i < inventory.getSize() - 9; i += 9) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, GuiItems.createPane((slots >= 2) ? 0 : 8));
            }
        }
        for (int i = 4; i < inventory.getSize() - 9; i += 9) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, GuiItems.createPane((slots >= 3) ? 0 : 8));
            }
        }
        for (int i = 5; i < inventory.getSize() - 9; i += 9) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, GuiItems.createPane((slots >= 4) ? 0 : 8));
            }
        }
        for (int i = 6; i < inventory.getSize() - 9; i += 9) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, GuiItems.createPane((slots >= 5) ? 0 : 8));
            }
        }
        for (int i = 0; i < inventory.getSize(); ++i) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, GuiItems.createPane(7));
            }
        }
        return inventory;
    }

    public static Inventory createRollingInventory(final Player player, final int slots) {
        final Inventory inventory = Bukkit.createInventory(null, 54, CC.DarkGray + "Astro Roll Bot");
        final ItemStack item = GuiItems.createLootTable();
        inventory.setItem(0, item);
        for (int i = 1; i < inventory.getSize() - 9; i += 9) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, GuiItems.createPane(15));
            }
        }
        for (int i = 7; i < inventory.getSize() - 9; i += 9) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, GuiItems.createPane(15));
            }
        }
        inventory.setItem(53, GuiItems.createHowToPlayBook());
        inventory.setItem(51, (slots >= 1) ? GuiItems.createRoller(4) : null);
        inventory.setItem(49, (slots >= 2) ? GuiItems.createRoller(3) : null);
        inventory.setItem(49, (slots >= 3) ? GuiItems.createRoller(2) : null);
        inventory.setItem(49, (slots >= 4) ? GuiItems.createRoller(1) : null);
        for (int i = 0; i < inventory.getSize(); ++i) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, GuiItems.createPane(7));
            }
        }
        return inventory;
    }

    public static void startRollingInColumn(final Player player, final Inventory inventory, final int column) {
        player.setMetadata("slot_rolling_" + column, new FixedMetadataValue(SlotBotPlugin.getInstance(), null));
        final ItemStack item =CosmicSlotBotAPI.getRandomSlotBotLootItem();
        final BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.scheduleSyncDelayedTask(SlotBotPlugin.getInstance(), (Runnable) new BukkitRunnable() {
            final ItemStack[] previousItems = new ItemStack[6];
            int time = 1;

            public void run() {
                if (this.previousItems[0] == null) {
                    for (int i = 0; i < this.previousItems.length; ++i) {
                        this.previousItems[i] = CosmicSlotBotAPI.getRandomSlotBotLootItem();
                    }
                } else {
                    System.arraycopy(this.previousItems, 0, this.previousItems, 1, this.previousItems.length - 2 + 1);
                    this.previousItems[0] = CosmicSlotBotAPI.getRandomSlotBotLootItem();
                }
                int x = column;
                for (ItemStack previousItem : this.previousItems) {
                    inventory.setItem(x, previousItem);
                    x += 9;
                }
                player.updateInventory();
                --this.time;
                if (this.time <= 0) {
                    x = column;
                    int iterations = 0;
                    for (int k = 0; k < this.previousItems.length; ++k) {
                        if (iterations != 2) {
                            inventory.setItem(x, null);
                        }
                        x += 9;
                        ++iterations;
                    }
                    for (int k = 18; k <= 26; ++k) {
                        ItemStack y = inventory.getItem(k);
                        if (y.getType() == Material.STAINED_GLASS_PANE) {
                            y = Utils.addGlow(y);
                            inventory.setItem(k, y);
                        }
                    }
                    player.removeMetadata("slot_rolling_" + column, SlotBotPlugin.getInstance());

                    LootFilterAPI.giveItem(player, LootTable.SLOTS, this.previousItems[2]);

                    this.cancel();
                } else if (!player.hasMetadata("slot_rolling_" + column)) {

                    LootFilterAPI.giveItem(player, LootTable.SLOTS, item);

                    this.cancel();
                }
            }
        }.runTaskTimer(SlotBotPlugin.getInstance(), 1L, 1L));
    }
}