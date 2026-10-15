package net.astropvp.itemfilter.utils;

import net.astropvp.itemfilter.ItemFilterPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class OresInventory {
    private static OresInventory ourInstance = new OresInventory();
    private Inventory inventory;

    public static OresInventory getInstance() {
        return ourInstance;
    }

    public Inventory oresInventory(Player paramPlayer) {
        String str1 = ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.ores-inventory.title"));
        this.inventory = Bukkit.createInventory(null, 27, str1);
        int i = 0;
        List<String> localList1 = ItemFilterPlugin.getInstance().getFilterManager().getFilterUser(paramPlayer.getUniqueId()).getFilter();
        for (ItemStack localItemStack1 : getFilterItems()) {
            ItemStack localItemStack2;
            ItemMeta localItemMeta;
            String str2;
            List localList2;
            if (localList1.contains(String.valueOf(localItemStack1.getType().name()))) {
                localItemStack2 = localItemStack1;
                localItemMeta = localItemStack2.getItemMeta();
                str2 = ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.Will.ItemName").replace("{2}", ChatColor.stripColor(localItemStack1.getItemMeta().getDisplayName())));
                localList2 = ItemFilterPlugin.colorList(ItemFilterPlugin.getInstance().getConfig().getStringList("settings.Will.ItemLore"));
                localItemMeta.setDisplayName(str2);
                localItemMeta.setLore(localList2);
                localItemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                localItemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
                localItemStack2.setItemMeta(localItemMeta);
                this.inventory.setItem(i, localItemStack2);
            } else if (!localList1.contains(String.valueOf(localItemStack1))) {
                localItemStack2 = localItemStack1;
                localItemMeta = localItemStack2.getItemMeta();
                str2 = ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.WillNot.ItemName").replace("{2}", ChatColor.stripColor(localItemStack1.getItemMeta().getDisplayName())));
                localList2 = ItemFilterPlugin.colorList(ItemFilterPlugin.getInstance().getConfig().getStringList("settings.WillNot.ItemLore"));
                localItemMeta.setDisplayName(str2);
                localItemMeta.removeEnchant(Enchantment.DURABILITY);
                localItemMeta.setLore(localList2);
                localItemStack2.setItemMeta(localItemMeta);
                this.inventory.setItem(i, localItemStack2);
            }
            i++;
        }
        paramPlayer.openInventory(this.inventory);
        return this.inventory;
    }

    private List<ItemStack> itemStackList = new ArrayList();

    public List<ItemStack> getFilterItems() {
        if (this.itemStackList.isEmpty()) {
            this.itemStackList.add(new ItemFactory("15").display("Iron Ore").build());
            this.itemStackList.add(new ItemFactory("265").display("Iron Ingot").build());
            this.itemStackList.add(new ItemFactory("42").display("Iron Block").build());
            this.itemStackList.add(new ItemFactory("14").display("Gold Ore").build());
            this.itemStackList.add(new ItemFactory("266").display("Gold Ingot").build());
            this.itemStackList.add(new ItemFactory("41").display("Gold Block").build());
            this.itemStackList.add(new ItemFactory("73").display("Redstone Ore").build());
            this.itemStackList.add(new ItemFactory("331").display("Redstone Ore").build());
            this.itemStackList.add(new ItemFactory("152").display("Redstone Block").build());
            this.itemStackList.add(new ItemFactory("21").display("Lapis Ore").build());
            this.itemStackList.add(new ItemFactory("351:4").display("Lapis Lazuli").build());
            this.itemStackList.add(new ItemFactory("22").display("Lapis Block").build());
            this.itemStackList.add(new ItemFactory("16").display("Coal Ore").build());
            this.itemStackList.add(new ItemFactory("263").display("Coal").build());
            this.itemStackList.add(new ItemFactory("173").display("Coal Block").build());
            this.itemStackList.add(new ItemFactory("129").display("Emerlad Ore").build());
            this.itemStackList.add(new ItemFactory("388").display("Emerald").build());
            this.itemStackList.add(new ItemFactory("133").display("Emerald Block").build());
            this.itemStackList.add(new ItemFactory("56").display("Diamond Ore").build());
            this.itemStackList.add(new ItemFactory("264").display("Diamond").build());
            this.itemStackList.add(new ItemFactory("57").display("Diamond Block").build());
        }
        return this.itemStackList;
    }
}
