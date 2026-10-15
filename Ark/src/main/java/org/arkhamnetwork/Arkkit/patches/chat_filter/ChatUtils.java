/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 */
package org.arkhamnetwork.Arkkit.patches.chat_filter;

import com.google.common.collect.Lists;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatUtils {
    protected static void clearChat(Player whoCleared) {
        String clearer = whoCleared == null ? "Console" : whoCleared.getName();
        for (Player player : Arkkit.get().getServer().getOnlinePlayers()) {
            for (int i = 0; i < 75; ++i) {
                player.sendMessage(" ");
            }
            player.sendMessage(ChatColor.GRAY + "The chat has been " + ChatColor.AQUA + ChatColor.UNDERLINE + "cleared" + ChatColor.GRAY + " by " + ChatColor.YELLOW + clearer);
        }
        whoCleared.sendMessage(ChatColor.GREEN + "You have cleared the chat.");
    }

    protected static void toggleChat(Player whoToggled) {
        String toggler;
        String string = toggler = whoToggled == null ? "Console" : whoToggled.getName();
        if (!ChatFilter.isChatHalted()) {
            ChatFilter.setChatHalted(true);
            for (Player player : Arkkit.get().getServer().getOnlinePlayers()) {
                for (int i = 0; i < 75; ++i) {
                    player.sendMessage(" ");
                }
                player.sendMessage(ChatColor.GRAY + "The chat has been " + ChatColor.RED + ChatColor.UNDERLINE + "disabled" + ChatColor.GRAY + " by " + ChatColor.YELLOW + toggler);
            }
            if (whoToggled != null) {
                whoToggled.sendMessage(ChatColor.RED + "You have toggled the chat OFF.");
            }
        } else {
            ChatFilter.setChatHalted(false);
            for (Player player : Arkkit.get().getServer().getOnlinePlayers()) {
                for (int i = 0; i < 75; ++i) {
                    player.sendMessage(" ");
                }
                player.sendMessage(ChatColor.GRAY + "The chat has been " + ChatColor.GREEN + ChatColor.UNDERLINE + "enabled" + ChatColor.GRAY + " by " + ChatColor.YELLOW + toggler);
            }
            if (whoToggled != null) {
                whoToggled.sendMessage(ChatColor.GREEN + "You have toggled the chat ON.");
            }
        }
    }

    protected static boolean checkMessageForAdvertising(String message) {
        if (ChatUtils.checkMessageForIP(message)) {
            Bukkit.getLogger().info("Matched IP to " + message);
            return true;
        }
        if (ChatUtils.checkMessageForWebPattern(message)) {
            Bukkit.getLogger().info("Matched Web Pattern to " + message);
            return true;
        }
        return false;
    }

    public static boolean checkMessageForIP(String message) {
        message = message.replace(" ", "");
        Matcher regexMatcher = ChatFilter.ipPattern.matcher(message);
        while (regexMatcher.find()) {
            if (regexMatcher.group().length() == 0 || !ChatFilter.ipPattern.matcher(message).find()) continue;
            return true;
        }
        return false;
    }

    public static boolean checkMessageForWebPattern(String message) {
        message = message.toLowerCase();
        message = message.replace("dt", ".").replace(" ", "").replace("<!>", ".").replace("[!]", ".").replace("(!)", ".").replace("0", "o").replace("3", "e").replace("1", "l").replace("4", "a").replace("7", "t").replace("(", "").replace(")", "").replace("{", "").replace("}", "").replace("[", "").replace("]", "").replace(",", ".").replace(">", "").replace("<", "").replace("*", ".").replace("^", "").replace("/", "").replace("\\", "").replace("-", "").replace("dot", ".").replace("d0t", ".");
        Matcher regexMatcherurl = ChatFilter.webpattern.matcher(message);
        String quoted = Pattern.quote("www.");
        while (regexMatcherurl.find()) {
            String text = regexMatcherurl.group().trim().replaceAll(quoted, "").replaceAll("http://", "").replaceAll("https://", "");
            if (regexMatcherurl.group().length() == 0 || text.length() == 0 || !ChatFilter.webpattern.matcher(message).find() || message.toLowerCase().contains("cosmicpvp.com") && message.toLowerCase().contains("cosmicpvp.me"))
                continue;
            return true;
        }
        return false;
    }

    private static String buildPlaceholders(int length, String placeHolderChar) {
        StringBuilder placeHolderBuilder = new StringBuilder();
        for (int i = 1; i <= length; ++i) {
            placeHolderBuilder.append(placeHolderChar);
        }
        return placeHolderBuilder.toString();
    }

    private static int getUppercaseCount(String word) {
        int count = 0;
        for (int i = 0; i <= word.length() - 1; ++i) {
            if (!Character.isUpperCase(word.charAt(i))) continue;
            ++count;
        }
        return count;
    }

    public static String getFilteredMessage(String message, Player receiver, boolean filterUppercase) {
        if (receiver != null && ChatFilter.disabledFilteredPlayers.contains(receiver.getUniqueId())) {
            return ChatUtils.getFilteredSwearMessage(message, true);
        }
        String msg = ChatUtils.getFilteredSwearMessage(message, false);
        if (filterUppercase) {
            msg = ChatUtils.getFilteredUppercaseMessage(msg);
        }
        return msg;
    }

    public static String getFilteredSwearMessage(String message) {
        return ChatUtils.getFilteredSwearMessage(message, true);
    }

    public static String getFilteredSwearMessage(String message, boolean permanentFilter) {
        ArrayList<String> filters;
        String oMessage = message.startsWith(" ") ? message.substring(1) : message;
        StringBuilder newMessage = new StringBuilder();
        ArrayList<String> arrayList = filters = permanentFilter ? (ArrayList<String>) ChatFilter.permanent_bad_words : Lists.newArrayList(ChatFilter.bad_words);
        if (!permanentFilter) {
            filters.addAll(ChatFilter.permanent_bad_words);
        }
        for (String s : filters) {
            if (!message.toLowerCase().contains(s.toLowerCase())) continue;
            try {
                if (s.contains("{") || s.contains("}")) {
                    message = message.replace(s, ChatUtils.buildPlaceholders(s.length(), "*"));
                    continue;
                }
                message = Pattern.compile(s, 2).matcher(message).replaceAll(ChatUtils.buildPlaceholders(s.length(), "*"));
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
        for (String word : message.split(" ")) {
            if (newMessage.length() > 0) {
                newMessage.append(" ");
            }
            if (word.length() <= 1) {
                newMessage.append(word);
                continue;
            }
            if (permanentFilter && ChatUtils.isPermanentSwearWord(word.toLowerCase()) || !permanentFilter && ChatUtils.isSwearWord(word.toLowerCase())) {
                newMessage.append(ChatUtils.buildPlaceholders(word.length(), "*"));
                continue;
            }
            newMessage.append(word);
        }
        return newMessage.toString();
    }

    public static boolean isSwearMessage(String message) {
        StringBuilder newMessage = new StringBuilder();
        for (String s : ChatFilter.bad_words) {
            if (!message.toLowerCase().contains(s.toLowerCase())) continue;
            return true;
        }
        for (String s : ChatFilter.permanent_bad_words) {
            if (!message.toLowerCase().contains(s.toLowerCase())) continue;
            return true;
        }
        for (String word : message.split(" ")) {
            if (newMessage.length() > 0) {
                newMessage.append(" ");
            }
            if (word.length() <= 1) {
                newMessage.append(word);
                continue;
            }
            if (ChatUtils.isSwearWord(word.toLowerCase())) {
                return true;
            }
            newMessage.append(word);
        }
        return false;
    }

    protected static boolean isPermanentSwearWord(String word) {
        if (ChatFilter.permanent_bad_words.contains(word.toLowerCase())) {
            return true;
        }
        for (String sw : ChatFilter.permanent_bad_words) {
            if (!word.contains(sw)) continue;
            return true;
        }
        List<Character> delimeters = Arrays.asList('.', ',', ';', ':', '-', '_', '|', '/', '?', '(', ')', '!', '@', '#', '4', '5', '6', '7', '8', '9', '0', '`', '~', '+', '<', '>');
        String noDelimeterWord = "";
        for (char c : word.toCharArray()) {
            if (delimeters.contains(c)) continue;
            noDelimeterWord = noDelimeterWord + c;
        }
        for (String sw : ChatFilter.permanent_bad_words) {
            if (!noDelimeterWord.contains(sw)) continue;
            return true;
        }
        return false;
    }

    protected static boolean isSwearWord(String word) {
        if (ChatFilter.bad_words.contains(word.toLowerCase())) {
            return true;
        }
        for (String sw : ChatFilter.bad_words) {
            if (!word.contains(sw)) continue;
            return true;
        }
        List<Character> delimeters = Arrays.asList('.', ',', ';', ':', '-', '_', '|', '/', '?', '(', ')', '!', '@', '#', '4', '5', '6', '7', '8', '9', '0', '`', '~', '+', '<', '>');
        String noDelimeterWord = "";
        for (char c : word.toCharArray()) {
            if (delimeters.contains(c)) continue;
            noDelimeterWord = noDelimeterWord + c;
        }
        for (String sw : ChatFilter.bad_words) {
            if (!noDelimeterWord.contains(sw)) continue;
            return true;
        }
        return false;
    }

    public static String getFilteredUppercaseMessage(String message) {
        StringBuilder newMessage = new StringBuilder();
        for (String word : message.split(" ")) {
            if (newMessage.length() > 0) {
                newMessage.append(" ");
            }
            if (word.length() <= 1) {
                newMessage.append(word);
                continue;
            }
            if (ChatUtils.getUppercaseCount(word) > 3) {
                newMessage.append(word.toLowerCase());
                continue;
            }
            newMessage.append(word);
        }
        return newMessage.toString();
    }
}

