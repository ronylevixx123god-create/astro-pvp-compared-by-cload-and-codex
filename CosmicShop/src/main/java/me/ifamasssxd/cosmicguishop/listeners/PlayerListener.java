/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 *  org.bukkit.event.player.PlayerInteractEntityEvent
 */
package me.ifamasssxd.cosmicguishop.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.ifamasssxd.cosmicguishop.ArkhamMoneyNotesAPI;
import me.ifamasssxd.cosmicguishop.CosmicGUIShop;
import org.arkhamnetwork.Arkkit.patches.ExpBottles;
import org.arkhamnetwork.Arkkit.patches.ItemID;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().equals("/shop")) {
            event.setMessage(event.getMessage().replace("/shop", "/cshop"));
        }
    }

    @EventHandler
    public void onPlayerinteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Player && ((Player) event.getRightClicked()).getName().equalsIgnoreCase("Cosmic Merchant")) {
            event.getPlayer().chat("/cshop");
        }
    }

    @EventHandler
    public void onCommandPreProcess(PlayerCommandPreprocessEvent e) {
        Player pl = e.getPlayer();
        String msg = e.getMessage().toLowerCase();
        if ((msg.startsWith("/pay ") || msg.startsWith("/epay ")) && msg.split(" ").length == 3) {
            double amt = 0.0;
            try {
                amt = Double.parseDouble(msg.split(" ")[2]);
            }
            catch (Exception err) {
                pl.sendMessage((Object) ChatColor.RED + "You cannot transfer more than 1 Quadrillion with /pay.");
                e.setCancelled(true);
                return;
            }
            String payee = msg.split(" ")[1];
            if (Bukkit.getPlayer((String)payee) != null) {
                Player pl_payee = Bukkit.getPlayer((String)payee);
                if ((double)((long)(CosmicGUIShop.getEconomy().getBalance((OfflinePlayer)pl_payee) + amt)) >= Double.MAX_VALUE) {
                    pl.sendMessage((Object)ChatColor.RED + payee + " does not have enough room in their /balance to complete this transaction.");
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        Player pl;
        if (e.getRightClicked() instanceof Player && (pl = (Player)e.getRightClicked()).hasMetadata("NPC") && ChatColor.stripColor((String)pl.getName()).equalsIgnoreCase("Banker")) {
            e.setCancelled(true);
            Player ply = e.getPlayer();
            ply.sendMessage("");
            ply.sendMessage((Object)ChatColor.GRAY + "Banker: " + (Object)ChatColor.WHITE + "Use /withdraw <$> to withdraw from your /balance!");
            ply.sendMessage("");
        }
    }

    @EventHandler(priority= EventPriority.MONITOR, ignoreCancelled=true)
    public void onInventoryClick(InventoryClickEvent e) {
        if (ArkhamMoneyNotesAPI.isMoneyNote(e.getCurrentItem()) && ArkhamMoneyNotesAPI.isMoneyNote(e.getCursor())) {

            Player p = (Player)e.getWhoClicked();
            if (p.getWorld().getName().equals("world_duels")) {
                p.sendMessage(CC.translate("&c&l(!)&c You can only apply this in the world"));
                e.setCancelled(true);
                return;
            }
            e.setCancelled(true);
            ItemStack newMoneyNote = ArkhamMoneyNotesAPI.getMoneyNote(p.getName(), ArkhamMoneyNotesAPI.getMoneyNoteVal(e.getCurrentItem()) + ArkhamMoneyNotesAPI.getMoneyNoteVal(e.getCursor()));
            e.setCursor(null);
            e.setCurrentItem(newMoneyNote);
            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.2f);
        }
    }

    @EventHandler(priority=EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.hasItem() && ArkhamMoneyNotesAPI.isMoneyNote(e.getItem())) {
            e.setUseItemInHand(Event.Result.DENY);
            e.setCancelled(true);
            Player pl = e.getPlayer();
            if (pl.getWorld().getName().equals("world_duels")) {
                pl.sendMessage(CC.translate("&c&l(!)&c You can only apply this in the world"));
                e.setCancelled(true);
                return;
            }
            double bal = CosmicGUIShop.getEconomy().getBalance((OfflinePlayer)pl);
            if (bal == 1.0E15) {
                pl.sendMessage((Object)ChatColor.RED + "You already have MAX cash. (1 Quadrillion)");
                pl.sendMessage((Object)ChatColor.GRAY + "Use /withdraw to convert some cash to cosmic notes!");
                pl.updateInventory();
                return;
            }
            if (pl.getWorld().getEnvironment() == World.Environment.THE_END && !pl.getWorld().getName().equalsIgnoreCase("nebula")) {
                pl.sendMessage((Object)ChatColor.RED + "You cannot deposit money notes in The Space dimension.");
                pl.sendMessage((Object)ChatColor.GRAY + "You can still combine them by stacking them in your inventory!");
                return;
            }


            long note_val = ArkhamMoneyNotesAPI.getMoneyNoteVal(e.getItem()) * e.getItem().getAmount();

            if (bal + (double)note_val > 1.0E15) {
                pl.sendMessage((Object)ChatColor.RED + "You do not have enough space in your /balance to deposit $" + note_val + "!");
                pl.updateInventory();
                return;
            }
            pl.setItemInHand(null);
            pl.updateInventory();
            double old_bal = CosmicGUIShop.getEconomy().getBalance((OfflinePlayer)pl);
            CosmicGUIShop.getEconomy().depositPlayer((OfflinePlayer)pl, Double.valueOf(note_val));
            if (CosmicGUIShop.getEconomy().getBalance((OfflinePlayer)pl) == old_bal && old_bal + (double)note_val >= 9.99999999999999E14) {
                CosmicGUIShop.getEconomy().withdrawPlayer((OfflinePlayer)pl, old_bal);
                CosmicGUIShop.getEconomy().depositPlayer((OfflinePlayer)pl, 1.0E15);
            }
            pl.sendMessage(String.valueOf(ChatColor.GREEN.toString()) + ChatColor.BOLD.toString() + "+ $" + note_val);
            pl.playSound(pl.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
        }
    }
}



