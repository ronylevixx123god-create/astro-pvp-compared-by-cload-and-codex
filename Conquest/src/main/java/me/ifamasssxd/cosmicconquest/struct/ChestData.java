// 
// Decompiled by Procyon v0.5.36
// 

package me.ifamasssxd.cosmicconquest.struct;

import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import java.util.List;

public class ChestData
{
    private int hits;
    private List<BossType> bossesTypeSpawn;
    
    public int getHits() {
        return this.hits;
    }
    
    public List<BossType> getBossesTypeSpawn() {
        return this.bossesTypeSpawn;
    }
    
    public void setHits(final int hits) {
        this.hits = hits;
    }
    
    public void setBossesTypeSpawn(final List<BossType> bossesTypeSpawn) {
        this.bossesTypeSpawn = bossesTypeSpawn;
    }
    
    public ChestData(final int hits, final List<BossType> bossesTypeSpawn) {
        this.hits = hits;
        this.bossesTypeSpawn = bossesTypeSpawn;
    }
}
