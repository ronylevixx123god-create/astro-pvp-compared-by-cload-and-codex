/*
 * Decompiled with CFR 0.150.
 */
package me.ifamasssxd.cosmicduel.struct;

public class DuelInvite {
    public int time = 30;
    String inviter;
    String invited;
    boolean pending = false;

    public DuelInvite(int time, String inviter, String invited, boolean pending) {
        this.time = time;
        this.inviter = inviter;
        this.invited = invited;
        this.pending = pending;
    }

    public int getTime() {
        return this.time;
    }

    public String getInviter() {
        return this.inviter;
    }

    public String getInvited() {
        return this.invited;
    }

    public boolean isPending() {
        return this.pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }
}

