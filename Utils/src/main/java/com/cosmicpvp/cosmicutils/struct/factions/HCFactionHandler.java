/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.massivecraft.factions.Board
 *  com.massivecraft.factions.FLocation
 *  com.massivecraft.factions.FPlayer
 *  com.massivecraft.factions.FPlayers
 *  com.massivecraft.factions.Faction
 *  com.massivecraft.factions.Factions
 *  com.massivecraft.factions.iface.RelationParticipator
 *  com.massivecraft.factions.net.astropvp.armorsets.struct.Relation
 */
package com.cosmicpvp.cosmicutils.struct.factions;

import com.cosmicpvp.cosmicutils.compat.ParamBuilder;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.ReflectionUtils;
import com.google.common.collect.Lists;

import com.massivecraft.factions.*;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.perms.Relation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class HCFactionHandler
        extends FactionHandler {
    private Factions instance;
    private FPlayers fPlayers;
    private Board board;
    private Class<?> memoryFPlayersClass;
    private Class<?> memoryFPlayerClass;
    private Class<?> jsonFactionClass;
    private Class<?> memoryFactionClass;
    private Class<?> memoryBoard;
    private Method factionGetId;
    private Method fplayerGetId;
    private Method fPlayerGetFaction;
    private Method fPlayerHasFaction;
    private Method getFplayerById;
    private Method getFplayerBytag;
    private Method isNone;
    private Method getFactionTag;
    private Method getPlayer;
    private Field focusPlayerField;

    @Override
    public void initHandler() throws NoSuchMethodException {
        Method getInstance = ReflectionUtils.getMethod(FPlayers.class, "getInstance");
        try {
            this.fPlayers = (FPlayers) getInstance.invoke(null, new Object[0]);
            Method fInstance = ReflectionUtils.getMethod(Factions.class, "getInstance");
            this.instance = (Factions) fInstance.invoke(null, new Object[0]);
            this.board = Board.getInstance();
            this.memoryFPlayerClass = Class.forName("com.massivecraft.factions.zcore.persist.MemoryFPlayer");
            this.memoryFPlayersClass = Class.forName("com.massivecraft.factions.zcore.persist.MemoryFPlayers");
            this.jsonFactionClass = Class.forName("com.massivecraft.factions.zcore.persist.json.JSONFaction");
            this.memoryFactionClass = Class.forName("com.massivecraft.factions.zcore.persist.MemoryFaction");
            this.factionGetId = ReflectionUtils.getMethod(Faction.class, "getId");
            this.fplayerGetId = ReflectionUtils.getMethod(FPlayer.class, "getId");
            this.fPlayerGetFaction = ReflectionUtils.getMethod(this.memoryFPlayerClass, "getFaction");
            this.fPlayerHasFaction = ReflectionUtils.getMethod(this.memoryFPlayerClass, "hasFaction");
            this.memoryBoard = Class.forName("com.massivecraft.factions.zcore.persist.MemoryBlockBoard");
            this.getFplayerBytag = ReflectionUtils.getMethod(this.memoryFPlayersClass, "getByTag", String.class);
            this.getFplayerById = ReflectionUtils.getMethod(this.memoryFPlayersClass, "getById", String.class);
            this.focusPlayerField = ReflectionUtils.getField(this.memoryFactionClass, "focusedPlayer");
            this.isNone = ReflectionUtils.getMethod(this.memoryFactionClass, "isNone");
            this.getPlayer = ReflectionUtils.getMethod(FPlayer.class, "getPlayer");
            this.getFactionTag = ReflectionUtils.getMethod(Faction.class, "getTag");
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isInOwnFaction(Player player) {
        Faction at = this.getFactionAt(player.getLocation());
        FPlayer fpla = this.getFPlayer(player);
        return at != null && fpla != null && (Relation) ReflectionUtils.callReflectionMethod(fpla, this.memoryFPlayerClass, "getRelationTo", new ParamBuilder(RelationParticipator.class, (Object) at)) == Relation.MEMBER;
    }

    @Override
    public boolean inSameFaction(Player player, Player other) {
        return this.getRelation(player, other) == Relation.MEMBER;
    }

    @Override
    public boolean inInEnemyFaction(Player player, Player other) {
        return this.getRelation(player, other) == Relation.ENEMY;
    }

    private Relation getRelation(Player player, Player other) {
        if (player == other) {
            return Relation.MEMBER;
        }
        FPlayer fPlayer = this.getFPlayer(player);
        FPlayer otherfPlayer = this.getFPlayer(other);
        if (fPlayer == null || otherfPlayer == null) {
            return Relation.NEUTRAL;
        }
        return (Relation) ReflectionUtils.callReflectionMethod((Object) fPlayer, this.memoryFPlayerClass, "getRelationTo", new ParamBuilder(RelationParticipator.class, (Object) otherfPlayer));
    }

    @Override
    public boolean inInAllyFaction(Player player, Player other) {
        return this.getRelation(player, other) == Relation.ALLY;
    }

    @Override
    public String getFactionTag(Player seeingMsg, Player checking) {
        FPlayer fplayer = this.getFPlayer(seeingMsg);
        FPlayer other = this.getFPlayer(checking);
        return (String) ReflectionUtils.callReflectionMethod((Object) other, this.memoryFPlayerClass, "getChatTag", new ParamBuilder(FPlayer.class, (Object) fplayer));
    }

    @Override
    public String getFactionTag(Player seeingMsg) {
        Faction faction = this.getFaction(seeingMsg, false);
        if (faction == null) {
            return null;
        }
        return (String) ReflectionUtils.callReflectionMethod((Object) faction, this.memoryFactionClass, "getTag");
    }

    @Override
    public String getFactionId(Player seeingMsg) {
        Faction faction = this.getFaction(seeingMsg, false);
        if (faction == null) {
            return null;
        }
        return (String) ReflectionUtils.invokeMethod(this.factionGetId, (Object) faction);
    }

    @Override
    public String getFPlayerName(FPlayer fplayer) {
        return (String) FactionUtils.callFPlayerMethod(fplayer, "getName", null);
    }

    @Override
    public String getFPlayerNameAndTag(FPlayer fplayer) {
        return (String) FactionUtils.callFPlayerMethod(fplayer, "getNameAndTag", null);
    }

    @Override
    public Map<String, Map<String, String>> dumpAsSaveFormat() {
        try {
            Class<?> boardClass = Class.forName("com.massivecraft.factions.zcore.persist.json.JSONBlockBoard");
            if (this.board != null && boardClass.isInstance((Object) this.board)) {
                Object jsonBoard = boardClass.cast((Object) this.board);
                Method dumpAsSaveFormat = ReflectionUtils.getMethod(jsonBoard.getClass(), "dumpAsSaveFormat");
                return (Map) dumpAsSaveFormat.invoke(jsonBoard, new Object[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, Map<String, String>>();
    }

    @Override
    public FPlayer getFPlayerByName(String name) {
        try {
            return (FPlayer) this.getFplayerBytag.invoke((Object) this.fPlayers, name);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Player getPlayer(FPlayer player) {
        try {
            return (Player) this.getPlayer.invoke((Object) player, new Object[0]);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Faction getFactionAt(Location location) {
        return this.getFactionAt(new FLocation(location));
    }

    @Override
    public Faction getFactionAt(FLocation location) {
        return (Faction) ReflectionUtils.callReflectionMethod((Object) this.board, this.memoryBoard, "getFactionAt", new ParamBuilder(FLocation.class, (Object) location));
    }

    @Override
    public Faction getFaction(Player player, boolean includeWilderness) {
        FPlayer fplayer = this.getFPlayer(player);
        return (Faction) ReflectionUtils.invokeMethod(this.fPlayerGetFaction, (Object) fplayer);
    }

    @Override
    public Faction getFaction(FPlayer player, boolean includeWilderness) {
        Faction faction = (Faction) ReflectionUtils.invokeMethod(this.fPlayerGetFaction, (Object) player);
        if (!includeWilderness && ((Boolean) ReflectionUtils.invokeMethod(this.isNone, (Object) faction)).booleanValue()) {
            return null;
        }
        return faction;
    }

    @Override
    public void sendMessage(FPlayer fplayer, String msg) {
        ReflectionUtils.callReflectionMethod((Object) fplayer, FPlayer.class, "sendMessage", new ParamBuilder(String.class, msg));
    }

    @Override
    public Faction getFactionByTag(String tag) {
        Faction fac = this.instance.getByTag(tag);
        return (Faction) this.jsonFactionClass.cast((Object) fac);
    }

    private String getFacId(Faction faction) {
        return (String) ReflectionUtils.callReflectionMethod((Object) faction, this.memoryFactionClass, "getId", null);
    }

    @Override
    public Faction getFactionById(String tag) {
        try {
            Method getFactionById = ReflectionUtils.getMethod(Factions.class, "getFactionById", String.class);
            return (Faction) getFactionById.invoke((Object) this.instance, tag);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<String> getFPlayerIdsByTag(String tag) {
        Faction faction = this.getFactionByTag(tag);
        if (faction == null) {
            return null;
        }
        return this.getFPlayerAccountIds(faction);
    }

    @Override
    public String getFactionIdByTag(String tag) {
        Faction faction = this.getFactionByTag(tag);
        if (faction == null) {
            return null;
        }
        return this.getFacId(faction);
    }

    @Override
    public boolean isFocusedPlayer(Faction faction, String name) {
        try {
            FPlayer focusing = (FPlayer) this.focusPlayerField.get((Object) faction);
            if (focusing == null) {
                return false;
            }
            String fName = this.getFPlayerName(focusing);
            if (fName.equals(name)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public FPlayer getFocusing(Faction faction) {
        try {
            return (FPlayer) this.focusPlayerField.get((Object) faction);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void setFocusing(Faction faction, FPlayer player) throws IllegalAccessException {
        ReflectionUtils.setObject((Object) faction, this.focusPlayerField, (Object) player);
    }

    @Override
    public boolean attemptClaim(FPlayer fplayer, Faction faction, Location location, boolean notifyFailure) {
        return (Boolean) ReflectionUtils.callReflectionMethod((Object) fplayer, FPlayer.class, "attemptClaim", new ParamBuilder(Faction.class, (Object) faction).add(Location.class, location).add(Boolean.TYPE, notifyFailure));
    }

    @Override
    public Class<?> getMainFactionClass() {
        return this.memoryFactionClass;
    }

    @Override
    public Class<?> getMainFPlayerClass() {
        return this.memoryFPlayerClass;
    }

    @Override
    public List<String> getFPlayerIdsById(String id) {
        Faction faction = this.getFactionById(id);
        if (faction == null) {
            return null;
        }
        return this.getFPlayerAccountIds(faction);
    }

    private List<String> getFPlayerAccountIds(Faction fac) {
        ArrayList<String> retr = Lists.newArrayList();
        Set players = (Set) ReflectionUtils.callReflectionMethod((Object) fac, this.memoryFactionClass, "getFPlayers", null);
        players.forEach(pl -> retr.add((String) ReflectionUtils.callReflectionMethod(pl, FPlayer.class, "getAccountId", null)));
        return retr;
    }

    @Override
    public FPlayer getFPlayer(Player player) {
        try {
            Method method = ReflectionUtils.getMethod(this.memoryFPlayersClass, "getByPlayer", Player.class);
            return (FPlayer) method.invoke((Object) this.fPlayers, player);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getFactionId(Faction faction) {
        return (String) ReflectionUtils.invokeMethod(this.factionGetId, (Object) faction);
    }

    @Override
    public String getFPlayerId(FPlayer fplayer) {
        return (String) ReflectionUtils.invokeMethod(this.fplayerGetId, (Object) fplayer);
    }

    @Override
    public int getLand(Faction faction) {
        return (Integer) ReflectionUtils.callReflectionMethod((Object) faction, Faction.class, "getLand", null);
    }

    @Override
    public int getLandMax(Faction faction) {
        return (Integer) ReflectionUtils.callReflectionMethod((Object) faction, Faction.class, "getMaxLand", null);
    }

    @Override
    public double getPower(Faction faction) {
        return 0.0;
    }

    @Override
    public double getPowerMax(Faction faction) {
        return 0.0;
    }

    @Override
    public double getDTR(Faction faction) {
        return (Double) ReflectionUtils.callReflectionMethod((Object) faction, Faction.class, "getDTR", null);
    }

    @Override
    public double getDTRMax(Faction faction) {
        return (Double) ReflectionUtils.callReflectionMethod((Object) faction, Faction.class, "getMaxDTR", null);
    }

    @Override
    public double getDTRMin(Faction faction) {
        return (Double) ReflectionUtils.callReflectionMethod((Object) faction, Faction.class, "getMinDTR", null);
    }

    @Override
    public double getFactionBalance(Faction faction) throws NoSuchFieldException, IllegalAccessException {
        return (Double) ReflectionUtils.getObject(Faction.class, (Object) faction, "money");
    }

    private String getAccountId(Faction faction) {
        return (String) ReflectionUtils.callReflectionMethod((Object) faction, Faction.class, "getAccountId", null);
    }

    private FPlayers getFPlayers() {
        return this.fPlayers;
    }

    @Override
    public Collection<FPlayer> getAllFPlayers(boolean online) {
        return online ? (Collection) ReflectionUtils.callReflectionMethod((Object) this.getFPlayers(), FPlayers.class, "getOnlinePlayers", null) : (Collection) ReflectionUtils.callReflectionMethod((Object) this.getFPlayers(), FPlayers.class, "getAllFPlayers", null);
    }

    @Override
    public Collection<Faction> getAllFactions() {
        return (List) ReflectionUtils.callReflectionMethod((Object) this.instance, Factions.class, "getAllFactions", null);
    }
}

