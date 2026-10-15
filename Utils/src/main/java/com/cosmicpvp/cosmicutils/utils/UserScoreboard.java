/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.utils;

import com.cosmicpvp.cosmicutils.struct.log.CosmicLog;
import com.google.common.base.Splitter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class UserScoreboard {
    private final Scoreboard scoreboard;
    private final ConcurrentHashMap<Integer, String> storedLines = new ConcurrentHashMap();
    private final String objectiveName = "scoreboard";
    Random random = new Random();

    public UserScoreboard(String title) {
        this.scoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        this.getClass();
        if (this.scoreboard.getObjective("scoreboard") == null) {
            this.getClass();
            Objective obj = this.scoreboard.registerNewObjective("scoreboard", "dummy");
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
            this.setTitle(title);
        }
    }

    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    public boolean hasLine(int lineID) {
        return this.storedLines.get(lineID) != null;
    }

    public void setSlot(DisplaySlot slot) {
        this.getClass();
        this.scoreboard.getObjective("scoreboard").setDisplaySlot(slot);
    }

    public String getName() {
        this.getClass();
        return this.scoreboard.getObjective("scoreboard").getDisplayName();
    }

    public void setName(String name) {
        this.getClass();
        this.scoreboard.getObjective("scoreboard").setDisplayName(name);
    }

    public void addLongLine(int id, String whole, int score) {
        int length;
        Team team = this.getScoreboard().getTeam("line_" + id);
        if (team == null) {
            team = this.getScoreboard().registerNewTeam("line_" + id);
        }
        if ((length = whole.length()) <= 16) {
            this.addLine(id, whole);
            return;
        }
        if (length > 48) {
            whole = whole.substring(0, 47);
        }
        Iterator<String> iter = Splitter.fixedLength(16).split(whole).iterator();
        String original = iter.next();
        boolean splitOnChatColor = original.endsWith("\u00a7");
        if (!(original = this.trimColorCode(original)).equals(team.getPrefix())) {
            team.setPrefix(original);
        }
        String key = (splitOnChatColor ? Character.valueOf('\u00a7') : "") + iter.next();
        if (length > 32) {
            splitOnChatColor = key.endsWith("\u00a7");
            String next = (splitOnChatColor ? Character.valueOf('\u00a7') : "") + iter.next();
            String newSpoofedSuffix = (splitOnChatColor ? Character.valueOf('\u00a7') : "") + this.trimColorCode(next);
            if (!newSpoofedSuffix.equals(team.getSuffix())) {
                team.setSuffix(newSpoofedSuffix);
            }
            CosmicLog.logDebug("(SB) ", "newSpoofed > 32: " + newSpoofedSuffix + " Next=" + next);
        } else if (!team.getSuffix().isEmpty()) {
            team.setSuffix("");
        }
        CosmicLog.logDebug("(SB)", "Split: " + splitOnChatColor + " ori: " + original + " key: " + key);
        if (!team.hasEntry(key)) {
            team.addEntry(key);
        }
        this.addLine(id, key, score);
    }

    private String trimColorCode(String str) {
        if (str.endsWith("\u00a7")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public void addLine(int id, String name, int scoreValue) {
        if (name.length() > 16) {
            name = name.substring(0, 16);
        }
        this.addLine(id, name, scoreValue, false);
    }

    public void addLine(int id, String name) {
        this.addLine(id, name, id, false);
    }

    public void addLine(int id, String name, boolean update) {
        if (this.hasLine(id) && this.storedLines.get(id).equalsIgnoreCase(name) && !update) {
            return;
        }
        this.addLine(id, name);
    }

    public void addLine(int id, String name, int scoreValue, boolean update) {
        if (this.hasLine(id) && !update) {
            this.updateLine(id, name, scoreValue);
            return;
        }
        if ((name = this.getUniqueString(name)).length() > 16) {
            name = name.substring(0, 16);
        }
        this.storedLines.put(id, name);
        this.getClass();
        this.scoreboard.getObjective("scoreboard").getScore(name).setScore(scoreValue);
    }

    public void clear() {
        Iterator<Object> iterator = ((ConcurrentHashMap.KeySetView) this.storedLines.keySet()).iterator();
        while (iterator.hasNext()) {
            int lines = (Integer) iterator.next();
            this.removeLine(lines);
        }
        for (Team t : this.getScoreboard().getTeams()) {
            t.unregister();
        }
    }

    public void removeLine(int id) {
        Team found;
        String currentLine = this.storedLines.get(id);
        if (currentLine != null) {
            this.scoreboard.resetScores(currentLine);
        }
        if ((found = this.scoreboard.getTeam("line_" + id)) != null) {
            found.unregister();
        }
        this.storedLines.remove(id);
    }

    public void updateLine(int id, String newName, int newScoreValue) {
        if ((newName = this.getUniqueString(newName)).length() > 16) {
            newName = newName.substring(0, 16);
        }
        if (this.storedLines.get(id) != null && this.storedLines.get(id).trim().equals(newName.trim())) {
            return;
        }
        this.scoreboard.resetScores(this.storedLines.get(id));
        this.addLine(id, newName, newScoreValue, true);
    }

    public void setTitle(String title) {
        this.getClass();
        this.scoreboard.getObjective("scoreboard").setDisplayName(title);
    }

    public String getUniqueString(String string) {
        for (String line : this.storedLines.values()) {
            if (!line.equals(string)) continue;
            return this.getUniqueString(string + " ");
        }
        return string;
    }

    public void setForPlayer(Player player) {
        player.setScoreboard(this.scoreboard);
    }
}

