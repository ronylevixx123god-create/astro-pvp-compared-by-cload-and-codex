/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.NBTWrapper
 *  com.google.common.collect.Lists
 *  me.vaqxine.NetworkManager.NetworkManager
 *  net.milkbowl.vault.economy.Economy
 *  org.arkhamnetwork.Arkkit.Arkkit
 *  org.arkhamnetwork.Arkkit.net.astropvp.armorsets.commands.CommandArkReboot
 *  org.arkhamnetwork.Arkkit.patches.AntiCrash
 *  org.arkhamnetwork.Arkkit.patches.ItemID
 *  org.arkhamnetwork.Arkkit.plugins.ArkPlugins
 *  org.arkhamnetwork.Arkkit.net.astropvp.armorsets.utils.TimeUtils
 *  org.arkhamnetwork.CosmicMoneyNotes.ArkhamMoneyNotesAPI
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Material
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package me.recolance.auction.command;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicguishop.ArkhamMoneyNotesAPI;
import me.recolance.auction.Auction;
import me.recolance.auction.controller.Controller;
import me.recolance.auction.controller.Events;
import me.recolance.auction.data.AuctionBid;
import me.recolance.auction.data.SafetyToggle;
import me.recolance.auction.menu.BidManager;
import me.recolance.auction.menu.MenuRenderer;
import me.recolance.auction.menu.bidmenus.AuctionPackageMenu;
import me.recolance.auction.menu.bidmenus.BidCollectionBinMenu;
import me.recolance.auction.menu.bidmenus.BidMenu;
import me.recolance.auction.util.AuctionUtil;
import me.recolance.auction.util.Util;
import net.milkbowl.vault.economy.Economy;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.commands.CommandArkReboot;
import org.arkhamnetwork.Arkkit.patches.ItemID;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.arkhamnetwork.Arkkit.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CommandHandler
        implements CommandExecutor {
    private static Economy economy = Auction.economy;
    private static HashSet<Material> noCustomNamedMaterials;
    private static HashSet<Short> invisibilityEffectIds;

    public CommandHandler() {
        noCustomNamedMaterials = new HashSet();
        noCustomNamedMaterials.add(Material.CHEST);
        noCustomNamedMaterials.add(Material.TRAPPED_CHEST);
        noCustomNamedMaterials.add(Material.MONSTER_EGG);
        noCustomNamedMaterials.add(Material.PAPER);
        noCustomNamedMaterials.add(Material.MOB_SPAWNER);
        noCustomNamedMaterials.add(Material.EXP_BOTTLE);
        noCustomNamedMaterials.add(Material.BOOK);
        noCustomNamedMaterials.add(Material.REDSTONE_TORCH_ON);
        noCustomNamedMaterials.add(Material.REDSTONE_TORCH_OFF);
        noCustomNamedMaterials.add(Material.EYE_OF_ENDER);
        noCustomNamedMaterials.add(Material.EMERALD);
        noCustomNamedMaterials.add(Material.EMPTY_MAP);
        noCustomNamedMaterials.add(Material.PAPER);
        noCustomNamedMaterials.add(Material.INK_SACK);
        noCustomNamedMaterials.add(Material.ENCHANTED_BOOK);
        noCustomNamedMaterials.add(Material.GLOWSTONE_DUST);
        noCustomNamedMaterials.add(Material.SUGAR);
        noCustomNamedMaterials.add(Material.SULPHUR);
        noCustomNamedMaterials.add(Material.MAGMA_CREAM);
        noCustomNamedMaterials.add(Material.BONE);
        noCustomNamedMaterials.add(Material.getMaterial((int) 99));
        noCustomNamedMaterials.add(Material.getMaterial((int) 100));
        invisibilityEffectIds = new HashSet();
        invisibilityEffectIds.add((short) 8206);
        invisibilityEffectIds.add((short) 8238);
        invisibilityEffectIds.add((short) 16398);
        invisibilityEffectIds.add((short) 16430);
    }

    public static boolean canSellItem(ItemStack item) {
        double dura;
        if (item.getAmount() <= 0) {
            return false;
        }
        Material type = item.getType();
        if (type == Material.STAINED_GLASS_PANE) {
            return false;
        }
        if (!(type != Material.TRIPWIRE_HOOK || item.hasItemMeta() && item.getItemMeta().hasLore() && (((String) item.getItemMeta().getLore().get(1)).contains("by right-clicking the warchest") || ((String) item.getItemMeta().getLore().get(0)).contains(CC.Gray + "Consumed upon entering a")))) {
            return false;
        }
        if (type == Material.CHEST && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().contains("???")) {
            return false;
        }
        if (ArkhamMoneyNotesAPI.isMoneyNote(item)) {
            return false;
        }
        if (type == Material.POTION && invisibilityEffectIds.contains(item.getDurability())) {
            return false;
        }
        if (noCustomNamedMaterials.contains((Object) type) && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName() != null && !item.getItemMeta().getDisplayName().contains("\u00a7")) {
            return true;
        }
        if (ArkPlugins.ITEM_ID.getArkPlugin().isRunning() && item.getAmount() > 1 && ItemID.needsItemID((ItemStack) item)) {
            return false;
        }
        if (type == Material.POTION && Bukkit.getPluginManager().isPluginEnabled("CosmicFactionUtils") && ((dura = (double) item.getDurability()) == 8198.0 || dura == 8230.0 || dura == 8262.0 || dura == 16390.0 || dura == 16422.0 || dura == 16454.0)) {
            return false;
        }
        if (item.containsEnchantment(Enchantment.DIG_SPEED) && item.getEnchantmentLevel(Enchantment.DIG_SPEED) > 5) {
            return false;
        }
        if (Util.itemLoreContains(item, "mcMMO Ability Tool")) {
            return false;
        }
        if (CommandHandler.isItemExpired(null, item)) {
            return false;
        }
        if (noCustomNamedMaterials.contains((Object) type) && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName() != null && !item.getItemMeta().getDisplayName().contains("\u00a7")) {
            return false;
        }
        return type == Material.AIR || !new NBTWrapper(item).getString("cosmicType").startsWith("dungeon");
    }

    public static boolean handleAuction(Player player) {
        MenuRenderer.openAuction(player);
        return true;
    }

    private static boolean isItemExpired(Player player, ItemStack item) {
        Long expires;
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder builder = wrapper.getCompound("conaxData");
        if (builder != null && builder.hasKey("expiration") && (expires = builder.getLong("expiration")) != null && expires > 0L && expires < System.currentTimeMillis()) {
            if (player != null) {
                player.sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot sell expired items!");
                player.sendMessage(CC.Gray + "This item expired " + CC.GrayU + TimeUtils.formatDifference((long) ((System.currentTimeMillis() - expires) / 1000L)) + CC.GrayU + " ago!");
            }
            return true;
        }
        return false;
    }

    public static boolean handleAuctionSell(Player player, String price) {
        double dura;
        Long expires;
        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            Util.message(player, "&c&l(!) &cEquip the item you want to sell in your hand.");
            return true;
        }
        if (item.getType() == Material.STAINED_GLASS_PANE) {
            Util.message(player, "&cYou cannot sell this item.");
            return true;
        }
        if (!(item.getType() != Material.TRIPWIRE_HOOK || item.hasItemMeta() && item.getItemMeta().hasLore() && (((String) item.getItemMeta().getLore().get(1)).contains("by right-clicking the warchest") || ((String) item.getItemMeta().getLore().get(0)).contains(CC.Gray + "Consumed upon entering a")))) {
            Util.message(player, "&cYou cannot sell this item.");
            return true;
        }
        if (item.getType() == Material.CHEST && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().contains("???")) {
            Util.message(player, "&cYou cannot sell this item.");
            return true;
        }
        if (ArkhamMoneyNotesAPI.isMoneyNote((ItemStack) item)) {
            Util.message(player, "&cYou cannot sell this item.");
            return true;
        }
        if (item.getType() == Material.POTION && invisibilityEffectIds.contains(item.getDurability())) {
            Util.message(player, "&cYou cannot sell this item.");
            return true;
        }
        if (noCustomNamedMaterials.contains((Object) item.getType()) && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName() != null && !item.getItemMeta().getDisplayName().contains("\u00a7")) {
            Util.message(player, "&cYou cannot sell this item.");
            return true;
        }
        if (CommandHandler.isItemExpired(player, item)) {
            return true;
        }
        if (ArkPlugins.ITEM_ID.getArkPlugin().isRunning() && item.getAmount() > 1 && ItemID.needsItemID((ItemStack) item)) {
            Util.message(player, "&cThe max quantity per listing for this item is 1.");
            return true;
        }
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder builder = wrapper.getCompound("conaxData");
        if (builder != null && builder.hasKey("expiration") && (expires = builder.getLong("expiration")) != null && expires > 0L && expires < System.currentTimeMillis()) {
            player.sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot sell expired items!");
            player.sendMessage(CC.Gray + "This item expired " + CC.GrayU + TimeUtils.formatDifference((long) ((System.currentTimeMillis() - expires) / 1000L)) + CC.GrayU + " ago!");
            return false;
        }
        if (item.getType() == Material.POTION && Bukkit.getPluginManager().isPluginEnabled("CosmicFactionUtils") && ((dura = (double) item.getDurability()) == 8198.0 || dura == 8230.0 || dura == 8262.0 || dura == 16390.0 || dura == 16422.0 || dura == 16454.0)) {
            Util.message(player, "&cYou cannot sell this item.");
            return true;
        }
        if (item.containsEnchantment(Enchantment.DIG_SPEED) && item.getEnchantmentLevel(Enchantment.DIG_SPEED) > 5) {
            Util.message(player, "&cYou cannot sell this item.");
            return true;
        }
        if (Util.itemLoreContains(item, "mcMMO Ability Tool") || new NBTWrapper(item).getString("cosmicType").startsWith("dungeon")) {
            Util.message(player, "&cYou cannot sell this item.");
            return true;
        }
        if (Auction.plugin.getConfig().getBoolean("auction.preventCreativeSale") && player.getGameMode() == GameMode.CREATIVE) {
            Util.message(player, "&cYou cannot sell items while in creative.");
        } else if (!AuctionUtil.canSellItemPermission(player)) {
            Util.message(player, "&cYou cannot list more than " + AuctionUtil.getCurrentlySellingAmount(player) + " item(s) at once.");
            Util.message(player, "&7Purchase ranks with &c/buy&7 to list more items concurrently.");
        } else if (AuctionUtil.getItemsToCollectAmount(player) >= 6) {
            Util.message(player, "&cYou have too many items in your Collection Bin.");
            Util.message(player, "&7Claim those items before they vanish, &c/ah expired");
        } else if (!Util.stringIsNumerical(price) && !CommandHandler.isValidAlternatePrice(price)) {
            Util.message(player, "&cThat is not a valid price.");
        } else if (price.length() > 11) {
            Util.message(player, "&cYou can only list something for a max of $99,999,999,999.");
        } else {
            Controller.attemptSell(player, item, CommandHandler.isValidAlternatePrice(price) ? Util.getMoneyValue(price) : Double.parseDouble(price));
        }
        return true;
    }

    private static boolean isValidAlternatePrice(String priceString) {
        try {
            long money = Util.getMoneyValue(priceString);
            return money >= 0L && money <= Double.MAX_VALUE;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean handleExpired(Player player) {
        MenuRenderer.expired(player);
        return true;
    }

    public static boolean handleSelling(Player player) {
        MenuRenderer.selling(player);
        return true;
    }

    public static boolean handleHelp(Player player) {
        Controller.help(player);
        return true;
    }

    public static boolean sendNonArg(Player player, Command command) {
        if (command == Command.SELL) {
            Util.message(player, "&e/ah sell <price>");
            Util.message(player, "&7List the item you are holding on the auction house.");
        } else {
            Util.message(player, "&cUse /ah help for help.");
        }
        return true;
    }

    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String commandLabel, String[] argument) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;
        if (Auction.stoppingServer || Arkkit.get().shutting_down || CommandArkReboot.restart_task_exists) {
            Util.message(player, "&cYou cannot use the Auction House while the server is rebooting.");
            return true;
        }
        if (Events.isLoading(player)) {
            player.sendMessage(CC.RedB + "(!) " + CC.Red + "Please wait while your Auction data loads before using /ah!");
            return true;
        }
        switch (commandLabel.toLowerCase()) {
            case "ah": {
                int length = argument.length;
                if (length < 1) {
                    return CommandHandler.handleAuction(player);
                }
                switch (argument[0].toLowerCase()) {
                    case "sell": {
                        if (length != 2) {
                            return CommandHandler.sendNonArg(player, Command.SELL);
                        }
                        return CommandHandler.handleAuctionSell(player, argument[1]);
                    }
                    case "ex":
                    case "expired": {
                        if (length != 1) {
                            return CommandHandler.sendNonArg(player, Command.EXPIRED);
                        }
                        return CommandHandler.handleExpired(player);
                    }
                    case "selling":
                    case "listed": {
                        if (length != 1) {
                            return CommandHandler.sendNonArg(player, Command.SELLING);
                        }
                        return CommandHandler.handleSelling(player);
                    }
                    case "help":
                    case "commands": {
                        if (length != 1) {
                            return CommandHandler.sendNonArg(player, Command.HELP);
                        }
                        return CommandHandler.handleHelp(player);
                    }
                    case "start": {
                        return CommandHandler.handleAuction(player);
                    }
                    case "togglesafety":
                    case "toggleconfirm": {
                        this.handleToggle(player);
                        return true;
                    }
                    case "startbid":
                    case "hand":
                    case "auction":
                    case "auc": {
                        this.handleStartBid(player, argument);
                        return true;
                    }
                    case "collect": {
                        new BidCollectionBinMenu(player, Auction.getPlugin().getBidManager().getBidCollectionBin().get(player.getUniqueId())).openGUI(Auction.getPlugin());
                        return true;
                    }
                    case "bids": {
                        this.handleOpenBids(player);
                        return true;
                    }
                    case "stfu":
                    case "aucnotify": {
                        this.handleAuctionMute(player);
                        return true;
                    }
                    case "test": {
                        if (!player.isOp()) {
                            return true;
                        }
                        for (int i = 0; i < 60; ++i) {
                            AuctionBid newBid = new AuctionBid(UUID.randomUUID(), player, Lists.newArrayList(new ItemStack(Material.DIRT)), 1000L, 10L, -1L, 60);
                            Auction.getPlugin().getBidManager().getActiveAuctionBids().put(newBid.getBidID(), newBid);
                        }
                        return true;
                    }
                }
                Util.message(player, "&cType /ah help to list auction commands.");
            }
            case "bid":
            case "bids":
            case "auction":
            case "auc": {
                String arg;
                if (argument.length > 1 && ((arg = argument[0]).equalsIgnoreCase("stfu") || arg.equalsIgnoreCase("aucnotify"))) {
                    this.handleToggle(player);
                    return true;
                }
                this.handleOpenBids(player);
            }
        }
        return false;
    }

    public void handleAuctionMute(Player player) {
        if (Auction.getPlugin().getToggles().isBidMessageToggled(player)) {
            Auction.getPlugin().getToggles().getNotifytoggles().remove(player.getUniqueId().toString());
            player.sendMessage((Object) ChatColor.GREEN + ChatColor.BOLD.toString() + "(!) " + (Object) ChatColor.GREEN + "You have toggled Auction Bid Notifications: " + (Object) ChatColor.GREEN + ChatColor.BOLD.toString() + "ON");
        } else {
            Auction.getPlugin().getToggles().getNotifytoggles().add(player.getUniqueId().toString());
            player.sendMessage((Object) ChatColor.GREEN + ChatColor.BOLD.toString() + "(!) " + (Object) ChatColor.GREEN + "You have toggled Auction Bid Notifications: " + (Object) ChatColor.RED + ChatColor.BOLD.toString() + "OFF");
        }
    }

    public void handleOpenBids(Player player) {
        List<ItemStack> toCollect = Auction.getPlugin().getBidManager().getBidCollectionBin().get(player.getUniqueId());
        if (toCollect != null && !toCollect.isEmpty()) {
            player.sendMessage((Object) ChatColor.GRAY + "You have items awaiting collection in your " + CC.Green + "/ah collect!");
            return;
        }
        new BidMenu(player).openGUI(Auction.getPlugin());
    }

    public void handleStartBid(Player player, String[] arguments) {
        Long buyoutPrice;
        if (arguments.length < 3) {
            player.sendMessage(CC.Red + "Invalid usage: /ah startbid <startPrice> <incrementPrice> [buyoutPrice]");
            return;
        }
        List<ItemStack> toCollect = Auction.getPlugin().getBidManager().getBidCollectionBin().get(player.getUniqueId());
        if (toCollect != null && !toCollect.isEmpty()) {
            player.sendMessage((Object) ChatColor.GRAY + "You have items awaiting collection in your " + CC.Green + "/ah collect!");
            return;
        }
        Set<AuctionBid> currentids = Auction.getPlugin().getBidManager().getCurrentBids().get(player.getUniqueId());
        if (currentids != null && currentids.size() >= 3) {
            player.sendMessage(CC.RedB + "You can only have 3 Bid Auctions running at a time!");
            return;
        }
        Long startPrice = this.parseLongOrNull(arguments[1]);
        Long incrementPrice = this.parseLongOrNull(arguments[2]);
        Long l = buyoutPrice = arguments.length > 3 ? this.parseLongOrNull(arguments[3]) : null;
        if (startPrice == null || startPrice < 1L) {
            player.sendMessage(CC.RedB + "(!) " + CC.Red + "You must enter a valid starting bid price!");
            return;
        }
        if (incrementPrice == null || incrementPrice < 5L) {
            player.sendMessage((Object) ChatColor.RED + "You must enter a valid bid increment amount!");
            return;
        }
        if (buyoutPrice == null && arguments.length > 3 || buyoutPrice != null && buyoutPrice <= startPrice) {
            player.sendMessage((Object) ChatColor.RED + "You must enter a valid buyout price greater than the starting bid!");
            return;
        }
        if (incrementPrice > 250000L) {
            player.sendMessage(CC.RedB + "You cannot have an increment price more than $250,000");
            return;
        }
        BidManager bidManager = Auction.getPlugin().getBidManager();
        AuctionPackageMenu packageMenu = new AuctionPackageMenu(player, startPrice, incrementPrice, buyoutPrice == null ? -1L : buyoutPrice, player.getItemInHand(), successItems -> {
            AuctionBid bid = new AuctionBid(UUID.randomUUID(), player, (List) successItems.getLeft(), startPrice, incrementPrice, buyoutPrice == null ? -1L : buyoutPrice, (Integer) successItems.getRight());
            bidManager.getActiveAuctionBids().put(bid.getBidID(), bid);
            bidManager.getCurrentBids().computeIfAbsent(player.getUniqueId(), aucs -> new HashSet()).add(bid);
            new BidMenu(player).openGUI(Auction.getPlugin());
            player.sendMessage(CC.GreenB + "(AH) " + CC.Gray + "You have started a bid for '" + CC.White + bid.getBidSellDisplayName() + CC.Gray + "' starting at " + CC.GreenB + "$" + CC.Green + Util.formatMoney(startPrice) + "!");
            Bukkit.getLogger().info("[AuctionHouse] " + player.getName() + " has started a bid for " + bid.getSellingItems() + " for $" + bid.getStartingBid() + "  Inc=" + bid.getBidIncrementAmount() + " buyNow=" + bid.getBuyoutAmount());
        });
        packageMenu.openGUI(Auction.getPlugin());
    }

    private Long parseLongOrNull(String val) {
        Long originalParse = Util.parseLongOrNull(val);
        if (originalParse == null) {
            try {
                originalParse = Util.getMoneyValue(val);
            } catch (Exception exception) {
                // empty catch block
            }
        }
        return originalParse;
    }

    private void handleToggle(Player pl) {
        SafetyToggle toggle = Auction.getPlugin().getToggles();
        if (toggle.isToggled(pl)) {
            toggle.getToggles().remove(pl.getUniqueId().toString());
            pl.sendMessage((Object) ChatColor.GREEN + ChatColor.BOLD.toString() + "(!) " + (Object) ChatColor.GREEN + "You have toggled Auction Item Sell Confirmation: " + (Object) ChatColor.GREEN + ChatColor.BOLD.toString() + "ON");
        } else {
            toggle.getToggles().add(pl.getUniqueId().toString());
            pl.sendMessage((Object) ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + (Object) ChatColor.RED + "You have toggled Auction Item Sell Confirmation: " + (Object) ChatColor.RED + ChatColor.BOLD.toString() + "OFF");
        }
    }
}

