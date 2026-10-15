/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.JSONUtils
 *  com.google.common.collect.Lists
 *  lilypad.libs.io.netty.util.internal.ConcurrentSet
 *  net.minecraft.util.com.google.gson.reflect.TypeToken
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches.chat_filter;

import com.cosmicpvp.cosmicutils.utils.JSONUtils;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import io.netty.util.internal.ConcurrentSet;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Pattern;

public class ChatFilter
        extends ArkPlugin {
    protected static final Pattern ipPattern = Pattern.compile("((?<![0-9])(?:(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[ ]?[.,-:; ][ ]?(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[ ]?[., ][ ]?(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[ ]?[., ][ ]?(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2}))(?![0-9]))");
    protected static final Pattern webpattern = Pattern.compile("[-a-zA-Z0-9@:%_\\+.~#?&//=]{2,256}\\.[a-z]{2,4}\\b(\\/[-a-zA-Z0-9@:%_\\+.~#?&//=]*)?");
    public static Set disabledFilteredPlayers = new ConcurrentSet();
    protected static List<String> bad_words = new ArrayList<String>();
    protected static List<String> permanent_bad_words = new ArrayList<String>();
    protected static List<UUID> hasntMoved = new ArrayList<UUID>();
    protected static HashMap<UUID, String> lastMessage = new HashMap();
    protected static HashMap<UUID, Long> lastMessage_time = new HashMap();
    protected static boolean block_chat_until_move = true;
    protected static boolean block_duplicate_messages = true;
    protected static boolean remove_chat_colors = true;
    private static boolean chat_haulted = false;
    private final Type token = new TypeToken<ConcurrentSet<UUID>>() {
    }.getType();

    public ChatFilter(Plugin p) {
        super(p);
    }

    public static boolean isChatHalted() {
        return chat_haulted;
    }

    protected static void setChatHalted(boolean b) {
        chat_haulted = b;
    }

    @Override
    public void enable() throws IOException {
        bad_words = this.getBukkitPlugin().getConfig().getStringList("settings.chat_filter.bad_words");
        permanent_bad_words = this.getBukkitPlugin().getConfig().getStringList("settings.chat_filter.permanent_bad_words");
        block_chat_until_move = this.getBukkitPlugin().getConfig().getBoolean("settings.chat_filter.block_chat_until_move");
        block_duplicate_messages = this.getBukkitPlugin().getConfig().getBoolean("settings.chat_filter.no_duplicates");
        remove_chat_colors = this.getBukkitPlugin().getConfig().getBoolean("settings.chat_filter.remove_chat_colors");
        if (Bukkit.getPluginManager().isPluginEnabled("ArkhamItemRename")) {
            remove_chat_colors = false;
        }
        for (String s : Arkkit.get().getConfig().getDefaults().getStringList("settings.chat_filter.bad_words")) {
            if (bad_words.contains(s)) continue;
            bad_words.add(s);
            Bukkit.getLogger().info("(CommandBlocker) Added command '" + s + "' to blocked net.astropvp.armorsets.commands list (default).");
        }
        bad_words.remove("fu");
        bad_words.remove("fk");
        bad_words.remove("muff");
        this.registerCommand(new CommandChatFilter("togglefilter", "/<command>", "Toggle filter", Lists.newArrayList("chatfilter")));
        this.registerCommand(new CommandChat("chat", "/<command>", "Chat toggling and clearing.", new ArrayList<String>(Arrays.asList("simplechat"))));
        this.registerCommand(new CommandFilter("filterchat", "/<command>", "Allows modification of world filter and censor list.", new ArrayList<String>(Arrays.asList("censor"))));
        this.registerListener(new ChatFilterListener());
        disabledFilteredPlayers = (Set) JSONUtils.fromJson("plugins/Arkkit/filterToggles.json", this.token);
        if (disabledFilteredPlayers == null) {
            disabledFilteredPlayers = new ConcurrentSet();
        }
    }

    @Override
    public void disable() throws IOException {
        chat_haulted = false;
        JSONUtils.saveJSONToFile("plugins/Arkkit/filterToggles.json", disabledFilteredPlayers, this.token);
    }
}

