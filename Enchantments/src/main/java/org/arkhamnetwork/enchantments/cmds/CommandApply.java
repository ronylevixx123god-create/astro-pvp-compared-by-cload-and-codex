package org.arkhamnetwork.enchantments.cmds;

import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.enchanting.EListener;
import org.arkhamnetwork.Arkkit.patches.combatlog.CombatLog;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.libs.AbstractPotionEffect;
import org.arkhamnetwork.enchantments.libs.PlayerPotionEffects;
import org.arkhamnetwork.enchantments.libs.SpecificCustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class CommandApply implements CommandExecutor {
    public static boolean debug = false;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            boolean op = p.isOp();
            if (op && args.length == 1) {
                Player playerToCheck = Bukkit.getPlayer(args[0]);
                for (Map.Entry<SpecificCustomEnchantment, CopyOnWriteArrayList<AbstractPotionEffect>> data : PlayerPotionEffects.getTrackedPotionEffects(playerToCheck).getEffects().entrySet()) {
                    for (AbstractPotionEffect ape : data.getValue())
                        p.sendMessage(playerToCheck.getName() + ": " + data.getKey().getEnchantment().name() + " " + data.getKey().getEnchantmentLevel() + " giving " + ape.toString());
                }
                return true;
            }
            if (op && args.length == 2) {
                debug = !debug;
                sender.sendMessage("debug => " + debug);
                return true;
            }
            if (CombatLog.inCombat(p)) {
                p.sendMessage(ChatColor.RED + "You cannot use /apply while in combat!");
                return true;
            }
            if (!op &&
                    p.hasMetadata("lastApplyCommand") &&
                    System.currentTimeMillis() - ((MetadataValue) p.getMetadata("lastApplyCommand").get(0)).asLong() < 5000L) {
                p.sendMessage(ChatColor.RED + "You can only use /apply once every 5 seconds.");
                return true;
            }
            p.setMetadata("lastApplyCommand", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), Long.valueOf(System.currentTimeMillis())));
            ArrayList<ItemStack> armor = new ArrayList<>();
            for (ItemStack i : p.getEquipment().getArmorContents())
                armor.add(i);
            for (Map.Entry<CustomEnchantment, Integer> data : (Iterable<Map.Entry<CustomEnchantment, Integer>>) EListener.getValidEnchantments((LivingEntity) p, armor).entries())
                ((CustomEnchantment) data.getKey()).applyUnequipEffect(p, ((Integer) data.getValue()).intValue());
            for (Map.Entry<CustomEnchantment, Integer> data : (Iterable<Map.Entry<CustomEnchantment, Integer>>) EListener.getValidEnchantments((LivingEntity) p, armor).entries())
                ((CustomEnchantment) data.getKey()).applyEquipEffect(p, ((Integer) data.getValue()).intValue());
            PlayerPotionEffects.applyPotionEffects(p, true);
            p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "All your custom enchantment buffs have been refreshed.");
            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 0.6F);
        }
        return true;
    }
}