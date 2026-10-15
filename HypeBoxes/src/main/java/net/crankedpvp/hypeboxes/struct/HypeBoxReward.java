package net.crankedpvp.hypeboxes.struct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.crankedpvp.hypeboxes.utils.ItemBuilder;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter
public class HypeBoxReward {

    private final ItemStack itemStack;
    private final double chance;
    private final double value;

    public ItemStack buildWithInfo() {
        ItemBuilder itemBuilder = new ItemBuilder(itemStack.clone());
        itemBuilder.addLore("", "&fChance: &e" + chance + "%", "&fValue: &e$" + value);
        return itemBuilder.colorize().build();
    }
}