/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.ItemUtils
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.NumberUtils
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.PlayerUtil
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.TimeUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package me.ifamasssxd.cosmicmysterychest.commands;

import com.cosmicpvp.cosmicutils.utils.*;
import me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest;
import me.ifamasssxd.cosmicmysterychest.exceptions.MysteryChestException;
import me.ifamasssxd.cosmicmysterychest.exceptions.MysteryChestSuccessException;
import me.ifamasssxd.cosmicmysterychest.guis.ActiveMysteryChestGUI;
import me.ifamasssxd.cosmicmysterychest.guis.LootboxesMenu;
import me.ifamasssxd.cosmicmysterychest.struct.MysteryChest;
import net.astropvp.core.utils.GrindingMessagesUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CommandMysteryChest
        implements CommandExecutor {
    private CosmicMysteryChest plugin;

    public CommandMysteryChest(CosmicMysteryChest plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 && !sender.isOp() || sender.isOp() && args.length == 1 && args[0].equalsIgnoreCase("view")) {
            new ActiveMysteryChestGUI((Player) sender, this.plugin.getChestManager().getActiveMysteryChest()).openGUI(this.plugin);
            return true;
        }
        if (!sender.isOp()) {
            return true;
        }
        try {
            if (args.length > 0) {
                String operation = args[0];
                if (operation.equalsIgnoreCase("saveglobal")) {
                    this.plugin.getChestManager().loadGlobalMysteryChest();
                    sender.sendMessage(CC.Red + "Reloaded!");
                    return true;
                }
                if (operation.equalsIgnoreCase("reload")) {
                    this.plugin.getChestManager().loadMysteryChests();
                    this.plugin.getChestManager().loadGlobalMysteryChest();
                    this.plugin.getMysteryLootboxItem().calculateLootTable();
                    sender.sendMessage(CC.Red + "Reloaded!");
                    return true;
                }
                if (operation.equalsIgnoreCase("create") && args.length == 4 && sender instanceof Player) {
                    String name = args[1];
                    if (this.plugin.getChestManager().getMysteryChestMap().containsKey(name)) {
                        throw new MysteryChestException("Mystery Chest already exists!");
                    }
                    ItemStack handItem = this.getHandItem(sender);
                    double amount = this.getNumber(args[2]);
                    boolean isStoreLootbox = Boolean.parseBoolean(args[3]);

                    MysteryChest chest = new MysteryChest(name, (int) amount, handItem.clone(), isStoreLootbox, false);
                    this.plugin.getChestManager().getMysteryChestMap().put(name, chest);
                    this.plugin.getChestManager().saveMysteryChest(chest);
                    Bukkit.getLogger().info("Registering MysteryChest: " + chest + " Display: " + (Object) handItem + " ToGive: " + amount);
                    throw new MysteryChestSuccessException("Mystery Chest Registered as '" + name + "'");
                }
                if (operation.equalsIgnoreCase("setlootboxitem") && args.length == 2) {
                    ItemStack handItem2 = this.getHandItem(sender);
                    MysteryChest found = this.fromName(args[1]);
                    found.setHandDisplay(handItem2);
                    this.plugin.getChestManager().saveMysteryChest(found);
                    throw new MysteryChestSuccessException("Mystery Chest Display Item set as '" + ItemUtils.getDisplayName((ItemStack) handItem2) + "'");
                }
                if (operation.equalsIgnoreCase("addpermloot") && args.length == 2) {
                    MysteryChest found2 = this.fromName(args[1]);
                    ItemStack handItem = this.getHandItem(sender);
                    found2.registerGarunteed(handItem.clone());
                    this.plugin.getChestManager().saveMysteryChest(found2);
                    throw new MysteryChestSuccessException("Garunteed Chest item registered!");
                }
                if (operation.equalsIgnoreCase("addloot") && args.length >= 3 && sender instanceof Player) {
                    MysteryChest found2 = this.fromName(args[1]);
                    double chance = this.getNumber(args[2]);
                    boolean jackpot = args[3].equalsIgnoreCase("jackpot") || args[3].equalsIgnoreCase("true");
                    ItemStack handItem3 = this.getHandItem(sender);
                    found2.registerLoot(handItem3.clone(), chance, jackpot);
                    this.plugin.getChestManager().saveMysteryChest(found2);
                    throw new MysteryChestSuccessException("Chest item registered with relative chance '" + chance + "' out of " + found2.getLootTable().getTotalWeight() + " total weight.");
                }
                if (operation.equalsIgnoreCase("clear") && args.length == 2) {
                    MysteryChest found2 = this.fromName(args[1]);
                    int itemsFound = found2.getLootItems().size();
                    found2.reset();
                    this.plugin.getChestManager().saveMysteryChest(found2);
                    throw new MysteryChestSuccessException("Cleared " + itemsFound + " Mystery Item(s) from " + found2.getInternalName());
                }
                if (operation.equalsIgnoreCase("delete") && args.length == 2) {
                    MysteryChest found2 = this.fromName(args[1]);
                    this.plugin.getChestManager().removeMysteryChest(found2);
                    throw new MysteryChestSuccessException("Mystery chest DELETED!");
                }
                if (operation.equalsIgnoreCase("list")) {
                    sender.sendMessage(CC.DarkAquaB + "(!) " + CC.DarkAqua + "Listing created Mystery Chests..");
                    LootboxesMenu menu = new LootboxesMenu((Player) sender);
                    menu.getExpShopUI().open((Player)sender);
                    return true;
                }
                if (operation.equalsIgnoreCase("give") && args.length >= 3) {
                    Long time = null;
                    int amount2;
                    Player pl = Bukkit.getPlayer((String) args[1]);
                    if (pl == null) {
                        throw new MysteryChestException("No player found for " + args[1] + "!");
                    }
                    MysteryChest chest2 = this.fromName(args[2]);
                    int n = amount2 = args.length > 3 ? Integer.parseInt(args[3]) : 1;
                    Long l = args.length > 4 && args[4].length() > 2 ? Long.valueOf(args[4].equalsIgnoreCase("default") ? System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7L) : Long.parseLong(args[4])) : (time = null);
                    while (amount2-- > 0) {
                        ItemStack item = chest2.createClaimableItem(true, time);
                        PlayerUtil.giveOrDropProtectedItem((Player) pl, (ItemStack) item, (int) -1);
                        if (GrindingMessagesUtil.isPlayerIndicating(pl)) {
                            pl.sendMessage(CC.GreenB + "(!) " + CC.Green + "Lootbox Received - " + CC.White + ItemUtils.getDisplayName((ItemStack) item));
                        }
                        //sender.sendMessage(CC.Red + pl.getName() + " has received a mystery chest " + chest2.getInternalName() + " -> " + ItemUtils.getDisplayName((ItemStack) item));
                    }
                    return true;
                }
                if (operation.equalsIgnoreCase("setweeklychest") && args.length == 2) {
                    MysteryChest found2 = this.fromName(args[1]);
                    //Long expiration = this.getLong(args[2]);
                    this.plugin.getChestManager().setActiveMysteryChest(found2, 1);
                    throw new MysteryChestSuccessException("Set Active Mystery Chest to '" + found2.getInternalName());
                }
            }
        } catch (MysteryChestException | IOException ex) {
            sender.sendMessage(CC.RedB + "(!) " + CC.Red + ex.getMessage());
            return true;
        }
        sender.sendMessage(CC.AquaB + "AstroPvP Lootbox System");
        sender.sendMessage("");
        sender.sendMessage(CC.DarkAqua + "/lootbox create <internalName> <amount> <isStoreLootbox>");
        sender.sendMessage(CC.DarkAqua + "/lootbox setlootboxitem <internalName>" + CC.Gray + " Sets the display items to your held item.");
        sender.sendMessage(CC.DarkAqua + "/lootbox addloot <internalName> <chance> [jackpotItem]" + CC.Gray + " Add the item in your hand to the loot table.");
        sender.sendMessage(CC.DarkAqua + "/lootbox addpermloot <internalName>" + CC.Gray + " Adds the item in your hand to the loot table.");
        sender.sendMessage(CC.DarkAqua + "/lootbox clear <internalName>" + CC.Gray + " Clear the Lootbox of all its loot items.");
        sender.sendMessage(CC.DarkAqua + "/lootbox delete <internalName>" + CC.Gray + " Delete the Lootbox FOREVER, and its file");
        sender.sendMessage(CC.DarkAqua + "/lootbox list" + CC.Gray + " List all the created Lootboxes");
        sender.sendMessage(CC.DarkAqua + "/lootbox give <player> <internalName> [amount] [expiration]");
        sender.sendMessage(CC.DarkAqua + "/lootbox giveall <player> <internalName> [amount] [expiration]");
        sender.sendMessage(CC.DarkAqua + "/lootbox view" + CC.Gray + " View current active mystery chest menu.");
        sender.sendMessage(CC.DarkAqua + "/lootbox setweeklychest <internalName> <expiration>" + CC.Gray + " Sets the current active Lootbox as well as its expiration time");
        return false;
    }

    public double getNumber(String val) throws MysteryChestException {
        Double valid = NumberUtils.parseDoubleOrNull((String) val);
        if (valid == null) {
            throw new MysteryChestException("Invalid number for '" + val + "'");
        }
        return valid;
    }

    public Long getLong(String val) throws MysteryChestException {
        try {
            return Long.parseLong(val);
        } catch (Exception e) {
            throw new MysteryChestException("Invalid number for '" + val + "'");
        }
    }

    public ItemStack getHandItem(CommandSender sender) throws MysteryChestException {
        if (!(sender instanceof Player)) {
            throw new MysteryChestException("Must be a player!");
        }
        ItemStack hand = ((Player) sender).getItemInHand();
        if (hand == null || hand.getType() == Material.AIR) {
            throw new MysteryChestException("You must have a valid item in your hand!");
        }
        return hand;
    }

    public MysteryChest fromName(String name) throws MysteryChestException {
        MysteryChest found = this.plugin.getChestManager().getMysteryChestMap().get(name);
        if (found == null) {
            throw new MysteryChestException("No Mystery Chest found for '" + name + "'");
        }
        return found;
    }
}

