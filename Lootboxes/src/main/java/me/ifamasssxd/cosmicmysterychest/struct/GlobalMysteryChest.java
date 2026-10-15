/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.TimeUtils
 */
package me.ifamasssxd.cosmicmysterychest.struct;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.TimeUtils;

public class GlobalMysteryChest {
    private MysteryChest activeMysteryChest;
    private long expiration;
    private int secondsTillExpiration;
    private int purchaseCount;

    public void setExpiration(long expiration) {
        this.expiration = expiration;
        this.secondsTillExpiration = (int) Math.ceil((expiration - System.currentTimeMillis()) / 1000L);
    }

    public String getExpirationString() {
/*        if (this.expiration > System.currentTimeMillis()) {
            return CC.Yellow + (this.activeMysteryChest != null && this.activeMysteryChest.getDisplayName() != null ? this.activeMysteryChest.getDisplayName() + CC.Yellow + " sale ends in " : "No longer available in ") + CC.YellowB + TimeUtils.formatFutureTime((long) this.expiration) + CC.Yellow + "...";
        }*/
        return CC.translate("&a&lAVAILABLE");
    }

    public MysteryChest getActiveMysteryChest() {
        return this.activeMysteryChest;
    }

    public long getExpiration() {
        return this.expiration;
    }

    public int getSecondsTillExpiration() {
        return this.secondsTillExpiration;
    }

    public int getPurchaseCount() {
        return this.purchaseCount;
    }

    public void setActiveMysteryChest(MysteryChest activeMysteryChest) {
        this.activeMysteryChest = activeMysteryChest;
    }

    public void setSecondsTillExpiration(int secondsTillExpiration) {
        this.secondsTillExpiration = secondsTillExpiration;
    }

    public void setPurchaseCount(int purchaseCount) {
        this.purchaseCount = purchaseCount;
    }
}

