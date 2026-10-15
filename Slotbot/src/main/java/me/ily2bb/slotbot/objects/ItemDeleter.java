package me.ily2bb.slotbot.objects;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.ily2bb.slotbot.SlotBotPlugin;
import me.ily2bb.slotbot.utils.Inventories;
import me.ily2bb.slotbot.utils.GuiItems;
import me.ily2bb.slotbot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class ItemDeleter {
    public static HashSet<ItemDeleter> deleters;

    static {
        ItemDeleter.deleters = new HashSet<ItemDeleter>();
    }

    private ItemStack item;
    private UUID player;
    private Inventory inventory;

    public ItemDeleter(final Player player, ItemStack item, final Inventory inventory) {
        if (isDeletingItem(player)) {
            player.sendMessage(String.valueOf(CC.Red) + "You are already deleting a different item");
            return;
        }
        item = item.clone();
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            final ItemMeta meta = item.getItemMeta();
            final List<String> lore = (List<String>) meta.getLore();
            for (int i = lore.size() - 1; i >= 0; --i) {
                final String l = lore.get(i);
                if (l.startsWith(CC.GreenB + "CHANCE:")) {
                    lore.remove(i);
                }
            }
            meta.setLore((List) lore);
            item.setItemMeta(meta);
        }
        this.item = item;
        this.player = player.getUniqueId();
        this.inventory = inventory;
        ItemDeleter.deleters.add(this);
    }

    public static ItemDeleter getPlayer(final Player player) {
        final UUID u = player.getUniqueId();
        for (final ItemDeleter deleter : ItemDeleter.deleters) {
            if (deleter.getUUID().equals(u)) {
                return deleter;
            }
        }
        return null;
    }

    public static void removeItemDeleter(final ItemDeleter deleter) {
        if (ItemDeleter.deleters.contains(deleter)) {
            ItemDeleter.deleters.remove(deleter);
        }
    }

    public static boolean isDeletingItem(final Player player) {
        return getPlayer(player) != null;
    }

    public void addDeletionPane(final int slot) {
        this.inventory.setItem(slot, this.getDeletionPane());
    }

    public ItemStack getDeletionPane() {
        ItemStack item = GuiItems.createPane(14);
        item = Utils.addGlow(item);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(String.valueOf(CC.RedB) + "Confirm Item Deletion:");
        if (this.item.hasItemMeta() && this.item.getItemMeta().hasLore()) {
            meta.setLore(this.item.getItemMeta().getLore());
        }
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public boolean isDeletionPane(final ItemStack item) {
        return item.equals((Object) this.getDeletionPane());
    }

    public Player getPlayer() {
        if (this.player != null) {
            Player[] onlinePlayers = Bukkit.getOnlinePlayers().toArray(new Player[0]);
            for (int length = onlinePlayers.length, i = 0; i < length; ++i) {
                final Player player = onlinePlayers[i];
                if (player.getUniqueId().equals(this.player)) {
                    return player;
                }
            }
        }
        return null;
    }

    public UUID getUUID() {
        return this.player;
    }

    public void deleteItem() {

    }
}
