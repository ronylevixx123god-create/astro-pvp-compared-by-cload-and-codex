/*
 * Decompiled with CFR 0.150.
 */
package org.arkhamnetwork.Arkkit.libs;

public class SeasonalWin {
    private final String playerName;
    private final String faction;
    private final String planet;
    private final int season;

    public SeasonalWin(String playerName, String faction, String planet, int season) {
        this.playerName = playerName;
        this.faction = faction;
        this.planet = planet;
        this.season = season;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public String getFaction() {
        return this.faction;
    }

    public String getPlanet() {
        return this.planet;
    }

    public int getSeason() {
        return this.season;
    }
}

