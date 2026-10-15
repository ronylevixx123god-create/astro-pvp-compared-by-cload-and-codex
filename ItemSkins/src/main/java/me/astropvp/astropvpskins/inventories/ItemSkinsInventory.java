package me.astropvp.astropvpskins.inventories;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.sun.jna.platform.win32.WinDef;
import me.astropvp.astropvpskins.enums.ItemSkinType;
import me.astropvp.astropvpskins.enums.SkinType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cosmicpvp.cosmicutils.utils.CC;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import java.util.HashMap;
import java.util.Map;

public class ItemSkinsInventory {
    public static void openInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 45, CC.translate("&8Astro Item Skins"));
        HashMap<ItemSkinType, Integer> map = new HashMap<>();
        map.put(ItemSkinType.HELMET, Integer.valueOf(4));
        map.put(ItemSkinType.CHESTPLATE, Integer.valueOf(13));
        map.put(ItemSkinType.AMULET, Integer.valueOf(14));
        map.put(ItemSkinType.BACKPACK, 15);
        map.put(ItemSkinType.LEGGINGS, Integer.valueOf(22));
        map.put(ItemSkinType.BELT, Integer.valueOf(23));
        map.put(ItemSkinType.BOOTS, Integer.valueOf(31));
        map.put(ItemSkinType.BOW, Integer.valueOf(40));
        map.put(ItemSkinType.SWORD, Integer.valueOf(39));
        map.put(ItemSkinType.AXE, Integer.valueOf(41));
        map.put(ItemSkinType.PICKAXE, Integer.valueOf(42));
        for (Map.Entry<ItemSkinType, Integer> entry : map.entrySet())
            inventory.setItem(((Integer)entry.getValue()).intValue(), createItemSkinTemplate(entry.getKey()));
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null)
                inventory.setItem(i, createPane(7));
        }
        player.openInventory(inventory);
    }

    private static ItemStack createItemSkinTemplate(ItemSkinType type) {
        ItemStack item = (type == ItemSkinType.HELMET) ? new ItemStack(Material.DIAMOND_HELMET) : ((type == ItemSkinType.CHESTPLATE) ? new ItemStack(Material.DIAMOND_CHESTPLATE) : ((type == ItemSkinType.LEGGINGS) ? new ItemStack(Material.DIAMOND_LEGGINGS) : ((type == ItemSkinType.BOOTS) ? new ItemStack(Material.DIAMOND_BOOTS) : ((type == ItemSkinType.SWORD) ? new ItemStack(Material.DIAMOND_SWORD) : ((type == ItemSkinType.AXE) ? new ItemStack(Material.DIAMOND_AXE) : ((type == ItemSkinType.BOW) ? new ItemStack(Material.BOW) : ((type == ItemSkinType.PICKAXE) ? new ItemStack(Material.DIAMOND_PICKAXE) : null)))))));
        String display = (type == ItemSkinType.HELMET) ? "Helmets" : ((type == ItemSkinType.BACKPACK) ? "Backpacks": ((type == ItemSkinType.CHESTPLATE) ? "Chestplates" : ((type == ItemSkinType.LEGGINGS) ? "Leggings" : ((type == ItemSkinType.BOOTS) ? "Boots" : ((type == ItemSkinType.SWORD) ? "Swords" : ((type == ItemSkinType.AXE) ? "Axes" : ((type == ItemSkinType.BOW) ? "Bows" : ((type == ItemSkinType.AMULET) ? "Amulets" : ((type == ItemSkinType.BELT) ? "Belts" : ((type == ItemSkinType.PICKAXE) ? "Pickaxes" : ((type== ItemSkinType.BACKPACK) ? "Backpacks" : null)))))))))));
        if (item == null)
            if (type == ItemSkinType.BELT) {
                item = new ItemStack(Material.IRON_LEGGINGS);
                NBTWrapper wrapper = new NBTWrapper(item);
                NBTTagCompoundBuilder cosmicData = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
                cosmicData.setString("itemSkinType", "CUPID_BELT");
                wrapper.setNBTBase("cosmicData", cosmicData.get());
                item = wrapper.build();
            } else if (type == ItemSkinType.AMULET) {
                item = new ItemStack(Material.IRON_CHESTPLATE);
                NBTWrapper wrapper = new NBTWrapper(item);
                NBTTagCompoundBuilder cosmicData = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
                cosmicData.setString("itemSkinType", "SEXY_AMULET");
                wrapper.setNBTBase("cosmicData", cosmicData.get());
                item = wrapper.build();
            } else if (type == ItemSkinType.BACKPACK) {
                item = new ItemStack(Material.GOLD_CHESTPLATE);
                NBTWrapper wrapper = new NBTWrapper(item);
                NBTTagCompoundBuilder cosmicData = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
                cosmicData.setString("itemSkinType", "DOOMSDAY_BACKPACK_RAINBOW");
                wrapper.setNBTBase("cosmicData", cosmicData.get());
                item = wrapper.build();
            }
        ItemMeta meta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        byte b = 0;
        SkinType[] arrayOfSkinType;
        for (SkinType skinType : SkinType.values()) {
            if (!skinType.isEnabled()) continue;
            if (ItemSkinType.isSimilarItemSkin(skinType.getItemSkinType(), type)) {
                String d = ChatColor.stripColor(skinType.getName());
                d = String.valueOf(CC.GoldB) + "* " + CC.Yellow + d;
                lore.add(d);
            }
            b = (byte) (b + 1);
        }
        if (lore.size() != 0) {
            lore.add(0, String.valueOf(CC.GoldB) + "Item Skins:");
            lore.add(0, "");
            lore.add("");
            lore.add(String.valueOf(CC.Gray) + "Click to view category.");
        }
        meta.setDisplayName(String.valueOf(CC.GoldB) + display);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createPane(int id) {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)id);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName((new StringBuilder(String.valueOf(CC.DarkRedB))).toString());
        item.setItemMeta(meta);
        return item;
    }
}
