package net.crankedpvp.hypeboxes.inventory;

import com.cosmicpvp.cosmicutils.utils.CC;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.crankedpvp.hypeboxes.HypeboxesPlugin;
import net.crankedpvp.hypeboxes.language.Language;
import net.crankedpvp.hypeboxes.struct.HypeBox;
import net.crankedpvp.hypeboxes.utils.GuiUtil;
import net.crankedpvp.hypeboxes.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Objects;

public class AdminHypeboxInventory {

    private final PaginatedGui lootboxMenu;

    public PaginatedGui getUI() {
        return this.lootboxMenu;
    }


    public AdminHypeboxInventory(Player player, HypeboxesPlugin plugin) {

        Map<String, HypeBox> lootboxObjectMap = plugin.getHypeBoxRegister().getSortedHypeboxes();

        this.lootboxMenu = new PaginatedGui(6, 28, "Astro Hypeboxes");
        this.lootboxMenu.setDefaultClickAction(event -> event.setCancelled(true));
        this.lootboxMenu.getFiller().fillBorder(new GuiItem(GuiUtil.createFillerItem()));
        ItemStack info = new ItemBuilder(Material.BOOK_AND_QUILL, CC.translate("&6Astro&fPvP &6Hypeboxes")).build();
        this.lootboxMenu.setItem(1, 5, new GuiItem(info));

        this.lootboxMenu.setItem(6, 1, new GuiItem(GuiUtil.createPreviousItem(), event -> {
            this.lootboxMenu.previous();
            event.setCancelled(true);
        }));
        this.lootboxMenu.setItem(6, 9, new GuiItem(GuiUtil.createNextItem(), event -> {
            this.lootboxMenu.next();
            event.setCancelled(true);
        }));

        lootboxObjectMap.entrySet().stream()
                .filter(Objects::nonNull)
                .forEach(lootbox -> {
                    ItemStack itemStack = lootbox.getValue().createHypeBox(1);
                    this.lootboxMenu.addItem(new GuiItem(itemStack, event -> {
                        if(player.hasPermission("astro.admin")) {
                            player.getInventory().addItem(itemStack);
                            player.sendMessage(CC.translate(Language.PREFIX.getMessage() + "&aYou have been given a " + lootbox.getKey() + " hypebox"));
                            player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 2.0F, 2.0F);
                        }
                    }));
                });
    }

}
