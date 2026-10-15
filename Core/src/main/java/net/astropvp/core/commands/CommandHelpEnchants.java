package net.astropvp.core.commands;



import java.util.Collections;
import java.util.LinkedList;

import com.cosmicpvp.cosmicutils.utils.JSONMessage;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import org.apache.commons.lang.StringUtils;
import org.arkhamnetwork.enchantments.util.EnchantmentDescriptions;
import org.arkhamnetwork.enchantments.util.TieredEnchantments;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class CommandHelpEnchants
        implements CommandExecutor {
    private LinkedList<CustomEnchantment> customEnchants = null;

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        LinkedList<CustomEnchantment> customEnchants;
        int maxPages;
        Player player = (Player)sender;
        int specificTier = -1;
        if (args.length > 0 && !StringUtils.isNumeric(args[0]) && (specificTier = this.getTierID(args[0])) == 0) {
            return true;
        }
        double PER_PAGE = 10.0;
        int page = 1;
        if (args.length >= 1) {
            if (StringUtils.isNumeric(args[args.length - 1])) {
                page = Integer.parseInt(args[args.length - 1]);
                if (page <= 0) {
                    page = 1;
                }
            } else {
                page = 1;
            }
        }
        if (page > (maxPages = (int)Math.ceil((double)(customEnchants = this.getCustomEnchants(specificTier)).size() / PER_PAGE))) {
            page = maxPages;
        }
        int startIndex = (int)((double)page * PER_PAGE - PER_PAGE);
        player.sendMessage("");
        player.sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + "Enchantment Guide " + ChatColor.AQUA + ChatColor.BOLD.toString() + "(" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + page + ChatColor.AQUA + ChatColor.BOLD.toString() + "/" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + maxPages + ChatColor.AQUA + ChatColor.BOLD.toString() + ")");
        player.sendMessage("");
        for (int i = startIndex; (double)i < (double)startIndex + PER_PAGE && i < customEnchants.size(); ++i) {
            CustomEnchantment enchant = customEnchants.get(i);
            ChatColor color = TieredEnchantments.getTierColor(enchant);
            int tier = TieredEnchantments.getEnchantmentTier(enchant.name());
            JSONMessage message = new JSONMessage(color.toString());
            message.addHoverData(color + ChatColor.BOLD.toString() + enchant.name() + ChatColor.GRAY + " [Level 1-" + enchant.getMaxLevel() + "]", ChatColor.GRAY + ChatColor.UNDERLINE.toString() + "Tier\n" + this.getTierColor(tier) + this.getTierName(tier) + "\n\n" + ChatColor.GRAY + ChatColor.UNDERLINE.toString() + "Description\n" + ChatColor.GRAY + this.getBrokenUpString(EnchantmentDescriptions.getDescription(enchant), 40, ChatColor.GRAY));
            message.sendToPlayer(player);
        }
        player.sendMessage("");
        player.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "(!) " + ChatColor.YELLOW + "Hover over an Enchantment to learn more about it.");
        player.sendMessage(ChatColor.GRAY + "Use " + ChatColor.GRAY + ChatColor.UNDERLINE.toString() + "/helpenchants <page>" + ChatColor.GRAY + " to browse other enchantments.");
        player.sendMessage("");
        return false;
    }

    private LinkedList<CustomEnchantment> getCustomEnchants() {
        if (this.customEnchants == null) {
            this.customEnchants = new LinkedList(EnchantmentAPI.getEnchantments());
            for (CustomEnchantment enchant : EnchantmentAPI.getEnchantments()) {
                if (!this.isVanillaEnchant(enchant.name())) continue;
                this.customEnchants.remove(enchant);
            }
            Collections.sort(this.customEnchants, (o1, o2) -> o1.name().compareTo(o2.name()));
        }
        return this.customEnchants;
    }

    private LinkedList<CustomEnchantment> getCustomEnchants(int tier) {
        if (tier == -1) {
            return this.getCustomEnchants();
        }
        LinkedList<CustomEnchantment> enchants = new LinkedList<CustomEnchantment>(TieredEnchantments.getAllEnchantmentsInTier(tier));
        Collections.sort(enchants, (o1, o2) -> o1.name().compareTo(o2.name()));
        return enchants;
    }

    private String getBrokenUpString(String string, int lineLength, ChatColor startLineColor) {
        if (string.length() < lineLength) {
            return string;
        }
        StringBuilder builder = new StringBuilder();
        char[] chars = string.toCharArray();
        int count = 0;
        for (int i = 0; i < chars.length; ++i) {
            char c = chars[i];
            if (count >= lineLength && (c == ' ' || c == ',')) {
                count = 0;
                builder.append("\n").append(startLineColor);
                continue;
            }
            ++count;
            builder.append(c);
        }
        return builder.toString();
    }

    private boolean isVanillaEnchant(String name) {
        try {
            if (Enchantment.getByName(name) != null) {
                return true;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return false;
    }

    private ChatColor getTierColor(int tier) {
        if (tier == 1) {
            return ChatColor.GRAY;
        }
        if (tier == 2) {
            return ChatColor.GREEN;
        }
        if (tier == 3) {
            return ChatColor.AQUA;
        }
        if (tier == 4) {
            return ChatColor.YELLOW;
        }
        if (tier == 5) {
            return ChatColor.GOLD;
        }
        if (tier == 6) {
            return ChatColor.RED;
        }
        if (tier == 7) {
            return ChatColor.LIGHT_PURPLE;
        }
        if (tier == 8) {
            return ChatColor.DARK_RED;
        }
        return ChatColor.GRAY;
    }

    private String getTierName(int tier) {
        switch (tier) {
            case 1: {
                return "Simple";
            }
            case 2: {
                return "Unique";
            }
            case 3: {
                return "Elite";
            }
            case 4: {
                return "Ultimate";
            }
            case 5: {
                return "Legendary";
            }
            case 6: {
                return "Soul";
            }
            case 7: {
                return "Heroic";
            }
            case 8: {
                return "Mastery";
            }
        }
        return "Unknown";
    }

    private int getTierID(String tierName) {
        switch (tierName.toLowerCase()) {
            case "simple": {
                return 1;
            }
            case "unique": {
                return 2;
            }
            case "elite": {
                return 3;
            }
            case "ultimate": {
                return 4;
            }
            case "legendary": {
                return 5;
            }
            case "soul": {
                return 6;
            }
            case "heroic": {
                return 7;
            }
            case "mastery": {
                return 8;
            }
        }
        return 0;
    }
}
