package net.astropvp.itemfilter.utils;

import net.astropvp.itemfilter.ItemFilterPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainInventory {
    private static final MainInventory ourInstance = new MainInventory();
    private Inventory inventory;

    public static MainInventory getInstance() {
        return ourInstance;
    }

    public Inventory mainInventory(Player paramPlayer) {
        int i = roundUpToNine(ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.size"));
        String str1 = ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.main-inventory.title"));
        this.inventory = Bukkit.createInventory(null, i, str1);
        if ((ItemFilterPlugin.getInstance().getConfig().get("settings.main-inventory.settings.spacers") != null) &&
                (ItemFilterPlugin.getInstance().getConfig().getBoolean("settings.main-inventory.settings.spacers.enable"))) {
            String str2 = ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.main-inventory.settings.spacers.name"));
            String str3 = ItemFilterPlugin.getInstance().getConfig().getString("settings.main-inventory.settings.spacers.id");
            List localList = ItemFilterPlugin.getInstance().getConfig().getStringList("settings.main-inventory.settings.spacers.lore");
            boolean bool = ItemFilterPlugin.getInstance().getConfig().getBoolean("settings.main-inventory.settings.spacers.glow");
            ItemStack localItemStack = new ItemFactory(str3).display(str2).lore('&', localList).glow(bool).build();
            for (int j = 0; j < i; j++) {
                this.inventory.setItem(j, localItemStack);
            }
        }
        if (ItemFilterPlugin.getInstance().getConfig().get("settings.main-inventory.items") != null) {
            this.inventory.setItem(ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.items.Equipment.slot"), loadEquipment(paramPlayer));
            this.inventory.setItem(ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.items.Food.slot"), loadFood(paramPlayer));
            this.inventory.setItem(ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.items.Ores.slot"), loadOres(paramPlayer));
            this.inventory.setItem(ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.items.Potion.slot"), loadPotions(paramPlayer));
            this.inventory.setItem(ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.items.Raiding.slot"), loadRaiding(paramPlayer));
            this.inventory.setItem(ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.items.Specialty.slot"), loadSpecialty(paramPlayer));
        }
        paramPlayer.openInventory(this.inventory);
        return this.inventory;
    }

    private ItemStack loadEquipment(Player paramPlayer) {
        List<String> localList1 = ItemFilterPlugin.getInstance().getFilterManager().getFilterUser(paramPlayer.getUniqueId()).getFilter();
        List<String> itemsList = new ArrayList();
        int i = 1;
        List<String> lore = new ArrayList();

        for (ItemStack localObject : EquipmentInventory.getInstance().getFilterItems() ) {
            if (localList1.contains( localObject.getType().name())) {
                itemsList.add(ChatColor.stripColor( localObject.getItemMeta().getDisplayName()));
            }
        }

        Object localObject;
        if (!itemsList.isEmpty()) {
            i = itemsList.size();
            lore.add(" ");
            lore.add(ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.filtered-items-line").replace("{0}", String.valueOf(itemsList.size()))));
            for(String s : itemsList) {
                localObject = s;
                lore.add(ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.filtered-list-format").replace("{1}", (CharSequence) localObject)));
            }
        }
        return new ItemFactory(
                ItemFilterPlugin.getInstance().getConfig().getString("settings.main-inventory.items.Equipment.id"))
                .display('&', ItemFilterPlugin.getInstance().getConfig().getString("settings.main-inventory.items.Equipment.name"))
                .lore(lore)
                .amount(i)
                .build();
    }

    private ItemStack loadPotions(Player paramPlayer) {
        List<String> localList1 = ItemFilterPlugin.getInstance().getFilterManager().getFilterUser(paramPlayer.getUniqueId()).getFilter();
        ArrayList localArrayList1 = new ArrayList();
        int i = 1;
        ArrayList localArrayList2 = new ArrayList();
        for (ItemStack localObject : PotionInventory.getInstance().getFilterItems() ) {
            if (localList1.contains( localObject.getType().name())) {
                localArrayList1.add(ChatColor.stripColor( localObject.getItemMeta().getDisplayName()));
            }
        }
        Object localObject;
        if (!localArrayList1.isEmpty()) {
            i = localArrayList1.size();
            localArrayList2.add(" ");
            localArrayList2.add(ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.filtered-items-line").replace("{0}", String.valueOf(localArrayList1.size()))));
            for(String s : (Iterable<String>) localArrayList1) {
                localObject = s;
                localArrayList2.add(ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.filtered-list-format").replace("{1}", (CharSequence) localObject)));
            }
        }
        return new ItemFactory(ItemFilterPlugin.getInstance().getConfig().getString("settings.main-inventory.items.Potion.id")).display('&', ItemFilterPlugin.getInstance().getConfig().getString("settings.main-inventory.items.Potion.name")).lore(localArrayList2).amount(i).build();
    }

    private ItemStack loadRaiding(Player paramPlayer) {
        List<String> localList1 = ItemFilterPlugin.getInstance().getFilterManager().getFilterUser(paramPlayer.getUniqueId()).getFilter();
        ArrayList localArrayList1 = new ArrayList();
        int i = 1;
        ArrayList localArrayList2 = new ArrayList();
        for (ItemStack localObject : RaidingInventory.getInstance().getFilterItems() ) {
            if (localList1.contains( localObject.getType().name())) {
                localArrayList1.add(ChatColor.stripColor( localObject.getItemMeta().getDisplayName()));
            }
        }
        Object localObject;
        if (!localArrayList1.isEmpty()) {
            i = localArrayList1.size();
            localArrayList2.add(" ");
            localArrayList2.add(ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.filtered-items-line").replace("{0}", String.valueOf(localArrayList1.size()))));
            Iterator<String> localIterator = localList1.iterator();
            for (localIterator = localArrayList1.iterator(); localIterator.hasNext(); ) {
                localObject = localIterator.next();
                localArrayList2.add(ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.filtered-list-format").replace("{1}", (CharSequence) localObject)));
            }
        }
        return new ItemFactory(ItemFilterPlugin.getInstance().getConfig().getString("settings.main-inventory.items.Raiding.id")).display('&', ItemFilterPlugin.getInstance().getConfig().getString("settings.main-inventory.items.Raiding.name")).lore(localArrayList2).amount(i).build();
    }

    private ItemStack loadFood(Player paramPlayer) {
        List<String> localList1 = ItemFilterPlugin.getInstance().getFilterManager().getFilterUser(paramPlayer.getUniqueId()).getFilter();
        ArrayList localArrayList1 = new ArrayList();
        int i = 1;
        ArrayList localArrayList2 = new ArrayList();
        for (ItemStack localObject : FoodInventory.getInstance().getFilterItems() ) {
            if (localList1.contains( localObject.getType().name())) {
                localArrayList1.add(ChatColor.stripColor( localObject.getItemMeta().getDisplayName()));
            }
        }
        Object localObject;
        if (!localArrayList1.isEmpty()) {
            i = localArrayList1.size();
            localArrayList2.add(" ");
            localArrayList2.add(ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.filtered-items-line").replace("{0}", String.valueOf(localArrayList1.size()))));
            for(String s : (Iterable<String>) localArrayList1) {
                localObject = s;
                localArrayList2.add(ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.filtered-list-format").replace("{1}", (CharSequence) localObject)));
            }
        }
        return new ItemFactory(ItemFilterPlugin.getInstance().getConfig().getString("settings.main-inventory.items.Food.id")).display('&', ItemFilterPlugin.getInstance().getConfig().getString("settings.main-inventory.items.Food.name")).lore(localArrayList2).amount(i).build();
    }

    private ItemStack loadSpecialty(Player paramPlayer) {
        List<String> localList1 = ItemFilterPlugin.getInstance().getFilterManager().getFilterUser(paramPlayer.getUniqueId()).getFilter();
        ArrayList localArrayList1 = new ArrayList();
        int i = 1;
        ArrayList localArrayList2 = new ArrayList();
        for (ItemStack localObject : SpecialtyInventory.getInstance().getFilterItems() ) {
            if (localList1.contains( localObject.getType().name())) {
                localArrayList1.add(ChatColor.stripColor( localObject.getItemMeta().getDisplayName()));
            }
        }
        Object localObject;
        if (!localArrayList1.isEmpty()) {
            i = localArrayList1.size();
            localArrayList2.add(" ");
            localArrayList2.add(ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.filtered-items-line").replace("{0}", String.valueOf(localArrayList1.size()))));
            Iterator<String> localArrayList = localArrayList1.iterator();
            while (localArrayList.hasNext()) {
                localObject = localArrayList.next();
                localArrayList2.add(ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.filtered-list-format").replace("{1}", (CharSequence) localObject)));
            }
        }
        return new ItemFactory(ItemFilterPlugin.getInstance().getConfig().getString("settings.main-inventory.items.Specialty.id")).display('&', ItemFilterPlugin.getInstance().getConfig().getString("settings.main-inventory.items.Specialty.name")).lore(localArrayList2).amount(i).build();
    }

    private ItemStack loadOres(Player paramPlayer) {
        List<String> localList1 = ItemFilterPlugin.getInstance().getFilterManager().getFilterUser(paramPlayer.getUniqueId()).getFilter();
        ArrayList localArrayList1 = new ArrayList();
        int i = 1;
        ArrayList localArrayList2 = new ArrayList();
        for (ItemStack localObject : OresInventory.getInstance().getFilterItems() ) {
            if (localList1.contains( localObject.getType().name())) {
                localArrayList1.add(ChatColor.stripColor( localObject.getItemMeta().getDisplayName()));
            }
        }
        Object localObject;
        if (!localArrayList1.isEmpty()) {
            i = localArrayList1.size();
            localArrayList2.add(" ");
            localArrayList2.add(ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.filtered-items-line").replace("{0}", String.valueOf(localArrayList1.size()))));
            Iterator<String> localArrayList = localArrayList1.iterator();
            while (localArrayList.hasNext()) {
                localObject = localArrayList.next();
                localArrayList2.add(ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.filtered-list-format").replace("{1}", (CharSequence) localObject)));
            }
        }
        return new ItemFactory(ItemFilterPlugin.getInstance().getConfig().getString("settings.main-inventory.items.Ores.id")).display('&', ItemFilterPlugin.getInstance().getConfig().getString("settings.main-inventory.items.Ores.name")).lore(localArrayList2).amount(i).build();
    }

    public int roundUpToNine(int paramInt) {
        if (paramInt * 9 <= 9) {
            return 9;
        }
        if (paramInt * 9 <= 18) {
            return 18;
        }
        if (paramInt * 9 <= 27) {
            return 27;
        }
        if (paramInt * 9 <= 36) {
            return 36;
        }
        if (paramInt * 9 <= 45) {
            return 45;
        }
        return 54;
    }
}
