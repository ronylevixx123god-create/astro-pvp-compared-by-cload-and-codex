package net.astropvp.core.guis;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.core.cosmiccoinflip.utils.ItemBuilder;
import net.astropvp.core.AstroCore;
import net.astropvp.core.utils.GUIBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChatColorGUI extends GUIBuilder {
    public ChatColorGUI(Player player) {
        super(player);
    }

    @Override
    protected String getName() {
        return CC.translate("&7Select a Color...");
    }

    @Override
    protected int getSlots() {
        return 9;
    }

    @Override
    protected void setItems() {
        for (int i = 0; i < 9; i++) {
        }
        this.inventory.setItem(0, getItems(0));
        this.inventory.setItem(1, getItems(1));
        this.inventory.setItem(2, getItems(2));
        this.inventory.setItem(3, getItems(3));

        this.inventory.setItem(4, getItems(4));
        this.inventory.setItem(5, getItems(5));
        this.inventory.setItem(6, getItems(6));
        this.inventory.setItem(7, getItems(7));
        this.inventory.setItem(8, getItems(8));
    }

    @Override
    protected ItemStack getItems(int slot) {
        if (slot == 0) {
            return ItemBuilder.buildItem(Material.STAINED_GLASS_PANE,1, (short) 14,
                    CC.translate("&c&lRed"), CC.translate("&7Click this to select the color red."));
        }
        if (slot == 1) {
            return ItemBuilder.buildItem(Material.STAINED_GLASS_PANE,1, (short) 5,
                    CC.translate("&a&lGreen"), CC.translate("&7Click this to select the color green."));
        }
        if (slot == 2) {
            return ItemBuilder.buildItem(Material.STAINED_GLASS_PANE,1, (short) 3,
                    CC.translate("&b&lBlue"), CC.translate("&7Click this to select the color blue."));
        }
        if (slot == 3) {
            return ItemBuilder.buildItem(Material.STAINED_GLASS_PANE,1, (short) 10,
                    CC.translate("&5&lPurple"), CC.translate("&7Click this to select the color purple."));
        }
        if (slot == 4) {
            return ItemBuilder.buildItem(Material.STAINED_GLASS_PANE,1, (short) 6,
                    CC.translate("&d&lPink"), CC.translate("&7Click this to select the color pink."));
        }
        if (slot == 5) {
            return ItemBuilder.buildItem(Material.STAINED_GLASS_PANE,1, (short) 4,
                    CC.translate("&e&lYellow"), CC.translate("&7Click this to select the color yellow."));
        }
        if (slot == 6) {
            return ItemBuilder.buildItem(Material.STAINED_GLASS_PANE,1, (short) 1,
                    CC.translate("&6&lOrange"), CC.translate("&7Click this to select the color orange."));
        }
        if (slot == 7) {
            return ItemBuilder.buildItem(Material.STAINED_GLASS_PANE,1, (short) 0,
                    CC.translate("&f&lWhite"), CC.translate("&7Click this to select the color white."));
        }
        if (slot == 8) {
            return ItemBuilder.buildItem(Material.STAINED_GLASS_PANE,1, (short) 15,
                    CC.translate("&8&lBlack"), CC.translate("&7Click this to select the color black."));
        }
        return null;
    }

    @Override
    public void click(InventoryClickEvent event) {
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
        switch (event.getSlot()) {
            case 0:
                setColor(player, ChatColor.RED);
                setItems();
                break;
            case 1:
                setColor(player, ChatColor.GREEN);
                setItems();
                break;
            case 2:
                setColor(player, ChatColor.AQUA);
                setItems();
                break;
            case 3:
                setColor(player, ChatColor.DARK_PURPLE);
                setItems();
                break;
            case 4:
                setColor(player, ChatColor.LIGHT_PURPLE);
                setItems();
                break;
            case 5:
                setColor(player, ChatColor.YELLOW);
                setItems();
                break;
            case 6:
                setColor(player, ChatColor.GOLD);
                setItems();
                break;
            case 7:
                setColor(player, ChatColor.WHITE);
                setItems();
                break;
            case 8:
                setColor(player, ChatColor.BLACK);
                setItems();
                break;
        }
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    public void setColor(Player player, ChatColor color) {
        AstroCore.getInstance().getAstroPlayerManager().getAstroPlayer(player.getUniqueId()).setChatColor(color);
        player.sendMessage(CC.translate("&aYou changed your chat color..."));
    }


}
