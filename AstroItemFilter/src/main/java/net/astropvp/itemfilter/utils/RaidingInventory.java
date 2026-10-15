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

public class RaidingInventory {
    private static RaidingInventory ourInstance = new RaidingInventory();
    private Inventory inventory;

    public static RaidingInventory getInstance() {
        return ourInstance;
    }

    public Inventory raidingInventory(Player paramPlayer) {
        String str1 = ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.raiding-inventory.title"));
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
            this.itemStackList.add(new ItemFactory("331").display("Redstone").build());
            this.itemStackList.add(new ItemFactory("123").display("Redstone Lamp").build());
            this.itemStackList.add(new ItemFactory("76").display("Redstone Torch").build());
            this.itemStackList.add(new ItemFactory("73").display("Redstone Ore").build());
            this.itemStackList.add(new ItemFactory("404").display("Redstone Comparator").build());
            this.itemStackList.add(new ItemFactory("356").display("Redstone Repeater").build());
            this.itemStackList.add(new ItemFactory("327").display("Lava Bucket").build());
            this.itemStackList.add(new ItemFactory("326").display("Water Bucket").build());
            this.itemStackList.add(new ItemFactory("46").display("Tnt").build());
            this.itemStackList.add(new ItemFactory("30").display("Web").build());
            this.itemStackList.add(new ItemFactory("19").display("Sponge").build());
            this.itemStackList.add(new ItemFactory("79").display("Ice").build());
            this.itemStackList.add(new ItemFactory("174").display("Packed Ice").build());
            this.itemStackList.add(new ItemFactory("23").display("Dispenser").build());
            this.itemStackList.add(new ItemFactory("158").display("Dropper").build());
            this.itemStackList.add(new ItemFactory("383").display("Spawner Eggs").build());
            this.itemStackList.add(new ItemFactory("33").display("Piston").build());
            this.itemStackList.add(new ItemFactory("29").display("Sticky Piston").build());
            this.itemStackList.add(new ItemFactory("154").display("Hopper").build());
            this.itemStackList.add(new ItemFactory("259").display("Flint And Steel").build());
        }
        return this.itemStackList;
    }
}
