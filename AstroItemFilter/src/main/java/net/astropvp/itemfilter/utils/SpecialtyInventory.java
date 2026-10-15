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

public class SpecialtyInventory {
    private static SpecialtyInventory ourInstance = new SpecialtyInventory();
    private Inventory inventory;

    public static SpecialtyInventory getInstance() {
        return ourInstance;
    }

    public Inventory specialtyInventory(Player paramPlayer) {
        String str1 = ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.specialty-inventory.title"));
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
            this.itemStackList.add(new ItemFactory("52").display("Monster Spawners").build());
            this.itemStackList.add(new ItemFactory("383").display("Spawner Eggs").build());
            this.itemStackList.add(new ItemFactory("421").display("Name Tag").build());
            this.itemStackList.add(new ItemFactory("54").display("Chest").build());
            this.itemStackList.add(new ItemFactory("146").display("Trapped Chest").build());
            this.itemStackList.add(new ItemFactory("130").display("Ender Chest").build());
            this.itemStackList.add(new ItemFactory("339").display("Paper").build());
            this.itemStackList.add(new ItemFactory("395").display("Empty Map").build());
            this.itemStackList.add(new ItemFactory("384").display("Exp Bottle").build());
            this.itemStackList.add(new ItemFactory("76").display("Redstone Torch").build());
            this.itemStackList.add(new ItemFactory("340").display("Book").build());
            this.itemStackList.add(new ItemFactory("351").display("Ink Sack").build());
            this.itemStackList.add(new ItemFactory("50").display("Torch").build());
            this.itemStackList.add(new ItemFactory("388").display("Emerald").build());
            this.itemStackList.add(new ItemFactory("378").display("Magma Cream").build());
            this.itemStackList.add(new ItemFactory("322").display("Golden Apple").build());
            this.itemStackList.add(new ItemFactory("264").display("Diamond").build());
            this.itemStackList.add(new ItemFactory("352").display("Bone").build());
            this.itemStackList.add(new ItemFactory("368").display("Ender Pearl").build());
            this.itemStackList.add(new ItemFactory("369").display("Blaze Rod").build());
            this.itemStackList.add(new ItemFactory("399").display("Nether Star").build());
            this.itemStackList.add(new ItemFactory("402").display("Firework Charge").build());
        }
        return this.itemStackList;
    }
}
