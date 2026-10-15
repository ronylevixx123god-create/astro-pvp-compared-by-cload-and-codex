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

public class EquipmentInventory {
    private static final EquipmentInventory ourInstance = new EquipmentInventory();
    private Inventory inventory;

    public static EquipmentInventory getInstance() {
        return ourInstance;
    }

    public Inventory equipmentInventory(Player paramPlayer) {
        String str1 = ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.equipment-inventory.title"));
        this.inventory = Bukkit.createInventory(null, 36, str1);
        int i = 0;
        List<String> localList1 = ItemFilterPlugin.getInstance().getFilterManager().getFilterUser(paramPlayer.getUniqueId()).getFilter();
        for (ItemStack localItemStack1 : getFilterItems()) {
            ItemStack localItemStack2;
            ItemMeta localItemMeta;
            String str2;
            List localList2;

            if (localList1.contains(localItemStack1.getType().name())) {
                localItemStack2 = localItemStack1;
                localItemMeta = localItemStack2.getItemMeta();
                str2 = ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.Will.ItemName").replace("{2}", ChatColor.stripColor(localItemStack1.getItemMeta().getDisplayName())));
                localList2 = ItemFilterPlugin.colorList(ItemFilterPlugin.getInstance().getConfig().getStringList("settings.Will.ItemLore"));
                localItemMeta.setDisplayName(str2);
                localItemMeta.setLore(localList2);
                localItemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                localItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
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

    private final List<ItemStack> itemStackList = new ArrayList();

    public List<ItemStack> getFilterItems() {
        if (this.itemStackList.isEmpty()) {
            this.itemStackList.add(new ItemFactory("298").display("Leather Helmet").build());
            this.itemStackList.add(new ItemFactory("306").display("Iron Helmet").build());
            this.itemStackList.add(new ItemFactory("314").display("Gold Helmet").build());
            this.itemStackList.add(new ItemFactory("310").display("Diamond Helmet").build());
            this.itemStackList.add(new ItemFactory("299").display("Leather Chestplate").build());
            this.itemStackList.add(new ItemFactory("307").display("Iron Chestplate").build());
            this.itemStackList.add(new ItemFactory("315").display("Gold Chestplate").build());
            this.itemStackList.add(new ItemFactory("311").display("Diamond Chestplate").build());
            this.itemStackList.add(new ItemFactory("300").display("Leather Leggings").build());
            this.itemStackList.add(new ItemFactory("308").display("Iron Leggings").build());
            this.itemStackList.add(new ItemFactory("316").display("Gold Leggings").build());
            this.itemStackList.add(new ItemFactory("312").display("Diamond Leggings").build());
            this.itemStackList.add(new ItemFactory("301").display("Leather Boots").build());
            this.itemStackList.add(new ItemFactory("309").display("Iron Boots").build());
            this.itemStackList.add(new ItemFactory("317").display("Gold Boots").build());
            this.itemStackList.add(new ItemFactory("313").display("Diamond Boots").build());
            this.itemStackList.add(new ItemFactory("267").display("Iron Sword").build());
            this.itemStackList.add(new ItemFactory("283").display("Gold Sword").build());
            this.itemStackList.add(new ItemFactory("276").display("Diamond Sword").build());
            this.itemStackList.add(new ItemFactory("258").display("Iron Axe").build());
            this.itemStackList.add(new ItemFactory("286").display("Gold Axe").build());
            this.itemStackList.add(new ItemFactory("279").display("Diamond Axe").build());
            this.itemStackList.add(new ItemFactory("261").display("Bow").build());
            this.itemStackList.add(new ItemFactory("292").display("Iron Hoe").build());
            this.itemStackList.add(new ItemFactory("294").display("Gold Hoe").build());
            this.itemStackList.add(new ItemFactory("293").display("Diamond Hoe").build());
            this.itemStackList.add(new ItemFactory("262").display("Arrow").build());
            this.itemStackList.add(new ItemFactory("368").display("Ender Pearl").build());
            this.itemStackList.add(new ItemFactory("373").display("Potions").build());
            this.itemStackList.add(new ItemFactory("397:3").display("Skulls/Pets").build());
        }
        return this.itemStackList;
    }
}
