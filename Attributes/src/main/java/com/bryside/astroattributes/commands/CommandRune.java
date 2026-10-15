package com.bryside.astroattributes.commands;

import com.bryside.astroattributes.enums.RuneType;
import com.bryside.astroattributes.items.HolyGrail;
import com.bryside.astroattributes.items.RuneExperienceShard;
import com.bryside.astroattributes.items.RuneExtractor;
import com.bryside.astroattributes.objects.RuneCacher;
import com.bryside.astroattributes.utils.Utils;
import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class CommandRune implements CommandExecutor {
    public static Inventory getGUI() {
        Inventory inventory = Bukkit.createInventory((InventoryHolder) null, 18, CC.DarkGray + "Astro Attributes");

        int i;
        for (i = 0; i < 4; ++i) {
            inventory.setItem(i, createPane(15));
        }

        inventory.setItem(4, createRuneHelpBook());

        for (i = 5; i < 9; ++i) {
            inventory.setItem(i, createPane(15));
        }

        for (i = 0; i < inventory.getSize(); ++i) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, createUndiscovered());
            }
        }

        for (i = 0; i < RuneType.values().length; ++i) {
            ItemStack item = RuneType.values()[i].getRune().getItemStack(1, 100);
            inventory.setItem(i + 9, item);
        }

        return inventory;
    }

    public static ItemStack createPane(int x) {
        ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) x);
        ItemMeta paneMeta = pane.getItemMeta();
        paneMeta.setDisplayName(CC.Red);
        pane.setItemMeta(paneMeta);
        return pane;
    }

    public static ItemStack createRuneHelpBook() {
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta bookMeta = book.getItemMeta();
        bookMeta.setDisplayName(CC.RedB + "Titan Attributes");
        ArrayList lore = new ArrayList();
        lore.add(CC.RedB + "* " + CC.Gray + "Titan Attributes are special meta-shifting");
        lore.add(CC.Gray + "abilities that can be attached to");
        lore.add(CC.Gray + "heroic weapons, and then leveled up");
        lore.add(CC.Gray + "once you reach the required amount of exp.");
        lore.add("");
        lore.add(CC.RedB + "* " + CC.Gray + "Titan Attributes have 10 levels each.");
        lore.add(CC.Gray + "In order to level-up your Titan Attribute");
        lore.add(CC.Gray + "you must apply " + CC.Underline + "Titan Experience Shards");
        lore.add(CC.Gray + "to the weapon it is attached to, until the XP");
        lore.add(CC.Gray + "meter on the item is completely full.");
        lore.add("");
        lore.add(CC.RedB + "* " + CC.Gray + "Weapons can only have 1 Titan Attribute.");
        lore.add(CC.Gray + "Additionally, Titan Attributes " + CC.Underline + "cannot" + CC.Gray + " be applied");
        lore.add(CC.Gray + "to non-heroic (gold) weapons.");
        bookMeta.setLore(lore);
        book.setItemMeta(bookMeta);
        return book;
    }

    public static ItemStack createUndiscovered() {
        ItemStack item = createPane(14);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(CC.RedB + "???");
        ArrayList lore = new ArrayList();
        lore.add(CC.Gray + "This Titan Attribute has");
        lore.add(CC.Gray + "not been discovered yet.");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public static int getFixedValue(int value) {
        return value <= 9 ? 9 : (value <= 18 ? 18 : (value <= 27 ? 27 : (value <= 36 ? 36 : 45)));
    }

    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (!sender.hasPermission("titan.give")) {
            if (!(sender instanceof Player)) {
                return false;
            } else {
                ((Player) sender).openInventory(getGUI());
                return false;
            }
        } else {
            ItemStack item;
            if (args.length >= 1 && args[0].contentEquals("testitem") && sender instanceof Player) {
                Player player = (Player) sender;
                item = player.getItemInHand();
                RuneCacher cacher = new RuneCacher(item);
                RuneType type = cacher.getRuneType();
                String name = type != null ? type.name() : null;
                player.sendMessage("");
                player.sendMessage(CC.YellowB + "Applied Attribute:");
                player.sendMessage("level: " + cacher.getLevel() + ", exp: " + cacher.getExperience() + ", type: " + name + ", can level up: " + cacher.canLevelUp());
                player.sendMessage("");
                type = RuneType.getType(item);
                name = type != null ? type.name() : null;
                player.sendMessage(CC.YellowB + "Unapplied Attribute");
                player.sendMessage("type: " + name + ", apply: " + RuneType.getSuccessRateOfRune(item) + "%, level: " + RuneType.getLevelOfRune(item));
                player.sendMessage("");
                return false;
            } else if (args.length >= 1 && args[0].contentEquals("list") && sender instanceof Player) {
                ((Player) sender).openInventory(getGUI());
                return false;
            } else {
                int amount2;
                int amount;
                ItemStack item2;
                if (args.length >= 4 && args[0].contentEquals("givecorrupt")) {
                    amount = Integer.valueOf(args[1]);
                    amount2 = Integer.valueOf(args[2]);
                    item2 = RuneExtractor.createNewRuneExtractor(amount2);
                    item2.setAmount(amount);
                    Bukkit.getPlayer(args[3]).getInventory().addItem(new ItemStack[]{item2});
                    return false;
                } else if (args.length >= 3 && args[0].contentEquals("giveholygrail")) {
                    amount = Integer.valueOf(args[1]);
                    item = HolyGrail.createNewHolyGrail();
                    item.setAmount(amount);
                    Bukkit.getPlayer(args[2]).getInventory().addItem(new ItemStack[]{item});
                    return false;
                } else if (args.length >= 4 && args[0].contentEquals("givexp")) {
                    amount = Integer.valueOf(args[1]);
                    amount2 = Integer.valueOf(args[2]);
                    item2 = RuneExperienceShard.createNewExperienceShard(amount2);
                    item2.setAmount(amount);
                    Bukkit.getPlayer(args[3]).getInventory().addItem(new ItemStack[]{item2});
                    return false;
                } else if (args.length <= 4) {
                    if (sender.isOp()) {
                        sender.sendMessage("");
                        sender.sendMessage(CC.translate("&a&lAstro&e&lAttributes&f - &7V1.0"));
                        sender.sendMessage("/titan give <type> <amount> <level> <chance> <player>");
                        sender.sendMessage("/titan givecorrupt <amount> <success rate> <player>");
                        sender.sendMessage("/titan giveholygrail <amount> <player>");
                        sender.sendMessage("/titan givexp <amount> <amount of exp> <player>");

                        sender.sendMessage("/titan testitem");
                        sender.sendMessage("/titan list");
                        sender.sendMessage("");
                    }

                    return false;
                } else {
                    String type2 = args[1].toUpperCase();
                    amount2 = Integer.valueOf(args[2]);
                    int level = Integer.valueOf(args[3]);
                    int chance = Math.abs(Integer.valueOf(args[4]));
                    chance = chance > 100 ? 100 : chance;
                    Player player2 = Bukkit.getPlayer(args[5]);
                    RuneType runeType = RuneType.valueOf(type2);
                    ItemStack item3 = runeType.getRune().getItemStack(level, chance);
                    item3.setAmount(amount2);
                    Bukkit.getLogger().info("Gave " + player2.getName() + " " + item3.getAmount() + "x  " + runeType.name());
                    if (Utils.canPickupItems(player2)) {
                        player2.getInventory().addItem(new ItemStack[]{item3});
                        return false;
                    } else {
                        player2.getWorld().dropItem(player2.getLocation(), item3);
                        return false;
                    }
                }
            }
        }
    }
}
