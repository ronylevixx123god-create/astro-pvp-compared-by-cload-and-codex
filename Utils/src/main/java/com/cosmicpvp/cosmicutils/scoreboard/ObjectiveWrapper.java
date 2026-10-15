/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class ObjectiveWrapper {
    private final Scoreboard scoreboard;
    private final ConcurrentHashMap<Integer, String> storedLines = new ConcurrentHashMap();
    Random random = new Random();
    private String objectiveName = "scoreboard";

    public ObjectiveWrapper(Scoreboard scoreboard, String objectiveName, DisplaySlot slot, String title, boolean use) {
        this.scoreboard = scoreboard;
        this.objectiveName = this.scoreboard.getObjective(slot) != null ? this.scoreboard.getObjective(slot).getName() : objectiveName;
        if (this.scoreboard.getObjective(this.objectiveName) == null) {
            Objective obj = this.scoreboard.registerNewObjective(this.objectiveName, "dummy");
            obj.setDisplaySlot(slot);
            this.setTitle(title);
        }
    }

    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    public boolean hasLine(int lineID) {
        return this.storedLines.get(lineID) != null;
    }

    public ObjectiveWrapper setSlot(DisplaySlot slot) {
        this.scoreboard.getObjective(this.objectiveName).setDisplaySlot(slot);
        return this;
    }

    public String getName() {
        return this.scoreboard.getObjective(this.objectiveName).getDisplayName();
    }

    public ObjectiveWrapper setName(String name) {
        this.scoreboard.getObjective(this.objectiveName).setDisplayName(name);
        return this;
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
        this.scoreboard.getObjective(this.objectiveName).getScore(name).setScore(scoreValue);
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
        String currentLine = this.storedLines.get(id);
        if (currentLine != null) {
            this.scoreboard.resetScores(currentLine);
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
        this.scoreboard.getObjective(this.objectiveName).setDisplayName(title);
    }

    public String getUniqueString(String string) {
        for (String line : this.storedLines.values()) {
            if (!line.equals(string)) continue;
            return this.getUniqueString(string + " ");
        }
        return string;
    }

    public void unregister() {
        Objective obj = this.getScoreboard().getObjective(this.objectiveName);
        if (obj != null) {
            obj.unregister();
        }
    }

    public void setForPlayer(Player player) {
        player.setScoreboard(this.scoreboard);
    }
}

