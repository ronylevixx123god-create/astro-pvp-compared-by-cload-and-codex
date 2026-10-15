/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.massivecraft.factions.FLocation
 *  com.massivecraft.factions.FPlayer
 *  com.massivecraft.factions.Faction
 */
package com.cosmicpvp.cosmicutils.struct.factions;

import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class FactionHandler {
    public abstract void initHandler() throws NoSuchMethodException;

    public abstract boolean isInOwnFaction(Player var1);

    public abstract boolean inSameFaction(Player var1, Player var2);

    public abstract boolean inInEnemyFaction(Player var1, Player var2);

    public abstract boolean inInAllyFaction(Player var1, Player var2);

    public abstract String getFactionTag(Player var1, Player var2);

    public abstract String getFactionTag(Player var1);

    public abstract String getFactionId(Player var1);

    public abstract String getFPlayerName(FPlayer var1);

    public abstract String getFPlayerNameAndTag(FPlayer var1);

    public abstract Collection<FPlayer> getAllFPlayers(boolean var1);

    public abstract Collection<Faction> getAllFactions();

    public abstract Map<String, Map<String, String>> dumpAsSaveFormat();

    public abstract boolean isFocusedPlayer(Faction var1, String var2);

    public abstract Faction getFactionAt(Location var1);

    public abstract Faction getFactionAt(FLocation var1);

    public abstract Faction getFaction(Player var1, boolean var2);

    public abstract Faction getFaction(FPlayer var1, boolean var2);

    public abstract void sendMessage(FPlayer var1, String var2);

    public abstract Faction getFactionByTag(String var1);

    public abstract Faction getFactionById(String var1);

    public abstract List<String> getFPlayerIdsByTag(String var1);

    public abstract String getFactionIdByTag(String var1);

    public abstract FPlayer getFPlayerByName(String var1);

    public abstract Player getPlayer(FPlayer var1);

    public abstract void setFocusing(Faction var1, FPlayer var2) throws IllegalAccessException;

    public abstract FPlayer getFocusing(Faction var1);

    public abstract boolean attemptClaim(FPlayer var1, Faction var2, Location var3, boolean var4);

    public abstract Class<?> getMainFactionClass();

    public abstract Class<?> getMainFPlayerClass();

    public abstract List<String> getFPlayerIdsById(String var1);

    public abstract FPlayer getFPlayer(Player var1);

    public abstract String getFactionId(Faction var1);

    public abstract String getFPlayerId(FPlayer var1);

    public abstract int getLand(Faction var1);

    public abstract int getLandMax(Faction var1);

    public abstract double getPower(Faction var1);

    public abstract double getPowerMax(Faction var1);

    public abstract double getDTR(Faction var1);

    public abstract double getDTRMax(Faction var1);

    public abstract double getDTRMin(Faction var1);

    public abstract double getFactionBalance(Faction var1) throws NoSuchFieldException, IllegalAccessException;
}

