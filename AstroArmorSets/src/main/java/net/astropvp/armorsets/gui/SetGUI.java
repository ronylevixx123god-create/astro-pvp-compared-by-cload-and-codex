package net.astropvp.armorsets.gui;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.armorsets.AstroArmorSetsAPI;
import net.astropvp.armorsets.struct.ArmorSet;
import net.astropvp.armorsets.struct.ArmorSetType;
import net.astropvp.armorsets.struct.ArmorSetWeapon;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SetGUI {

    private final Player player;
    private final String title;

    public SetGUI(Player player, String title) {
        this.player = player;
        this.title = title;
    }

    public void openBaseGUI() {
        player.closeInventory();
        int size = (int) (Math.ceil(ArmorSetType.values().length / 9d) * 9);
        Inventory inventory = Bukkit.createInventory(player, size, title);

        for(ArmorSetType setType : ArmorSetType.values()) {
            if (setType.name().contains("MUTATED")) continue;
            ItemStack item = new ItemStack(setType.getDisplayItem());
            //item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(setType.getDisplay());
            meta.setLore(Collections.singletonList(CC.translate("&7Click here to preview the " + setType.name().toLowerCase() + " set.")));
            //meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
            inventory.addItem(item);
        }

        inventory.setItem(size-1, getCompass());
        player.openInventory(inventory);
    }

    public void openSubGUI(ArmorSetType setType) {
        player.closeInventory();
        player.playSound(player.getLocation(), Sound.CHEST_OPEN, 1, 1);
        Inventory inventory = Bukkit.createInventory(player, 9, title);

        for(ItemStack item : setType.getItems()) {
            inventory.addItem(item);
        }
        inventory.addItem(AstroArmorSetsAPI.createCrystal(Collections.singletonList(setType), 100));

        inventory.setItem(8, getFeather());
        player.openInventory(inventory);
    }

    public ItemStack getCompass() {
        ItemStack barrier = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta = barrier.getItemMeta();
        meta.setDisplayName(CC.translate("&c&lExit Menu"));
        meta.setLore(Collections.singletonList(CC.translate("&7Click to exit the menu")));
        barrier.setItemMeta(meta);
        return barrier;
    }

    public ItemStack getFeather() {
        ItemStack bed = new ItemStack(Material.BED, 1);
        ItemMeta meta = bed.getItemMeta();
        meta.setDisplayName(CC.translate("&c&lMain Menu"));
        meta.setLore(Collections.singletonList(CC.translate("&7Click to go to the main menu")));
        bed.setItemMeta(meta);
        return bed;
    }

}
