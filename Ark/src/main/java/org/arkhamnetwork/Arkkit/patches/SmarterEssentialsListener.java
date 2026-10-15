package org.arkhamnetwork.Arkkit.patches;

import com.cosmicpvp.cosmicutils.compat.ParamBuilder;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.earth2me.essentials.User;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.perms.Relation;
import net.ess3.api.events.NickChangeEvent;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;

import java.util.*;

final class SmarterEssentialsListener
        implements Listener {
    private final List<String> message_aliases = new ArrayList<String>(Arrays.asList("msg", "message", "tell", "whisper", "m", "emsg", "w", "t", "etell", "ewhisper"));
    private final List<String> reply_aliases = new ArrayList<String>(Arrays.asList("reply", "r", "repl"));
    private final LinkedList<String> listCommandCache = new LinkedList();
    List<String> blockedMutedCommands;
    boolean factions = false;
    boolean essentials = false;
    private long listCommandCacheExpiration = 0L;

    SmarterEssentialsListener() {
        this.blockedMutedCommands = new ArrayList<String>(Arrays.asList("bounty", "f"));
        this.factions = Bukkit.getPluginManager().isPluginEnabled("Factions");
        this.essentials = Bukkit.getPluginManager().isPluginEnabled("Essentials");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!e.getPlayer().hasPermission("essentials.god")) {
            FPlayer fp;
            if (this.essentials) {
                User u = SmarterEssentials.ess3.getUser(e.getPlayer());
                if (u.isGodModeEnabled()) {
                    u.setGodModeEnabled(false);
                }
                if (u.isSocialSpyEnabled()) {
                    u.setSocialSpyEnabled(false);
                }
            }
            if (this.factions && (fp = FactionUtils.getFPlayer(e.getPlayer())) != null && (Boolean) FactionUtils.callFPlayerMethod(fp, "isSpyingChat")) {
                FactionUtils.callFPlayerMethod(fp, "setSpyingChat", new ParamBuilder(Boolean.TYPE, false));
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        if (e.getPlayer().hasPermission("*")) {
            Bukkit.getLogger().info(e.getMessage());
            Bukkit.getLogger().info(e.getFormat());
            e.setFormat(e.getFormat().replace(ChatColor.GRAY.toString(), ChatColor.RED.toString()));
            e.setMessage(e.getMessage().replace(ChatColor.GRAY.toString(), ChatColor.RED.toString()));
            e.setFormat(e.getFormat().replace(ChatColor.WHITE.toString(), ChatColor.RED.toString()));
            e.setMessage(e.getMessage().replace(ChatColor.WHITE.toString(), ChatColor.RED.toString()));
            e.setFormat(e.getFormat().replace(ChatColor.LIGHT_PURPLE.toString(), ChatColor.RED.toString()));
            e.setMessage(e.getMessage().replace(ChatColor.LIGHT_PURPLE.toString(), ChatColor.RED.toString()));
            return;
        }
        if (e.getPlayer().hasPermission("bm.command.ban")) {
            e.setFormat(e.getFormat().replace(ChatColor.GRAY.toString(), ChatColor.AQUA.toString()));
            e.setMessage(e.getMessage().replace(ChatColor.GRAY.toString(), ChatColor.AQUA.toString()));
            e.setFormat(e.getFormat().replace(ChatColor.WHITE.toString(), ChatColor.AQUA.toString()));
            e.setMessage(e.getMessage().replace(ChatColor.WHITE.toString(), ChatColor.AQUA.toString()));
            e.setFormat(e.getFormat().replace(ChatColor.LIGHT_PURPLE.toString(), ChatColor.AQUA.toString()));
            e.setMessage(e.getMessage().replace(ChatColor.LIGHT_PURPLE.toString(), ChatColor.AQUA.toString()));
            return;
        }
        if (e.getPlayer().hasPermission("bm.command.warn")) {
            e.setFormat(e.getFormat().replace(ChatColor.GRAY.toString(), ChatColor.LIGHT_PURPLE.toString()));
            e.setMessage(e.getMessage().replace(ChatColor.GRAY.toString(), ChatColor.LIGHT_PURPLE.toString()));
            e.setFormat(e.getFormat().replace(ChatColor.WHITE.toString(), ChatColor.LIGHT_PURPLE.toString()));
            e.setMessage(e.getMessage().replace(ChatColor.WHITE.toString(), ChatColor.LIGHT_PURPLE.toString()));
            return;
        }
        if (e.getPlayer().hasPermission("setchannel.youtube") || e.getPlayer().hasPermission("setchannel.twitch")) {
            e.setFormat(e.getFormat().replace(ChatColor.GRAY.toString(), ChatColor.DARK_PURPLE.toString()));
            e.setMessage(e.getMessage().replace(ChatColor.GRAY.toString(), ChatColor.DARK_PURPLE.toString()));
            e.setFormat(e.getFormat().replace(ChatColor.WHITE.toString(), ChatColor.DARK_PURPLE.toString()));
            e.setMessage(e.getMessage().replace(ChatColor.WHITE.toString(), ChatColor.DARK_PURPLE.toString()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onVanishedPlayerItemPickup(PlayerPickupItemEvent e) {
        User u = SmarterEssentials.ess3.getUser(e.getPlayer());
        if (u.isVanished()) {
            e.setCancelled(true);
        }
    }

    private Relation getRelationWish(Faction original, Faction fac) {
        return (Relation) FactionUtils.callFactionMethod(original, "getRelationWish", new ParamBuilder(Faction.class, fac));
    }

    @EventHandler
    public void onIllegalPlayerTeleport(PlayerTeleportEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND && SmarterEssentials.factions) {
            Player p = e.getPlayer();
            Faction fpFaction = FactionUtils.getFaction(p);
            Faction locationFaction = FactionUtils.getFactionAt(e.getTo());
            Relation relation = this.getRelationWish(fpFaction, locationFaction);
            if (!(locationFaction == fpFaction || relation.isAlly() || relation.isTruce() || relation.isNeutral() || (Boolean) FactionUtils.callFactionMethod(locationFaction, "isWarZone", null) || (Boolean) FactionUtils.callFactionMethod(locationFaction, "isSafeZone", null) || ((Boolean) FactionUtils.callFactionMethod(locationFaction, "isNone", null)).booleanValue() || p.isOp())) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You cannot teleport to an enemy's territory.");
            }
        }
    }

    @EventHandler
    public void onNickChangeEvent(NickChangeEvent e) {
        if (e.getValue() != null && e.getValue().length() < 3 && !e.getController().getBase().isOp()) {
            e.setCancelled(true);
            e.getController().sendMessage(ChatColor.RED + "Error: " + ChatColor.DARK_RED + "That nickname is too short.");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent e) {
        String cmd = e.getMessage().replace("/", "").toLowerCase();
        if (cmd.contains(" ")) {
            cmd = cmd.split(" ")[0];
        }
        if (cmd.equals("list") || cmd.equals("elist") || cmd.equals("who") || cmd.equals("ewho") || cmd.equals("online") || cmd.equals("eonline") || cmd.equals("playerlist") || cmd.equals("eplayerlist")) {
            e.setCancelled(true);
            e.setMessage("/null");
            Player p = e.getPlayer();
            if (this.listCommandCache != null && System.currentTimeMillis() < this.listCommandCacheExpiration) {
                for (String m : this.listCommandCache) {
                    p.sendMessage(m);
                }
                return;
            }
            this.listCommandCache.clear();
            this.listCommandCacheExpiration = System.currentTimeMillis() + 10000L;
            int onlinePlayers = Bukkit.getOnlinePlayers().size();
            int maxPlayers = Bukkit.getMaxPlayers();
            this.listCommandCache.add("");
            this.listCommandCache.add(ChatColor.AQUA + "" + ChatColor.BOLD + "--- " + ChatColor.UNDERLINE + "AstroPvP Player List" + ChatColor.AQUA + ChatColor.BOLD + " ---");
            this.listCommandCache.add("");
            this.listCommandCache.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Players: " + ChatColor.GREEN + ChatColor.BOLD + onlinePlayers + ChatColor.GRAY + ChatColor.BOLD + " / " + ChatColor.GRAY + ChatColor.BOLD + (maxPlayers - 50) + ChatColor.GRAY + " players on your planet!");
            this.listCommandCache.add("");
            this.listCommandCache.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Factions: " + ChatColor.AQUA + this.getOnlinePlayersInFactions() + ChatColor.GRAY + " players" + ChatColor.GRAY + " online in " + ChatColor.AQUA + this.getOnlineFactionCount() + " different factions.");
            this.listCommandCache.add("");
            for (String msg : this.listCommandCache) {
                p.sendMessage(msg);
            }
            return;
        }
        if (this.message_aliases.contains(cmd) || this.reply_aliases.contains(cmd)) {
            if (e.getMessage().contains("&")) {
                e.setCancelled(true);
                e.setMessage("/null");
                e.getPlayer().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You cannot use &color codes in private messages.");
            } else {
                e.setMessage(ChatColor.stripColor(e.getMessage()));
            }
        }
    }


    @EventHandler
    public void onPlayerChatTabCompleteEvent(PlayerChatTabCompleteEvent e) {
        if (!e.getPlayer().hasPermission("arkkit.staff.hidden")) {
            for (String s : e.getTabCompletions()) {
                if (Bukkit.getPlayer(s) == null || !Bukkit.getPlayer(s).hasPermission("arkkit.staff.hidden"))
                    continue;
                e.getTabCompletions().remove(s);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.UNKNOWN) {
            return;
        }
        if (!e.getFrom().getWorld().equals(e.getTo().getWorld()) || e.getFrom().distanceSquared(e.getTo()) >= 256.0) {
            this.clearTeleportRequests(e.getPlayer());
        }
    }


    private void clearTeleportRequests(Player p) {
    }



    private boolean isNormalFaction(Player player) {
        Faction faction = FactionUtils.getFaction(player);
        return faction != null && (Boolean) FactionUtils.callFactionMethod(faction, "isNormal", null) != false;
    }

    private int getOnlineFactionCount() {
        HashSet<String> onlineFactionIds = new HashSet<String>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            String factionId = FactionUtils.getFactionId(p);
            if (factionId == null || !this.isNormalFaction(p)) continue;
            onlineFactionIds.add(factionId);
        }
        return onlineFactionIds.size();
    }

    private int getOnlinePlayersInFactions() {
        int x = 0;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!this.isNormalFaction(p)) continue;
            ++x;
        }
        return x;
    }
}

