package me.ily2bb.slotbot;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.plugin.NBTAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.ily2bb.slotbot.utils.Utils;
import net.astropvp.loottable.LootTableAPI;
import net.astropvp.loottable.struct.LootTable;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CosmicSlotBotAPI {
    public static ItemStack createSlotBotItem() {
        final ItemStack item = new ItemStack(Material.PAPER);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lAstro Slot Bot &f&lTicket"));
        final List<String> lore = Arrays.asList(CC.Gray + "Right-Click the Roll Bot located", String.valueOf(CC.Gray) + "at " + CC.White + "/spawn " + CC.Gray + "or use /bot then", String.valueOf(CC.Gray) + "insert (Left-Click) this ticket", String.valueOf(CC.Gray) + "into the bot");
        meta.setLore((List) lore);
        item.setItemMeta(meta);
        return Utils.addSkin("SLOT_BOT_TICKET", item);
    }

    public static ItemStack createHeroicSlotBotItem() {
        ItemStack item = new ItemStack(Material.PAPER);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate("&b&kiii&f&l&nHeroic&d&kiii&r ") +  CC.GoldB + "Astro" + CC.GoldB + " Slot Bot " + CC.WhiteB + "Ticket");
        final List<String> lore = Arrays.asList(CC.Gray + "Right-Click the Slot Bot located", String.valueOf(CC.Gray) + "at " + CC.White + "/spawn " + CC.Gray + "or use /bot then", String.valueOf(CC.Gray) + "insert (Left-Click) this ticket", String.valueOf(CC.Gray) + "into the bot");
        meta.setLore((List) lore);
        item.setItemMeta(meta);
        return Utils.addSkin("SLOT_BOT_TICKET", item);
    }


    public static ItemStack getRandomSlotBotLootItem() {
        return LootTableAPI.getRandomLootItem(LootTable.SLOTS);
    }



    public static boolean isSlotbotTicket(ItemStack item) {
        List<String> skins = new ArrayList<>();
        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta() || item.getType() != Material.PAPER)
            return false;
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder cosmicData = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
        if (!cosmicData.hasKey("itemSkinType"))
            return false;
        List<String> stringSkins = new ArrayList<>(Arrays.asList(cosmicData.getString("itemSkinType").split(",")));
        for (String string : stringSkins) {
            string = string.replace("[", "");
            string = string.replace("]", "");
            string = string.replace(" ", "");
            try {
                skins.add(string);
            } catch (Exception ignored) {

            }
        }
        return skins.contains("SLOT_BOT_TICKET");
    }

    public static boolean isHeroicSlotbotTicket(ItemStack item) {
        List<String> skins = new ArrayList<>();
        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta() || item.getType() != Material.PAPER)
            return false;
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder cosmicData = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
        if (!cosmicData.hasKey("itemSkinType"))
            return false;
        List<String> stringSkins = new ArrayList<>(Arrays.asList(cosmicData.getString("itemSkinType").split(",")));
        for (String string : stringSkins) {
            string = string.replace("[", "");
            string = string.replace("]", "");
            string = string.replace(" ", "");
            try {
                skins.add(string);
            } catch (Exception ignored) {

            }
        }
        return skins.contains("SLOT_BOT_TICKET") && CC.strip(item.getItemMeta().getDisplayName()).toLowerCase().contains("heroic");
    }

    public static List<ItemStack> getSlotBotItems() {
        return LootTableAPI.getAllLootFromLootTable(LootTable.SLOTS);

    }


    public static int getSlotsInInventory(Player p) {
        int i=0;
        for (ItemStack itemStack : p.getInventory()) {
            if (isSlotbotTicket(itemStack)) {
                i+=itemStack.getAmount();
            }
        }
        return i;
    }
}
