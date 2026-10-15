/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.google.common.collect.MapMaker
 *  net.milkbowl.vault.economy.Economy
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.RegisteredServiceProvider
 */
package org.arkhamnetwork.Arkkit.patches;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.milkbowl.vault.economy.Economy;
import org.apache.commons.lang.StringUtils;
import org.arkhamnetwork.Arkkit.libs.JSONMessage;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class ItemLinking
        extends ArkPlugin {
    public static boolean bragEnabled = false;
    public static String split = "[ITEM]";
    public static String bragSplit = "[BRAG]";
    //public static Map<UUID, TimedInventory> inventoryViews = new MapMaker().expireAfterWrite(10L, TimeUnit.MINUTES).makeMap();
    public static Cache<UUID, TimedInventory> inventoryViews = CacheBuilder.newBuilder().expireAfterWrite(10L, TimeUnit.MINUTES).build();
    //public static Map<UUID, UUIDStorage> recentlyAssignedIds = new MapMaker().expireAfterWrite(5L, TimeUnit.MINUTES).makeMap();
    public static Cache<UUID, UUIDStorage> recentlyAssignedIds = CacheBuilder.newBuilder().expireAfterWrite(10L, TimeUnit.MINUTES).build();
    protected static String FORMAT = "&8[&cP[PRESTIGE]&8] &8[[TITLE]&8] [PREFIX][NAME]&f: [COLOR][MESSAGE]";
    protected static boolean arkham_prestige = false;
    protected static Economy economy = null;

    public ItemLinking(Plugin p) {
        super(p);
    }

    private static void sendItemViaJSON(ItemStack is, Player p, String message) {
        JSONMessage msg = new JSONMessage("");
        msg.addItem(is, message);
        msg.sendToPlayer(p);
    }

    public static void sendItemViaJSON(String whole, ItemStack is, Player p) {
        ItemLinking.sendItemViaJSON(whole, is, p, null);
    }

    public static void sendItemViaJSON(String whole, ItemStack is, Player p, Player sender) {
        String spliterator;
        boolean bragShow = whole.toUpperCase().contains(bragSplit) && bragEnabled;
        boolean itemLinking = whole.toUpperCase().contains(split);
        if (!bragShow && !itemLinking) {
            ItemLinking.sendItemViaJSON(is, p, whole);
            return;
        }
        whole = whole.replaceAll("(?i)" + Pattern.quote(split.toLowerCase()), split).replaceAll("(?i)" + Pattern.quote(bragSplit.toLowerCase()), bragSplit);
        int index = 0;
        if (bragShow) {
            spliterator = bragSplit;
            index = whole.indexOf(spliterator);
        } else {
            spliterator = split;
            index = whole.indexOf(spliterator);
        }
        String before = whole.substring(0, index);
        String after = whole.substring(index + spliterator.length());
        if (bragShow && sender != null) {
            JSONMessage message = new JSONMessage(before.trim(), ChatColor.WHITE);
            UUID uuid = recentlyAssignedIds.asMap().computeIfAbsent(sender.getUniqueId(), e -> new UUIDStorage()).getUUID();
            ChatColor color = ChatColor.GREEN;
            message.addRunCommand(color + ChatColor.ITALIC.toString() + sender.getName() + "'s Inventory", color, "/brag " + uuid, ChatColor.GRAY + "Click to view " + CC.GrayU + sender.getName() + CC.Gray + "'s inventory!");
            message.addText(after);
            message.sendToPlayer(p);
        } else {
            ItemLinking.sendItemViaJSON(before, is, after, p);
        }
    }

    public static void sendItemViaJSON(String whole, Player p) {
        ItemLinking.sendItemViaJSON(whole, p.getItemInHand(), p);
    }

    public static void sendItemViaJSON(String before, ItemStack is, String after, Player p) {
        JSONMessage msg = new JSONMessage("");
        msg.addText(before.split(":")[0] + ":" + ChatColor.WHITE + ChatColor.stripColor(before.split(":")[1]));
        ItemLinking.addItemToMessage(msg, is);
        msg.addText(ChatColor.stripColor(after));
        msg.sendToPlayer(p);
    }

    private static void addItemToMessage(JSONMessage msg, ItemStack is) {
        HoverText text = is.hasItemMeta() && is.getItemMeta().hasDisplayName() ? ItemLinking.formatColors(is.getItemMeta().getDisplayName()) : null;
        msg.addItem(is, ChatColor.WHITE + "\u00bb " + (text != null ? ChatColor.RESET + text.getText() : StringUtils.capitaliseAllWords(is.getType().name().toLowerCase().replace("_", " "))) + ChatColor.GRAY + (is.getMaxStackSize() >= 1 ? " (x" + is.getAmount() + ")" : "") + ChatColor.WHITE + " \u00ab", text == null || text.getLastColors().isEmpty() ? ChatColor.WHITE : text.getLastColors().get(0));
    }

    public static HoverText formatColors(String s) {
        HoverText text = new HoverText();
        StringBuilder main = new StringBuilder();
        if (s.contains(" ")) {
            ArrayList<ChatColor> letterColors = new ArrayList<ChatColor>();
            for (String word : s.split(" ")) {
                boolean foundColorChar = false;
                boolean lastLetterLegit = true;
                StringBuilder newWord = new StringBuilder();
                for (char c : word.toCharArray()) {
                    if (c == '\u00a7') {
                        foundColorChar = true;
                        if (!lastLetterLegit) continue;
                        letterColors.clear();
                        lastLetterLegit = false;
                        continue;
                    }
                    if (foundColorChar) {
                        ChatColor color = ChatColor.getByChar(c);
                        if (color != null) {
                            letterColors.add(color);
                        }
                        foundColorChar = false;
                        continue;
                    }
                    for (ChatColor color : letterColors) {
                        if (color == null) continue;
                        newWord.append(color.toString());
                    }
                    newWord.append(c);
                    lastLetterLegit = true;
                }
                newWord.append(" ");
                main.append(newWord.toString());
            }
        } else {
            main.append(s);
        }
        text.setText(main.toString().trim());
        return text;
    }

    @Override
    public void enable() {
        split = this.getBukkitPlugin().getConfig().getString("language.item_linking.chat_keyword");
        FORMAT = this.getBukkitPlugin().getConfig().getString("language.item_linking.senditem_format");
        arkham_prestige = this.getBukkitPlugin().getServer().getPluginManager().isPluginEnabled("ArkhamPrestige");
        bragEnabled = this.getBukkitPlugin().getConfig().getBoolean("settings.item_linking.bragEnabled", false);
        this.setupEconomy();

    }

    @Override
    public void disable() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            pl.removeMetadata("last_senditem", this.getBukkitPlugin());
        }
        economy = null;
        this.unregisterListeners();
        this.unregisterCommands();
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider economyProvider = this.getBukkitPlugin().getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            economy = (Economy) economyProvider.getProvider();
        }
        return economy != null;
    }
}

