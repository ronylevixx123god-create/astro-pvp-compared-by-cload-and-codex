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

public class PotionInventory {
    private static PotionInventory ourInstance = new PotionInventory();
    private Inventory inventory;

    public static PotionInventory getInstance() {
        return ourInstance;
    }

    public Inventory potionInventory(Player paramPlayer) {
        String str1 = ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.potion-inventory.title"));
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
            this.itemStackList.add(new ItemFactory("375").display("Spider Eye").build());
            this.itemStackList.add(new ItemFactory("378").display("Magma Cream").build());
            this.itemStackList.add(new ItemFactory("370").display("Ghast Tear").build());
            this.itemStackList.add(new ItemFactory("341").display("Slime Ball").build());
            this.itemStackList.add(new ItemFactory("372").display("Nether Stalk").build());
            this.itemStackList.add(new ItemFactory("382").display("Speckled Melon").build());
            this.itemStackList.add(new ItemFactory("353").display("Sugar").build());
            this.itemStackList.add(new ItemFactory("348").display("Glowstone Dust").build());
            this.itemStackList.add(new ItemFactory("89").display("Glowstone").build());
            this.itemStackList.add(new ItemFactory("396").display("Golden Carrot").build());
            this.itemStackList.add(new ItemFactory("373").display("Potions").build());
            this.itemStackList.add(new ItemFactory("374").display("Glass Bottle").build());
            this.itemStackList.add(new ItemFactory("111").display("Water Lily").build());
            this.itemStackList.add(new ItemFactory("391").display("Carrot Item").build());
            this.itemStackList.add(new ItemFactory("406").display("Quartz").build());
            this.itemStackList.add(new ItemFactory("40").display("Red Mushroom").build());
            this.itemStackList.add(new ItemFactory("260").display("Apple").build());
            this.itemStackList.add(new ItemFactory("39").display("Brown Mushroom").build());
            this.itemStackList.add(new ItemFactory("351").display("Ink Sack").build());
            this.itemStackList.add(new ItemFactory("394").display("Poisonous Potato").build());
            this.itemStackList.add(new ItemFactory("322").display("Golden Apple").build());
        }
        return this.itemStackList;
    }
}
