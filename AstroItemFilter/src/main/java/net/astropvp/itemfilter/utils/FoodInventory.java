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

public class FoodInventory {
    private static FoodInventory ourInstance = new FoodInventory();
    private Inventory inventory;

    public static FoodInventory getInstance() {
        return ourInstance;
    }

    public Inventory foodInventory(Player paramPlayer) {
        String str1 = ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.food-inventory.title"));
        this.inventory = Bukkit.createInventory(null, 18, str1);
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
            this.itemStackList.add(new ItemFactory("352").display("Bone").build());
            this.itemStackList.add(new ItemFactory("391").display("Carrot Item").build());
            this.itemStackList.add(new ItemFactory("86").display("Pumpkin").build());
            this.itemStackList.add(new ItemFactory("103").display("Melon Block").build());
            this.itemStackList.add(new ItemFactory("360").display("Melon").build());
            this.itemStackList.add(new ItemFactory("363").display("Raw Beef").build());
            this.itemStackList.add(new ItemFactory("364").display("Cooked Beef").build());
            this.itemStackList.add(new ItemFactory("338").display("Sugar Cane").build());
            this.itemStackList.add(new ItemFactory("81").display("Cactus").build());
            this.itemStackList.add(new ItemFactory("319").display("Pork").build());
            this.itemStackList.add(new ItemFactory("320").display("Grilled Pork").build());
            this.itemStackList.add(new ItemFactory("322").display("Golden Apple").build());
            this.itemStackList.add(new ItemFactory("361").display("Pumpkin Seeds").build());
            this.itemStackList.add(new ItemFactory("362").display("Melon Seeds").build());
            this.itemStackList.add(new ItemFactory("295").display("Seeds").build());
        }
        return this.itemStackList;
    }
}
