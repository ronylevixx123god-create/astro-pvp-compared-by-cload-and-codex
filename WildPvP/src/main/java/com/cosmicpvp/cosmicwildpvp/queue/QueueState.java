package com.cosmicpvp.cosmicwildpvp.queue;

public enum QueueState
{
    IDLE(true, false),
    INVINCIBILITY(false, true),
    CANCELLED(false, false),
    END(false, true);

    private boolean joinable;
    private boolean teleported;

    private QueueState(final boolean joinable, final boolean teleported) {
        this.joinable = joinable;
        this.teleported = teleported;
    }

    public boolean isJoinable() {
        return this.joinable;
    }

    public boolean isTeleported() {
        return this.teleported;
    }
}
