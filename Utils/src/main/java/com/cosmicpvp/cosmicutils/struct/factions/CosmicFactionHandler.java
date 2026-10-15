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
 *  com.massivecraft.factions.integration.Econ
 *  com.massivecraft.factions.net.astropvp.armorsets.struct.Relation
 *  com.massivecraft.factions.zcore.persist.Entity
 *  com.massivecraft.factions.zcore.persist.PlayerEntity
 *  com.massivecraft.factions.zcore.persist.PlayerEntityCollection
 */
package com.cosmicpvp.cosmicutils.struct.factions;

import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.ReflectionUtils;
import com.google.common.collect.Lists;

import com.massivecraft.factions.*;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.perms.Relation;
import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.*;

public class CosmicFactionHandler extends FactionHandler {
    @Override
    public void initHandler() {
    }

    @Override
    public boolean isInOwnFaction(Player player) {
        Faction at = Board.getInstance().getFactionAt((FLocation) new FLocation(player.getLocation()));
        FPlayer fpla = this.getFPlayer(player);
        return at != null && fpla != null && at.getFPlayers().contains((Object) fpla);
    }

    @Override
    public boolean inSameFaction(Player player, Player other) {
        if (player != null && other != null && other.equals(player)) {
            return true;
        }
        FPlayer pl = this.getFPlayer(player);
        FPlayer otherFPlayer = this.getFPlayer(other);
        return pl != null && otherFPlayer != null && pl.hasFaction() && pl.getFaction().getFPlayers().contains((Object) otherFPlayer);
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
        return fPlayer.getRelationTo(otherfPlayer);
    }

    @Override
    public boolean inInAllyFaction(Player player, Player other) {
        return this.getRelation(player, other) == Relation.ALLY;
    }

    @Override
    public String getFactionTag(Player seeingMsg, Player checking) {
        FPlayer fplayer = this.getFPlayer(seeingMsg);
        FPlayer other = this.getFPlayer(checking);
        return other.getChatTag(fplayer);
    }

    @Override
    public String getFactionTag(Player seeingMsg) {
        FPlayer fplayer = this.getFPlayer(seeingMsg);
        if (!fplayer.hasFaction()) {
            return null;
        }
        return fplayer.getFaction().getTag();
    }

    @Override
    public String getFactionId(Player seeingMsg) {
        FPlayer fplayer = this.getFPlayer(seeingMsg);
        if (!fplayer.hasFaction()) {
            return null;
        }
        return fplayer.getFaction().getId();
    }

    @Override
    public String getFPlayerName(FPlayer fplayer) {
        return fplayer.getName();
    }

    @Override
    public String getFPlayerNameAndTag(FPlayer fplayer) {
        return (String) FactionUtils.callFPlayerMethod(fplayer, "getNameAndTagAsync", null);
    }

    @Override
    public Collection<FPlayer> getAllFPlayers(boolean online) {
        return online ? FPlayers.getInstance().getOnlinePlayers() : FPlayers.getInstance().getAllFPlayers();
    }

    @Override
    public Collection<Faction> getAllFactions() {
        return Factions.getInstance().getAllFactions();
    }

    @Override
    public Map<String, Map<String, String>> dumpAsSaveFormat() {
        try {
            Method saveFormat = ReflectionUtils.getMethod(Board.class, "dumpAsSaveFormat");
            return (Map) saveFormat.invoke(null, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<String, Map<String, String>>();
        }
    }

    @Override
    public boolean isFocusedPlayer(Faction var1, String var2) {
        return false;
    }

    @Override
    public Faction getFactionAt(Location location) {
        return Board.getInstance().getFactionAt((FLocation) new FLocation(location));
    }

    @Override
    public Faction getFactionAt(FLocation location) {
        return Board.getInstance().getFactionAt((FLocation) location);
    }

    @Override
    public List<String> getFPlayerIdsByTag(String tag) {
        Faction faction = this.getFactionByTag(tag);
        if (faction == null) {
            return null;
        }
        ArrayList<String> retr = Lists.newArrayList();
        faction.getFPlayers().forEach(mem -> retr.add(mem.getId()));
        return retr;
    }

    @Override
    public String getFactionIdByTag(String tag) {
        Faction faction = this.getFactionByTag(tag);
        if (faction == null) {
            return null;
        }
        return faction.getId();
    }

    @Override
    public FPlayer getFPlayerByName(String name) {
        return FPlayers.getInstance().getById(name);
    }

    @Override
    public Player getPlayer(FPlayer player) {
        return player.getPlayer();
    }

    @Override
    public void setFocusing(Faction var1, FPlayer var2) throws IllegalAccessException {

    }

    @Override
    public Faction getFaction(FPlayer player, boolean includeWilderness) {
        Faction faction = player.getFaction();
        if (!includeWilderness && faction.isNone()) {
            return null;
        }
        return faction;
    }

    @Override
    public void sendMessage(FPlayer fplayer, String msg) {
        fplayer.getPlayer().sendMessage(msg);
    }



    @SneakyThrows
    @Override
    public FPlayer getFocusing(Faction faction) {
        return (FPlayer) ReflectionUtils.getObject(Faction.class, (Object) faction, "focusedPlayer");
    }



    @Override
    public boolean attemptClaim(FPlayer fplayer, Faction faction, Location location, boolean notifyFailure) {
        return fplayer.attemptClaim(faction, location, notifyFailure);
    }

    @Override
    public Class<?> getMainFactionClass() {
        return Faction.class;
    }

    @Override
    public Class<?> getMainFPlayerClass() {
        return FPlayer.class;
    }

    @Override
    public List<String> getFPlayerIdsById(String id) {
        Faction faction = this.getFactionById(id);
        if (faction == null) {
            return null;
        }
        ArrayList<String> retr = Lists.newArrayList();
        faction.getFPlayers().forEach(mem -> retr.add(mem.getId()));
        return retr;
    }

    @Override
    public Faction getFaction(Player player, boolean includeWilderness) {
        FPlayer fplayer = this.getFPlayer(player);
        if (fplayer == null) {
            if (includeWilderness) {
                return Factions.getInstance().getWilderness();
            }
            return null;
        }
        Faction fac = fplayer.getFaction();
        if (!includeWilderness && fac.isNone()) {
            return null;
        }
        return fac;
    }

    @Override
    public Faction getFactionByTag(String tag) {
        return Factions.getInstance().getBestTagMatch(tag);
    }

    @Override
    public Faction getFactionById(String id) {
        return Factions.getInstance().getFactionById(id);
    }

    @Override
    public FPlayer getFPlayer(Player player) {
        try {
            return FPlayers.getInstance().getByPlayer(player);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getFactionId(Faction faction) {
        return faction.getId();
    }

    @Override
    public String getFPlayerId(FPlayer fplayer) {
        return fplayer.getId();
    }

    @Override
    public int getLand(Faction faction) {
        return faction.getLandRounded();
    }

    @Override
    public int getLandMax(Faction faction) {
        return (int) Math.round(this.getPowerMax(faction));
    }

    @Override
    public double getPower(Faction faction) {
        return faction.getPower();
    }

    @Override
    public double getPowerMax(Faction faction) {
        return faction.getPowerMax();
    }

    @Override
    public double getDTR(Faction faction) {
        return 0.0;
    }

    @Override
    public double getDTRMax(Faction faction) {
        return 0.0;
    }

    @Override
    public double getDTRMin(Faction faction) {
        return 0.0;
    }

    @Override
    public double getFactionBalance(Faction faction) {
        return Econ.getBalance(faction);
    }

    private FPlayers getFPlayers() {
        return FPlayers.getInstance();
    }
}

