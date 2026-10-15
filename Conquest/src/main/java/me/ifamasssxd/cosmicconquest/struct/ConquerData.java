// 
// Decompiled by Procyon v0.5.36
// 

package me.ifamasssxd.cosmicconquest.struct;

import org.bukkit.Location;

public class ConquerData
{
    private long conquestTime;
    private Location conquestLocation;
    private String whoUnlocked;
    
    public void setConquestTime(final long conquestTime) {
        this.conquestTime = conquestTime;
    }
    
    public void setConquestLocation(final Location conquestLocation) {
        this.conquestLocation = conquestLocation;
    }
    
    public void setWhoUnlocked(final String whoUnlocked) {
        this.whoUnlocked = whoUnlocked;
    }
    
    public long getConquestTime() {
        return this.conquestTime;
    }
    
    public Location getConquestLocation() {
        return this.conquestLocation;
    }
    
    public String getWhoUnlocked() {
        return this.whoUnlocked;
    }
    
    public ConquerData(final long conquestTime, final Location conquestLocation, final String whoUnlocked) {
        this.conquestTime = conquestTime;
        this.conquestLocation = conquestLocation;
        this.whoUnlocked = whoUnlocked;
    }
}
