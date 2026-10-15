package net.astropvp.core.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.InventoryUtils;
import net.astropvp.core.AstroCore;
import net.astropvp.core.cooldown.BlessCooldown;
import net.astropvp.core.guis.ArmorColorGUI;
import net.astropvp.core.utils.ChatManager;
import org.arkhamnetwork.enchantments.libs.PlayerPotionEffects;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffectType;

public class ArmorColorCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player p = (Player) sender;

        if (!(p.hasPermission("astrocore.apollo"))) {
            p.sendMessage("");
            p.sendMessage(CC.translate("&c&l(!)&c You cannot use the \"/armorcolor\" command."));
            p.sendMessage(CC.translate("&7Purchase the apollo rank on &f&nbuy.astropvp.net&7!"));
            p.sendMessage("");
            return false;
        }
        
        if (p.getItemInHand() == null) {
            sender.sendMessage(CC.translate("&cPlease hold an item."));
            return false;
        }
        
        if (p.getItemInHand().getType() == null) {
            sender.sendMessage(CC.translate("&cPlease hold an item."));
            return false;
        }
        
        if (p.getItemInHand().getType() == Material.AIR) {
            sender.sendMessage(CC.translate("&cPlease hold an item."));
            return false;
        }


        ItemStack item = p.getItemInHand();
        Material type = item.getType();

        if (!(type == Material.LEATHER_HELMET) && !(type == Material.LEATHER_CHESTPLATE) && !(type == Material.LEATHER_LEGGINGS) && !(type == Material.LEATHER_BOOTS) ) {
            sender.sendMessage(CC.translate("&cYou need to hold a leather armor piece."));
            return false;
        }

        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = ((Player) sender).getPlayer();

        ArmorColorGUI armorColorGUI = new ArmorColorGUI(player);
        armorColorGUI.open();
        player.sendMessage(CC.translate("&aOpening the armor color gui..."));


        return false;
    }



}