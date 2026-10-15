package me.ily2bb.slotbot.utils;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import me.ily2bb.slotbot.SlotBotPlugin;
import me.ily2bb.slotbot.objects.Glow;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    public static String toBase64(final Inventory inventory, final int size) {
        try {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(size);
            for (int i = 0; i < inventory.getSize(); ++i) {
                dataOutput.writeObject(inventory.getItem(i));
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Cannot into itemstacksz!", e);
        }
    }

    public static String toBase64(final ItemStack[] is, final int size) {
        final Inventory inventory = Bukkit.createInventory(null, size);
        inventory.setContents(is);
        return toBase64(inventory, size);
    }

    public static Inventory fromBase64(final String data) {
        try {
            final ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            final BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            final Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());
            for (int i = 0; i < inventory.getSize(); ++i) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }
            dataInput.close();
            return inventory;
        } catch (Exception ex) {
            return null;
        }
    }

    public static ItemStack addSkin(final String line, final ItemStack item) {
        ItemStack finalItem = item;
        final NBTWrapper wrapper = new NBTWrapper(item);
        final NBTTagCompoundBuilder compoundBuilder = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
        compoundBuilder.setString("itemSkinType", line);
        finalItem = wrapper.setNBTBase("cosmicData", compoundBuilder.get()).build();
        return finalItem;
    }

    public static boolean isInventoryFull(final Player player) {
        ItemStack[] contents;
        for (int length = (contents = player.getInventory().getContents()).length, i = 0; i < length; ++i) {
            final ItemStack item = contents[i];
            if (item == null) {
                return false;
            }
        }
        return true;
    }

    public static ItemStack getItem(final String line) {
        return fromBase64(line.split(":")[0]).getContents()[0];
    }

    public static int getPercentage(final String line) {
        final String[] split = line.split(":");
        return Integer.parseInt(split[split.length - 1]);
    }

    public static boolean isShowing(final String line) {
        final String[] split = line.split(":");
        String[] array;
        for (int length = (array = split).length, i = 0; i < length; ++i) {
            final String l = array[i];
            if (l.contentEquals("show")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBroadcasting(final String line) {
        final String[] split = line.split(":");
        String[] array;
        for (int length = (array = split).length, i = 0; i < length; ++i) {
            final String l = array[i];
            if (l.contentEquals("broadcast")) {
                return true;
            }
        }
        return false;
    }

    public static int getTotalPercentage() {
        final List<String> list = (List<String>) SlotBotPlugin.getInstance().getConfig().get("items");
        int total = 0;
        for (final String l : list) {
            total += getPercentage(l);
        }
        return total;
    }

    public static Object[] getMap() {
        final HashMap<Integer[], ItemStack> items = new HashMap<Integer[], ItemStack>();
        final List<String> list = (List<String>) SlotBotPlugin.getInstance().getConfig().get("items");
        int start = 0;
        for (final String l : list) {
            final int percentage = getPercentage(l);
            final ItemStack x = getItem(l);
            items.put(new Integer[]{start + 1, start + percentage}, x);
            start += percentage;
        }
        return new Object[]{items, start + 1};
    }

    public static ItemStack getRandomItemFromLootTable(Object[] objects) {
        HashMap<Integer[], ItemStack> map = (HashMap<Integer[], ItemStack>)objects[0];
        int random = ThreadLocalRandom.current().nextInt(1, (Integer) objects[1]);
        for (Map.Entry<Integer[], ItemStack> entry : map.entrySet()) {
            Integer[] values = entry.getKey();
            int from = values[0];
            int to = values[1];
            if (from <= random && to >= random)
                return entry.getValue();
        }
        return new ItemStack(Material.GRASS);
    }

    public static ItemStack addGlow(final ItemStack item) {
        final ItemMeta meta = item.getItemMeta();
        meta.addEnchant((Enchantment) new Glow(70), 1, true);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack removeGlow(final ItemStack item) {
        final ItemMeta meta = item.getItemMeta();
        if (meta.hasEnchant((Enchantment) new Glow(70))) {
            meta.removeEnchant((Enchantment) new Glow(70));
            item.setItemMeta(meta);
        }
        return item;
    }

    public static String getDisplayName(final ItemStack item) {
        String itemName = (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) ? item.getItemMeta().getDisplayName() : StringUtils.capitaliseAllWords(item.getType().name().toLowerCase().replace("_", " "));
        if (item.getType() == Material.ENCHANTED_BOOK) {
            itemName = "Depth Strider Enchantment Book";
        }
        return itemName;
    }
}