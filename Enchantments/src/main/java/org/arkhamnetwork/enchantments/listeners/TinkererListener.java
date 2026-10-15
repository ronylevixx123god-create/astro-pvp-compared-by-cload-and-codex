package org.arkhamnetwork.enchantments.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import org.arkhamnetwork.enchantments.util.ItemUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class TinkererListener implements Listener {
    private static final int tradeInventorySize = 54;
    protected static double modifier;
    protected static ItemStack divider;
    protected static ItemStack gray_button;
    protected static ItemStack green_button;

    static {
        TinkererListener.modifier = 4.0;
    }

    String tinkererName;

    public TinkererListener() {
        this.tinkererName = "Astro Tinkerer";
        TinkererListener.divider = ItemUtils.buildItem(Material.THIN_GLASS, (short) 0, ChatColor.WHITE + " ", new String[]{""});
        TinkererListener.gray_button = ItemUtils.buildItem(Material.STAINED_GLASS_PANE, (short) 14, ChatColor.YELLOW.toString() + "Click to ACCEPT Trade", new String[]{""});
        TinkererListener.green_button = ItemUtils.buildItem(Material.STAINED_GLASS_PANE, (short) 5, ChatColor.GREEN.toString() + "Trade ACCEPTED.", new String[]{ChatColor.GRAY.toString() + "Modify the trade to unaccept."});
    }

    public static Inventory buildTinkererWindow(final Player p1) {
        final Inventory TradeWindow = Bukkit.createInventory((InventoryHolder) null, 54, getTradeInventoryTitle(p1.getName(), "Cosmic Tinkerer"));
        for (int i = 4; i < TradeWindow.getSize(); i += 9) {
            TradeWindow.setItem(i, TinkererListener.divider);
        }
        TradeWindow.setItem(49, ItemUtils.buildItem(Material.STAINED_GLASS_PANE, (short) DyeColor.YELLOW.getWoolData(), CC.YellowB + "Deposit All", new String[]{CC.Gray + "Click to deposit all tinkerable items."}));
        TradeWindow.setItem(0, ItemUtils.buildItem(Material.STAINED_GLASS_PANE, (short) 14, ChatColor.YELLOW.toString() + "Click to ACCEPT Trade", new String[]{""}));
        TradeWindow.setItem(8, ItemUtils.buildItem(Material.STAINED_GLASS_PANE, (short) 14, ChatColor.YELLOW.toString() + "Click to ACCEPT Trade", new String[]{""}));
        return TradeWindow;
    }

    private static String getTradeInventoryTitle(String lPName, final String rPName) {
        lPName = "You";
        String title;
        for (title = "        " + lPName; title.length() + rPName.length() <= 31; title += " ") {
        }
        title += rPName;
        return (title.length() <= 32) ? title : title.substring(0, 31);
    }

    @EventHandler
    public void onPlayerRightClickTinkerer(final PlayerInteractEntityEvent e) {
        if (e.getRightClicked().hasMetadata("NPC") && e.getRightClicked() instanceof Player) {
            final Player pEntity = (Player) e.getRightClicked();
            if (pEntity.getName().equals(this.tinkererName)) {
                final Player p = e.getPlayer();
                p.openInventory(buildTinkererWindow(p));
                p.playSound(p.getLocation(), Sound.WOOD_CLICK, 1.0f, 0.8f);
                p.sendMessage(ChatColor.YELLOW + "Trading with " + ChatColor.BOLD + "Astro Tinkerer" + ChatColor.YELLOW + "...");
            }
        }
    }
}
