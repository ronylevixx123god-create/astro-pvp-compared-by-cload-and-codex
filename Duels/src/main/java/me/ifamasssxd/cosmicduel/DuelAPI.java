package me.ifamasssxd.cosmicduel;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicduel.listeners.SpectateInventory;
import me.ifamasssxd.cosmicduel.struct.*;
import me.ifamasssxd.cosmicduel.struct.enums.CancelReason;
import me.ifamasssxd.cosmicduel.struct.enums.DuelCreateState;
import me.ifamasssxd.cosmicduel.struct.enums.DuelState;
import me.ifamasssxd.cosmicduel.struct.enums.Setting;
import me.ifamasssxd.cosmicduel.struct.newduel.NewDuel;
import me.ifamasssxd.cosmicduel.struct.newduel.TeamDuel;
import me.ifamasssxd.cosmicduel.utils.ItemBuilder;
import me.ifamasssxd.cosmicduel.utils.JSONMessage;
import me.ifamasssxd.cosmicduel.utils.PlayerUtils;
import org.arkhamnetwork.Arkkit.commands.CommandArkReboot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DuelAPI {
    public static void invitePlayer(NewDuel duel, Player inviter, Player invited) {
        if (CosmicDuel.get().getDuelToggles().hasDuelsToggled(invited)) {
            inviter.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + invited.getName() + " has duels toggled off.");
            inviter.sendMessage(ChatColor.GRAY + "The duel has been cancelled.");
            cancelDuel(duel, CancelReason.TOGGLED_OFF_OPPONENT);
            return;
        }
        for (DuelInvite invite : getAllDuelInvites(inviter, InviteType.INVITED))
            declineDuel(inviter, invite.getInviter(), false);
        inviter.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "(!) " + ChatColor.YELLOW + "Your Duel request has been sent.");
        inviter.playSound(inviter.getLocation(), Sound.LEVEL_UP, 1.0F, 1.4F);
        invited.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "(!) " + ChatColor.YELLOW + inviter.getName() + " has invited you to a " + ChatColor.RED + ChatColor.UNDERLINE.toString() + duel.getName() + ChatColor.YELLOW + " Duel " + CC.translate("&ein the &c" + duel.getArena().getName() + " &eArena."));
        invited.sendMessage("     " + ChatColor.GRAY + "Click one of the following options below.");
        JSONMessage accept = new JSONMessage(ChatColor.YELLOW + "                ");
        accept.addRunCommand(ChatColor.GREEN + ChatColor.BOLD.toString() + "ACCEPT", ChatColor.YELLOW, "/duel accept " + inviter.getName(), ChatColor.GREEN + "/duel accept " + inviter.getName());
        accept.addText("    ", ChatColor.YELLOW);
        accept.addRunCommand(ChatColor.RED + ChatColor.BOLD.toString() + "DECLINE", ChatColor.YELLOW, "/duel decline " + inviter.getName(), ChatColor.RED + "/duel decline " + inviter.getName());
        accept.sendToPlayer(invited);
        invite_timer.add(new DuelInvite(30, inviter.getName(), invited.getName(), false));
    }

    public static int getDuelPosition(NewDuel d) {
        if (getArenaQueueSize(d.getArena()) > 0) {
            int position = 1;
            for (NewDuel duel : getArenaQueue(d.getArena())) {
                if (duel == null)
                    continue;
                if (duel.equals(d))
                    return position;
                position++;
            }
        }
        return 0;
    }

    public static boolean isRebooting() {
        if (arkkit == null)
            arkkit = Boolean.valueOf(Bukkit.getPluginManager().isPluginEnabled("Arkkit"));
        return ((arkkit.booleanValue() && CommandArkReboot.restart_task_exists));
    }

    public static void sendSettings(Player p, String p_name) {
        if (!hasPendingInvite(p)) {
            p.sendMessage(red + ChatColor.RED + "You do not have a pending duel invite.");
            return;
        }
        NewDuel d = getPendingDuel(p, p_name);
        if (d == null) {
            p.sendMessage(red + ChatColor.RED + "That duel is no longer valid.");
            DuelInvite invalid = getDuelInvite(p_name, p);
            if (invalid != null)
                invite_timer.remove(invalid);
            return;
        }
        Inventory inv = Bukkit.createInventory((InventoryHolder)null, 9, p_name + "'s Duel Settings");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(CC.translate("&e&lDuel Type: &c" + d.getDuelType()));
        lore.add("");
        for (Setting setting : Setting.values())
            lore.add(ChatColor.YELLOW + setting.getName() + ": " + (d.getSettings().hasSetting(setting) ? (ChatColor.GREEN + ChatColor.BOLD.toString() + "ON") : (ChatColor.RED + ChatColor.BOLD.toString() + "OFF")));
        lore.add(CC.Yellow + "Arena: " + CC.Green + ((d.getArena() == null) ? "N/A" : d.getArena().getName()));
        lore.add("");
        lore.add(ChatColor.GRAY + "Click to accept access the stakes menu.");
        inv.setItem(inv.getSize() - 5, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, (short)5, ChatColor.YELLOW + ChatColor.BOLD.toString() + "ACCEPT DUEL", lore));
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null || inv.getItem(i).getType() == Material.AIR)
                inv.setItem(i, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, (short)7, " ", " "));
        }
        p.openInventory(inv);
    }

    public static NewDuel getPendingDuel(Player p, String inviter) {
        for (DuelInvite invite : invite_timer) {
            if (invite.getInvited().equalsIgnoreCase(p.getName()) && invite.getInviter().equalsIgnoreCase(inviter)) {
                Player pl = Bukkit.getPlayer(invite.getInviter());
                if (pl == null)
                    return null;
                return getDuel(pl);
            }
        }
        return null;
    }

    public static boolean canDuel(Player p) {
        return (!hasDuel(p) && !isWaitingOnInvite(p));
    }

    public static List<DuelInvite> getAllDuelInvites(Player p, InviteType inviteType) {
        List<DuelInvite> retr = new ArrayList<>();
        for (DuelInvite invite : invite_timer) {
            if ((inviteType == InviteType.INVITER && invite.getInviter().equalsIgnoreCase(p.getName())) || (inviteType == InviteType.INVITED && invite.getInvited().equalsIgnoreCase(p.getName())))
                retr.add(invite);
        }
        return retr;
    }

    public static DuelInvite getDuelInvite(Player p, InviteType inviteType) {
        for (DuelInvite invite : invite_timer) {
            if ((inviteType == InviteType.INVITER && invite.getInviter().equalsIgnoreCase(p.getName())) || (inviteType == InviteType.INVITED && invite.getInvited().equalsIgnoreCase(p.getName())))
                return invite;
        }
        return null;
    }

    public static DuelInvite getDuelInvite(String inviter, Player invited) {
        for (DuelInvite invite : invite_timer) {
            if (invite.getInviter().equalsIgnoreCase(inviter) && invite.getInvited().equalsIgnoreCase(invited.getName()))
                return invite;
        }
        return null;
    }

    public static void acceptInvite(Player p, String inviter, boolean settings) {
        //p guy who is invited
        //inviter is guy who invites
        if (settings) {
            p.performCommand("duel settings " + inviter);
            DuelInvite d = getDuelInvite(inviter, p);
            if (d == null) {
                p.sendMessage(ChatColor.RED + "That is not a valid duel request.");
                return;
            }
            d.setPending(true);
        } else {
            NewDuel d2 = getPendingDuel(p, inviter);
            if (d2 == null) {
                p.sendMessage(ChatColor.RED + "That is not a valid duel request.");
                return;
            }
            if (d2 instanceof TeamDuel) {
                if (CosmicDuel.get().getTeamDataManager().tryFindTeam(p.getUniqueId()) != null) {
                    TeamData teamData = CosmicDuel.get().getTeamDataManager().tryFindTeam(p.getUniqueId());
                    TeamData teamData2 = CosmicDuel.get().getTeamDataManager().tryFindTeam(Bukkit.getPlayer(inviter).getUniqueId());
                    ((TeamDuel)d2).setTeamB(teamData);
                    ((TeamDuel) d2).setTeamA(teamData2);
                    d2.addPlayer(Bukkit.getPlayer(teamData.getLeaderUUID()), true);
                    //d2.addPlayer(Bukkit.getPlayer(teamData2.getLeaderUUID()), true);
                    for (UUID uuid : teamData.getTeamates())
                        d2.addPlayer(Bukkit.getPlayer(uuid), true);
                    for (UUID uuid : teamData2.getTeamates())
                        d2.addPlayer(Bukkit.getPlayer(uuid), true);

                    for (Player player : d2.getPlayers()) {
                        System.out.println("Duel Team DEBUG: "+ player.getName());                    }
                }
            } else {
                d2.addPlayer(p, true);
            }
            invite_timer.remove(getDuelInvite(p, InviteType.INVITED));
            DuelInvite i = null;
            while ((i = getDuelInvite(p, InviteType.INVITED)) != null) {
                Player pl = Bukkit.getPlayer(i.getInviter());
                if (pl != null)
                    declineDuel(p, pl.getName(), true);
                invite_timer.remove(i);
            }
        }
    }

    public static void declineDuel(Player p, String inviter, boolean notify) {
        NewDuel d = getPendingDuel(p, inviter);
        if (d == null) {
            p.sendMessage(ChatColor.RED + "That is not a valid duel request.");
            return;
        }
        if (notify)
            if (d instanceof TeamDuel) {
                d.sendMessage(red + ChatColor.RED + ChatColor.UNDERLINE.toString() + p.getName() + ChatColor.RED + "'s team has declined the duel.", false);
            } else {
                d.sendMessage(red + ChatColor.RED + ChatColor.UNDERLINE.toString() + p.getName() + ChatColor.RED + " has declined the duel.", false);
            }
        DuelInvite duelInvite = getDuelInvite(p, InviteType.INVITED);
        invite_timer.remove(duelInvite);
        if (d instanceof TeamDuel) {
            p.sendMessage(red + ChatColor.RED + "You have declined the duel invite from " + duelInvite.getInviter() + "'s team.");
        } else {
            p.sendMessage(red + ChatColor.RED + "You have declined the duel invite from " + duelInvite.getInviter() + ".");
        }
        cancelDuel(d, CancelReason.DECLINE_INVITE);
    }

    public static void startQueuedDuel(NewDuel duel, Arena arena) {
        if (duel == null)
            return;
        if (getCurrentlyActiveDuel(arena) != null)
            return;
        LinkedList<NewDuel> duels = getArenaQueue(arena);
        duels.remove(duel);
        duel_queue.put(arena, duels);
        for (NewDuel d : duels) {
            if (d.equals(duel))
                continue;
            if (d.getPlayers().size() != 2)
                continue;
            for (Player p : d.getPlayers()) {
                p.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "(!) " + ChatColor.YELLOW + ChatColor.UNDERLINE.toString() + "Duel Queue Update");
                if (d.getArena() == null) {
                    p.sendMessage(ChatColor.YELLOW + "You are #" + getDuelPosition(d) + ChatColor.YELLOW + " in Queue " + ChatColor.YELLOW + "vs " + ChatColor.YELLOW + ChatColor.BOLD.toString() + d.getOppositePlayer(p).getName() + ".");
                } else {
                    p.sendMessage(ChatColor.YELLOW + "You are #" + getDuelPosition(d) + ChatColor.YELLOW + " in Queue for the " + d.getArena().getName() + " Arena " + ChatColor.YELLOW + "vs " + ChatColor.YELLOW + ChatColor.BOLD.toString() + d.getOppositePlayer(p).getName() + ".");
                }
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 0.8F);
            }
        }
        if (duel.getPlayers().size() < duel.getPlayersToStart()) {
            System.out.println("Duel was trashed.");
            cancelDuel(duel, CancelReason.NOT_ENOUGH_PLAYERS_QUEUE);
            startQueuedDuel(getNextDuel(arena), arena);
            return;
        }
        for (Player p2 : duel.getPlayers()) {
            if (PlayerUtils.inCombat(p2)) {
                duel.sendMessage(ChatColor.RED + p2.getName() + ChatColor.YELLOW + " was in combat and the duel was cancelled.", true);
                cancelDuel(duel, CancelReason.IN_COMBAT_QUEUE);
                startQueuedDuel(getNextDuel(arena), arena);
                return;
            }
        }
        duel.setCreateState(DuelCreateState.QUEUE_POP);
    }

    public static boolean inSameDuel(Player p1, Player p2) {
        return (hasDuel(p1) && hasDuel(p2) && getDuel(p1).equals(getDuel(p2)));
    }

    public static synchronized void queueDuel(Arena arena, NewDuel duel) {
        LinkedList<NewDuel> duels = getArenaQueue(arena);
        if (!duels.contains(duel))
            duels.add(duel);
        duel_queue.put(arena, duels);
    }

    public static NewDuel getNextDuel(Arena arena) {
        LinkedList<NewDuel> duels = getArenaQueue(arena);
        if (duels.isEmpty())
            return null;
        return duels.getFirst();
    }

    public static Arena getLeastPopulatedRankedQueue() {
        Arena arena = null;
        int queueCount = 0;
        for (Arena area : Arena.arenas) {
            if (area.isRankedArena()) {
                int queue = getArenaQueueSize(area);
                if (queue > queueCount && arena != null)
                    continue;
                arena = area;
                queueCount = queue;
            }
        }
        return arena;
    }

    public static boolean doesArenaHaveQueue(Arena arena) {
        return doesArenaHaveQueue(arena, true);
    }

    public static boolean doesArenaHaveQueue(Arena arena, boolean takeActive) {
        return (getArenaQueue(arena).size() > 0 || (takeActive && getCurrentlyActiveDuel(arena) != null));
    }

    public static LinkedList<NewDuel> getArenaQueue(Arena arena) {
        if (arena == null)
            return new LinkedList<>();
        if (!duel_queue.containsKey(arena))
            duel_queue.put(arena, new LinkedList<>());
        boolean changed = false;
        LinkedList<NewDuel> duels = duel_queue.get(arena);
        for (Object duel1 : Lists.newArrayList(duels)) {
            NewDuel duel = (NewDuel)duel1;
            if (duel.getPlayers().size() < duel.getPlayersToStart() && duel.getDuelState() == DuelState.IN_QUEUE) {
                duels.remove(duel);
                changed = true;
                cancelDuel(duel, CancelReason.NOT_ENOUGH_PLAYERS_QUEUE);
            }
        }
        if (changed)
            duel_queue.put(arena, duels);
        return duels;
    }

    public static NewDuel getCurrentlyActiveDuel(Arena arena) {
        List<NewDuel> deadDuels = Lists.newArrayList();
        for (NewDuel d : duels) {
            if (d.getArena() != null && d.getDuelState() != DuelState.ENDED && (d.getCreateState() == DuelCreateState.DONE || d.getCreateState() == DuelCreateState.QUEUE_POP) && d.getArena().equals(arena)) {
                if (d.getPlayers().size() >= d.getPlayersToStart())
                    return d;
                if (d.getPlayers().size() != 0)
                    continue;
                deadDuels.add(d);
                CosmicDuel.logMessage("Found DEAD duel with ID: " + d.getDuelID() + " stuck for arena: " + arena.getName() + ", clearing...");
            }
        }
        deadDuels.forEach(duel -> duels.remove(duel));
        return null;
    }

    public static int getArenaQueueSize(Arena arena) {
        List<NewDuel> duels = new ArrayList<>();
        duels.addAll(getArenaQueue(arena));
        if (getCurrentlyActiveDuel(arena) != null)
            duels.add(getCurrentlyActiveDuel(arena));
        return duels.size();
    }

    public static boolean checkStatus(Player player, Player target) {
        try {
            if (isRebooting()) {
                player.sendMessage(ChatColor.RED + "You cannot duel while the server is rebooting.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (CosmicDuel.get().getDuelToggles().hasDuelsToggled(player)) {
            player.sendMessage(red + ChatColor.RED + "You have duels toggled off.");
            return false;
        }
        if (isSpectator(player)) {
            player.sendMessage(red + ChatColor.RED + "You are spectating a duel.");
            player.sendMessage(ChatColor.GRAY + "Use /duel leave to leave the duel.");
            return false;
        }
        if (isWaitingOnInvite(player)) {
            player.sendMessage(red + ChatColor.RED + "You must wait for your current invite to expire.");
            return false;
        }
        if (player.hasMetadata("in_ranked_queue_menu")) {
            player.sendMessage(ChatColor.RED + "You cannot do this while in queue.");
            return false;
        }
        NewDuel d = getDuel(player);
        if (d != null) {
            Player opposite = d.getOppositePlayer(player);
            if (opposite != null)
                if (d.getDuelState() == DuelState.IN_QUEUE && d.getArena() != null) {
                    player.sendMessage(red + ChatColor.RED + "You are #" + getDuelPosition(d) + " in queue for the " + d.getArena().getName() + " Arena " + ChatColor.RED + "vs " + opposite.getName() + ".");
                    player.sendMessage(ChatColor.GRAY + "Use '/duel leave' to leave the queue.");
                } else {
                    player.sendMessage(red + ChatColor.RED + "You are already in a duel with " + d.getOppositePlayer(player).getName() + ".");
                    player.sendMessage(ChatColor.GRAY + "Use /duel leave to leave the duel.");
                }
            return false;
        }


        if (getDuel(player) != null) {
            player.sendMessage(ChatColor.RED + "You are in a duel already.");
            return false;
        }

        if (PlayerUtils.inCombat(player)) {
            player.sendMessage(ChatColor.RED + "You are in combat and cannot duel at this time.");
            return false;
        }

        NewDuel targetDuel = getDuel(target);
        if (targetDuel != null) {
            if (targetDuel.getCreateState() != DuelCreateState.DONE) {
                if (targetDuel.getCreateState() == DuelCreateState.WAITING && isDuelQueued(targetDuel)) {
                    Player opposite2 = targetDuel.getOppositePlayer(target);
                    if (opposite2 != null) {
                        player.sendMessage(red + ChatColor.RED + target.getName() + " is currently #" + getDuelPosition(targetDuel) + " in queue for the " + targetDuel.getArena().getName() + " Arena " + ChatColor.YELLOW + "vs " + ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + opposite2.getName() + ".");
                        return false;
                    }
                }
                if (targetDuel instanceof TeamDuel) {
                    if (CosmicDuel.get().getTeamDataManager().tryFindTeam(target.getUniqueId()) != null) {
                        player.sendMessage(CC.translate(Bukkit.getPlayer(CosmicDuel.get().getTeamDataManager().tryFindTeam(target.getUniqueId()).getLeaderUUID()) + "'s team is already creating a duel!"));
                        return false;
                    }
                } else {
                    player.sendMessage(red + ChatColor.RED + target.getName() + " is currently creating a duel.");
                    return false;
                }
            }
            if (targetDuel.getDuelState() == DuelState.STARTING) {
                player.sendMessage(red + ChatColor.RED + target.getName() + ChatColor.RED + " is currently starting a Duel with " + ChatColor.RED + ChatColor.UNDERLINE + targetDuel.getOppositePlayer(target).getName() + ChatColor.RED + ".");
            } else if (targetDuel.getDuelState() == DuelState.PLAYING) {
                player.sendMessage(red + ChatColor.RED + target.getName() + ChatColor.RED + " is currently in a Duel with " + ChatColor.RED + ChatColor.UNDERLINE + targetDuel.getOppositePlayer(target).getName() + ChatColor.RED + ".");
            } else if (targetDuel.getDuelState() == DuelState.ENDING) {
                player.sendMessage(red + ChatColor.RED + target.getName() + ChatColor.RED + " is currently ending a Duel with " + ChatColor.RED + ChatColor.UNDERLINE + targetDuel.getOppositePlayer(target).getName() + ChatColor.RED + ".");
            } else {
                player.sendMessage(red + ChatColor.RED + target.getName() + ChatColor.RED + " is currently in a Duel.");
            }
            return false;
        }
        if (CosmicDuel.get().getDuelToggles().hasDuelsToggled(target)) {
            player.sendMessage(red + ChatColor.RED + "That player has duel invites toggled off.");
            return false;
        }
        if (isWaitingOnInvite(target)) {
            DuelInvite invite = getDuelInvite(player, InviteType.INVITER);
            if (invite == null) {
                player.sendMessage(ChatColor.RED + "That player already has a pending invite.");
            } else {
                player.sendMessage(red + ChatColor.RED + "That player already has a pending invite to " + invite.getInvited() + ".");
            }
            return false;
        }
        if (isSpectator(target)) {
            player.sendMessage(red + ChatColor.RED + "That player is currently spectating a duel.");
            return false;
        }
        if (target.hasMetadata("in_ranked_queue_menu")) {
            player.sendMessage(red + ChatColor.RED + "This player is currently in a ranked queue.");
            return false;
        }
        if (PlayerUtils.inCombat(target)) {
            player.sendMessage(ChatColor.RED + "That player is in combat and cannot duel at this time.");
            return false;
        }


        return true;
    }

    public static boolean isDuelQueued(NewDuel d) {
        if (d == null)
            return false;
        LinkedList<NewDuel> queue = getArenaQueue(d.getArena());
        if (queue.isEmpty())
            return false;
        for (NewDuel duel : queue) {
            if (duel.getDuelID() == d.getDuelID())
                return true;
        }
        return false;
    }

    public static void cancelDuel(NewDuel duel, CancelReason cancelReason) {
        Bukkit.getLogger().info("Cancelling duel for " + cancelReason);
        if (duel == null || duel.isCancelled())
            return;
        Arena arena = duel.getArena();
        if (arena != null) {
            if (isDuelCurrentlyQueued(duel, arena)) {
                if (duel.getDuelState() == DuelState.IN_QUEUE) {
                    LinkedList<NewDuel> currentQueue = getArenaQueue(arena);
                    currentQueue.remove(duel);
                    duel_queue.put(arena, currentQueue);
                    CosmicDuel.logMessage("Removing duel " + duel.getDuelID() + " from queue list.");
                    CosmicDuel.logMessage("Returning stakes from left queue.");
                    if (!duel.isRanked())
                        duel.returnStakes();
                }
            } else if (duel.getCreateState() == DuelCreateState.STAKES || duel.isInQueueMenu()) {
                System.out.println("F");
                duel.returnStakes();
            }
            if (doesArenaHaveQueue(arena)) {
                NewDuel nextDuelInQueue = getNextDuel(arena);
                if (nextDuelInQueue != null && cancelReason != CancelReason.RELOAD)
                    startQueuedDuel(nextDuelInQueue, arena);
            }
        }
        duel.setCancelled(true);
        for (Player p : duel.getPlayers()) {
            if (p != null) {
                p.removeMetadata("in_duel", CosmicDuel.get());
                p.closeInventory();
            }
        }
        duel.clearSpectators();
        duel.getPlayers().clear();
        duel.getSpectators().clear();
        duel.getStakes().clear();
        duel.resetEnvoys();
        duels.remove(duel);
        SpectateInventory.updateInventory();
    }

    public static boolean isDuelCurrentlyQueued(NewDuel duel, Arena arena) {
        for (NewDuel nDuel : getArenaQueue(arena)) {
            if (duel.getDuelID() == nDuel.getDuelID())
                return true;
        }
        return false;
    }

    public static Arena getBestOpenRankedArena() {
        Arena arena = null;
        for (Arena area : Arena.arenas) {
            if (!area.isRankedArena())
                continue;
            if (doesArenaHaveQueue(area))
                continue;
            arena = area;
        }
        return arena;
    }

    public static boolean isWaitingOnInvite(Player p) {
        return (getDuelInvite(p, InviteType.INVITER) != null);
    }

    public static boolean hasPendingInvite(Player p) {
        return (getDuelInvite(p, InviteType.INVITED) != null);
    }

    public static NewDuel getDuelSpectating(Player p) {
        for (NewDuel d : duels) {
            if (d.getSpectators().contains(p))
                return d;
        }
        return null;
    }

    public static boolean isSpectator(Player p) {
        return p.hasMetadata("spectator");
    }

    public static boolean hasDuel(Player p) {
        if (p.hasMetadata("in_duel") &&  p.getLocation().getWorld().getName().equals("world_duels")) {
            NewDuel d = getDuel(p);
            if (d == null) {
                p.removeMetadata("in_duel", CosmicDuel.get());
                return false;
            }
        }
        return p.hasMetadata("in_duel");
    }

    public static boolean isInArenaQueue(Player player) {
        for (List<NewDuel> duels : duel_queue.values()) {
            for (NewDuel duel : duels) {
                if (duel.getPlayers().contains(player))
                    return true;
            }
        }
        return false;
    }

    public static boolean isDuelValid(NewDuel d) {
        return (d.getDuelState() != DuelState.WAITING && d.getDuelState() != DuelState.IN_QUEUE && d.getCreateState() == DuelCreateState.DONE);
    }

    public static NewDuel getDuel(Player p) {
        for (NewDuel duel : duels) {
            if (duel.getPlayers().contains(p))
                return duel;
        }
        return null;
    }

    public static int MAX_ARENA_QUEUE = 10;

    public static List<NewDuel> duels = new ArrayList<>();

    public static List<DuelInvite> invite_timer = new ArrayList<>();

    private static final ConcurrentHashMap<Arena, LinkedList<NewDuel>> duel_queue = new ConcurrentHashMap<>();

    private static final String red = ChatColor.RED + ChatColor.BOLD.toString() + "(!) ";

    private static Boolean networkManager = null;

    private static Boolean arkkit = null;


    public enum InviteType {
        INVITER, INVITED
    }
}
