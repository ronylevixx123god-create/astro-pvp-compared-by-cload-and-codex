package net.astropvp.gambling.crash.listeners;


import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import  net.astropvp.gambling.crash.CrashAPI;
import  net.astropvp.gambling.crash.manager.CrashManager;
import net.astropvp.gambling.utils.ItemBuilder;
import net.astropvp.gambling.utils.Pair;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import net.astropvp.gambling.GamblingPlugin;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CrashListener implements Listener {

    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private static  final DecimalFormat moneyFormat = new DecimalFormat("#,###");

    private final GamblingPlugin plugin;
    private final CrashManager crashManager;
    public CrashListener(GamblingPlugin plugin) {
        this.plugin = plugin;
        this.crashManager = plugin.getCrashManager();
    }

    @EventHandler
    public void inventoryClickCheck(InventoryClickEvent event) {

        if(!Objects.equals(event.getInventory().getTitle(), "Astro Crash")) return;
        if(event.getSlot() != 49) return;

        if(!crashManager.isActive()) return;
        Player player = (Player) event.getWhoClicked();
        if(!crashManager.hasBetted(player)) return;
        double amount = Double.parseDouble(StringUtils.chop(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).split("Cashout: ")[1]));
        if(amount <= crashManager.getFinalCrashX()) {
            double moneyBetted = crashManager.getBets().get(player);
            double cashedOutAmount = moneyBetted * amount;
            crashManager.getBets().remove(player);
            EconomyUtils.depositBalance(player, cashedOutAmount);
            player.sendMessage(CC.translate(CrashAPI.prefix + "&eYou have cashed out for &a$" + moneyFormat.format(cashedOutAmount)));
            player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 3.0F, 3.0F);
            crashManager.getCashedAt().put(player, new Pair<>(amount, moneyBetted));

            List<String> lore = new ArrayList<>();
            lore.add("&fBet: &a$" + moneyFormat.format(moneyBetted));
            lore.add("&fCashed Out: &a" + decimalFormat.format(amount) + "x");
            lore.add("&fCashed Amount: &a$" + moneyFormat.format(cashedOutAmount));
            ItemBuilder itemBuilder = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 5,
                    "&fPlayer: &a" + player.getName());
            itemBuilder.setLore(lore);
            itemBuilder.setGlowing();

            CrashAPI.getFixedCrashOpenInventoryList().forEach(loopPlayer -> {
                Inventory inventory = loopPlayer.getOpenInventory().getTopInventory();
                for(int i = 0; i < 50; i++) {
                    if(inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR) continue;
                    String toLookIn = ChatColor.stripColor(inventory.getItem(i).getItemMeta().getDisplayName());
                    if(!toLookIn.contains(":")) continue;
                    toLookIn = toLookIn.split(": ")[1];
                    if(Objects.equals(toLookIn, player.getName())) {
                        inventory.setItem(i, itemBuilder.colorize().build());
                        loopPlayer.updateInventory();
                    }
                }
            });
        }
    }
}
