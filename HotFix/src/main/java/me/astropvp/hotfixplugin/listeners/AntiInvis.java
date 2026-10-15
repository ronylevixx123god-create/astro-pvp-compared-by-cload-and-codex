package me.astropvp.hotfixplugin.listeners;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class AntiInvis implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDrink(PlayerInteractEvent e) {
        Action a = e.getAction();
        Player p = e.getPlayer();
        if (a != Action.RIGHT_CLICK_AIR && a != Action.RIGHT_CLICK_BLOCK)
            return;
        ItemStack itemInHand = p.getInventory().getItemInHand();
        if (itemInHand.getType() != Material.POTION)
            return;
        Potion potion = Potion.fromItemStack(itemInHand);
        if (potion.getType() != PotionType.INVISIBILITY)
            return;
        e.setCancelled(true);
        ItemStack setAir = new ItemStack(Material.AIR);
        p.getInventory().setItemInHand(setAir);
        p.updateInventory();
        Bukkit.getLogger().warning(String.valueOf(p.getName()) + " tried to consume Invisibility Potion!");
        p.sendMessage(ChatColor.YELLOW + "Invisibility Potions have been disabled on the server!");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDrinkPotion(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();
        ItemStack i = e.getItem();
        if (i.getType() != Material.POTION)
            return;
        Potion pot = Potion.fromItemStack(i);
        if (pot.getType() != PotionType.INVISIBILITY)
            return;
        e.setCancelled(true);
        ItemStack setAir = new ItemStack(Material.AIR);
        p.getInventory().setItemInHand(setAir);
        p.updateInventory();
        Bukkit.getLogger().warning(String.valueOf(p.getName()) + " tried to consume Invisibility Potion!");
        p.sendMessage(ChatColor.YELLOW + "Invisibility Potions have been disabled on the server!");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void hitSplashPotion(PotionSplashEvent e) {
        if (Potion.fromItemStack(e.getEntity().getItem()) == null)
            return;
        if (Potion.fromItemStack(e.getEntity().getItem()).getType() == PotionType.INVISIBILITY &&
                e.getEntity().getShooter() instanceof Player) {
            Player p = (Player)e.getEntity().getShooter();
            p.sendMessage(ChatColor.YELLOW + "Invisibility Potions have been disabled on the server!");
            Bukkit.getLogger().warning(String.valueOf(p.getName()) + " tried to splash Invisibility Potion!");
            e.setCancelled(true);
            e.getEntity().getLocation().getWorld().dropItem(e.getEntity().getLocation(), e.getPotion().getItem());
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onDispense(BlockDispenseEvent e) {
        ItemStack i = e.getItem();
        Material t = i.getType();
        if (t != Material.POTION)
            return;
        Potion p = Potion.fromItemStack(i);
        if (p.getType() != PotionType.INVISIBILITY)
            return;
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onLingerUse(PlayerInteractEvent e) {
        Action a = e.getAction();
        Player p = e.getPlayer();
        if (a != Action.RIGHT_CLICK_AIR && a != Action.RIGHT_CLICK_BLOCK)
            return;
        ItemStack itemInHand = p.getInventory().getItemInHand();
        if (itemInHand.getType() != Material.POTION) return;
        Potion potion = Potion.fromItemStack(itemInHand);
        if (potion.getType() != PotionType.INVISIBILITY)
            return;
        e.setCancelled(true);
        ItemStack setAir = new ItemStack(Material.AIR);
        p.getInventory().setItemInHand(setAir);
        p.updateInventory();
        Bukkit.getLogger().warning(String.valueOf(p.getName()) + " tried to splash Lingering Invisibility Potion!");
        p.sendMessage(ChatColor.YELLOW + "Invisibility Potions have been disabled on the server!");
    }


}
