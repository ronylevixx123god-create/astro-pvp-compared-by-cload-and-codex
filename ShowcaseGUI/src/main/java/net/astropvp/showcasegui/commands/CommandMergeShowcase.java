package net.astropvp.showcasegui.commands;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;

import net.astropvp.showcasegui.ShowcaseAPI;
import net.astropvp.showcasegui.ShowcaseGUI;
import net.astropvp.showcasegui.struct.Showcase;
import net.astropvp.showcasegui.utils.ItemSerialization;
import net.astropvp.showcasegui.utils.MojangUUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;


public class CommandMergeShowcase
        implements CommandExecutor {
    public static boolean merging = false;

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof ConsoleCommandSender)) {
            return true;
        }
        if (args.length == 1) {
            merging = true;
            for (Player pl : Bukkit.getOnlinePlayers()) {
                InventoryView view = pl.getOpenInventory();
                if (view == null || view.getTopInventory() == null || !view.getTopInventory().getName().contains("'s Showcase")) continue;
                pl.closeInventory();
            }
            CompletableFuture.runAsync(() -> {
                try {
                    long start = System.currentTimeMillis();
                    PreparedStatement statement = ShowcaseGUI.getConnection().prepareStatement("SELECT * FROM `" + args[0] + "`;");
                    ResultSet rs = statement.executeQuery();
                    try {
                        HashMap<UUID, TempShowcase> tempOldShowcases = new HashMap<UUID, TempShowcase>();
                        while (rs.next()) {
                            UUID oldUser = UUID.fromString(rs.getString("uuid"));
                            int oldSize = rs.getInt("size");
                            List<ItemStack> items = ItemSerialization.itemListFromBase64(rs.getString("showcase"));
                            Showcase current = ShowcaseAPI.getShowcase(oldUser);
                            if (current != null) {
                                System.out.println("Found online showcase for " + oldUser + ", adding 6 rows and adding " + items.size() + " items.");
                                CommandEditShowcase.editShowcase(current, "add", 6);
                                items.forEach(current::addItem);
                                continue;
                            }
                            tempOldShowcases.put(oldUser, new TempShowcase(items, Math.max(54, oldSize)));
                        }
                        statement.close();
                        int totalOldShowcases = tempOldShowcases.size();
                        PreparedStatement currentServerShowcase = ShowcaseGUI.getConnection().prepareStatement("SELECT * FROM showcases;");
                        ResultSet result = currentServerShowcase.executeQuery();
                        PreparedStatement batch = ShowcaseGUI.getConnection().prepareStatement("");
                        int updates = 0;
                        while (result.next()) {
                            try {
                                UUID user = UUID.fromString(result.getString("uuid"));
                                TempShowcase showcase = (TempShowcase)tempOldShowcases.get(user);
                                if (showcase == null) continue;
                                int size = result.getInt("size");
                                List<ItemStack> items = ItemSerialization.itemListFromBase64(result.getString("showcase"));
                                items.addAll(showcase.getItems());
                                System.out.println("[Merge] Found " + items.size() + " items for " + user + ", merging with " + showcase.getItems().size() + " old Items, size: " + (size += showcase.getSize()));
                                ++updates;
                                batch.addBatch("UPDATE showcases SET showcase = '" + ItemSerialization.itemListToBase64(items) + "', size = '" + size + "' WHERE uuid = '" + user + "';");
                                tempOldShowcases.remove(user);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        AtomicInteger newlyCreated = new AtomicInteger(0);
                        tempOldShowcases.forEach((uuid, temp) -> {
                            System.out.println("Inserting new showcase with " + temp.getItems().size() + " for " + uuid + " due to not existing on this planet.");
                            try {
                                batch.addBatch("INSERT IGNORE INTO showcases(uuid, showcase, size) VALUES ('" + uuid.toString() + "', '" + ItemSerialization.itemListToBase64(temp.getItems()) + "', '" + temp.getSize() + "');");
                                newlyCreated.incrementAndGet();
                            }
                            catch (SQLException e) {
                                e.printStackTrace();
                            }
                        });
                        batch.executeBatch();
                        currentServerShowcase.close();
                        batch.close();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    merging = false;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }, ForkJoinPool.commonPool());
        }
        return false;
    }

    class TempShowcase {
        private List<ItemStack> items;
        private int size;

        public TempShowcase(List<ItemStack> items, int size) {
            this.items = items;
            this.size = size;
        }

        public List<ItemStack> getItems() {
            return this.items;
        }

        public int getSize() {
            return this.size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }
}
