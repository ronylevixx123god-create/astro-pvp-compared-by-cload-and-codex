package net.crankedpvp.slotbot.struct;

import com.cosmicpvp.cosmicutils.utils.CC;
import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;

import net.astropvp.loottable.LootTableAPI;
import net.astropvp.loottable.struct.LootTable;
import net.crankedpvp.slotbot.SlotBotAPI;
import net.crankedpvp.slotbot.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public enum SlotBotType {
    SLOT_BOT(CC.translate(""), Material.PAPER, Collections.emptyList()),
    HEROIC_SLOT_BOT("&b&kiii&r&f&l&nHeroic&d&kiii&r", Material.PAPER, Arrays.asList("", "&7This slot bot will &fnot &7give", "&7common or uncommon rewards.")),
    MASTERY_SLOT_BOT("&c&ki&r&4&lMastery&c&ki&r", Material.PAPER, Arrays.asList("", "&7This slot bot will &fnot &7give", "&7common, uncommon or rare rewards.")),
    ;

    private final String fancyName;
    private final Material material;
    private final List<String> additionalLore;

    SlotBotType(String fancyName, Material material, List<String> additionalLore) {
        this.fancyName = fancyName;
        this.material = material;
        this.additionalLore = additionalLore;
    }

    public boolean equals(ItemStack itemStack) {
        return (this == SlotBotAPI.getSlotBotType(itemStack));
    }

    public ItemStack build() {
        return this.build(1);
    }

    public ItemStack build(int amount) {
        final List<String> lore = Arrays.asList(CC.Gray + "Right-Click the Slot Bot located", CC.Gray + "at " + CC.White + "/spawn " + CC.Gray + "or use /bot then", CC.Gray + "insert (Left-Click) this ticket", CC.Gray + "into the bot");
        ItemBuilder itemBuilder = new ItemBuilder(this.getMaterial(), this.fancyName + " &6&lSlot Bot &f&lTicket");
        itemBuilder.addLoreLines(lore);
        itemBuilder.addLoreLines(this.additionalLore);
        ItemStack itemStack = itemBuilder.colorize().build();
        itemStack.setAmount(amount);
        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setString("crankedCustomItem", "slotBotTicket");
        nbtItem.setString("slotBotType", this.name());
        return SlotBotAPI.addSkin("SLOT_BOT_TICKET", nbtItem.getItem());
    }

    public ItemStack getRandomItem() {
        return LootTableAPI.getRandomLootItem(LootTable.SLOTS);
    }
}
