package net.astropvp.core.conaxgames.staff.staffmode.staffmodules;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.astropvp.core.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter
public class StaffModule implements Listener {

    private final String itemName;
    private final Material material;
    private final int slot;
    private final String[] description;

    public ItemStack getModuleItem() {
        return new ItemBuilder(material)
                .setName("&a&l" + itemName)
                .setLore("")
                .addLore(description)
                .colorize()
                .build();
    }
}
