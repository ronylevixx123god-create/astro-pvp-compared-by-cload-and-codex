package net.crankedpvp.slotbot;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import de.tr7zw.nbtapi.NBTItem;

import net.astropvp.loottable.LootTableAPI;
import net.astropvp.loottable.struct.LootTable;
import net.crankedpvp.slotbot.struct.SlotBotType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SlotBotAPI {

    public static boolean isSlotBot(final ItemStack itemStack) {
        return CustomItemUtils.getCrankedCustomItem(itemStack).equals("slotBotTicket");
    }

    public static SlotBotType getSlotBotType(ItemStack itemStack) {
        if(ItemUtils.isAirOrNull(itemStack)) return null;
        if(!isSlotBot(itemStack)) return null;
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.getString("slotBotType") == null) return SlotBotType.SLOT_BOT;
        if (nbtItem.getString("slotBotType").equals("")) return  SlotBotType.SLOT_BOT;
        return SlotBotType.valueOf(nbtItem.getString("slotBotType"));
    }

    public static List<ItemStack> getRewards() {
        return LootTableAPI.getAllLootFromLootTable(LootTable.SLOTS);
    }


    public static ItemStack addSkin(final String line, final ItemStack item) {
        ItemStack finalItem = item;
        final NBTWrapper wrapper = new NBTWrapper(item);
        final NBTTagCompoundBuilder compoundBuilder = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
        compoundBuilder.setString("itemSkinType", line);
        finalItem = wrapper.setNBTBase("cosmicData", compoundBuilder.get()).build();
        return finalItem;
    }
}
